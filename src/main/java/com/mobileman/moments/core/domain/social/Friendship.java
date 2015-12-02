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
package com.mobileman.moments.core.domain.social;

import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.mobileman.moments.core.domain.Entity;

@Document
@CompoundIndexes({
    @CompoundIndex(name = "from_to_idx", def = "{'from_user_id': 1, 'to_user_id': 1}", unique = true)
})
public class Friendship extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
	@Field("from_user_id")
	private String fromUserId;
	
	@NotNull
	@Field("from_user_facebook_id")
	private String fromUserFacebookId;
	
	@NotNull
	@Field("to_user_id")
	private String toUserId; 
	
	@NotNull
	@Field("to_user_facebook_id")
	private String toUserFacebookId;
	
	@Field("from_is_new")
	private boolean newFrom;
	
	@Field("to_is_new")
	private boolean newTo;
	
	@Field("blocked_from")
	private boolean blockedFrom;
	
	@Field("blocked_to")
	private boolean blockedTo;

	public String getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public boolean isNewFrom() {
		return newFrom;
	}

	public void setNewFrom(boolean fromIsNew) {
		this.newFrom = fromIsNew;
	}

	public boolean isNewTo() {
		return newTo;
	}

	public void setNewTo(boolean toIsNew) {
		this.newTo = toIsNew;
	}

	public boolean isBlockedFrom() {
		return blockedFrom;
	}

	public void setBlockedFrom(boolean blockedFrom) {
		this.blockedFrom = blockedFrom;
	}

	public boolean isBlockedTo() {
		return blockedTo;
	}

	public void setBlockedTo(boolean blockedTo) {
		this.blockedTo = blockedTo;
	}

	public String getFromUserFacebookId() {
		return fromUserFacebookId;
	}

	public void setFromUserFacebookId(String fromUserFacebookId) {
		this.fromUserFacebookId = fromUserFacebookId;
	}

	public String getToUserFacebookId() {
		return toUserFacebookId;
	}

	public void setToUserFacebookId(String toUserFacebookId) {
		this.toUserFacebookId = toUserFacebookId;
	}
	
	
}
