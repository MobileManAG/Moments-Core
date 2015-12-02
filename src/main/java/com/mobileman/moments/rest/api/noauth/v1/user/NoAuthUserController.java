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
package com.mobileman.moments.rest.api.noauth.v1.user;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mobileman.moments.core.domain.dto.user.ForgotPasswordData;
import com.mobileman.moments.core.domain.dto.user.SigninData;
import com.mobileman.moments.core.domain.dto.user.SignupData;
import com.mobileman.moments.core.domain.user.Account;
import com.mobileman.moments.core.domain.user.User;
import com.mobileman.moments.core.domain.user.role.UserRole;
import com.mobileman.moments.core.services.user.UserService;
import com.mobileman.moments.rest.api.AbstractMomentsController;
import com.mobileman.moments.rest.api.MomentsRestURIConstants;

@RestController
@RequestMapping(
		value = MomentsRestURIConstants.API_NOAUTH_V1 + "/users",
		consumes = MediaType.APPLICATION_JSON_VALUE, 
		produces = MediaType.APPLICATION_JSON_VALUE)
public class NoAuthUserController extends AbstractMomentsController {

	@Value("${moments.admin.username}") 
	private String adminUsername;
	
	@Value("${moments.admin.password}") 
	private String adminPassword;
	
	@Autowired
	private UserService userService;

	@RequestMapping(value="/signup", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
    public User signup(@Valid @RequestBody SignupData data) {
		User user = this.userService.signup(data);
        return user;
    }
	
	@RequestMapping(value="/forgot_password", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordData data) {
		this.userService.forgotPassword(data);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

	@RequestMapping(value="/signin", method = RequestMethod.POST)
	public User signin(@Valid @RequestBody SigninData data) {
		final User user;
		if (data.getEmail() != null && data.getEmail().equalsIgnoreCase(this.adminUsername)) {
			user = new User();
			user.setAccount(new Account());
			user.getAccount().setCreatedOn(new Date());
			user.getAccount().setModifiedOn(user.getCreatedOn());
			user.getAccount().setEmail(data.getEmail());
			user.getAccount().setRole(UserRole.USER);
			user.setCreatedOn(new Date());
			user.setModifiedOn(user.getCreatedOn());
			user.setUserName(data.getEmail());
			
		} else {
			user = this.userService.signin(data);
		}
		
		return user;
	}
}
