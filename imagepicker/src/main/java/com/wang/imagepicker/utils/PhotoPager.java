package com.wang.imagepicker.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import com.wang.imagepicker.Extra;
import com.wang.imagepicker.activity.PhotoPagerActivity;
import com.wang.imagepicker.activity.PhotoPickerActivity;
import com.wang.imagepicker.model.Photo;

import java.util.ArrayList;

/**
 * Created on 2016/8/23.
 * Author: wang
 */

public class PhotoPager {

    public static PhotoPagerActivityBuilder builderActivity() {
        return new PhotoPagerActivityBuilder();
    }

    public static PhotoPagerFragmentBuilder builderFragment(){
        return new PhotoPagerFragmentBuilder();
    }

    @SuppressWarnings("unchecked")
    static class BaseBuilder<T extends BaseBuilder> {

        protected Bundle mBundle;

        public BaseBuilder() {
            mBundle = new Bundle();
        }

        public T setPaths(ArrayList<String> paths) {
            mBundle.putStringArrayList(Extra.EXTRA_PHOTOS, paths);
            mBundle.putBoolean(Extra.EXTRA_IS_PHOTO, false);
            return (T) this;
        }

        public T setPhotos(ArrayList<Photo> photos) {
            mBundle.putParcelableArrayList(Extra.EXTRA_PHOTOS, photos);
            mBundle.putBoolean(Extra.EXTRA_IS_PHOTO, true);
            return (T) this;
        }

        public T setErrorPhoto(@DrawableRes int resId) {
            mBundle.putInt(Extra.EXTRA_ERROR_IMG, resId);
            return (T) this;
        }

        public T setHaveCamera(boolean haveCamera) {
            mBundle.putBoolean(Extra.EXTRA_HAVE_CAMERA, haveCamera);
            return (T) this;
        }

        public T setShowDelete(boolean showDelete) {
            mBundle.putBoolean(Extra.EXTRA_SHOW_DELETE, showDelete);
            return (T) this;
        }

        public T setAlwaysShow(boolean alwaysShow){
            mBundle.putBoolean(Extra.EXTRA_ALWAYS_SHOW, alwaysShow);
            return (T) this;
        }

        public T setShowTop(boolean showTop) {
            mBundle.putBoolean(Extra.EXTRA_SHOW_TOP, showTop);
            return (T) this;
        }

        public T setPosition(int position) {
            mBundle.putInt(Extra.EXTRA_POSITION, position);
            return (T) this;
        }

        public T setFullscreen(boolean fullScreen) {
            mBundle.putBoolean(Extra.EXTRA_FULLSCREEN, fullScreen);
            return (T) this;
        }

        public T setShowCrop(boolean showCrop){
            mBundle.putBoolean(Extra.EXTRA_SHOW_CROP, showCrop);
            return (T) this;
        }


        public T setCropFullPath(String path) {
            mBundle.putString(Extra.EXTRA_CROP_DEST_PATH, path);
            mBundle.putBoolean(Extra.EXTRA_CROP_FULL_PATH, true);
            return (T) this;
        }

        public T setCropDirPath(String path) {
            mBundle.putString(Extra.EXTRA_CROP_DEST_PATH, path);
            mBundle.putBoolean(Extra.EXTRA_CROP_FULL_PATH, false);
            return (T) this;
        }

        public T setCropAspectRatioX(float x) {
            mBundle.putFloat(Extra.EXTRA_CROP_ASPECT_RATIO_X, x);
            return (T) this;
        }

        public T setCropAspectRatioY(float y) {
            mBundle.putFloat(Extra.EXTRA_CROP_ASPECT_RATIO_Y, y);
            return (T) this;
        }

        public T setCropMaxWidth(int width) {
            mBundle.putInt(Extra.EXTRA_CROP_MAX_WIDTH, width);
            return (T) this;
        }

        public T setCropMaxHeight(int height) {
            mBundle.putInt(Extra.EXTRA_CROP_MAX_HEIGHT, height);
            return (T) this;
        }

        public T setCropFreeStyleEnable(boolean enable) {
            mBundle.putBoolean(Extra.EXTRA_CROP_FREE_STYLE, enable);
            return (T) this;
        }

        public T setCropCircle(boolean circle) {
            mBundle.putBoolean(Extra.EXTRA_CROP_CIRCLE,circle);
            return (T) this;
        }

        public T setCropToolbarColor(@ColorInt int color){
            mBundle.putInt(Extra.EXTRA_CROP_TOOLBAR_COLOR, color);
            return (T) this;
        }
        
    }


    public static class PhotoPagerActivityBuilder extends BaseBuilder<PhotoPagerActivityBuilder> {

        /**
         * Send the Intent from an Activity with a custom request code
         *
         * @param activity    Activity to receive result
         * @param requestCode requestCode for result
         */
        public void start(@NonNull Activity activity, int requestCode) {
            activity.startActivityForResult(getIntent(activity), requestCode);
        }

        public void start(@NonNull Context context) {
            context.startActivity(getIntent(context));
        }

        /**
         * Send the Intent with a custom request code
         *
         * @param fragment    Fragment to receive result
         * @param requestCode requestCode for result
         */
        public void start(@NonNull android.support.v4.app.Fragment fragment, int requestCode) {
            fragment.startActivityForResult(getIntent(fragment.getContext()), requestCode);
        }

        /**
         * Get Intent to start {@link PhotoPickerActivity}
         *
         * @return Intent for {@link PhotoPickerActivity}
         */
        public Intent getIntent(@NonNull Context context) {
            Intent intent = new Intent();
            intent.setClass(context, PhotoPagerActivity.class);
            intent.putExtras(mBundle);
            intent.putExtra(Extra.EXTRA_SHOW_BOTTOM, false);
            intent.putExtra(Extra.EXTRA_HAS_ANIM, false);
            return intent;
        }
    }

    public static class PhotoPagerFragmentBuilder extends BaseBuilder<PhotoPagerFragmentBuilder> {

        public PhotoPagerFragmentBuilder setShowBottom(boolean showBottom){
            mBundle.putBoolean(Extra.EXTRA_SHOW_BOTTOM, showBottom);
            return this;
        }

        public PhotoPagerFragmentBuilder setHasAnim(boolean hasAnim){
            mBundle.putBoolean(Extra.EXTRA_HAS_ANIM, hasAnim);
            return this;
        }

        public PhotoPagerFragmentBuilder setThumbanil(int[] screenLocation, int thumbnailWidth, int thumbnailHeight){
            mBundle.putInt(Extra.EXTRA_THUMBNAIL_LEFT, screenLocation[0]);
            mBundle.putInt(Extra.EXTRA_THUMBNAIL_TOP, screenLocation[1]);
            mBundle.putInt(Extra.EXTRA_THUMBNAIL_WIDTH, thumbnailWidth);
            mBundle.putInt(Extra.EXTRA_THUMBNAIL_HEIGHT, thumbnailHeight);
            return this;
        }

        public Bundle getBundle(){
            return mBundle;
        }

    }
}
