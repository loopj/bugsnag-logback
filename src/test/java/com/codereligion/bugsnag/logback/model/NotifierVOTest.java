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
package com.codereligion.bugsnag.logback.model;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class NotifierVOTest {

    @Test
    public void providesName() {
        assertThat(new NotifierVO().getName(), is("Bugsnag Logback Notifier"));
    }

    @Test
    public void providesVersion() {
        assertThat(new NotifierVO().getVersion(), is("1.0.0"));
    }

    @Test
    public void providesUrl() {
        assertThat(new NotifierVO().getUrl(), is("https://github.com/codereligion/bugsnag-logback"));
    }
}
