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
package com.mobileman.moments.core.services.stream.impl;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.ValidationException;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mobileman.moments.core.domain.stream.Stream;
import com.mobileman.moments.core.domain.stream.StreamMetadata;
import com.mobileman.moments.core.domain.stream.StreamState;
import com.mobileman.moments.core.domain.user.Location;
import com.mobileman.moments.core.domain.user.User;
import com.mobileman.moments.core.repositories.user.UserRepository;
import com.mobileman.moments.core.services.stream.StreamService;

@Service
public class StreamServiceImpl implements StreamService {
	
	@Autowired
	private UserRepository userRepository;
		
	@Value("${storage.bucket.name}") 
	private String bucketName;
	
	@Value("${storage.base.url.fmt}") 
	private String baseUrFmt;
	
	private final DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	
	private final DateFormat timeFormat = new SimpleDateFormat("HHmmss");
	
	public String getBucketName() {
		return bucketName;
	}
	
	private String formatDayPart(Date date) {
		synchronized (dateFormat) {
			String string = dateFormat.format(date);
			return string;
		}
	}
	
	private String formatTimePart(Date date) {
		synchronized (timeFormat) {
			String string = timeFormat.format(date);
			return string;
		}
	}
	
	private String generatePathPrefix() {
		final Date now = new Date();
		String result = "streams/" + formatDayPart(now) + "/" + formatTimePart(now);
		return result;
	}

	
	
	@Override
	public Stream createStream(String text, Location location, String videoFileName, String thumbnailFileName) {
		
		if (videoFileName == null) {
			videoFileName = "index.m3u8";
		}
		
		if (thumbnailFileName == null) {
			thumbnailFileName = "thumb.jpg";
		}
		
		Stream stream = new Stream(new ObjectId().toString());
		stream.setState(StreamState.CREATED);
		stream.setCreatedOn(new Date());
		stream.setModifiedOn(stream.getCreatedOn());
		stream.setText(text);
		stream.setLocation(location);
		stream.setBucketName(getBucketName());
		String pathPrefix = generatePathPrefix();
		stream.setPathPrefix(pathPrefix);
		stream.setVideoFileName(videoFileName);
		stream.setThumbnailFileName(thumbnailFileName);
		String baseUrl = MessageFormat.format(this.baseUrFmt, new Object[]{
				pathPrefix,
				stream.getId()
		});
		stream.setBaseUrl(baseUrl);
		return stream;
	}

	@Override
	public StreamMetadata getStreamMetadata(String streamId, String watcherId) {
		StreamMetadata metadata = new StreamMetadata();
		metadata.setState(StreamState.CLOSED);
		
		User watcher = this.userRepository.findOne(watcherId);
		if (watcher == null) {
			return metadata;
		}
		
		User broadcastingUser = this.userRepository.findByStreamId(new ObjectId(streamId));
		if (broadcastingUser == null || broadcastingUser.getStream() == null) {
			return metadata;
		}
		
		metadata.setState(broadcastingUser.getStream().getState());
		return metadata;
	}

	@Override
	public StreamMetadata joinBroadcast(String streamId, String userId) {
		StreamMetadata metadata = new StreamMetadata();
		metadata.setState(StreamState.CLOSED);
		
		User watcher = this.userRepository.findOne(userId);
		if (watcher == null) {
			return metadata;
		}
		
		User broadcastingUser = this.userRepository.findByStreamId(new ObjectId(streamId));
		if (broadcastingUser == null || broadcastingUser.getStream() == null) {
			return metadata;
		}
		
		if (broadcastingUser.equals(watcher)) {
			throw new ValidationException("Joining an owned broadcast not allowd: uid=" + userId);
		}
		
		return metadata;
	}

	@Override
	public StreamMetadata leaveBroadcast(String streamId, String userId) {
		StreamMetadata metadata = new StreamMetadata();
		metadata.setState(StreamState.CLOSED);
		
		User watcher = this.userRepository.findOne(userId);
		if (watcher == null) {
			return metadata;
		}
		
		User broadcastingUser = this.userRepository.findByStreamId(new ObjectId(streamId));
		if (broadcastingUser == null || broadcastingUser.getStream() == null) {
			return metadata;
		}
		
		return metadata;
	}

}
