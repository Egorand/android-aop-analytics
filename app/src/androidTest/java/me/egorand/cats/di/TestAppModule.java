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

package me.egorand.cats.di;

import android.content.Context;
import android.view.LayoutInflater;

import com.bumptech.glide.RequestManager;
import com.google.android.gms.analytics.Tracker;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;
import me.egorand.cats.rest.ImgurApiClient;

@Module
public class TestAppModule {

    @Mock ImgurApiClient mockImgurApiClient;
    @Mock RequestManager mockGlideRequestManager;
    @Mock Tracker mockAnalyticsTracker;

    private final Context context;

    public TestAppModule(Context context) {
        this.context = context;
        MockitoAnnotations.initMocks(this);
    }

    @Provides @Singleton public Context provideAppContext() {
        return context;
    }

    @Provides public ImgurApiClient provideImgurApiClient() {
        return mockImgurApiClient;
    }

    @Provides public LayoutInflater provideLayoutInflater() {
        return LayoutInflater.from(context);
    }

    @Provides @Singleton public RequestManager provideGlideRequestManager() {
        return mockGlideRequestManager;
    }

    @Provides @Singleton public EventBus provideEventBus() {
        return EventBus.getDefault();
    }

    @Provides @Singleton public Tracker provideAnalyticsTracker() {
        return mockAnalyticsTracker;
    }
}
