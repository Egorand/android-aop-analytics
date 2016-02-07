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

package me.egorand.cats.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;

import javax.inject.Inject;

import me.egorand.cats.CatsApp;
import me.egorand.cats.R;
import me.egorand.cats.analytics.AnalyticsTags;
import me.egorand.cats.analytics.AnalyticsTrackerHelper;
import me.egorand.cats.analytics.annotations.TrackEvent;
import me.egorand.cats.analytics.annotations.TrackScreenView;
import me.egorand.cats.data.ImgurGalleryItem;

public class CatViewerActivity extends AppCompatActivity {

    private static final String ARG_CAT_IMAGE = "cat_image";

    @Inject RequestManager glideRequestManager;
    @Inject AnalyticsTrackerHelper analyticsTrackerHelper;

    private ImgurGalleryItem catImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((CatsApp) getApplication()).appComponent().inject(this);
        setContentView(R.layout.activity_cat_viewer);

        catImage = getIntent().getParcelableExtra(ARG_CAT_IMAGE);

        getSupportActionBar().setTitle(catImage.getTitle());
        loadCatImage();

        trackScreenView(analyticsTrackerHelper, AnalyticsTags.SCREEN_CAT_VIEWER_ACTIVITY);
    }

    @TrackScreenView
    private void trackScreenView(AnalyticsTrackerHelper analyticsTrackerHelper, String screenName) {
        // do nothing, handled by AspectJ
    }

    private void loadCatImage() {
        ImageView catImageView = (ImageView) findViewById(R.id.cat_image);
        glideRequestManager.load(catImage.getLink()).into(catImageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cat_viewer, menu);
        initShareActionProvider(menu);
        return true;
    }

    private void initShareActionProvider(Menu menu) {
        MenuItem shareMenuItem = menu.findItem(R.id.action_share);
        ShareActionProvider actionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareMenuItem);
        Intent shareIntent = new Intent(Intent.ACTION_VIEW);
        shareIntent.setType(catImage.getType());
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(catImage.getLink()));
        actionProvider.setShareIntent(shareIntent);
        actionProvider.setOnShareTargetSelectedListener((source, intent) -> {
            trackSharingAction(analyticsTrackerHelper, AnalyticsTags.CATEGORY_ACTION, AnalyticsTags.ACTION_SHARE);
            return true;
        });
    }

    @TrackEvent
    private void trackSharingAction(AnalyticsTrackerHelper analyticsTrackerHelper, String category, String action) {
        // do nothing, handled by AspectJ
    }

    public static Intent withCatImage(Context context, ImgurGalleryItem catImage) {
        Intent intent = new Intent(context, CatViewerActivity.class);
        intent.putExtra(ARG_CAT_IMAGE, catImage);
        return intent;
    }
}
