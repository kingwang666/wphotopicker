package com.wang.imagepicker.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import com.wang.imagepicker.Extra;
import com.wang.imagepicker.activity.PhotoPickerActivity;
import com.wang.imagepicker.model.Photo;

import java.util.ArrayList;
import java.util.List;

public class PhotoPicker {

    public static PhotoPickerBuilder builder() {
        return new PhotoPickerBuilder();
    }

    public static class PhotoPickerBuilder {
        private Bundle mPickerOptionsBundle;


        public PhotoPickerBuilder() {
            mPickerOptionsBundle = new Bundle();
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
            Intent intent = new Intent();
            intent.setClass(context, PhotoPickerActivity.class);
            intent.putExtras(mPickerOptionsBundle);
            return intent;
        }


        public PhotoPickerBuilder setPhotoCount(int photoCount) {
            mPickerOptionsBundle.putInt(Extra.EXTRA_MAX_COUNT, photoCount);
            return this;
        }

        public PhotoPickerBuilder setPhotoSaveDir(String dirName) {
            mPickerOptionsBundle.putString(Extra.EXTRA_SAVE_DIR, dirName);
            return this;
        }

        public PhotoPickerBuilder setGridColumnCount(int columnCount) {
            mPickerOptionsBundle.putInt(Extra.EXTRA_GRID_COLUMN, columnCount);
            return this;
        }

        public PhotoPickerBuilder setShowGif(boolean showGif) {
            mPickerOptionsBundle.putBoolean(Extra.EXTRA_SHOW_GIF, showGif);
            return this;
        }

        public PhotoPickerBuilder setShowCamera(boolean showCamera) {
            mPickerOptionsBundle.putBoolean(Extra.EXTRA_SHOW_CAMERA, showCamera);
            return this;
        }

        public PhotoPickerBuilder setSelected(ArrayList<Photo> photos) {
            mPickerOptionsBundle.putParcelableArrayList(Extra.EXTRA_SELECTED_PHOTOS, photos);
            return this;
        }

        public PhotoPickerBuilder setPreviewEnabled(boolean previewEnabled) {
            mPickerOptionsBundle.putBoolean(Extra.EXTRA_PREVIEW_ENABLED, previewEnabled);
            return this;
        }

        public PhotoPickerBuilder setToolbarBg(@ColorInt int toolbarBg) {
            mPickerOptionsBundle.putInt(Extra.EXTRA_TOOLBAR_BG, toolbarBg);
            return this;
        }

        public PhotoPickerBuilder setCompleteBg(@DrawableRes int completeBg) {
            mPickerOptionsBundle.putInt(Extra.EXTRA_COMPLETE_BG, completeBg);
            return this;
        }

        public PhotoPickerBuilder setFullscreen(boolean fullScreen) {
            mPickerOptionsBundle.putBoolean(Extra.EXTRA_FULLSCREEN, fullScreen);
            return this;
        }

        public PhotoPickerBuilder setCrop(boolean crop) {
            mPickerOptionsBundle.putBoolean(Extra.EXTRA_CROP, crop);
            return this;
        }

        public PhotoPickerBuilder setCropFullPath(String path) {
            mPickerOptionsBundle.putString(Extra.EXTRA_CROP_DEST_PATH, path);
            mPickerOptionsBundle.putBoolean(Extra.EXTRA_CROP_FULL_PATH, true);
            return this;
        }

        public PhotoPickerBuilder setCropDirPath(String path) {
            mPickerOptionsBundle.putString(Extra.EXTRA_CROP_DEST_PATH, path);
            mPickerOptionsBundle.putBoolean(Extra.EXTRA_CROP_FULL_PATH, false);
            return this;
        }

        public PhotoPickerBuilder setCropAspectRatioX(float x) {
            mPickerOptionsBundle.putFloat(Extra.EXTRA_CROP_ASPECT_RATIO_X, x);
            return this;
        }

        public PhotoPickerBuilder setCropAspectRatioY(float y) {
            mPickerOptionsBundle.putFloat(Extra.EXTRA_CROP_ASPECT_RATIO_Y, y);
            return this;
        }

        public PhotoPickerBuilder setCropMaxWidth(int width) {
            mPickerOptionsBundle.putInt(Extra.EXTRA_CROP_MAX_WIDTH, width);
            return this;
        }

        public PhotoPickerBuilder setCropMaxHeight(int height) {
            mPickerOptionsBundle.putInt(Extra.EXTRA_CROP_MAX_HEIGHT, height);
            return this;
        }

        public PhotoPickerBuilder setCropFreeStyleEnable(boolean enable) {
            mPickerOptionsBundle.putBoolean(Extra.EXTRA_CROP_FREE_STYLE, enable);
            return this;
        }

        public PhotoPickerBuilder setCropCircle(boolean circle) {
            mPickerOptionsBundle.putBoolean(Extra.EXTRA_CROP_CIRCLE,circle);
            return this;
        }
    }
}
