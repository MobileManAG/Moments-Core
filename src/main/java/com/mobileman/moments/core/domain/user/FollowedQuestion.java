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

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.mobileman.moments.core.domain.question.Question;

public class FollowedQuestion extends Question implements Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonSerialize(using=DateSerializer.class)
	@Field("last_read_on")
	private Date lastReadOn;

	public FollowedQuestion() {
		super();
	}

	public FollowedQuestion(String id) {
		super(id);
	}

	/**
	 * @return the lastReadOn
	 */
	public Date getLastReadOn() {
		return lastReadOn;
	}

	/**
	 * @param lastReadOn the lastReadOn to set
	 */
	public void setLastReadOn(Date lastReadOn) {
		this.lastReadOn = lastReadOn;
	}
	
	@Override
	public FollowedQuestion clone() {
		try {
			return (FollowedQuestion)super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
	
}
