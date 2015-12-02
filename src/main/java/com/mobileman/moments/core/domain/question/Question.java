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
package com.mobileman.moments.core.domain.question;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.mobileman.moments.core.domain.Entity;

@Document
public class Question extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotBlank
	private String text;
		
	@Field("insights_count")
	private Integer insightsCount;
	
	@Field("new_insights_count")
	private Integer newInsightsCount;

    private boolean following;

	public Question() {
		super();
	}

	public Question(String _id) {
		super(_id);
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * @return the insightsCount
	 */
	public Integer getInsightsCount() {
		return insightsCount;
	}

	/**
	 * @param insightsCount the insightsCount to set
	 */
	public void setInsightsCount(Integer insightsCount) {
		this.insightsCount = insightsCount;
	}

	/**
	 * @return the newInsightsCount
	 */
	public Integer getNewInsightsCount() {
		return newInsightsCount;
	}

	/**
	 * @param newInsightsCount the newInsightsCount to set
	 */
	public void setNewInsightsCount(Integer newInsightsCount) {
		this.newInsightsCount = newInsightsCount;
	}
	
	public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }
}
