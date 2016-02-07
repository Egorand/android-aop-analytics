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

package me.egorand.cats.server;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import me.egorand.cats.R;
import me.egorand.cats.data.ImgurResponse;
import me.egorand.cats.data.ImgurTag;
import me.egorand.cats.rest.ImgurApiClient;
import me.egorand.cats.rest.ImgurAuthRequestInterceptor;
import retrofit.RestAdapter;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class ImgurApiServerTest {

    private static final String TAG_NAME = "cats";

    private ImgurApiClient imgurApiClient;

    @Before
    public void setUp() {
        String clientId = InstrumentationRegistry.getTargetContext().getString(R.string.imgur_client_id);
        imgurApiClient = new RestAdapter.Builder()
                .setEndpoint(ImgurApiClient.ENDPOINT)
                .setRequestInterceptor(new ImgurAuthRequestInterceptor(clientId))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build()
                .create(ImgurApiClient.class);
    }

    @Test
    public void shouldLoadProperTagData() {
        ImgurResponse<ImgurTag> response = imgurApiClient.getTag(TAG_NAME).toBlocking().first();

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getData()).isNotNull();

        ImgurTag tagData = response.getData();
        assertThat(tagData.getName()).isEqualTo(TAG_NAME);
        assertThat(tagData.getItems()).isNotEmpty();
    }
}
