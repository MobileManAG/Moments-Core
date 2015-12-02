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
package com.mobileman.moments.core.domain.user.role;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserRole {

	UNKNOWN(0),
	
	USER(1),
	
	ADMIN(2);
	
	private final int value;
	
	public int getValue() {
		return value;
	}
	
	UserRole(int val) {
		this.value = val;
	}
	
	@JsonValue
	public int toJson() {
		return getValue();
	}
	
	@JsonCreator
	public static UserRole fromJson(int value) {
		for (UserRole eVAL : values()) {
			if (eVAL.getValue() == value) {
				return eVAL;
			}
		}
		
		return UserRole.UNKNOWN;
	}
}
