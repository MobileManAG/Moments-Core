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
package com.mobileman.moments.rest.api.auth.v1.user;

import static com.mobileman.moments.rest.api.auth.v1.question.QuestionDataFixture.getLoggedUser;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.mobileman.moments.Application;
import com.mobileman.moments.SecurityUtil;
import com.mobileman.moments.core.domain.user.User;
import com.mobileman.moments.core.services.user.UserService;
import com.mobileman.moments.rest.api.MomentsRestURIConstants;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class UserControllerTest {

	MockMvc mockMvc;

	@InjectMocks
	UserController controller;
	
	@Mock
	UserService userService;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		
		this.mockMvc = standaloneSetup(controller)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
				.setMessageConverters(new MappingJackson2HttpMessageConverter()).build();

		SecurityContextHolder.setContext(SecurityUtil.securityContext(getLoggedUser()));
	}
	
	public static String followQuestionJSON(String id) {
		return "{ \"id\":\"" + id + "\" }";
	}
	
	public static String followQuestionJSON() {
		return followQuestionJSON("5449663aea614dfeec9a9335");
	}
	
	public static String followTagJSON(String id) {
		return "{ \"id\":\"" + id + "\" }";
	}
		
	@Test
	public void thatUserProfileUsesHttpOk() throws Exception {
		
		User user = getLoggedUser();
		
		when(userService.findUserProfile(eq(getLoggedUser().getId()))).thenReturn(user);

		this.mockMvc.perform(
						get(MomentsRestURIConstants.API_AUTH_V1 + "/users/profile/" + user.getId())
								.contentType(MediaType.ALL)
								.accept(MediaType.ALL))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(user.getId()))
				;
	}
	
	@Test
	public void thatUserProfileUsesHttpNotFound() throws Exception {
		
		this.mockMvc.perform(
						get(MomentsRestURIConstants.API_AUTH_V1 + "/users/profile/11")
								.contentType(MediaType.ALL)
								.accept(MediaType.ALL))
				.andDo(print())
				.andExpect(status().isNotFound())
				;
	}
}
