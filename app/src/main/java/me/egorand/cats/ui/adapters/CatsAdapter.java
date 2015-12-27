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

package me.egorand.cats.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import me.egorand.cats.R;
import me.egorand.cats.data.CatImageSelectedEvent;
import me.egorand.cats.data.ImgurGalleryItem;

public class CatsAdapter extends RecyclerView.Adapter<CatsAdapter.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final RequestManager glideRequestManager;
    private final EventBus eventBus;

    private final List<ImgurGalleryItem> catImages;

    @Inject
    public CatsAdapter(LayoutInflater layoutInflater, RequestManager glideRequestManager,
                       EventBus eventBus) {
        this.layoutInflater = layoutInflater;
        this.glideRequestManager = glideRequestManager;
        this.eventBus = eventBus;
        this.catImages = new ArrayList<>();
    }

    public void addCatImage(ImgurGalleryItem image) {
        catImages.add(image);
        notifyItemInserted(catImages.size() - 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.row_cat, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImgurGalleryItem catImage = catImages.get(position);
        glideRequestManager.load(catImage.getThumbnailLink()).into(holder.catImage);
        holder.itemView.setOnClickListener(view -> eventBus.post(new CatImageSelectedEvent(catImage)));
    }

    @Override
    public int getItemCount() {
        return catImages.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView catImage;

        public ViewHolder(View itemView) {
            super(itemView);
            catImage = (ImageView) itemView.findViewById(R.id.cat_image);
        }
    }
}
