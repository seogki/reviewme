package com.skh.reviewme.Base;

import android.app.Activity;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.skh.reviewme.Util.KeyboardUtils;
import com.skh.reviewme.Util.UtilMethod;

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

        byte[] decodedString = Base64.decode(url, Base64.DEFAULT);
        Glide.with(imageView.getContext())
                .load(decodedString)
                .apply(new RequestOptions()
                        .centerCrop()
                        .override(190, 190)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .thumbnail(0.1f)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        imageView.setImageDrawable(resource);
                    }
                });
//        }
    }

    @BindingAdapter("innerCommunityImageUrl")
    public static void innerCommunityImage(final ImageView imageView, String url) {

        if (url != null) {
            byte[] decodedString = Base64.decode(url, Base64.DEFAULT);
            Glide.with(imageView.getContext())
                    .load(decodedString)
                    .apply(new RequestOptions()
                            .centerCrop()
                            .override(500, 500)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE))
                    .thumbnail(0.1f)
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            imageView.setImageDrawable(resource);
                        }
                    });
        }
    }

    @BindingAdapter("CommunityMainImageUrl")
    public static void CommunityMainImage(final ImageView imageView, String url) {
        if (url == null) {
            Glide.with(imageView).clear(imageView);
            imageView.setImageDrawable(null);
        } else {
            byte[] decodedString = Base64.decode(url, Base64.DEFAULT);
            Glide.with(imageView)
                    .load(decodedString)
                    .apply(new RequestOptions()
                            .dontTransform()
                            .override(800, 800)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE))
                    .thumbnail(0.1f)
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            imageView.setImageDrawable(resource);
                        }

                    });
        }
    }

    @BindingAdapter("keyboardDetect")
    public static void keyboardDetect(@NonNull final View view, final String data) {

        Context context = view.getContext();
        if (context == null) {
            return;
        } else if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isFinishing() || activity.isDestroyed()) {
                return;
            }
        }


        if (UtilMethod.getActivity(view.getContext()) != null) {
            KeyboardUtils.addKeyboardToggleListener(UtilMethod.getActivity(view.getContext()), new KeyboardUtils.SoftKeyboardToggleListener() {
                @Override
                public void onToggleSoftKeyboard(boolean isVisible) {
                    view.setVisibility(isVisible ? View.GONE : View.VISIBLE);
                }
            });
        }

    }
}
