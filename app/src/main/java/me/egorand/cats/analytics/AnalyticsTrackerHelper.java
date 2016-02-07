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

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import javax.inject.Inject;

public class AnalyticsTrackerHelper {

    private final Tracker analyticsTracker;

    @Inject
    public AnalyticsTrackerHelper(Tracker analyticsTracker) {
        this.analyticsTracker = analyticsTracker;
    }

    public void trackScreenView(String screenName) {
        analyticsTracker.setScreenName(screenName);
        analyticsTracker.send(
                new HitBuilders.ScreenViewBuilder()
                        .build()
        );
    }

    public void trackEvent(String category, String action) {
        analyticsTracker.send(
                new HitBuilders.EventBuilder()
                        .setCategory(category)
                        .setAction(action)
                        .build()
        );
    }
}
