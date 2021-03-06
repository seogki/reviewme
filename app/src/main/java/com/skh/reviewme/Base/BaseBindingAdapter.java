package com.skh.reviewme.Base;

import android.app.Activity;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.skh.reviewme.Util.Const;
import com.skh.reviewme.Util.KeyboardUtils;
import com.skh.reviewme.Util.UtilMethod;

import java.util.Objects;

/**
 * Created by Seogki on 2018. 4. 12..
 */

public class BaseBindingAdapter {

    @BindingAdapter("galleryImageUrl")
    public static void galleryImage(final ImageView imageView, String url) {

        Context context = imageView.getContext();
        if (context == null) {
            return;
        } else if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isFinishing() || activity.isDestroyed()) {
                return;
            }
        }

        if (url == null) {
            Glide.with(imageView.getContext()).clear(imageView);
            imageView.setImageDrawable(null);
        } else {

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
    }

    @BindingAdapter("reviewImageUrl")
    public static void ReviewImage(final ImageView imageView, String url) {

        Context context = imageView.getContext();
        if (context == null) {
            return;
        } else if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isFinishing() || activity.isDestroyed()) {
                return;
            }
        }

        if (url == null) {
            Glide.with(imageView.getContext()).clear(imageView);
            imageView.setImageDrawable(null);
        } else {
            String murl = Const.Companion.getServer_url() + url;
            Uri uri = Uri.parse(murl);

            Glide.with(imageView.getContext())
                    .load(uri)
                    .apply(new RequestOptions()
                            .centerCrop()
                            .override(225, 225)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))

                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            imageView.setImageDrawable(resource);
                        }
                    });
        }
    }

    @BindingAdapter("horizontalImageUrl")
    public static void horizontalImageUrl(final ImageView imageView, String url) {

        Context context = imageView.getContext();
        if (context == null) {
            return;
        } else if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isFinishing() || activity.isDestroyed()) {
                return;
            }
        }

        if (url == null) {
            Glide.with(imageView.getContext()).clear(imageView);
            imageView.setImageDrawable(null);
        } else {
            String murl = Const.Companion.getServer_url() + url;
            Uri uri = Uri.parse(murl);

            Glide.with(imageView.getContext())
                    .load(uri)
                    .apply(new RequestOptions()
                            .centerCrop()
                            .transform(new RoundedCorners(8))
                            .override(225, 225)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            imageView.setImageDrawable(resource);
                        }
                    });
        }
    }

    @BindingAdapter("horizontalcomImageUrl")
    public static void horizontalcomImageUrl(final ImageView imageView, String url) {

        Context context = imageView.getContext();
        if (context == null) {
            return;
        } else if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isFinishing() || activity.isDestroyed()) {
                return;
            }
        }

        if (url == null) {
            Glide.with(imageView.getContext()).clear(imageView);
            imageView.setImageDrawable(null);
        } else {
            String murl = Const.Companion.getServer_url() + url;
            Uri uri = Uri.parse(murl);

            Glide.with(imageView.getContext())
                    .load(uri)
                    .apply(new RequestOptions()
                            .centerCrop()
                            .transform(new RoundedCorners(8))
                            .override(300, 300)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            imageView.setImageDrawable(resource);
                        }
                    });
        }
    }

    @BindingAdapter("reviewThumbnailImageUrl")
    public static void reviewThumbnailImageUrl(final ImageView imageView, String url) {

        Context context = imageView.getContext();
        if (context == null) {
            return;
        } else if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isFinishing() || activity.isDestroyed()) {
                return;
            }
        }

        if (url == null) {
            Glide.with(imageView.getContext()).clear(imageView);
            imageView.setImageDrawable(null);
        } else {
            String murl = Const.Companion.getServer_url() + url;
            Uri uri = Uri.parse(murl);
            Glide.with(imageView.getContext())
                    .load(uri)
                    .apply(new RequestOptions()
                            .centerCrop()
                            .circleCrop()
                            .override(100, 100)
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

    @BindingAdapter("innerCommunityImageUrl")
    public static void innerCommunityImage(final ImageView imageView, String url) {

        Context context = imageView.getContext();
        if (context == null) {
            return;
        } else if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isFinishing() || activity.isDestroyed()) {
                return;
            }
        }

        if (url == null) {
            imageView.setVisibility(View.GONE);
            Glide.with(imageView).clear(imageView);
            imageView.setImageDrawable(null);
        } else {
            imageView.setVisibility(View.VISIBLE);
            String murl = Const.Companion.getServer_url() + url;
            Uri uri = Uri.parse(murl);
            Glide.with(imageView.getContext())
                    .load(uri)
                    .apply(new RequestOptions()
                            .fitCenter()
                            .override(900, 900)
                            .skipMemoryCache(true)
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

    @BindingAdapter("CommunityMainImageUrl")
    public static void CommunityMainImage(final ImageView imageView, String url) {

        Context context = imageView.getContext();
        if (context == null) {
            return;
        } else if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isFinishing() || activity.isDestroyed()) {
                return;
            }
        }

        if (url == null) {
            Glide.with(imageView).clear(imageView);
            imageView.setImageDrawable(null);
        } else {
            String murl = Const.Companion.getServer_url() + url;
            Uri uri = Uri.parse(murl);
            Glide.with(imageView)
                    .load(uri)
                    .apply(new RequestOptions()
                            .fitCenter()
                            .override(750, 750)
                            .skipMemoryCache(true)
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

    @BindingAdapter("CommunityInnerThumbnailImageUrl")
    public static void CommunityInnerThumbnailImageUrl(final ImageView imageView, String url) {

        Context context = imageView.getContext();
        if (context == null) {
            return;
        } else if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isFinishing() || activity.isDestroyed()) {
                return;
            }
        }

        if (url == null) {
            Glide.with(imageView.getContext()).clear(imageView);
            imageView.setImageDrawable(null);
        } else {
            String murl = Const.Companion.getServer_url() + url;
            Uri uri = Uri.parse(murl);
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

    @BindingAdapter("settingImageUrl")
    public static void SettingImage(final ImageView imageView, String url) {

        Context context = imageView.getContext();
        if (context == null) {
            return;
        } else if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isFinishing() || activity.isDestroyed()) {
                return;
            }
        }

        if (url == null) {
            Glide.with(imageView.getContext()).clear(imageView);
            imageView.setImageDrawable(null);
        } else {
            String murl = Const.Companion.getServer_url() + url;
            Uri uri = Uri.parse(murl);

            Glide.with(imageView.getContext())
                    .load(uri)
                    .apply(new RequestOptions()
                            .centerCrop()
                            .circleCrop()
                            .override(350, 350)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))

                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            imageView.setImageDrawable(resource);
                        }
                    });
        }
    }

    @BindingAdapter("checkGender")
    public static void genderCheck(final TextView textView, final String data) {
        if (Objects.equals(data, "M")) {
            textView.setText("(남)");
        } else if (Objects.equals(data, "F")) {
            textView.setText("(여)");
        }
    }


    @BindingAdapter("checkTag")
    public static void checkTag(final TextView textView, final String data) {
        if (Objects.equals(data, "메인")) {
            textView.setText("[메인]");
        } else if (Objects.equals(data, "커뮤니티")) {
            textView.setText("[커뮤니티]");
        } else if (Objects.equals(data, "세팅")) {
            textView.setText("[세팅]");
        } else if (Objects.equals(data, "기타")) {
            textView.setText("[기타]");
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


        if (UtilMethod.getActivity(view.getContext()) != null)
            KeyboardUtils.addKeyboardToggleListener(UtilMethod.getActivity(view.getContext()), new KeyboardUtils.SoftKeyboardToggleListener() {
                @Override
                public void onToggleSoftKeyboard(boolean isVisible) {
                    view.setVisibility(isVisible ? View.GONE : View.VISIBLE);
                }
            });

    }
}
