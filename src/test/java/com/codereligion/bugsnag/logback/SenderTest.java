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
package com.codereligion.bugsnag.logback;

import ch.qos.logback.core.spi.ContextAware;
import com.codereligion.bugsnag.logback.Configuration;
import com.codereligion.bugsnag.logback.Sender;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

// TODO test different response behaviours
public class SenderTest {

    private Sender sender = new Sender();

    @Test
    public void isStartedAfterStart() {
        sender.start(mock(Configuration.class), mock(ContextAware.class));
        assertThat(sender.isStarted(), is(true));
    }

    @Test
    public void isStoppedAfterStart() {
        sender.start(mock(Configuration.class), mock(ContextAware.class));
        sender.stop();
        assertThat(sender.isStopped(), is(true));
    }
}
