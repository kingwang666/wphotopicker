package com.wang.imagepicker.utils;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.wang.imagepicker.Extra;
import com.wang.imagepicker.loader.PhotoDirectoryLoader;
import com.wang.imagepicker.loader.VideoLoader;
import com.wang.imagepicker.model.Photo;
import com.wang.imagepicker.model.PhotoDirectory;
import com.wang.imagepicker.model.Video;

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


    public static void getPhotoDirs(FragmentActivity activity, Bundle args, OnPhotosResultCallback resultCallback) {
        ArrayList<Photo> selectPhotos = args.getParcelableArrayList(Extra.EXTRA_SELECTED_PHOTOS);
        activity.getSupportLoaderManager()
                .initLoader(0, args, new PhotoDirLoaderCallbacks(activity, selectPhotos, resultCallback));
    }

    public static void getVideos(FragmentActivity activity, Bundle args, OnVideoResultCallback callback) {
        ArrayList<Video> selectVideo = args.getParcelableArrayList(Extra.EXTRA_SELECTED_VIDEOS);
        activity.getSupportLoaderManager()
                .initLoader(0, args, new VideoLoaderCallbacks(activity, selectVideo, callback));
    }

    private static class PhotoDirLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

        private WeakReference<Context> context;
        private OnPhotosResultCallback resultCallback;
        private ArrayList<Photo> mSelectPhotos;

        public PhotoDirLoaderCallbacks(Context context, ArrayList<Photo> selectPhotos, OnPhotosResultCallback resultCallback) {
            this.context = new WeakReference<>(context);
            this.resultCallback = resultCallback;
            mSelectPhotos = selectPhotos;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new PhotoDirectoryLoader(context.get(), args.getBoolean(Extra.EXTRA_SHOW_GIF, false));
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


    private static class VideoLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

        private WeakReference<Context> context;
        private OnVideoResultCallback resultCallback;
        private ArrayList<Video> mSelectVideos;

        public VideoLoaderCallbacks(Context context, ArrayList<Video> selectVideos, OnVideoResultCallback resultCallback) {
            this.context = new WeakReference<>(context);
            this.resultCallback = resultCallback;
            mSelectVideos = selectVideos;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new VideoLoader(context.get());
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data == null) return;
            List<Video> videos = new ArrayList<>();

            while (data.moveToNext()) {

                String path = data.getString(data.getColumnIndex(MediaStore.Video.Media.DATA));
                int id = data.getInt(data.getColumnIndex(MediaStore.Video.Media._ID));
                String title = data.getString(data.getColumnIndex(MediaStore.Video.Media.TITLE));
                String type = data.getString(data.getColumnIndex(MediaStore.Video.Media.MIME_TYPE));
                long size = data.getLong(data.getColumnIndex(MediaStore.Video.Media.SIZE));
                String date = data.getString(data.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN));
                long duration = data.getLong(data.getColumnIndex(MediaStore.Video.Media.DURATION));

                Video video = new Video(id, path);
                video.title = title;
                video.mimeType = type;
                video.size = size;
                video.dateTaken = date;
                video.duration = duration;

                videos.add(video);
                if (mSelectVideos.contains(video)) {
                    video.select = true;
                }

            }
            data.close();

            if (resultCallback != null) {
                resultCallback.onResultCallback(videos);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }

    public interface OnPhotosResultCallback {
        void onResultCallback(List<PhotoDirectory> directories);
    }

    public interface OnVideoResultCallback {
        void onResultCallback(List<Video> videos);
    }

}
