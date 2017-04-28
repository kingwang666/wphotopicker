package com.wang.imagepicker.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import com.wang.imagepicker.Extra;
import com.wang.imagepicker.activity.VideoPickerActivity;
import com.wang.imagepicker.model.Photo;

import java.util.ArrayList;

/**
 * Created by wang
 * on 2017/4/27
 */

public class VideoPicker {

    public static VideoPickerBuilder builder() {
        return new VideoPickerBuilder();
    }

    public static class VideoPickerBuilder {
        private Bundle mBundle;

        public VideoPickerBuilder() {
            mBundle = new Bundle();
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
         * Get Intent to start {@link VideoPickerActivity}
         *
         * @return Intent for {@link VideoPickerActivity}
         */
        public Intent getIntent(@NonNull Context context) {
            Intent intent = new Intent();
            intent.setClass(context, VideoPickerActivity.class);
            intent.putExtras(mBundle);
            return intent;
        }


        public VideoPickerBuilder setPhotoCount(int photoCount) {
            mBundle.putInt(Extra.EXTRA_MAX_COUNT, photoCount);
            return this;
        }

        public VideoPickerBuilder setGridColumnCount(int columnCount) {
            mBundle.putInt(Extra.EXTRA_GRID_COLUMN, columnCount);
            return this;
        }


        public VideoPickerBuilder setSelected(ArrayList<Photo> photos) {
            mBundle.putParcelableArrayList(Extra.EXTRA_SELECTED_VIDEOS, photos);
            return this;
        }

        public VideoPickerBuilder setToolbarBg(@ColorInt int toolbarBg){
            mBundle.putInt(Extra.EXTRA_TOOLBAR_BG, toolbarBg);
            return this;
        }

        public VideoPickerBuilder setCompleteBg(@DrawableRes int completeBg){
            mBundle.putInt(Extra.EXTRA_COMPLETE_BG, completeBg);
            return this;
        }
    }
}
