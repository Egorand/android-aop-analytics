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

package me.egorand.cats.rest;

import me.egorand.cats.data.ImgurResponse;
import me.egorand.cats.data.ImgurTag;
import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public interface ImgurApiClient {

    Endpoint ENDPOINT = Endpoints.newFixedEndpoint("https://api.imgur.com/3");

    @GET("/gallery/t/{tag_name}")
    Observable<ImgurResponse<ImgurTag>> getTag(@Path("tag_name") String tagName);
}
