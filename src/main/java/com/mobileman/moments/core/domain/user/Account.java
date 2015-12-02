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
package com.mobileman.moments.core.domain.user;

import org.hibernate.validator.constraints.Email;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mobileman.moments.core.domain.Entity;
import com.mobileman.moments.core.domain.user.role.UserRole;

@Document
public class Account extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Indexed(unique = true, sparse=true)
	@Email
	private String email;
	
	// FB token
	@Field("token")
	private String token;
	
	// IOS remote notifrication device token
	@Indexed
	@Field("device_token")
	private String deviceToken;
	
	// IOS push notifrication token
	@Indexed
	@Field("push_notification_token")
	private String pushNotificationToken;
	
	@Field("push_notification_id")
	private String pushNotificationID;
	
	@JsonIgnore
	private String password;
	
	private UserRole role;
	
	private AccountType type;
	
	public Account() {
		super();
	}
	
	public Account(String _id) {
		super(_id);
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the accountType
	 */
	public AccountType getType() {
		return type;
	}

	/**
	 * @param accountType the accountType to set
	 */
	public void setType(AccountType accountType) {
		this.type = accountType;
	}

	/**
	 * @return the deviceToken
	 */
	public String getDeviceToken() {
		return deviceToken;
	}

	/**
	 * @param deviceToken the deviceToken to set
	 */
	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	/**
	 * @return the pushNotificationID
	 */
	public String getPushNotificationID() {
		return pushNotificationID;
	}

	/**
	 * @param pushNotificationID the pushNotificationID to set
	 */
	public void setPushNotificationID(String pushNotificationID) {
		this.pushNotificationID = pushNotificationID;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPushNotificationToken() {
		return pushNotificationToken;
	}

	public void setPushNotificationToken(String pushNotificationToken) {
		this.pushNotificationToken = pushNotificationToken;
	}
	
	
}
