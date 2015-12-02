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
package com.mobileman.moments.core.services.notification.impl.gcn.question;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.CollectionCallback;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import com.mobileman.moments.core.domain.question.Question;
import com.mobileman.moments.core.domain.user.User;
import com.mobileman.moments.core.services.esb.MomentsESBConstants;
import com.mobileman.moments.core.services.esb.MomentsGateway;
import com.mobileman.moments.core.services.notification.NotificationService;
import com.mobileman.moments.core.services.user.UserService;
import com.mobileman.moments.core.util.locale.LocalizationUtils;
import com.mobileman.moments.core.util.security.SecurityUtils;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

@MessageEndpoint
public class GCNQuestionNotifications {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private MessageSource messageSource;
		
	@Autowired
	private UserService userService;
	
	@Autowired
	private MomentsGateway momentsGateway;
	
	@Autowired
	private NotificationService notificationService;

	@SuppressWarnings("unchecked")
	@ServiceActivator(inputChannel=MomentsESBConstants.QUESTION_CREATED_CHANNEL)
	public void questionCreated(final Question question) {
		
		final String location = "";
		final String alert = this.messageSource.getMessage("push_notification.new_question_in_interest.alert", new Object[]{location}, LocalizationUtils.DEFAULT_LOCALE);
		final String title = this.messageSource.getMessage("push_notification.new_question_in_interest.title", new Object[]{"name"}, null, LocalizationUtils.DEFAULT_LOCALE);
		final User loggedUser = SecurityUtils.getLoggedUser();
		
		mongoTemplate.execute(User.class, new CollectionCallback<Void>() {

			@Override
			public Void doInCollection(DBCollection collection)
					throws MongoException, DataAccessException {
				Query query = Query.query(Criteria.where("interests._id").is(new ObjectId("id")).and("_id").ne(new ObjectId(loggedUser.getId())));
				query.fields().include("_id");
				query.fields().include("interests");
				
				DBCursor cursor = collection.find(query.getQueryObject(), query.getFieldsObject());
				for (DBObject dbObject : cursor) {
					// UserSettings = settings = dbObject.get
					ObjectId userId = (ObjectId)dbObject.get("_id");
					
					List<DBObject> interests = (List<DBObject>)dbObject.get("interests");
					
					for (DBObject interest : interests) {
						if (interest.get("_id").toString().equals("id")) {
							if (interest.containsField("following_questions") && Boolean.TRUE.equals(interest.get("following_questions"))) {
								//notificationService.questionByInterestCreated(question, userId.toString(), alert, title);
							}
						}
					}
					
					// if (settings.getFollowesQuestionsNotification().isEnabled()) 
					// UserPushNotificationEvent event = new UserPushNotificationEvent(userId.toString(), title, alert);
					// momentsGateway.postPushNotificationEvent(event);
					
				}
				
				return null;
			}
		
		});
	}
}
