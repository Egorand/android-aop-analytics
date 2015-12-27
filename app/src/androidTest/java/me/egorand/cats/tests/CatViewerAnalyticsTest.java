/*
 * Copyright 2015 Egor Andreevici
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

package me.egorand.cats.tests;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.RequestManager;
import com.google.android.gms.analytics.Tracker;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import me.egorand.cats.TestCatsApp;
import me.egorand.cats.analytics.AnalyticsTags;
import me.egorand.cats.data.ImgurGalleryItem;
import me.egorand.cats.ui.activities.CatViewerActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CatViewerAnalyticsTest {

    private static final ImgurGalleryItem TEST_CAT_IMAGE = new ImgurGalleryItem(
            "id1", "Test Cat", "image/jpeg", "http://i.imgur.com/ic4jkov.jpg"
    );

    @Rule public ActivityTestRule<CatViewerActivity> activityTestRule
            = new ActivityTestRule<>(CatViewerActivity.class, true, false);

    @Inject RequestManager mockGlideRequestManager;
    @Inject Tracker mockAnalyticsTracker;

    @Before
    public void setUp() {
        TestCatsApp app = (TestCatsApp) InstrumentationRegistry.getTargetContext().getApplicationContext();
        app.appComponent().inject(this);
        initMockGlideRequestManager();
        initMockAnalyticsTracker();

        Intent intent = CatViewerActivity.withCatImage(app, TEST_CAT_IMAGE);
        activityTestRule.launchActivity(intent);
    }

    private void initMockGlideRequestManager() {
        when(mockGlideRequestManager.load(TEST_CAT_IMAGE.getLink()))
                .thenReturn(mock(DrawableTypeRequest.class));
    }

    private void initMockAnalyticsTracker() {
        reset(mockAnalyticsTracker);
    }

    @Test
    public void shouldTrackScreenViewedWithLink() {
        verify(mockAnalyticsTracker).setScreenName(AnalyticsTags.SCREEN_CAT_VIEWER_ACTIVITY);
        verify(mockAnalyticsTracker).send(argThat(isMapWithValues("screenview")));
    }

    @Test
    public void shouldTrackImageSharingAction() {
        onView(withId(android.support.v7.appcompat.R.id.default_activity_button)).perform(click());

        verify(mockAnalyticsTracker).send(argThat(isMapWithValues("event",
                AnalyticsTags.CATEGORY_ACTION, AnalyticsTags.ACTION_SHARE)));
    }

    private static Matcher<Map<String, String>> isMapWithValues(String... values) {
        return new TypeSafeMatcher<Map<String, String>>() {
            @Override
            protected boolean matchesSafely(Map<String, String> item) {
                return setOf(values).equals(new HashSet<>(item.values()));
            }

            private Set<String> setOf(String... values) {
                Set<String> result = new HashSet<>();
                Collections.addAll(result, values);
                return result;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is a Map that contains values " + Arrays.toString(values));
            }
        };
    }
}
