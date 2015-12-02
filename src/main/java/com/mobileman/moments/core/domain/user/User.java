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

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.mobileman.moments.core.domain.Entity;
import com.mobileman.moments.core.domain.stream.Stream;

@Document
public class User extends Entity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Field("username")
	private String userName;
	
	@Field("first_name")
	private String firstName;
	
	@Field("last_name")
	private String lastName;
	
	@Indexed(unique = true, sparse=true)
	@Field("facebook_id")
	private String facebookId;
	
	private Gender gender;
	
	@Field("user_stats")
	private UserStats userStats;
	
	private Stream stream;
	
	@DBRef
	private Account account;
	
	public User(String _id) {
		super(_id);
	}
	
	public User() {
		super();
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String name) {
		this.userName = name;
	}
	
	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}
	
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	
	/**
	 * @return the userStats
	 */
	public UserStats getUserStats() {
		return userStats;
	}

	/**
	 * @param userStats the userStats to set
	 */
	public void setUserStats(UserStats userStats) {
		this.userStats = userStats;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
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

	public Stream getStream() {
		return stream;
	}

	public void setStream(Stream stream) {
		this.stream = stream;
	}
	
}
