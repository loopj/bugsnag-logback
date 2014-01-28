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
import com.codereligion.bugsnag.logback.integration.CustomMetaDataProvider;
import com.codereligion.bugsnag.logback.Configuration;
import com.google.common.collect.Sets;
import java.util.HashSet;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class ConfigurationTest {

    private final Configuration configuration = new Configuration();

    @Test
    public void hasDefaultEndPoint() {
        assertThat(configuration.getEndpoint(), is("notify.bugsnag.com"));
    }

    @Test
    public void defaultsToProductionReleaseStage() {
        assertThat(configuration.getReleaseStage(), is("production"));
    }

    @Test
    public void doesNotDefaultToSSL() {
        assertThat(configuration.isSslEnabled(), is(false));
    }

    @Test
    public void hasNoDefaultApiKeyValue() {
        assertThat(configuration.getApiKey(), is(nullValue()));
    }

    @Test
    public void isValidForDefaultValuesWhenAllRequiredFieldsArePresent() {
        configuration.setApiKey("someKey");
        assertThat(configuration.isInvalid(), is(false));
    }

    @Test
    public void isInvalidWhenEndpointIsNull() {
        configuration.setEndpoint(null);
        configuration.setApiKey("someKey");
        assertThat(configuration.isInvalid(), is(true));
    }

    @Test
    public void isInvalidWhenEndpointIsEmpty() {
        configuration.setEndpoint("");
        configuration.setApiKey("someKey");
        assertThat(configuration.isInvalid(), is(true));
    }

    @Test
    public void isInvalidWhenApiKeyIsNull() {
        configuration.setApiKey(null);
        assertThat(configuration.isInvalid(), is(true));
    }

    @Test
    public void isInvalidWhenApiKeyIsEmpty() {
        configuration.setApiKey("");
        assertThat(configuration.isInvalid(), is(true));
    }

    @Test
    public void isInvalidWhenReleaseStageIsNull() {
        configuration.setApiKey("someKey");
        configuration.setReleaseStage(null);
        assertThat(configuration.isInvalid(), is(true));
    }

    @Test
    public void isInvalidWhenReleaseStageIsEmpty() {
        configuration.setApiKey("someKey");
        configuration.setReleaseStage("");
        assertThat(configuration.isInvalid(), is(true));
    }

    @Test
    public void isInvalidWhenMetaProviderClassIsPresentButNotLoadable() {
        configuration.setApiKey("someKey");
        configuration.setMetaDataProviderClassName("SomeRandomClassName");
        assertThat(configuration.isInvalid(), is(true));
    }

    @Test
    public void isValidWhenMetaProviderClassIsPresentAndLoadable() {
        configuration.setApiKey("someKey");
        configuration.setMetaDataProviderClassName(CustomMetaDataProvider.class.getCanonicalName());
        assertThat(configuration.isInvalid(), is(false));
    }

    @Test
    public void isValidWhenMetaProviderClassIsNotPresent() {
        configuration.setApiKey("someKey");
        configuration.setMetaDataProviderClassName(null);
        assertThat(configuration.isInvalid(), is(false));
    }

    @Test
    public void addsNoErrorsForValidConfiguration() {
        // given
        configuration.setApiKey("someKey");
        final ContextAware contextAware = mock(ContextAware.class);

        // when
        configuration.addErrors(contextAware);

        // then
        verifyZeroInteractions(contextAware);
    }

    @Test
    public void addsErrorWhenEndpointIsInvalid() {
        // given
        configuration.setApiKey("someKey");
        configuration.setEndpoint(null);
        final ContextAware contextAware = mock(ContextAware.class);

        // when
        configuration.addErrors(contextAware);

        // then
        verify(contextAware).addError("endpoint must not be null nor empty");
    }

    @Test
    public void addsErrorWhenApiKeyIsInvalid() {
        // given
        configuration.setApiKey(null);
        final ContextAware contextAware = mock(ContextAware.class);

        // when
        configuration.addErrors(contextAware);

        // then
        verify(contextAware).addError("apiKey must not be null nor empty");
    }

    @Test
    public void addsErrorWhenReleaseStageIsInvalid() {
        // given
        configuration.setApiKey("someKey");
        configuration.setReleaseStage(null);
        final ContextAware contextAware = mock(ContextAware.class);

        // when
        configuration.addErrors(contextAware);

        // then
        verify(contextAware).addError("releaseStage must not be null nor empty");
    }

    @Test
    public void addsErrorWhenProviderClassNameIsInvalid() {
        // given
        configuration.setApiKey("someKey");
        configuration.setMetaDataProviderClassName("foo.bar.SomeRandomClass");
        final ContextAware contextAware = mock(ContextAware.class);

        // when
        configuration.addErrors(contextAware);

        // then
        verify(contextAware).addError("Could not instantiate class: foo.bar.SomeRandomClass. " +
                "Make sure that you provided the fully qualified class name and that the class has public access.");
    }

    @Test
    public void prefixesEndpointWithHttpsWhenSpecified() {
        configuration.setEndpoint("localhost");
        configuration.setSslEnabled(true);
        assertThat(configuration.getEndpointWithProtocol(), is("https://localhost"));
    }

    @Test
    public void prefixesEndpointWithHttpWhenSpecified() {
        configuration.setEndpoint("localhost");
        configuration.setSslEnabled(false);
        assertThat(configuration.getEndpointWithProtocol(), is("http://localhost"));
    }

    @Test
    public void ignoresStageWhenNotPresentInTheNotifyReleaseStages() {
        configuration.setNotifyReleaseStages(Sets.newHashSet("live"));
        configuration.setReleaseStage("staging");

        assertThat(configuration.isStageIgnored(), is(true));
    }

    @Test
    public void doesNotIgnoreStageWhenPresentInTheNotifyReleaseStages() {
        configuration.setNotifyReleaseStages(Sets.newHashSet("staging", "live"));
        configuration.setReleaseStage("staging");

        assertThat(configuration.isStageIgnored(), is(false));
    }

    @Test
    public void doesNotIgnoreStageWhenTheNotifyReleaseStagesAreEmpty() {
        configuration.setNotifyReleaseStages(new HashSet<String>());
        configuration.setReleaseStage("staging");

        assertThat(configuration.isStageIgnored(), is(false));
    }

    @Test
    public void classInProjectPackageIsInProject() {
        configuration.setProjectPackages(Sets.newHashSet("some.package"));
        assertThat(configuration.isInProject("some.package.ClassName"), is(true));
    }

    @Test
    public void classInAnotherPackageIsNotInProject() {
        configuration.setProjectPackages(Sets.newHashSet("some.package"));
        assertThat(configuration.isInProject("some.other.package.ClassName"), is(false));
    }

    @Test
    public void shouldNotNotifyForIgnoredClass() {
        configuration.setIgnoreClasses(Sets.newHashSet("some.ClassName"));

        assertThat(configuration.shouldNotifyFor("some.ClassName"), is(false));
    }

    @Test
    public void shouldNotifyForNotIgnoredClass() {
        configuration.setIgnoreClasses(Sets.newHashSet("some.ClassName"));

        assertThat(configuration.shouldNotifyFor("some.OtherClassName"), is(true));
    }

    @Test
    public void ignoresFilteredKeys() {
        configuration.setFilters(Sets.newHashSet("password"));

        assertThat(configuration.isIgnoredByFilter("password"), is(true));
    }

    @Test
    public void doesNotIgnoreUnFilteredKeys() {
        configuration.setFilters(Sets.newHashSet("password"));

        assertThat(configuration.isIgnoredByFilter("otherSecretField"), is(false));
    }

    @Test
    public void hasMetaDataProviderWhenConfigured() {
        configuration.setMetaDataProviderClassName(CustomMetaDataProvider.class.getCanonicalName());

        assertThat(configuration.hasMetaDataProvider(), is(true));
    }
}