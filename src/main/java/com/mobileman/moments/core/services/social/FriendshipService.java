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
package com.mobileman.moments.core.services.social;

import java.util.List;

import org.bson.types.ObjectId;

import com.mobileman.moments.core.domain.social.Friendship;
import com.mobileman.moments.core.domain.user.User;
import com.mobileman.moments.core.services.EntityService;

public interface FriendshipService extends EntityService<Friendship> {

	/**
	 * 
	 * @param user
	 * @param friendsFacebookId
	 */
	void refreshFriendship(User user, List<String> friendsFacebookId);

	void deleteAllForUser(User user);

	List<ObjectId> findAllFriendsUuid(String userId);

	List<User> findAllFriendsOfUser(String userId);
}
