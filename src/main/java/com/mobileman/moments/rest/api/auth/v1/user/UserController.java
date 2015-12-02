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

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mobileman.moments.core.domain.dto.user.TokensData;
import com.mobileman.moments.core.domain.dto.user.UserProfileUpdateData;
import com.mobileman.moments.core.domain.user.User;
import com.mobileman.moments.core.services.user.UserService;
import com.mobileman.moments.core.util.security.SecurityUtils;
import com.mobileman.moments.rest.api.AbstractMomentsController;
import com.mobileman.moments.rest.api.MomentsRestURIConstants;

@RestController
@RequestMapping(
	value = MomentsRestURIConstants.API_AUTH_V1 + "/users", 
	consumes = MediaType.APPLICATION_JSON_VALUE, 
	produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController extends AbstractMomentsController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/profile/{id}", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.GET)
	public ResponseEntity<User> userProfile(@PathVariable("id") String id) {
		User user = this.userService.findUserProfile(id);
		return new ResponseEntity<User>(user, user == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
	}
	
	@RequestMapping(value="", consumes = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.POST)
	public User updateProfile(@Valid @RequestBody() UserProfileUpdateData data) {
		User user = this.userService.updateProfile(SecurityUtils.getLoggedUser(), data);
		return user;
	}
	
	@RequestMapping(value="/signout", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
	public void signout() {
		this.userService.signout(SecurityUtils.getLoggedUser());
	}
	
	@RequestMapping(value="/tokens", consumes = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.PUT)
	public void updateTokens(@Valid @RequestBody() TokensData data) {
		this.userService.updateTokens(SecurityUtils.getLoggedUser().getId(), data);
	}
}
