package com.wang.imagepicker.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by wang
 * on 2017/4/27
 */

public class CropUtil {

    public static void onStartCrop(Activity activity, @NonNull String path, @NonNull String destPath, boolean destFullPath, UCrop.Options options, int requestCode) {
        Uri uri = new Uri.Builder()
                .scheme("file")
                .appendPath(path)
                .build();
        String dest;
        if (destFullPath) {
            dest = destPath;
        } else {
            if (!path.startsWith(destPath)) {
                String[] values = path.split(File.separator);
                dest = destPath + File.separator + values[values.length - 1];
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss", Locale.getDefault());
                dest = destPath + File.separator + "crop_" + sdf.format(new Date()) + ".jpg";
            }
        }

        Uri uriDest = new Uri.Builder()
                .scheme("file")
                .appendPath(dest)
                .build();
        UCrop.of(uri, uriDest)
                .withOptions(options)
                .start(activity, requestCode);
    }

    public static void onStartCrop(Fragment fragment, @NonNull String path, @NonNull String destPath, boolean destFullPath, UCrop.Options options, int requestCode) {
        Uri uri = new Uri.Builder()
                .scheme("file")
                .appendPath(path)
                .build();
        String dest;
        if (destFullPath) {
            dest = destPath;
        } else {
            if (!path.startsWith(destPath)) {
                String[] values = path.split(File.separator);
                dest = destPath + File.separator + values[values.length - 1];
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss", Locale.getDefault());
                dest = destPath + File.separator + "crop_" + sdf.format(new Date()) + ".jpg";
            }
        }

        Uri uriDest = new Uri.Builder()
                .scheme("file")
                .appendPath(dest)
                .build();
        UCrop.of(uri, uriDest)
                .withOptions(options)
                .start(fragment.getContext(), fragment, requestCode);
    }
}
