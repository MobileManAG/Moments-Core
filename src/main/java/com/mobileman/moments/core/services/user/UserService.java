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

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.mobileman.moments.core.domain.dto.broadcast.BroadcastData;
import com.mobileman.moments.core.domain.dto.user.ForgotPasswordData;
import com.mobileman.moments.core.domain.dto.user.SigninData;
import com.mobileman.moments.core.domain.dto.user.SignupData;
import com.mobileman.moments.core.domain.dto.user.TokensData;
import com.mobileman.moments.core.domain.dto.user.UserProfileUpdateData;
import com.mobileman.moments.core.domain.question.Question;
import com.mobileman.moments.core.domain.stream.Stream;
import com.mobileman.moments.core.domain.user.Account;
import com.mobileman.moments.core.domain.user.User;
import com.mobileman.moments.core.services.EntityService;

public interface UserService extends EntityService<User> {

	User findByEmail(String email);

	User signup(SignupData data);
	User signin(SigninData data);
	void signout(User loggedUser);
	
	User findUserProfile(String id);
	
	// PROFILE
	User updateProfile(User loggedUser, UserProfileUpdateData data);
	
	Account forgotPassword(ForgotPasswordData data);
		
	void questionDeleted(Question question);

	Stream startBroadcast(String userId, BroadcastData data);

	Stream stopBroadcast(String userId);
	
	Stream updateStream(String userId, String streamId, Stream data);

	Slice<User> liveStreams(String userId, Pageable pageable);

	void updateTokens(String userId, TokensData data);
}
