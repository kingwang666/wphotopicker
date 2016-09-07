package com.wang.imagepicker.utils;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ImageCaptureManager implements MediaScannerConnection.MediaScannerConnectionClient {

    private final static String CAPTURED_PHOTO_PATH_KEY = "mCurrentPhotoPath";
    public static final int REQUEST_TAKE_PHOTO = 1;

    private String mCurrentPhotoPath;
    private Context mContext;
    private MediaScannerConnection mScanner;

    public ImageCaptureManager(Context mContext) {
        this.mContext = mContext.getApplicationContext();
        mScanner = new MediaScannerConnection(mContext, this);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss", Locale.getDefault());
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            throw new IOException("no find sd card");
        }
        String dirName = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + PhotoPicker.DIR_NAME + File.separator;
        File file = new File(dirName);
        if (!file.exists()) {
            file.mkdirs();
        }
        File image = new File(dirName, sdf.format(new Date()) + ".jpg");
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    public Intent dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = createImageFile();
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
            }
        }
        return takePictureIntent;
    }


    public void galleryAddPic() {
        mScanner.connect();
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//
//        if (TextUtils.isEmpty(mCurrentPhotoPath)) {
//            return;
//        }
//
//        File f = new File(mCurrentPhotoPath);
//        Uri contentUri = Uri.fromFile(f);
//        mediaScanIntent.setData(contentUri);
//        mContext.sendBroadcast(mediaScanIntent);
    }

    @Deprecated
    public void galleryDir(String filePath){
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_MOUNTED);
        scanIntent.setData(Uri.fromFile(new File(filePath)));
        mContext.sendBroadcast(scanIntent);
    }


    public String getCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }


    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null && mCurrentPhotoPath != null) {
            savedInstanceState.putString(CAPTURED_PHOTO_PATH_KEY, mCurrentPhotoPath);
        }
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(CAPTURED_PHOTO_PATH_KEY)) {
            mCurrentPhotoPath = savedInstanceState.getString(CAPTURED_PHOTO_PATH_KEY);
        }
    }

    @Override
    public void onMediaScannerConnected() {
        mScanner.scanFile(mCurrentPhotoPath, "image/jpg");
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        mScanner.disconnect();
    }

    public void onDestroy() {
        mScanner.disconnect();
        mScanner.onServiceDisconnected(null);
        mScanner = null;
        mContext = null;
    }
}
