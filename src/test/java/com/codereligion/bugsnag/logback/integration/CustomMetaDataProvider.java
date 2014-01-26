/**
 * Copyright 2014 www.codereligion.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codereligion.bugsnag.logback.integration;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.codereligion.bugsnag.logback.MetaDataProvider;
import com.codereligion.bugsnag.logback.model.MetaDataVO;

public class CustomMetaDataProvider implements MetaDataProvider {

    @Override
    public MetaDataVO provide(final ILoggingEvent loggingEvent) {
        final MetaDataVO metaDataVO = new MetaDataVO();
        metaDataVO.addToTab("Logging", "level", loggingEvent.getLevel().toString());
        metaDataVO.addToTab("Logging", "message", loggingEvent.getMessage());
        metaDataVO.addToTab("User", "password", loggingEvent.getMDCPropertyMap().get("password"));
        return metaDataVO;
    }
}
