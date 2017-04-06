package com.wang.imagepicker.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import com.wang.imagepicker.activity.PhotoPickerActivity;
import com.wang.imagepicker.model.Photo;

import java.util.ArrayList;
import java.util.List;

public class PhotoPicker {

    public static final int TAKE_PHOTO = -100;

    public final static String EXTRA_SELECTED_PHOTOS = "SELECTED_PHOTOS";
    public final static String EXTRA_MAX_COUNT = "MAX_COUNT";
    public final static String EXTRA_SAVE_DIR = "SAVE_DIR";
    public final static String EXTRA_SHOW_CAMERA = "SHOW_CAMERA";
    public final static String EXTRA_SHOW_GIF = "SHOW_GIF";
    public final static String EXTRA_GRID_COLUMN = "column";
    public final static String EXTRA_PREVIEW_ENABLED = "PREVIEW_ENABLED";
    public final static String EXTRA_TOOLBAR_BG = "TOOLBAR_BG";
    public final static String EXTRA_COMPLETE_BG = "COMPLETE_BG";
    public final static String EXTRA_FULLSCREEN = "FULLSCREEN";


    public static PhotoPickerBuilder builder() {
        return new PhotoPickerBuilder();
    }

    public static class PhotoPickerBuilder {
        private Bundle mPickerOptionsBundle;
        private Intent mPickerIntent;

        public PhotoPickerBuilder() {
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
            mPickerIntent.setClass(context, PhotoPickerActivity.class);
            mPickerIntent.putExtras(mPickerOptionsBundle);
            return mPickerIntent;
        }


        public PhotoPickerBuilder setPhotoCount(int photoCount) {
            mPickerOptionsBundle.putInt(EXTRA_MAX_COUNT, photoCount);
            return this;
        }

        public PhotoPickerBuilder setPhotoSaveDir(String dirName) {
            mPickerOptionsBundle.putString(EXTRA_SAVE_DIR, dirName);
            return this;
        }

        public PhotoPickerBuilder setGridColumnCount(int columnCount) {
            mPickerOptionsBundle.putInt(EXTRA_GRID_COLUMN, columnCount);
            return this;
        }

        public PhotoPickerBuilder setShowGif(boolean showGif) {
            mPickerOptionsBundle.putBoolean(EXTRA_SHOW_GIF, showGif);
            return this;
        }

        public PhotoPickerBuilder setShowCamera(boolean showCamera) {
            mPickerOptionsBundle.putBoolean(EXTRA_SHOW_CAMERA, showCamera);
            return this;
        }

        @Deprecated
        public PhotoPickerBuilder setSelected(List<String> imagesUri) {
            mPickerOptionsBundle.putStringArrayList(EXTRA_SELECTED_PHOTOS, (ArrayList<String>) imagesUri);
            return this;
        }

        public PhotoPickerBuilder setSelected(ArrayList<Photo> photos) {
            mPickerOptionsBundle.putParcelableArrayList(EXTRA_SELECTED_PHOTOS, photos);
            return this;
        }

        public PhotoPickerBuilder setPreviewEnabled(boolean previewEnabled) {
            mPickerOptionsBundle.putBoolean(EXTRA_PREVIEW_ENABLED, previewEnabled);
            return this;
        }

        public PhotoPickerBuilder setToolbarBg(@ColorInt int toolbarBg){
            mPickerOptionsBundle.putInt(EXTRA_TOOLBAR_BG, toolbarBg);
            return this;
        }

        public PhotoPickerBuilder setCompleteBg(@DrawableRes int completeBg){
            mPickerOptionsBundle.putInt(EXTRA_COMPLETE_BG, completeBg);
            return this;
        }

        public PhotoPickerBuilder setFullscreen(boolean fullScreen){
            mPickerOptionsBundle.putBoolean(EXTRA_FULLSCREEN, fullScreen);
            return this;
        }
    }
}
