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
package com.mobileman.moments.core.services.social.facebook.impl;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.mobileman.moments.core.domain.user.User;
import com.mobileman.moments.core.services.social.FriendshipService;
import com.mobileman.moments.core.services.social.facebook.FacebookService;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;

@Service
public class FacebookServiceImpl implements FacebookService {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private FriendshipService friendshipService;
	
	@Value("${facebook.app_secret}") 
	private String app_secret;
	
	private Mac _mac;
	private SecretKey _secretKey;
	
	@Autowired
	Environment environment;
	
	public synchronized Mac getMac() throws NoSuchAlgorithmException, InvalidKeyException {
		if (_mac == null) {
			_mac = Mac.getInstance("HmacSHA256");
			byte[] key = getApp_secret().getBytes(Charset.forName("UTF-8"));
			_secretKey = new SecretKeySpec(key, "HmacSHA256");
			_mac.init(_secretKey);
		}
		
		return _mac;
	}
	
	public String getApp_secret() {
		return app_secret;
	}
	
	private FacebookClient getFacebookClient(String accessToken) {
		FacebookClient facebookClient = new DefaultFacebookClient(accessToken, getApp_secret(), Version.VERSION_2_3);
		return facebookClient;
	}

	@Override
	public void updateFriendsOfUser(User user) {
		
		try {
			final String token = user.getAccount().getToken();
			final FacebookClient facebookClient = getFacebookClient(token);

			// Fetch fiends who have moments app installed
			Connection<com.restfb.types.User> friendsConnection = facebookClient.fetchConnection("/me/friends", com.restfb.types.User.class);
			List<String> friendsFbIds = new ArrayList<String>(friendsConnection.getTotalCount().intValue());
			// TODO
			if (Arrays.asList(environment.getActiveProfiles()).contains("dev")) {
				friendsFbIds.addAll(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8"));
			}

			for (List<com.restfb.types.User> friends : friendsConnection) {
				for (com.restfb.types.User fbFriend : friends) {
					if (fbFriend.getId() != null) {
						friendsFbIds.add(fbFriend.getId());
					}
				}
			}

			/*
			 * friendsConnection =
			 * facebookClient.fetchConnection("/me/taggable_friends",
			 * com.restfb.types.User.class); for (List<com.restfb.types.User>
			 * friends : friendsConnection) { for (com.restfb.types.User
			 * fbFriend : friends) { if (fbFriend.getId() != null) {
			 * friendsFbIds.add(fbFriend.getId()); } } }
			 */

			this.friendshipService.refreshFriendship(user, friendsFbIds);
			
		} catch (Exception e) {
			log.error("Error while fetching FB friends", e);
		}
	}

}
