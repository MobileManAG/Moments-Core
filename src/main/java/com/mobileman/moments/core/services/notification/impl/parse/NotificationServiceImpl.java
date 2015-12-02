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
package com.mobileman.moments.core.services.notification.impl.parse;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.mobileman.moments.core.domain.notification.IOSNotification;
import com.mobileman.moments.core.domain.notification.NotificationType;
import com.mobileman.moments.core.domain.stream.Stream;
import com.mobileman.moments.core.domain.user.User;
import com.mobileman.moments.core.services.notification.NotificationService;
import com.mobileman.moments.core.services.social.FriendshipService;
import com.mobileman.moments.core.services.user.UserService;

@Service
public class NotificationServiceImpl implements NotificationService {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private FriendshipService friendshipService;
	
	@Qualifier("parseRestTemplate")
	@Autowired
	private RestTemplate parseRestTemplate;
	
	private static final String PARSE_POST_PUSH_URL = "https://api.parse.com/1/push";

	@Override
	public void userStartedBroadcasting(User user, Stream stream) {
		
		List<User> friends = this.friendshipService.findAllFriendsOfUser(user.getId());
		for (User userToNotify : friends) {
			if (userToNotify == null || userToNotify.getAccount() == null || !StringUtils.hasText(userToNotify.getAccount().getPushNotificationID())) {
				continue;
			}
		
			if (ObjectUtils.nullSafeEquals(user, userToNotify)) {
				// do not notify current user
				continue;
			}
			
			final ParseRequest request = new ParseRequest();
			IOSNotification iosNotification = new IOSNotification(NotificationType.BROADCAST_STARTED);
			iosNotification.setUserId(user.getId());
			iosNotification.setUserName(user.getUserName());
			iosNotification.setAlert(stream.getText());
			iosNotification.setBadge("Increment");
			iosNotification.setCategory("pass_watch");
					
			final ParseQuery query = new ParseQuery(userToNotify.getAccount().getPushNotificationID());
			request.setWhere(query);
			request.setData(iosNotification);
						
			try {
				HttpEntity<ParseRequest> requestEntity = new HttpEntity<ParseRequest>(request);
				ParseResponse response = this.parseRestTemplate.postForObject(PARSE_POST_PUSH_URL, requestEntity, ParseResponse.class);
				if (response.getError() != null) {
					System.out.println("PARSE PUSH NOTIFICATIOn ERROR: " + response.getError());
				}
			} catch (Exception e) {
				log.error("Error while sending notification to Parse for iOS clients", e);
			}
			
		}
	}
	
	

}
