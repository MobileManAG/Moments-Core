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
package com.mobileman.moments.core.domain.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mobileman.moments.core.domain.user.Gender;
import com.mobileman.moments.core.domain.user.User;
import com.mobileman.moments.core.domain.utils.UserUtils;

public class UserUtilsTest {

	@Test
	public void createBasicUser() throws Exception {
		assertNull(UserUtils.createBasicUser(null));
		
		User source = new User("1");
		source.setUserName("a");
		source.setGender(Gender.FEMALE);
		
		User copy = UserUtils.createBasicUser(source);
		assertEquals("1", copy.getId());
		assertEquals("a", copy.getUserName());
		assertEquals(Gender.FEMALE, copy.getGender());

	}
}
