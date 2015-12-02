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
package com.mobileman.moments.data;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import au.com.bytecode.opencsv.CSVReader;

import com.mobileman.moments.Application;
import com.mobileman.moments.SecurityUtil;
import com.mobileman.moments.core.domain.dto.user.SigninData;
import com.mobileman.moments.core.domain.user.Location;
import com.mobileman.moments.core.domain.user.User;
import com.mobileman.moments.core.services.question.QuestionService;
import com.mobileman.moments.core.services.user.UserService;
import com.mobileman.moments.core.services.user.UserStatsService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class DataImport {
	
	private static long beginTime;
	private static long endTime;

	@Autowired
	UserService userService;
	
	@Autowired
	QuestionService questionService;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	UserStatsService userStatsService;
	
	static {
		beginTime = Timestamp.valueOf("2014-11-10 07:00:00").getTime();
	    endTime = Timestamp.valueOf("2014-11-17 17:00:00").getTime();
	}
	
	private static long getRandomDate () {
	    return getRandomDate(beginTime);
	}
	
	private static long getRandomDate (long _beginTime) {
	    long diff = endTime - _beginTime + 1;
	    return _beginTime + (long) (Math.random() * diff);
	}
	
	protected static Date getQuestionDate() {
        Date date  = new Date(getRandomDate());
        return date;
	}
	
	@Before
	public void setup() {
		
	}
	
	private User createDefaultUser() {
		User user = createUser("#Iboga", "ZWQ8U8AB", "#Iboga@iboga.com", new Location("Switzerland", "Zurich"));
		return user;
	}
	
	static private Random USER_RAND = new Random();
	public static int randUserIndex(int max) {
		int min = 0;
	    int randomNum = USER_RAND.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
	
	@Test
	public void importData() throws Exception {
		
		//userStatsService.computeUserStats();
		
		if (!Boolean.valueOf(System.getProperty("data-import", "false"))) {
			return;
		}
		
		String file = DataImport.class.getResource("/data/pre-compiled-q_and_i.csv").getFile();
		CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(file), "UTF-8"), ';');
		
		String [] nextLine;
	    while ((nextLine = reader.readNext()) != null) {
	    	if (nextLine[0].equalsIgnoreCase("interests")) {
	    		continue;
			}
	    	
	    	String tagName = nextLine[0];
	    	String questionText= nextLine[1];
	    	String questionLocation= nextLine[2];
	    	//User questionUser = getUserForLocation(questionLocation);
	    	SecurityContextHolder.setContext(SecurityUtil.securityContext(null));
	    	
	    }
	    
	    reader.close();
	}

	private User createUser(String userName, String password, String email, Location location) {
		SigninData data = new SigninData();
		data.setDeviceToken(UUID.randomUUID().toString());
		data.setEmail(email);
		data.setUserName(userName);
		data.setToken("CAAXMIu1gzDgBALffwKQDp4VULGFdKKVKf60ftbu8IqXjXuqpDRJZAX9YZBnEbczN5ClxZA4Wf3MEGSETQlZBHZADyB7CzYtqH91b1HxQnOgAlgi8oiCC5VVcH0ejoHiLg3dRfZA3a1HbrDm0mgv3UZANE0l449nYVHgvKtrqBCaXltf2LjImZBQcn1DvrCgz36xpkzE1B5igwH80o8rik4UtaAZB2ZCDxjRgnOEQLxbmyTojbXsfx63DzG");
		data.setFacebookID("1025788014115458");
		User user = userService.signin(data);
		
		return user;
	}
}
