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
package com.mobileman.moments.core.services.stream;

import com.mobileman.moments.core.domain.stream.Stream;
import com.mobileman.moments.core.domain.stream.StreamMetadata;
import com.mobileman.moments.core.domain.user.Location;

public interface StreamService {

	Stream createStream(String text, Location location, String videoFileName, String thumbnailFileName);

	StreamMetadata getStreamMetadata(String streamId, String userId);

	StreamMetadata joinBroadcast(String streamId, String userId);

	StreamMetadata leaveBroadcast(String streamId, String userId);
}
