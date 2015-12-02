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

import org.springframework.data.mongodb.core.mapping.Field;

public class UserStats {

	@Field("number_of_questions")
	private int numberOfQuestions;
	
	@Field("number_of_likes_received")
	private int numberOfLikesReceived;
	
	/**
	 * @return the numberOfQuestions
	 */
	public int getNumberOfQuestions() {
		return numberOfQuestions;
	}

	/**
	 * @param numberOfQuestions the numberOfQuestions to set
	 */
	public void setNumberOfQuestions(int numberOfQuestions) {
		this.numberOfQuestions = numberOfQuestions;
	}
	
	/**
	 * @return the numberOfLikesReceived
	 */
	public int getNumberOfLikesReceived() {
		return numberOfLikesReceived;
	}

	/**
	 * @param numberOfLikesReceived the numberOfLikesReceived to set
	 */
	public void setNumberOfLikesReceived(int numberOfLikesReceived) {
		this.numberOfLikesReceived = numberOfLikesReceived;
	}
}
