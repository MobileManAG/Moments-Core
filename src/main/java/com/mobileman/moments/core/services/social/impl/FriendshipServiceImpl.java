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
package com.mobileman.moments.core.services.social.impl;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.mobileman.moments.core.domain.social.Friendship;
import com.mobileman.moments.core.domain.user.User;
import com.mobileman.moments.core.repositories.social.FriendshipRepository;
import com.mobileman.moments.core.repositories.user.UserRepository;
import com.mobileman.moments.core.services.impl.EntityServiceImpl;
import com.mobileman.moments.core.services.social.FriendshipService;
import com.mongodb.WriteResult;

@Service
public class FriendshipServiceImpl extends EntityServiceImpl<Friendship> implements FriendshipService {

	@Autowired
	private FriendshipRepository friendshipRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public void refreshFriendship(User user, List<String> friendsFacebookId) {
		
		List<Friendship> existingFriendships = this.friendshipRepository.findByFromUserIdOrToUserId(user.getId(), user.getId());
		for (Friendship friendship : existingFriendships) {
			
			if (!friendsFacebookId.contains(friendship.getFromUserFacebookId()) && !friendsFacebookId.contains(friendship.getToUserFacebookId())) {
				// not a friend anymore, delete 
				this.friendshipRepository.delete(friendship);
			} else {
				friendsFacebookId.remove(friendship.getFromUserFacebookId());
				friendsFacebookId.remove(friendship.getToUserFacebookId());
			}
		}
		
		for (String newFriendFacebookId : friendsFacebookId) {
			User newFriend = this.userRepository.findByFacebookId(newFriendFacebookId);
			if (newFriend != null) {
				Friendship friendship = new Friendship();
				friendship.setNewFrom(true);
				friendship.setNewTo(true);
				friendship.setFromUserId(user.getId());
				friendship.setFromUserFacebookId(user.getFacebookId());
				friendship.setToUserId(newFriend.getId());
				friendship.setToUserFacebookId(newFriend.getFacebookId());
				this.friendshipRepository.save(friendship);
			}
		}
	}

	@Override
	public void deleteAllForUser(User user) {
		
		final Criteria where = new Criteria().orOperator(
				Criteria.where("fromUser.id").is(user.getId()),
				Criteria.where("toUser.id").is(user.getId())
				);
		final Query query = Query.query(where);
		WriteResult result = mongoTemplate.remove(query, Friendship.class);
		if (getLog().isInfoEnabled()) {
			getLog().info("Removed all friendship (" + result.getN() + ") of user: " + user);
		}
	}

	@Override
	public List<ObjectId> findAllFriendsUuid(String userId) {
		
		List<ObjectId> ids = new ArrayList<ObjectId>();
		List<Friendship> friendships = this.friendshipRepository.findByFromUserIdOrToUserId(userId, userId);
		for (Friendship friendship : friendships) {
			if (friendship.getFromUserId().equals(userId)) {
				ids.add(new ObjectId(friendship.getToUserId()));
			} else {
				ids.add(new ObjectId(friendship.getFromUserId()));
			}
		}
		
		return ids;
	}

	@Override
	public List<User> findAllFriendsOfUser(String userId) {
		List<String> ids = new ArrayList<String>();
		List<Friendship> friendships = this.friendshipRepository.findByFromUserIdOrToUserId(userId, userId);
		for (Friendship friendship : friendships) {
			final String friendUuid;
			if (friendship.getFromUserId().equals(userId)) {
				friendUuid = friendship.getToUserId();
			} else {
				friendUuid = friendship.getFromUserId();
			}
			
			ids.add(friendUuid);
		}
		
		return this.userRepository.findByIdIn(ids);
	}

}
