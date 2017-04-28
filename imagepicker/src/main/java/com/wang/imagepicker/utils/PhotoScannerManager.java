package com.wang.imagepicker.utils;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;

/**
 * Created by wang
 * on 2017/4/27
 */

public class PhotoScannerManager implements MediaScannerConnection.MediaScannerConnectionClient  {

    private MediaScannerConnection mScanner;

    private String mCurrentPhotoPath;

    public PhotoScannerManager(Context context){
        mScanner = new MediaScannerConnection(context, this);
    }

    public void connect(String path){
        mCurrentPhotoPath = path;
        mScanner.connect();
    }

    @Override
    public void onMediaScannerConnected() {
        mScanner.scanFile(mCurrentPhotoPath, "image/jpg");
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        mScanner.disconnect();
    }

    public void disconnect(){
        mScanner.disconnect();
        mScanner.onServiceDisconnected(null);
        mScanner = null;
    }
}
