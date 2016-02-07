/*
 * Copyright 2015 - 2016 Egor Andreevici
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package me.egorand.cats.analytics;

import com.google.android.gms.analytics.Tracker;

import org.junit.Test;

import javax.inject.Inject;

import me.egorand.cats.TestBase;

import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.verify;

public class AnalyticsTrackerHelperTest extends TestBase {

    @Inject Tracker mockAnalyticsTracker;

    private AnalyticsTrackerHelper analyticsTrackerHelper;

    @Override
    public void setUp() {
        super.setUp();
        appComponent().inject(this);
        analyticsTrackerHelper = new AnalyticsTrackerHelper(mockAnalyticsTracker);
    }

    @Test
    public void shouldTrackScreenViewProperly() {
        analyticsTrackerHelper.trackScreenView(AnalyticsTags.SCREEN_CAT_VIEWER_ACTIVITY);

        verify(mockAnalyticsTracker).setScreenName(AnalyticsTags.SCREEN_CAT_VIEWER_ACTIVITY);
        verify(mockAnalyticsTracker).send(anyMap());
    }

    @Test
    public void shouldTrackEventProperly() {
        analyticsTrackerHelper.trackEvent(AnalyticsTags.CATEGORY_ACTION, AnalyticsTags.ACTION_SHARE);

        verify(mockAnalyticsTracker).send(anyMap());
    }
}