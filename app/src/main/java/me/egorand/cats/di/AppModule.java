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

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;
import me.egorand.cats.BuildConfig;
import me.egorand.cats.R;
import me.egorand.cats.analytics.AnalyticsTrackerHelper;
import me.egorand.cats.rest.ImgurApiClient;
import me.egorand.cats.rest.ImgurAuthRequestInterceptor;
import retrofit.RestAdapter;

@Module
public class AppModule {

    private final Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides @Singleton public Context provideAppContext() {
        return context;
    }

    @Provides public ImgurApiClient provideImgurApiClient() {
        String clientId = context.getString(R.string.imgur_client_id);
        return new RestAdapter.Builder()
                .setEndpoint(ImgurApiClient.ENDPOINT)
                .setRequestInterceptor(new ImgurAuthRequestInterceptor(clientId))
                .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                .build()
                .create(ImgurApiClient.class);
    }

    @Provides public LayoutInflater provideLayoutInflater() {
        return LayoutInflater.from(context);
    }

    @Provides @Singleton public RequestManager provideGlideRequestManager() {
        return Glide.with(context);
    }

    @Provides @Singleton public EventBus provideEventBus() {
        return EventBus.getDefault();
    }

    @Provides @Singleton public Tracker provideAnalyticsTracker() {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
        return analytics.newTracker(R.xml.global_tracker);
    }

    @Provides @Singleton public AnalyticsTrackerHelper provideAnalyticsTrackerHelper(Tracker tracker) {
        return new AnalyticsTrackerHelper(tracker);
    }
}
