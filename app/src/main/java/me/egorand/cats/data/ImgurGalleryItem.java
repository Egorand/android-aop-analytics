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

package me.egorand.cats.data;

import android.os.Parcel;
import android.os.Parcelable;

public class ImgurGalleryItem implements Parcelable {

    private static final String IMAGE_MIME_TYPE_PREFIX = "image/";
    private static final String TYPE_JPEG = "jpg";

    private static final String IMAGE_SUFFIX = ".jpg";
    private static final String THUMBNAIL_SUFFIX = "b.jpg";

    private String id;
    private String title;
    private String type;
    private String link;

    public ImgurGalleryItem() {
    }

    public ImgurGalleryItem(String id, String title, String type, String link) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.link = link;
    }

    private ImgurGalleryItem(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.type = in.readString();
        this.link = in.readString();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getThumbnailLink() {
        return link.replace(IMAGE_SUFFIX, THUMBNAIL_SUFFIX);
    }

    public String getLink() {
        return link;
    }

    public boolean isImage() {
        return type != null && type.startsWith(IMAGE_MIME_TYPE_PREFIX) &&
                link != null && link.endsWith(TYPE_JPEG);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(type);
        dest.writeString(link);
    }

    public static final Creator<ImgurGalleryItem> CREATOR = new Creator<ImgurGalleryItem>() {
        @Override
        public ImgurGalleryItem createFromParcel(Parcel source) {
            return new ImgurGalleryItem(source);
        }

        @Override
        public ImgurGalleryItem[] newArray(int size) {
            return new ImgurGalleryItem[size];
        }
    };
}
