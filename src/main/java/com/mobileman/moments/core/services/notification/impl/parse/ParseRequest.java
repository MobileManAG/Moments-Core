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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.mobileman.moments.core.domain.notification.Notification;

@JsonInclude(value=Include.NON_NULL)
public class ParseRequest {

	private ParseQuery where;
	
	private Notification data;

	/**
	 * @return the where
	 */
	public ParseQuery getWhere() {
		return where;
	}

	/**
	 * @param where the where to set
	 */
	public void setWhere(ParseQuery where) {
		this.where = where;
	}

	/**
	 * @return the data
	 */
	public Notification getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Notification data) {
		this.data = data;
	}
	
	
}
