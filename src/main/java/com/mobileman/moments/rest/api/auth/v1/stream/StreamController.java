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
package com.mobileman.moments.rest.api.auth.v1.stream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mobileman.moments.core.domain.dto.broadcast.BroadcastData;
import com.mobileman.moments.core.domain.stream.Stream;
import com.mobileman.moments.core.domain.stream.StreamMetadata;
import com.mobileman.moments.core.domain.user.User;
import com.mobileman.moments.core.services.stream.StreamService;
import com.mobileman.moments.core.services.user.UserService;
import com.mobileman.moments.core.util.security.SecurityUtils;
import com.mobileman.moments.rest.api.AbstractMomentsController;
import com.mobileman.moments.rest.api.MomentsRestURIConstants;

@RestController
@RequestMapping(
	value = MomentsRestURIConstants.API_AUTH_V1 + "/streams", 
	consumes = MediaType.APPLICATION_JSON_VALUE, 
	produces = MediaType.APPLICATION_JSON_VALUE)
public class StreamController extends AbstractMomentsController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private StreamService streamService;
	
	@RequestMapping(value="/start", consumes = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.POST)
	public Stream startBroadcast(@Valid @RequestBody() BroadcastData data) {
		Stream stream = this.userService.startBroadcast(SecurityUtils.getLoggedUser().getId(), data);
		return stream;
	}
	
	@RequestMapping(value="/{streamId}/stop", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
	public Stream stopBroadcast(@PathVariable String streamId) {
		Stream stream = this.userService.stopBroadcast(SecurityUtils.getLoggedUser().getId());
		return stream;
	}
	
	@RequestMapping(value="/{streamId}", consumes = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.PUT)
	public Stream updateStream(@PathVariable String streamId, @RequestBody() Stream data) {
		Stream stream = this.userService.updateStream(SecurityUtils.getLoggedUser().getId(), streamId, data);
		return stream;
	}
	
	@RequestMapping(value="/live", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.GET)
	public Slice<User> liveStreams(@PageableDefault(size = 20, page = 0) Pageable pageable) {
		Slice<User> streams = this.userService.liveStreams(SecurityUtils.getLoggedUser().getId(), pageable);
		return streams;
	}
	
	@RequestMapping(value="/{id}/metadata", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.GET)
	public StreamMetadata streamMetadata(@PathVariable String id) {
		StreamMetadata metadata = this.streamService.getStreamMetadata(id, SecurityUtils.getLoggedUser().getId());
		return metadata;
	}
	
	@RequestMapping(value="/{id}/join", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.PUT)
	public StreamMetadata joinBroadcast(@PathVariable String id) {
		StreamMetadata metadata = this.streamService.joinBroadcast(id, SecurityUtils.getLoggedUser().getId());
		return metadata;
	}
	
	@RequestMapping(value="/{id}/leave", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.PUT)
	public StreamMetadata leaveBroadcast(@PathVariable String id) {
		StreamMetadata metadata = this.streamService.leaveBroadcast(id, SecurityUtils.getLoggedUser().getId());
		return metadata;
	}
}
