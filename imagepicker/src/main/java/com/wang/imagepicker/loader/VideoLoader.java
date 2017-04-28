package com.wang.imagepicker.loader;

import android.content.Context;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

/**
 * Created by wang
 * on 2017/4/27
 */

public class VideoLoader extends CursorLoader {

    private static String[] VIDEO_PROJECTION = new String[]{
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.MIME_TYPE,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DATE_TAKEN,
            MediaStore.Video.Media.DURATION
    };

    public VideoLoader(Context context) {
        super(context);
        setProjection(VIDEO_PROJECTION);
        setUri(MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        setSortOrder(MediaStore.Audio.Media.DATE_ADDED + " DESC");
    }
}
