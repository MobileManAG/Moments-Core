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

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.validation.ValidationException;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.mongodb.core.CollectionCallback;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.mobileman.moments.core.domain.dto.broadcast.BroadcastData;
import com.mobileman.moments.core.domain.dto.user.ForgotPasswordData;
import com.mobileman.moments.core.domain.dto.user.SigninData;
import com.mobileman.moments.core.domain.dto.user.SignupData;
import com.mobileman.moments.core.domain.dto.user.TokensData;
import com.mobileman.moments.core.domain.dto.user.UserProfileUpdateData;
import com.mobileman.moments.core.domain.question.Question;
import com.mobileman.moments.core.domain.stream.Stream;
import com.mobileman.moments.core.domain.stream.StreamState;
import com.mobileman.moments.core.domain.user.Account;
import com.mobileman.moments.core.domain.user.User;
import com.mobileman.moments.core.domain.user.role.UserRole;
import com.mobileman.moments.core.repositories.user.AccountRepository;
import com.mobileman.moments.core.repositories.user.UserRepository;
import com.mobileman.moments.core.services.esb.MomentsESBConstants;
import com.mobileman.moments.core.services.impl.EntityServiceImpl;
import com.mobileman.moments.core.services.mail.MailService;
import com.mobileman.moments.core.services.notification.NotificationService;
import com.mobileman.moments.core.services.question.QuestionService;
import com.mobileman.moments.core.services.social.FriendshipService;
import com.mobileman.moments.core.services.social.facebook.FacebookService;
import com.mobileman.moments.core.services.stream.StreamService;
import com.mobileman.moments.core.services.user.UserService;
import com.mobileman.moments.core.util.security.SecurityUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;

@Service
public class UserServiceImpl extends EntityServiceImpl<User> implements UserService {
	
	private UserRepository userRepository;
	
	@Autowired
	private AccountRepository accountRepository;
		
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private FacebookService facebookService;
	
	@Autowired
	private StreamService streamService;
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private FriendshipService friendshipService;
	
