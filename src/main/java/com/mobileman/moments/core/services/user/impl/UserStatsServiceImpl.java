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
package com.mobileman.moments.core.services.user.impl;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.CollectionCallback;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mobileman.moments.core.domain.question.Question;
import com.mobileman.moments.core.domain.user.User;
import com.mobileman.moments.core.services.esb.MomentsESBConstants;
import com.mobileman.moments.core.services.user.UserStatsService;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

@Service
public class UserStatsServiceImpl implements UserStatsService {
	
	@Autowired
	protected MongoTemplate mongoTemplate;
		
	public void computeUserStats(final ObjectId userId) {
	
		computeNumberOfQuestions(userId);

	}

	/**
	 * @param userId
	 */
	private void computeNumberOfQuestions(final ObjectId userId) {
		mongoTemplate.execute(Question.class, new CollectionCallback<Void>() {

			@Override
			public Void doInCollection(DBCollection collection)
					throws MongoException, DataAccessException {
				
				final List<DBObject> pipeline = new ArrayList<DBObject>();
				pipeline.add(new BasicDBObject("$match", new BasicDBObject("created_by._id", userId)));
				pipeline.add(new BasicDBObject("$group", new BasicDBObject("_id", null).append("count", new BasicDBObject("$sum", 1))));
				AggregationOutput aggregationOutput = collection.aggregate(pipeline);
				long questionCount = 0;
				for (DBObject result : aggregationOutput.results()) {
					questionCount = Number.class.cast(result.get("count")).longValue();
					break;
				}
				
				mongoTemplate.findAndModify(Query.query(Criteria.where("_id").is(userId)), new Update().set("userStats.numberOfQuestions", questionCount), User.class);
				
				return null;
			}
		});
	}
	
	@Async
	@Scheduled(cron="${user_stats_update_job.cron.expression}")
	public void computeUserStats() {
		
		mongoTemplate.execute(User.class, new CollectionCallback<Void>() {

			@Override
			public Void doInCollection(DBCollection collection)
					throws MongoException, DataAccessException {
				
				DBCursor cursor = collection.find(new BasicDBObject(), new BasicDBObject("_id", 1));
				for (DBObject dbObject : cursor) {
					ObjectId _id = (ObjectId) dbObject.get("_id");
					computeUserStats(_id);
				}
				
				return null;
			}
		});
	}

	@ServiceActivator(inputChannel=MomentsESBConstants.QUESTION_CREATED_CHANNEL)
	public void questionCreated(Question question) {
		if (question.getCreatedBy() == null || StringUtils.isEmpty(question.getCreatedBy().getId())) {
			return;
		}
		
		final Query query = Query.query(Criteria.where("_id").is(question.getCreatedBy().getId()));
		this.mongoTemplate.findAndModify(query, new Update().inc("userStats.numberOfQuestions", 1), User.class);
	}
	
	@ServiceActivator(inputChannel=MomentsESBConstants.QUESTION_DELETED_CHANNEL)
	public void questionDeleted(Question question) {
		if (question.getCreatedBy() == null || StringUtils.isEmpty(question.getCreatedBy().getId())) {
			return;
		}
	
		final Query query = Query.query(Criteria.where("_id").is(question.getCreatedBy().getId()));
		this.mongoTemplate.findAndModify(query, new Update().inc("userStats.numberOfQuestions", -1), User.class);
	}
	
}
