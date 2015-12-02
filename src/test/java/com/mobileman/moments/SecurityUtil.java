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
package com.mobileman.moments;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import com.mobileman.moments.core.domain.user.User;
import com.mobileman.moments.core.security.UserDetail;

public class SecurityUtil {
	
	public static Authentication getAuthentication(final User user) {
		final UserDetail detail = new UserDetail(user);
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(detail, user.getAccount().getPassword(), detail.getAuthorities());
		return token;
	}
	
	public static SecurityContext securityContext(final User user) {
		return new SecurityContext() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void setAuthentication(Authentication authentication) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public Authentication getAuthentication() {
				return SecurityUtil.getAuthentication(user);
			}
		};
	}
}
