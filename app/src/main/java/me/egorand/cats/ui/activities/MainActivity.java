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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import me.egorand.cats.CatsApp;
import me.egorand.cats.R;
import me.egorand.cats.data.CatImageSelectedEvent;
import me.egorand.cats.rest.ImgurApiClient;
import me.egorand.cats.ui.adapters.CatsAdapter;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String LOGTAG = MainActivity.class.getSimpleName();

    @Inject CatsAdapter catsAdapter;
    @Inject ImgurApiClient imgurApiClient;
    @Inject EventBus eventBus;

    private View progress;

    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((CatsApp) getApplication()).appComponent().inject(this);
        setContentView(R.layout.activity_main);

        initCatsList();
        progress = findViewById(android.R.id.progress);

        loadCats();
    }

    private void initCatsList() {
        RecyclerView catsGrid = (RecyclerView) findViewById(R.id.cats_grid);
        catsGrid.setLayoutManager(new GridLayoutManager(this,
                getResources().getInteger(R.integer.cats_grid_span_count)));
        catsGrid.setAdapter(catsAdapter);
    }

    private void loadCats() {
        subscription = imgurApiClient.getTag("cats")
                .flatMap(response -> Observable.from(response.getData().getItems()))
                .filter(item -> item.isImage())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(catImage -> {
                    progress.setVisibility(View.GONE);
                    catsAdapter.addCatImage(catImage);
                }, throwable -> {
                    progress.setVisibility(View.GONE);
                    Log.e(LOGTAG, throwable.getLocalizedMessage(), throwable);
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        eventBus.register(this);
    }

    @Subscribe
    public void onEvent(CatImageSelectedEvent event) {
        Intent viewIntent = CatViewerActivity.withCatImage(this, event.catImage);
        startActivity(viewIntent);
    }

    @Override
    protected void onPause() {
        eventBus.unregister(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }
}
