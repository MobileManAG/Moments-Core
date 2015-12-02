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

import static com.mobileman.moments.rest.api.auth.v1.question.QuestionDataFixture.questionCreated;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mobileman.moments.Application;
import com.mobileman.moments.core.domain.question.Question;
import com.mobileman.moments.core.repositories.question.QuestionRepository;
import com.mobileman.moments.core.services.question.impl.QuestionServiceImpl;
import com.mobileman.moments.core.services.user.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class QuestionServiceTest {
	
	@InjectMocks
	private QuestionService questionService = new QuestionServiceImpl();
	
	@Mock
	private QuestionRepository questionRepository;
	
	@Mock
	private UserService userService;

	@Test
	public void findById_WrongId() throws Exception {
		assertNull(questionService.findById(""));
	}
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void thatFindQuestionsForTagReturnsData() throws Exception {
		
		Question question = questionCreated();
		Page<Question> page = new PageImpl<Question>(Arrays.asList(question));
		when(questionRepository.findAll(any(Pageable.class))).thenReturn(page);
				
	}
	
}
