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
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.mobileman.moments.core.domain.user.AccountType;
import com.mobileman.moments.core.domain.user.Gender;

@JsonInclude(value=Include.NON_NULL)
public class SignupData {

	@Email
	private String email;
	
	@Pattern(regexp = "^[A-Za-z0-9]+$")
	private String deviceToken;
	
	@Pattern(regexp = "^[A-Za-z0-9]+$")
	private String pushNotificationToken;
	
	@NotNull
	private String facebookID;
	
	@NotNull
	@Size(min=4)
	private String token;
	
	//@NotNull
	//@Size(min=3, max=18)
	private String userName;
	private String firstName;
	private String lastName;
	
	//@NotNull
	private Gender gender;
	
	private String pushNotificationID;
	
	private AccountType account_type;
	
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
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	
	public String getFacebookID() {
		return facebookID;
	}

	public void setFacebookID(String facebookID) {
		this.facebookID = facebookID;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
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

	/**
	 * @return the gender
	 */
	public Gender getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(Gender gender) {
		this.gender = gender;
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

	public AccountType getAccountType() {
		return account_type;
	}

	public void setAccountType(AccountType accountType) {
		this.account_type = accountType;
	}

	public String getPushNotificationToken() {
		return pushNotificationToken;
	}

	public void setPushNotificationToken(String pushNotificationToken) {
		this.pushNotificationToken = pushNotificationToken;
	}
	
}
