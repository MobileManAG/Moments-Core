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
package com.mobileman.moments.core.services.esb;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Payload;

import com.mobileman.moments.core.domain.Entity;
import com.mobileman.moments.core.domain.question.Question;

@MessagingGateway(name="momentsGateway")
public interface MomentsGateway {

	@Gateway(requestChannel=MomentsESBConstants.ENTITY_CREATED_CHANNEL)
	void entityCreated(@Payload Entity entity);
	
	@Gateway(requestChannel=MomentsESBConstants.ENTITY_DELETED_CHANNEL)
	void entityDeleted(@Payload Entity entity);
	
	
	@Gateway(requestChannel=MomentsESBConstants.QUESTION_CREATED_CHANNEL)
	void questionCreated(@Payload Question entity);
	
	@Gateway(requestChannel=MomentsESBConstants.QUESTION_DELETED_CHANNEL)
	void questionDeleted(@Payload Question entity);
}
