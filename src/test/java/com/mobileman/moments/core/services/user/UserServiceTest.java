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
package com.mobileman.moments.core.services.user;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mobileman.moments.core.domain.question.Question;
import com.mobileman.moments.core.domain.user.Account;
import com.mobileman.moments.core.domain.user.Gender;
import com.mobileman.moments.core.domain.user.User;
import com.mobileman.moments.core.repositories.user.AccountRepository;
import com.mobileman.moments.core.services.mail.MailService;
import com.mobileman.moments.core.services.question.QuestionService;
import com.mobileman.moments.core.services.user.impl.UserServiceImpl;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = Application.class)
public class UserServiceTest {

	@InjectMocks
	private UserService userService = new UserServiceImpl();
	
	@Mock
	private AccountRepository accountRepository;
		
	@Mock
	private QuestionService questionService;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@Mock
	private MongoTemplate mongoTemplate;
	
	@Mock
	private MailService mailService;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	private static final String FOLLOWED_QUESTION_ID = "5449663aea614dfeec9a9335";
	private static final String FOLLOWED_QUESTION2_ID = "5449663aea614dfeec9a9336";
	
	public static User getTestUser1() {
		User user = new User("5449663aea614dfeec9a9111");
		user.setGender(Gender.FEMALE);
		user.setUserName("U2");
		user.setAccount(new Account());
		user.getAccount().setId("123456");
		user.getAccount().setEmail("u2@test.com");
		user.getAccount().setPassword("password");
				
		return user;
	}
	
	public static Question followedQuestion() {
		Question question = new Question();
		question.setId(FOLLOWED_QUESTION_ID);
		question.setText("Q1");
		question.setCreatedBy(getTestUser1());
		return question;
	}
	
	public static Question followedQuestion2() {
		Question question = new Question();
		question.setId(FOLLOWED_QUESTION2_ID);
		question.setText("Q1");
		question.setCreatedBy(getTestUser1());
		return question;
	}
}
