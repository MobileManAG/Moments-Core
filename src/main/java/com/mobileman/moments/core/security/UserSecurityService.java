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
package com.mobileman.moments.core.security;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.mobileman.moments.core.domain.user.User;
import com.mobileman.moments.core.domain.user.role.UserRole;

@Service
public class UserSecurityService implements UserDetailsService {

	public static final String PASSWORD_VERIFIED_ID = "PasswordVerified:!@#$%^&*(){}";

	private static final String PASSWORD_PREFIX1 = "PasswordPrefix1";
	private static final String PASSWORD_PREFIX2 = "PasswordPrefix2";
	private static final String PASSWORD_PREFIX3 = "PasswordPrefix3";

	@Autowired
	private MongoTemplate mongoTemplate;

	@Value("${moments.admin.username}")
	private String adminUsername;

	@Value("${moments.admin.password}")
	private String adminPassword;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {

		final UserDetail userDetail;
		if (username.equalsIgnoreCase(this.adminUsername)) {

			userDetail = new UserDetail(this.adminUsername, this.adminPassword,
					UserRole.ADMIN, null);

		} else {

			User sysUser = getUserDetail(username);
			if (sysUser == null) {
				throw new UsernameNotFoundException("User with email "
						+ username + " not found");
			}

			// check for user exists
			HttpServletRequest request = getHttpServletRequest();
			String tokenB64 = request.getHeader("AuthorizationToken");
			if (tokenB64 != null && !"".equals(tokenB64)) {
				String token = null;
				try {
					token = new String(Base64.getDecoder().decode(tokenB64),
							"UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				UserCacheInfo userCacheInfo = cache.get(username);
				if (userCacheInfo != null) {
					synchronized (userCacheInfo) {
						sanitizeUserCacheInfo(userCacheInfo);
						// try to find token
						Date lastAccess = userCacheInfo.getTokens().get(token);
						if (lastAccess != null) {
							userCacheInfo.getTokens().put(token, new Date());
							userDetail = getUserDetailsAuthenticated(sysUser);
						} else {
							// new device...
							userDetail = getUserDetailsPasswordCheck(sysUser,
									username, token);
						}
					}
				} else {
					userDetail = getUserDetailsPasswordCheck(sysUser, username,
							token);
				}
			} else {
				userDetail = getUserDetails(sysUser);
			}
		}

		return userDetail;
	}

	private void sanitizeUserCacheInfo(UserCacheInfo userCacheInfo) {
		if (false) {
			HashMap<String, Date> tokens = userCacheInfo.getTokens();
			if (tokens.size() > 10) {
				tokens.clear(); // Protection against too many devices
			}
			// remove older items
			Date now = DateUtils.addSeconds(new Date(), -60 * 10);
			HashSet<String> ks = new HashSet<String>(tokens.keySet());
			for (String key : ks) {
				Date d = tokens.get(key);
				if (now.after(d)) {
					tokens.remove(key);
				}
			}
		}
	}

	public void tokenAuthentized(String username, String token) {
		UserCacheInfo userCacheInfo = cache.get(username);
		if (userCacheInfo != null) {
			synchronized (userCacheInfo) {
				sanitizeUserCacheInfo(userCacheInfo);
				userCacheInfo.getTokens().put(token, new Date());
			}
		} else {
			// new cache record
			userCacheInfo = new UserCacheInfo();
			userCacheInfo.getTokens().put(token, new Date());
			cache.put(username, userCacheInfo);
		}
	}

	public static String[] splitPassword(String password) {
		String[] result = new String[3];

		if (password != null && password.startsWith(PASSWORD_PREFIX1)) {
			int i2 = password.indexOf(PASSWORD_PREFIX2);
			int i3 = password.indexOf(PASSWORD_PREFIX3);

			result[0] = password.substring(PASSWORD_PREFIX1.length(), i2);
			result[1] = password.substring(i2 + PASSWORD_PREFIX2.length(), i3);
			result[2] = password.substring(i3 + PASSWORD_PREFIX3.length());
		} else {
			result[2] = password;
		}

		return result;
	}

	private UserDetail getUserDetailsPasswordCheck(User sysUser,
			String username, String token) {
		return getUserDetails(sysUser, PASSWORD_PREFIX1 + username
				+ PASSWORD_PREFIX2 + token + PASSWORD_PREFIX3
				+ sysUser.getAccount().getPassword());
	}

	private UserDetail getUserDetailsAuthenticated(User sysUser) {
		return getUserDetails(sysUser, PASSWORD_VERIFIED_ID);
	}

	private UserDetail getUserDetails(User sysUser) {
		return getUserDetails(sysUser, null);
	}

	private UserDetail getUserDetails(User sysUser, String password) {
		String username;
		password = sysUser.getId();
		if (password == null) {
			password = sysUser.getAccount().getPassword();
		}
		if (StringUtils.isEmpty(sysUser.getAccount().getDeviceToken())) {
			username = sysUser.getAccount().getEmail();
		} else {
			username = sysUser.getAccount().getDeviceToken();
		}
		final UserDetail userDetail = new UserDetail(sysUser.getId(), sysUser.getId(),
				sysUser.getAccount().getRole(), sysUser);
		return userDetail;
	}

	public User getUserDetail(String idToken) {
		User user = this.mongoTemplate
				.findOne(new Query(Criteria.where("id").is(idToken)), User.class);
		if (user == null) {
			throw new UsernameNotFoundException(
					"User account with idToken " + idToken
							+ " not found");
		}
		
		return user;
	}

	private HttpServletRequest getHttpServletRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes()).getRequest();
		return request;
	}

	private ConcurrentHashMap<String, UserCacheInfo> cache = new ConcurrentHashMap<String, UserCacheInfo>();

	static class UserCacheInfo {
		private HashMap<String, Date> tokens = new HashMap<String, Date>();

		public HashMap<String, Date> getTokens() {
			return tokens;
		}
	}
}
