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
package com.mobileman.moments.core.domain.dto.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.mobileman.moments.core.domain.user.AccountType;
import com.mobileman.moments.core.domain.user.Gender;

@JsonInclude(value=Include.NON_NULL)
public class SigninData {

	@Email
	private String email;
	
	@NotBlank
	private String facebookID;
	
	@NotBlank
	private String token;
	
	private String userName;
	
	private String firstName;
	
	private String lastName;
	
	@NotNull
	private AccountType account_type;
	
	@Pattern(regexp = "^[A-Za-z0-9]+$")
	private String deviceToken;
	
	@Pattern(regexp = "^[A-Za-z0-9]+$")
	private String pushNotificationToken;
	
	private String pushNotificationID;
	
	private Gender gender;

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
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

	public String getFacebookID() {
		return facebookID;
	}

	public void setFacebookID(String facebookID) {
		this.facebookID = facebookID;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public AccountType getAccount_type() {
		return account_type;
	}

	public void setAccount_type(AccountType accountType) {
		this.account_type = accountType;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String device_id) {
		this.deviceToken = device_id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPushNotificationToken() {
		return pushNotificationToken;
	}

	public void setPushNotificationToken(String pushNotificationToken) {
		this.pushNotificationToken = pushNotificationToken;
	}
	
	
}
