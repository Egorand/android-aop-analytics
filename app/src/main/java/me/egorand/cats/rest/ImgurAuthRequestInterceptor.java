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

import retrofit.RequestInterceptor;

public class ImgurAuthRequestInterceptor implements RequestInterceptor {

    private static final String HEADER_AUTH_NAME = "Authorization";
    private static final String HEADER_AUTH_VALUE_TEMPLATE = "Client-ID %s";

    private final String clientId;

    public ImgurAuthRequestInterceptor(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public void intercept(RequestFacade request) {
        request.addHeader(HEADER_AUTH_NAME, String.format(HEADER_AUTH_VALUE_TEMPLATE, clientId));
    }
}
