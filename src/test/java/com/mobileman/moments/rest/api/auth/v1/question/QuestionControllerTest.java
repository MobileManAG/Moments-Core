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

import static com.mobileman.moments.rest.api.auth.v1.question.QuestionDataFixture.getLoggedUser;
import static com.mobileman.moments.rest.api.auth.v1.question.QuestionDataFixture.questionCreated;
import static com.mobileman.moments.rest.api.auth.v1.question.QuestionDataFixture.standardQuestionJSON;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import com.mobileman.moments.Application;
import com.mobileman.moments.SecurityUtil;
import com.mobileman.moments.core.domain.question.Question;
import com.mobileman.moments.core.domain.user.User;
import com.mobileman.moments.core.services.question.QuestionService;
import com.mobileman.moments.rest.api.MomentsRestURIConstants;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = Application.class)
public class QuestionControllerTest {

	RestTemplate template = new TestRestTemplate();
	
	MockMvc mockMvc;
	/*
	@InjectMocks
	QuestionController controller;

	@Mock
	QuestionService questionService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		this.mockMvc = standaloneSetup(controller)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
				.setMessageConverters(new MappingJackson2HttpMessageConverter()).build();

		when(questionService.postQuestionByUser(any(Question.class), any(User.class))).thenReturn(questionCreated());
		
		SecurityContextHolder.getContext().setAuthentication(SecurityUtil.getAuthentication(getLoggedUser()));
	}

	@Test
	public void thatCreateQuestionUsesHttpCreated() throws Exception {

		this.mockMvc.perform(
						post(MomentsRestURIConstants.API_AUTH_V1 + "/questions")
								.content(standardQuestionJSON())
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isCreated());
	}
		
	@Test
	public void thatDeleteUnknownQuestionUsesHttpNotFound() throws Exception {

		Question question = questionCreated();
		when(questionService.delete(eq(question.getId()))).thenReturn(true);

		SecurityContextHolder.getContext().setAuthentication(SecurityUtil.getAuthentication(getLoggedUser()));
		
		this.mockMvc.perform(
						delete(MomentsRestURIConstants.API_AUTH_V1 + "/questions/11")
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void thatDeleteKnownQuestionUsesHttpOk() throws Exception {

		Question question = questionCreated();
		when(questionService.delete(eq(question.getId()))).thenReturn(true);

		SecurityContextHolder.getContext().setAuthentication(SecurityUtil.getAuthentication(getLoggedUser()));
		
		this.mockMvc.perform(
						delete(MomentsRestURIConstants.API_AUTH_V1 + "/questions/"+question.getId())
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());
	}
	*/
}
