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
package com.mobileman.moments.rest.api.auth.v1.question;

import com.mobileman.moments.core.domain.question.Question;
import com.mobileman.moments.core.domain.user.Account;
import com.mobileman.moments.core.domain.user.Gender;
import com.mobileman.moments.core.domain.user.User;

public class QuestionDataFixture {
	
	public static final Question TEST_QUESTION;
	public static final User LOGGED_USER;
	
	static {
		LOGGED_USER = new User("5449663aea614dfeec9a9334");
		LOGGED_USER.setGender(Gender.MALE);
		LOGGED_USER.setUserName("U1");
		LOGGED_USER.setAccount(new Account());
		LOGGED_USER.getAccount().setId("5449663aea614dfeec9a9330");
		LOGGED_USER.getAccount().setEmail("test@test.com");
		LOGGED_USER.getAccount().setPassword("password");
		
		TEST_QUESTION = new Question();
		TEST_QUESTION.setId("5449663aea614dfeec9c9333");
		TEST_QUESTION.setText("Q1");
		TEST_QUESTION.setCreatedBy(LOGGED_USER);
	}

	public static Question questionCreated() {
		return TEST_QUESTION;
	}
	
	public static User getLoggedUser() {
		return LOGGED_USER;
	}
	
	public static String standardQuestionJSON() {
		return "{ \"text\":\"Q1\" }";
	}
}