	@Autowired
	Environment environment;
	
	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		setEntityRespository(userRepository);
		this.userRepository = userRepository;
	}

	@Override
	public User findByEmail(String email) {
		Account account = this.accountRepository.findByEmail(email);
		User user = null;
		if (account !=null) {
			user = this.userRepository.findByAccount(account);
		}
		
		return user;
	}

	@Override
	public User signup(SignupData data) {
		
		if (StringUtils.isEmpty(data.getEmail()) && StringUtils.isEmpty(data.getFacebookID())) {
			throw new ValidationException("Either email or FB ID must be provided");
		}
		
		cleanupExistingAccontWithoutEmail(data.getDeviceToken());
		
		User user = new User(new ObjectId().toString());
		
		user.setAccount(new Account(new ObjectId().toString()));
		user.getAccount().setCreatedOn(new Date());
		user.getAccount().setModifiedOn(user.getCreatedOn());
		user.getAccount().setEmail(data.getEmail());
		user.getAccount().setDeviceToken(data.getDeviceToken());
		user.getAccount().setPushNotificationID(data.getPushNotificationID());
		user.getAccount().setPushNotificationToken(data.getPushNotificationToken());
		user.getAccount().setRole(UserRole.USER);
		user.getAccount().setToken(data.getToken());
		
		accountRepository.save(user.getAccount());
		
		user.setCreatedOn(new Date());
		user.setModifiedOn(user.getCreatedOn());
		user.setGender(data.getGender());
		user.setUserName(data.getUserName());
		user.setFirstName(data.getFirstName());
		user.setLastName(data.getLastName());
		user.setFacebookId(data.getFacebookID());
		userRepository.save(user);
		
		this.facebookService.updateFriendsOfUser(user);
		
		return user;
	}

	/**
	 * @param data
	 */
	private void cleanupExistingAccontWithoutEmail(String deviceToken) {
		if (!StringUtils.hasText(deviceToken)) {
			return;
		}
		
		Account existingAccount = this.accountRepository.findByDeviceToken(deviceToken);
		if (existingAccount != null) {
			if (!StringUtils.hasText(existingAccount.getEmail())) {
				// we have existing account associated with given device ID and it is not paired with any email yet
				// set to this account new random device ID
				existingAccount.setDeviceToken(UUID.randomUUID().toString());
				this.accountRepository.save(existingAccount);
			}
		}
	}

	@Override
	public boolean delete(String id) {
		User user = findById(id);
		if (user == null) {
			return false;
		}
		
		this.accountRepository.delete(user.getAccount());
				
		for (Question question : questionService.findQuestionsIdCreatedByUser(user)) {
			questionService.delete(question);
		}
		
		if (super.delete(id)) {
			return true;
		}
		
		return false;
	}

	@Override
	public User updateProfile(User loggedUser, UserProfileUpdateData data) {
		if (loggedUser == null) {
			throw new ValidationException("User does not exists");
		}
		
		if (data == null) {
			throw new ValidationException("Data does not exists");
		}
		
		Account account = this.accountRepository.findOne(loggedUser.getAccount().getId());
		if (account == null && StringUtils.hasText(data.getDeviceToken())) {
			account = this.accountRepository.findByDeviceToken(data.getDeviceToken());
		}
		
		if (account == null) {
			throw new ValidationException("User account does not exists");
		}
		
		boolean saveUser = false;
		boolean saveAccount = false;
		
		if (StringUtils.hasText(data.getEmail())) {
			account.setEmail(data.getEmail());
			// if user has entered email then remove the device ID
			account.setDeviceToken(data.getEmail());
			saveAccount = true;
		}
		
		if (StringUtils.hasText(data.getPassword())) {
			if (!ObjectUtils.nullSafeEquals(data.getPassword(), data.getPasswordRetyped())) {
				//throw new ValidationException("Password and retyped password does not match");
			}
			
			account.setPassword(this.passwordEncoder.encode(data.getPassword()));
			saveAccount = true;
		}
		
		User user = this.userRepository.findByAccount(account);
				
		if (saveAccount) {
			this.accountRepository.save(account);
		}
		
		if (saveUser) {
			this.userRepository.save(user);
		}
		
		return user;
	}

	@Override
	public User signin(SigninData data) {
		Account account = null;
		User user = null;
		if (!StringUtils.isEmpty(data.getEmail())) {
			account = this.accountRepository.findByEmail(data.getEmail());
			if (account != null) {
				user = this.userRepository.findByAccount(account);
				if (user == null) {
					this.accountRepository.delete(account);
					account= null;
				}
			}
			
		} else {
			user = this.userRepository.findByFacebookId(data.getFacebookID());
			if (user != null) {
				account = user.getAccount();
				if (account == null) {
					this.userRepository.delete(user);
					user= null;
				}
			}
		}
				
		if (account == null) {
			// not yet registered by this email
			SignupData signupData = new SignupData();
			signupData.setToken(data.getToken());
			signupData.setEmail(data.getEmail());
			signupData.setUserName(data.getUserName());
			signupData.setFirstName(data.getFirstName());
			signupData.setLastName(data.getLastName());
			signupData.setAccountType(data.getAccount_type());
			signupData.setGender(data.getGender());
			signupData.setDeviceToken(data.getDeviceToken());
			signupData.setPushNotificationToken(data.getPushNotificationToken());
			signupData.setFacebookID(data.getFacebookID());
			signupData.setPushNotificationID(data.getPushNotificationID());
			return signup(signupData);
		} 
		
		account.setDeviceToken(data.getDeviceToken());
		user.getAccount().setPushNotificationToken(data.getPushNotificationToken());
		account.setPushNotificationID(data.getPushNotificationID());
		account.setToken(data.getToken());
		this.accountRepository.save(account);
		
		
		user.setFacebookId(data.getFacebookID());
		user.setGender(data.getGender());
		user.setUserName(data.getUserName());
		user.setFirstName(data.getFirstName());
		user.setLastName(data.getLastName());
		this.userRepository.save(user);
		this.facebookService.updateFriendsOfUser(user);
		return user;
	}

	@Override
	public Account forgotPassword(ForgotPasswordData data) {
		Account account = this.accountRepository.findByEmail(data.getEmail());
		if (account == null) {
			return null;
		}
		
		User user = this.userRepository.findByAccount(account);
		if (user == null) {
			return null;
		}
		
		String password  = SecurityUtils.randomPassword();
		account.setPassword(this.passwordEncoder.encode(password));
		this.accountRepository.save(account);
		
		this.mailService.sendResetCredientialsEmail(user, password);
		
		return account;
	}
		
	@Override
	@ServiceActivator(inputChannel=MomentsESBConstants.QUESTION_DELETED_CHANNEL)
	public void questionDeleted(final Question question) {
		
		mongoTemplate.execute(User.class, new CollectionCallback<Boolean>() {

			@Override
			public Boolean doInCollection(DBCollection collection)
					throws MongoException, DataAccessException {
				
				final Query query = Query.query(Criteria.where("followed_questions._id").is(new ObjectId(question.getId())));
				final Update update = new Update().pull("followed_questions", new BasicDBObject("_id", new ObjectId(question.getId())));				
				WriteResult result = collection.update(query.getQueryObject(), update.getUpdateObject(), false, true);

				return result.getN() > 0;
			}
		});
		
	}

	@Override
	public User findUserProfile(String id) {
		User user = findById(id);
		if (user.getAccount() != null) {
			user.getAccount().setPassword(null);
		}
		
		return user;
	}
	
	@Override
	public Stream startBroadcast(String userId, BroadcastData data) {
		User user = findById(userId);
		if (user == null) {
			throw new ValidationException("User does no exists: " + userId);
		}
		
		Stream stream = this.streamService.createStream(
				data.getText(), data.getLocation(), data.getVideoFileName(), data.getThumbnailFileName());
		user.setStream(stream);
		save(user);
		
		this.facebookService.updateFriendsOfUser(user);
		
		return stream;
	}

	@Override
	public Stream stopBroadcast(String userId) {
		User user = findById(userId);
		if (user == null) {
			throw new ValidationException("User does not exists: " + userId);
		}
		
		Stream stream = user.getStream();
		if (stream != null && StringUtils.hasText(stream.getId())) {
			user.setStream(null);
			save(user);
		}
		
		return stream;
	}
	
	@Override
	public Stream updateStream(String userId, String streamId, Stream data) {
		User user = findById(userId);
		if (user == null) {
			throw new ValidationException("User does not exists: " + userId);
		}
		
		Stream stream = user.getStream();
		if (stream == null || !StringUtils.hasText(stream.getId())) {
			return null;
		}
		
		if (!stream.getId().equals(streamId)) {
			throw new ValidationException("Invalid stream ID: " + streamId);
		}
		
		boolean streamingJustStarted = false; 
		if (data.getState() != null && !data.getState().equals(stream.getState())) {
			stream.setState(data.getState());
			
			if (stream.getState().equals(StreamState.STREAMING)) {
				streamingJustStarted = true;
			}
		}
		
		save(user);
		
		if (streamingJustStarted) {
			try {
				this.notificationService.userStartedBroadcasting(user, stream);
			} catch (Exception e) {
				getLog().error("Error while notify friends: ", e);
			}
		}
		
		return stream;
	}

	@Override
	public void signout(User user) {
		if (user != null) {
			if (user.getAccount() != null) {
				this.accountRepository.delete(user.getAccount());
			}
			
			delete(user);
			this.friendshipService.deleteAllForUser(user);
		}
	}

	@Override
	public Slice<User> liveStreams(String userId, Pageable pageable) {
		User user = findById(userId);
		if (user == null) {
			throw new ValidationException("User does not exists: " + userId);
		}
		
		List<ObjectId> friendsUuid = this.friendshipService.findAllFriendsUuid(userId);
		if (friendsUuid.isEmpty()) {
			return new SliceImpl<User>(Collections.emptyList());
		}
		
		final Query query = Query.query(Criteria.where("id").in(friendsUuid).and("stream.state").is(StreamState.STREAMING)).with(pageable).limit(pageable.getPageSize() + 1);
		query.fields().exclude("account");
		List<User> resultList = this.mongoTemplate.find(query, User.class);
		boolean hasNext = resultList.size() > pageable.getPageSize();
		Slice<User> result = new SliceImpl<User>(hasNext ? resultList.subList(0, pageable.getPageSize()) : resultList, pageable, hasNext);
		//Slice<User> users = this.userRepository.findByIdInAndStreamState(friendsUuid, StreamState.STREAMING, pageable);
		
		if (Arrays.asList(environment.getActiveProfiles()).contains("dev")) {
			for (User streamingUser : result) {
				if (streamingUser.getFacebookId().equals("1")) {
					streamingUser.setFacebookId("1434162610212513");
				} else if (streamingUser.getFacebookId().equals("2")) {
					streamingUser.setFacebookId("1435388136774643");
				} else if (streamingUser.getFacebookId().equals("3")) {
					streamingUser.setFacebookId("1559422084321695");
				} else if (streamingUser.getFacebookId().equals("4")) {
					streamingUser.setFacebookId("1025788014115458");
				}
			}
		}
		
		return result;
	}
	
	@Override
	public void updateTokens(String userId, TokensData data) {
		User user = findById(userId);
		if (user == null) {
			throw new ValidationException("User does not exists: " + userId);
		}
		
		boolean save = false;
		if (StringUtils.hasText(data.getDeviceToken())) {
			user.getAccount().setDeviceToken(data.getDeviceToken());
			save = true;
		}

		if (StringUtils.hasText(data.getPushNotificationToken())) {
			user.getAccount().setPushNotificationToken(data.getPushNotificationToken());
			save = true;
		}
		
		if (save) {
			this.accountRepository.save(user.getAccount());
		}
	}
}
