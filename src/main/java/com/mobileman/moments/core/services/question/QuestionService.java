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
package com.mobileman.moments.core.services.question;

import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.mobileman.moments.core.domain.question.Question;
import com.mobileman.moments.core.domain.user.User;
import com.mobileman.moments.core.services.EntityService;

public interface QuestionService extends EntityService<Question> {
	
	Question findById(String id);
	List<Question> findAllById(Collection<String> ids);
		
	Question postQuestionByUser(Question questionData, User loggedUser);

	List<Question> findQuestionsWithInsightsById(Collection<ObjectId> ids, User forUser);

	Slice<Question> findQuestionsCreatedByUser(User user, Pageable pageable);

	List<Question> findQuestionsIdCreatedByUser(User user);
		
	//boolean deleteCascaded(String id);
	
}
