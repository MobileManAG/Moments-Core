/*******************************************************************************
 * Copyright 2015 MobileMan GmbH
 * www.mobileman.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.mobileman.moments.core.services.question.impl;

import java.util.Collection;
import java.util.List;

import javax.validation.ValidationException;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mobileman.moments.core.domain.question.Question;
import com.mobileman.moments.core.domain.user.User;
import com.mobileman.moments.core.domain.utils.EntityUtils;
import com.mobileman.moments.core.repositories.question.QuestionRepository;
import com.mobileman.moments.core.services.esb.MomentsGateway;
import com.mobileman.moments.core.services.impl.EntityServiceImpl;
import com.mobileman.moments.core.services.question.QuestionService;
import com.mobileman.moments.core.services.user.UserService;
import com.mobileman.moments.core.util.CacheConstants;

@Service
public class QuestionServiceImpl extends EntityServiceImpl<Question> implements QuestionService {
	
	private QuestionRepository questionRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CacheManager cacheManager;
	
	@Autowired
	private KeyGenerator keyGenerator;
	
	@Autowired
	private MomentsGateway momentsGateway;
	
	@Autowired
	public void setQuestionRepository(
			QuestionRepository consultationRepository) {
		setEntityRespository(consultationRepository);
		this.questionRepository = consultationRepository;
	}
	
	public QuestionRepository getQuestionRepository() {
		return questionRepository;
	}
	
	private void invalidateCommonCaches(Question question) {
		
		if (question.getCreatedBy() != null) {
			this.cacheManager.getCache(CacheConstants.QUESTIONS_CREATED_BY_USER).evict(keyGenerator.generate(this, null, question.getCreatedBy().getId()));
		}
		
	}
		
	@Override
	@Cacheable(value=CacheConstants.QUESTIONS_CREATED_BY_USER, key="#user.id")
	public Slice<Question> findQuestionsCreatedByUser(User user,
			Pageable pageable) {
		Slice<Question> questions = this.questionRepository.findByCreatedByIdOrderByCreatedOnDesc(new ObjectId(user.getId()), pageable);
		return questions;
	}
	
	@Override
	public List<Question> findQuestionsWithInsightsById(Collection<ObjectId> ids, User forUser) {
		List<Question> questions = this.questionRepository.findByIdIn(ids);
		return questions;
	}

	@Override
	public List<Question> findQuestionsIdCreatedByUser(User user) {
		Query query = Query.query(Criteria.where("createdBy.id").is(new ObjectId(user.getId())));
		query.fields().include("id");
		List<Question> questions = this.mongoTemplate.find(query, Question.class);
		return questions;
	}

	@Override
	@Caching(evict = {
			//@CacheEvict(value=CacheConstants.QUESTIONS_BY_TAG, key="#questionData.tag.id"),
	        @CacheEvict(value=CacheConstants.QUESTIONS_CREATED_BY_USER, key="#loggedUser.id")            
	})
	public Question postQuestionByUser(Question questionData, User loggedUser) {
		if (loggedUser == null) {
			throw new ValidationException("Logged user has to be present");
		}
		
		if (StringUtils.isEmpty(questionData.getText())) {
			throw new ValidationException("Text must not be empty");
		}
				
		Question question = new Question();
		question.setText(questionData.getText());
		this.questionRepository.save(question);
				
		this.momentsGateway.questionCreated(question);
		
		invalidateCommonCaches(question);
		
		return question;
	}
	
	@CacheEvict(value=CacheConstants.QUESTIONS, key="#question.id")
	private boolean deleteCascaded(Question question) {
		if (question == null) {
			return false;
		}
		
		invalidateCommonCaches(question);
		
		if (!super.delete(question)) {
			return false;
		}
		
		this.momentsGateway.questionDeleted(question);

		return true;
	}
	
	@Override
	@CacheEvict(value=CacheConstants.QUESTIONS, key="#id")
	public boolean delete(String id) {
		return deleteCascaded(findById(id));
	}
	
	@Override
	@CacheEvict(value=CacheConstants.QUESTIONS, key="#entity.id")
	public boolean delete(Question entity) {
		return deleteCascaded(entity);
	}
	
	@Override
	public List<Question> findAllById(Collection<String> ids) {
		List<Question> questions = this.questionRepository.findByIdIn(EntityUtils.toObjectsId(ids));
		return questions;
	}
}
