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
package com.mobileman.moments.core.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.mobileman.moments.core.domain.user.User;
import com.mobileman.moments.core.domain.user.role.UserRole;

public class UserDetail extends org.springframework.security.core.userdetails.User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final User sysUser;
	
	public UserDetail(User sysUser) {
		super(sysUser.getAccount().getEmail(), sysUser.getAccount().getPassword(), true, true, true,
				true, getAuthorities(sysUser.getAccount().getRole()));
		this.sysUser = sysUser;
	}
	
	public UserDetail(String username, String password, UserRole userRole, User sysUser) {
		super(username, password, getAuthorities(userRole));
		this.sysUser = sysUser;
	}
	
	public User getSysUser() {
		return sysUser;
	}
	
	public static List<GrantedAuthority> getAuthorities(UserRole role) {
		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
		if (role == null || role == UserRole.USER) {
			authList.add(new SimpleGrantedAuthority("ROLE_USER"));
		} else if (role == UserRole.ADMIN) {
			authList.add(new SimpleGrantedAuthority("ROLE_USER"));
			authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}
		
		return authList;
	}
}
