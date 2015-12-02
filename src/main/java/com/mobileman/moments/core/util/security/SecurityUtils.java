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
package com.mobileman.moments.core.util.security;

import java.util.Random;

import org.springframework.security.core.context.SecurityContextHolder;

import com.mobileman.moments.core.domain.user.User;
import com.mobileman.moments.core.security.UserDetail;

public class SecurityUtils {

	public static User getLoggedUser() {
		if (SecurityContextHolder.getContext() == null || SecurityContextHolder.getContext().getAuthentication() == null) {
			return null;
		}
		
		Object principalObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principalObj == null || String.class.isInstance(principalObj)) {
			return null;
		}
		
		UserDetail principal = (UserDetail)principalObj;
		return principal.getSysUser();
	}
	
	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static Random randomPassword = new Random();

    public static String randomPassword( ) {
        int len = 8;
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( randomPassword.nextInt(AB.length()) ) );
        return sb.toString();
    }
    
    public static String randomUserName( ) {
        int len = 8;
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ ) {
            sb.append( AB.charAt( randomPassword.nextInt(AB.length()) ) );
        }
        
        return sb.toString().toLowerCase();
    }
}
