package com.wang.imagepicker.utils;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.wang.imagepicker.model.Photo;
import com.wang.imagepicker.model.PhotoDirectory;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_ID;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_ADDED;


public class MediaStoreHelper {

    public final static int INDEX_ALL_PHOTOS = 0;


    public static void getPhotoDirs(FragmentActivity activity, Bundle args, PhotosResultCallback resultCallback) {
        ArrayList<Photo> selectPhotos = args.getParcelableArrayList(PhotoPicker.EXTRA_SELECTED_PHOTOS);
        activity.getSupportLoaderManager()
                .initLoader(0, args, new PhotoDirLoaderCallbacks(activity, selectPhotos, resultCallback));
    }

    static class PhotoDirLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

        private WeakReference<Context> context;
        private PhotosResultCallback resultCallback;
        private ArrayList<Photo> mSelectPhotos;

        public PhotoDirLoaderCallbacks(Context context, ArrayList<Photo> selectPhotos, PhotosResultCallback resultCallback) {
            this.context = new WeakReference<>(context);
            this.resultCallback = resultCallback;
            mSelectPhotos = selectPhotos;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new PhotoDirectoryLoader(context.get(), args.getBoolean(PhotoPicker.EXTRA_SHOW_GIF, false));
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data == null) return;
            List<PhotoDirectory> directories = new ArrayList<>();
            PhotoDirectory photoDirectoryAll = new PhotoDirectory();
            photoDirectoryAll.name = "全部图片";
            photoDirectoryAll.id = "ALL";

            while (data.moveToNext()) {

                int imageId = data.getInt(data.getColumnIndexOrThrow(_ID));
                String bucketId = data.getString(data.getColumnIndexOrThrow(BUCKET_ID));
                String name = data.getString(data.getColumnIndexOrThrow(BUCKET_DISPLAY_NAME));
                String path = data.getString(data.getColumnIndexOrThrow(DATA));

                PhotoDirectory photoDirectory = new PhotoDirectory();
                photoDirectory.id = bucketId;
                photoDirectory.name = name;
                Photo photo = new Photo(imageId, path);
                if (mSelectPhotos.contains(photo)) {
                    photo.select = true;
                }
                if (!directories.contains(photoDirectory)) {
                    photoDirectory.coverPath = path;
                    photoDirectory.photos.add(photo);
                    photoDirectory.dateAdded = data.getLong(data.getColumnIndexOrThrow(DATE_ADDED));
                    directories.add(photoDirectory);
                } else {
                    directories.get(directories.indexOf(photoDirectory)).photos.add(photo);
                }

                photoDirectoryAll.photos.add(photo);
            }
            data.close();
            if (photoDirectoryAll.photos.size() > 0) {
                photoDirectoryAll.coverPath = photoDirectoryAll.photos.get(0).path;
            }
            directories.add(INDEX_ALL_PHOTOS, photoDirectoryAll);
            if (resultCallback != null) {
                resultCallback.onResultCallback(directories);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }

    public static void onDestroy(FragmentActivity activity) {
        activity.getSupportLoaderManager().destroyLoader(0);
    }


    public interface PhotosResultCallback {
        void onResultCallback(List<PhotoDirectory> directories);
    }

}
