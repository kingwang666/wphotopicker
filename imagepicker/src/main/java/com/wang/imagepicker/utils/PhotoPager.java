package com.wang.imagepicker.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import com.wang.imagepicker.activity.PhotoPagerActivity;
import com.wang.imagepicker.activity.PhotoPickerActivity;
import com.wang.imagepicker.model.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2016/8/23.
 * Author: wang
 */

public class PhotoPager {

    public final static String IS_PHOTO = "key_is_photo";
    public final static String PHOTOS = "key_photos";
    public final static String HAVE_CAMERA = "key_have_camera";
    public final static String SHOW_DELETE = "key_show_delete";
    public final static String SHOW_TOP = "key_show_top";
    public final static String ERROR_IMG = "key_error_img";
    public final static String POSITION = "key_position";
    public final static String FULLSCREEN = "key_fullscreen";

    public static PhotoPager.PhotoPagerBuilder builder() {
        return new PhotoPager.PhotoPagerBuilder();
    }

    public static class PhotoPagerBuilder {
        private Bundle mPickerOptionsBundle;
        private Intent mPickerIntent;

        public PhotoPagerBuilder() {
            mPickerOptionsBundle = new Bundle();
            mPickerIntent = new Intent();
        }

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
            mPickerIntent.setClass(context, PhotoPagerActivity.class);
            mPickerIntent.putExtras(mPickerOptionsBundle);
            return mPickerIntent;
        }

        public PhotoPagerBuilder setIsPhoto(boolean isPhoto){
            mPickerOptionsBundle.putBoolean(IS_PHOTO, isPhoto);
            return this;
        }

        public PhotoPagerBuilder setPhotos(List<String> paths){
            mPickerOptionsBundle.putStringArrayList(PHOTOS, (ArrayList<String>) paths);
            return this;
        }

        public PhotoPagerBuilder setErrorPhoto(@DrawableRes int resId){
            mPickerOptionsBundle.putInt(ERROR_IMG, resId);
            return this;
        }

        public PhotoPagerBuilder setPhotos(ArrayList<Photo> photos){
            mPickerOptionsBundle.putParcelableArrayList(PHOTOS, photos);
            return this;
        }

        public PhotoPagerBuilder setHaveCamera(boolean haveCamera){
            mPickerOptionsBundle.putBoolean(HAVE_CAMERA, haveCamera);
            return this;
        }

        public PhotoPagerBuilder setShowDelete(boolean showDelete){
            mPickerOptionsBundle.putBoolean(SHOW_DELETE, showDelete);
            return this;
        }

        public PhotoPagerBuilder setShowTop(boolean showTop){
            mPickerOptionsBundle.putBoolean(SHOW_TOP, showTop);
            return this;
        }

        public PhotoPagerBuilder setPosition(int position){
            mPickerOptionsBundle.putInt(POSITION, position);
            return this;
        }

        public PhotoPagerBuilder setFullscreen(boolean fullScreen){
            mPickerOptionsBundle.putBoolean(FULLSCREEN, fullScreen);
            return this;
        }
    }
}
