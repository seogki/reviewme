package com.skh.reviewme.Base;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

/**
 * Created by Seogki on 2018. 4. 12..
 */

public class BaseBindingAdapter {

    @BindingAdapter("galleryImageUrl")
    public static void galleryImage(final ImageView imageView, String url) {

        Uri uri = Uri.parse("file://" + url);
        Glide.with(imageView.getContext())
                .load(uri)
                .apply(new RequestOptions()
                        .centerCrop()
                        .override(175, 175)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .thumbnail(0.1f)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        imageView.setImageDrawable(resource);
                    }
                });
    }

    @BindingAdapter("reviewImageUrl")
    public static void ReviewImage(final ImageView imageView, String url) {
        Uri uri;
        Log.d("ee", url);
        if (!url.contains("resource"))
            uri = Uri.parse("file://" + url);
        else
            uri = Uri.parse(url);

        Glide.with(imageView.getContext())
                .load(uri)
                .apply(new RequestOptions()
                        .centerCrop()
                        .override(450, 450)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .thumbnail(0.1f)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        imageView.setImageDrawable(resource);
                    }
                });
    }

    @BindingAdapter("CommunityMainImageUrl")
    public static void CommunityMainImage(final ImageView imageView, String url) {
        Uri uri;
        Log.d("ee", url);
        if (!url.contains("resource"))
            uri = Uri.parse("file://" + url);
        else
            uri = Uri.parse(url);

        Glide.with(imageView.getContext())
                .load(uri)
                .apply(new RequestOptions()
                        .centerCrop()
                        .circleCrop()
                        .override(150, 150)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .thumbnail(0.1f)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        imageView.setImageDrawable(resource);
                    }
                });
    }
}
