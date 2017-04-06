package com.wang.imagepicker.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wang.imagepicker.R;
import com.wang.imagepicker.adapter.PhotoGridAdapter;
import com.wang.imagepicker.fragment.ShowPicPagerFragment;
import com.wang.imagepicker.interfaces.OnPagerFragmentListener;
import com.wang.imagepicker.interfaces.OnPhotoListener;
import com.wang.imagepicker.model.Photo;
import com.wang.imagepicker.model.PhotoDirectory;
import com.wang.imagepicker.utils.ImageCaptureManager;
import com.wang.imagepicker.utils.MediaStoreHelper;
import com.wang.imagepicker.utils.PermissionUtil;
import com.wang.imagepicker.utils.PhotoPicker;
import com.wang.imagepicker.widget.FolderPopUpWindow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 2016/8/18.
 * Author: wang
 */

public class PhotoPickerActivity extends AppCompatActivity implements OnPhotoListener, OnPagerFragmentListener, View.OnClickListener {

    private TextView mSelectDirTV;
    private TextView mPositionTV;
    private Button mCompleteBtn;
    private RecyclerView mRecyclerView;
    private View mBottomView;
    private FolderPopUpWindow mPopUpWindow;

    private ImageCaptureManager mImageCaptureManager;

    private ArrayList<PhotoDirectory> mPhotoDirectories;
    private ArrayList<Photo> mPhotos;
    private ArrayList<Photo> mSelectPhotos;

    private boolean isShowGif;
    private boolean isShowCamera;
    private boolean isNeedCamera;
    private boolean mPreviewEnabled;
    private boolean mFullscreen;
    private int mMaxCount = 9;
    private int mColumn = 3;

    private int mToolbarBg;
    private int mCompleteBg;

    private int mLastPosition = -1;
    private int mLastDirPosition = 0;

    private String mSaveDirName = "";

    private String mName;
    private boolean isDestroyed;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDestroyed = false;
        setContentView(R.layout.activity_photo_picker);
        mPhotoDirectories = new ArrayList<>();
        mPhotos = new ArrayList<>();
        mImageCaptureManager = new ImageCaptureManager(this);
        initData(getIntent());
        Bundle mediaStoreArgs = new Bundle();
        mediaStoreArgs.putParcelableArrayList(PhotoPicker.EXTRA_SELECTED_PHOTOS, mSelectPhotos);
        mediaStoreArgs.putBoolean(PhotoPicker.EXTRA_SHOW_GIF, isShowGif);
        MediaStoreHelper.getPhotoDirs(this, mediaStoreArgs,
                new MediaStoreHelper.PhotosResultCallback() {
                    @Override
                    public void onResultCallback(List<PhotoDirectory> dirs) {
                        mPhotoDirectories.clear();
                        PhotoDirectory directory = dirs.get(0);
                        directory.select = true;
                        mPhotoDirectories.addAll(dirs);
                        mPhotos.add(new Photo(-1, "camera"));
                        mPhotos.addAll(directory.photos);
                        if (mRecyclerView != null) {
                            mName = directory.name;
                            mSelectDirTV.setText(mName);
                            mPositionTV.setText(mName);
                            mRecyclerView.getAdapter().notifyDataSetChanged();
                        }

                        if (mPopUpWindow != null) {
                            mPopUpWindow.getRecyclerView().getAdapter().notifyDataSetChanged();
                        }
                    }
                });
        initView();
    }

    private void initData(Intent intent) {
        if (intent != null) {
            if (mSelectPhotos == null) {
                mSelectPhotos = intent.getParcelableArrayListExtra(PhotoPicker.EXTRA_SELECTED_PHOTOS);
                if (mSelectPhotos == null) {
                    mSelectPhotos = new ArrayList<>();
                }
            }
            isShowGif = intent.getBooleanExtra(PhotoPicker.EXTRA_SHOW_GIF, false);
            isShowCamera = intent.getBooleanExtra(PhotoPicker.EXTRA_SHOW_CAMERA, true);
            isNeedCamera = true;
            mPreviewEnabled = intent.getBooleanExtra(PhotoPicker.EXTRA_PREVIEW_ENABLED, true);
            mMaxCount = intent.getIntExtra(PhotoPicker.EXTRA_MAX_COUNT, mMaxCount);
            mSaveDirName = intent.getStringExtra(PhotoPicker.EXTRA_SAVE_DIR);
            if (TextUtils.isEmpty(mSaveDirName)){
                mSaveDirName = "Photo";
            }
            mColumn = intent.getIntExtra(PhotoPicker.EXTRA_GRID_COLUMN, mColumn);
            mToolbarBg = intent.getIntExtra(PhotoPicker.EXTRA_TOOLBAR_BG, -1);
            mCompleteBg = intent.getIntExtra(PhotoPicker.EXTRA_COMPLETE_BG, -1);
            mFullscreen = intent.getBooleanExtra(PhotoPicker.EXTRA_FULLSCREEN, false);
        }
    }

    private void initView() {
        mSelectDirTV = (TextView) findViewById(R.id.select_dir_tv);
        mBottomView = findViewById(R.id.bottom_bar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, mColumn));
        mRecyclerView.setAdapter(new PhotoGridAdapter(mPhotos, isShowCamera, mSelectPhotos.size() < mMaxCount, this));
        mPositionTV = (TextView) findViewById(R.id.position_tv);
        if (mPhotos.size() > 0) {
            mName = mPhotoDirectories.get(0).name;
            mSelectDirTV.setText(mName);
            mPositionTV.setText(mName);
        }
        mCompleteBtn = (Button) findViewById(R.id.complete_btn);
        if (mSelectPhotos.size() > 0) {
            mCompleteBtn.setText(String.format("完成(%d/%d)".toLowerCase(), mSelectPhotos.size(), mMaxCount));
        }
        findViewById(R.id.back_img).setOnClickListener(this);
        if (mToolbarBg != -1) {
            findViewById(R.id.toolbar_view).setBackgroundColor(mToolbarBg);
        }
        if (mCompleteBg != -1) {
            mCompleteBtn.setBackgroundResource(mCompleteBg);
        }
        mCompleteBtn.setOnClickListener(this);
        mSelectDirTV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.complete_btn) {
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra(PhotoPicker.EXTRA_SELECTED_PHOTOS, mSelectPhotos);
            setResult(RESULT_OK, intent);
            finish();
        } else if (i == R.id.select_dir_tv) {
            mPopUpWindow = new FolderPopUpWindow(PhotoPickerActivity.this, mPhotoDirectories, this);
            mPopUpWindow.setMargin(mBottomView.getHeight());
            mPopUpWindow.showAtLocation(mBottomView, Gravity.NO_GRAVITY, 0, 0);
        } else if (i == R.id.back_img) {
            onBackPressed();
        }
    }

    @Override
    public void onClick(int position, Object data) {
        PhotoDirectory directory = mPhotoDirectories.get(position);
        if (!directory.select) {
            PhotoDirectory lastDirectory = mPhotoDirectories.get(mLastDirPosition);
            lastDirectory.select = false;
            directory.select = true;
            mPopUpWindow.getRecyclerView().getAdapter().notifyDataSetChanged();
            isNeedCamera = position == 0 && isShowCamera;
            mPhotos.clear();
            mPhotos.addAll(directory.photos);
            mLastDirPosition = position;
            mPopUpWindow.dismiss();
            PhotoGridAdapter adapter = (PhotoGridAdapter) mRecyclerView.getAdapter();
            adapter.setShowCamera(isNeedCamera);
            adapter.notifyDataSetChanged();
            mName = directory.name;
            mSelectDirTV.setText(mName);
            mPositionTV.setText(mName);
        } else {
            mPopUpWindow.dismiss();
        }
    }

    @Override
    public void onCheck(int position, boolean check) {
        Photo photo = mPhotos.get(position);
        photo.select = check;
        PhotoGridAdapter adapter = (PhotoGridAdapter) mRecyclerView.getAdapter();
        if (check) {
            if (mMaxCount == 1 && mSelectPhotos.size() == 1) {
                Photo lastPhoto = mSelectPhotos.get(0);
                lastPhoto.select = false;
                mSelectPhotos.remove(0);
            }
            mLastPosition = position;
            mSelectPhotos.add(photo);
        } else {
            mSelectPhotos.remove(photo);
            mLastPosition = -1;
        }
        adapter.setCheckEnabled(mSelectPhotos.size() < mMaxCount || mMaxCount == 1);
        adapter.notifyDataSetChanged();
        mCompleteBtn.setText(mSelectPhotos.size() == 0 ? "完成" : String.format("完成(%d/%d)".toLowerCase(), mSelectPhotos.size(), mMaxCount));
    }

    @Override
    public void onItemClick(View v, int position, boolean check) {
        if (isNeedCamera && position == 0) {
            requestPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else if (mPreviewEnabled) {
            int[] screenLocation = new int[2];
            v.getLocationOnScreen(screenLocation);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, ShowPicPagerFragment.newInstance(mPhotos, mFullscreen, isNeedCamera, position, false, false, true, screenLocation, v.getWidth(), v.getHeight()), "pic_pager")
                    .commit();
        }
    }

    private void takePhoto() {
        try {
            startActivityForResult(mImageCaptureManager.dispatchTakePictureIntent(mSaveDirName), ImageCaptureManager.REQUEST_TAKE_PHOTO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void requestPermissions(String... permissions) {

        if (PermissionUtil.checkSelfPermission(this, permissions)) {
            ActivityCompat.requestPermissions(this, permissions, 0);
        } else {
            requestPermissionsEnd();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        List<String> allPermissions = Arrays.asList(permissions);
        if (!PermissionUtil.verifyPermissions(allPermissions, grantResults)) {
            Toast.makeText(this, "权限请求失败，无法进行拍照", Toast.LENGTH_SHORT).show();
        }
        else {
            requestPermissionsEnd();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void requestPermissionsEnd() {
        takePhoto();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImageCaptureManager.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            mImageCaptureManager.galleryAddPic();
            mPhotos.add(1, new Photo(PhotoPicker.TAKE_PHOTO, mImageCaptureManager.getCurrentPhotoPath()));
            mRecyclerView.getAdapter().notifyItemInserted(1);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mImageCaptureManager.onSaveInstanceState(outState);
        outState.putParcelableArrayList(PhotoPicker.EXTRA_SELECTED_PHOTOS, mSelectPhotos);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mImageCaptureManager.onRestoreInstanceState(savedInstanceState);
        mSelectPhotos = savedInstanceState.getParcelableArrayList(PhotoPicker.EXTRA_SELECTED_PHOTOS);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        final ShowPicPagerFragment fragment = (ShowPicPagerFragment) getSupportFragmentManager().findFragmentByTag("pic_pager");
        if (fragment != null) {
            fragment.runExitAnimation(new Runnable() {
                @Override
                public void run() {
                    mPositionTV.setText(mName);
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
            }, false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDelete(int currentItem) {
        if (mLastPosition == currentItem) {
            mLastPosition = -1;
        }
        mRecyclerView.getAdapter().notifyItemRemoved(currentItem);
    }

    @Override
    public void onSelect(Photo photo, int currentItem) {
        PhotoGridAdapter adapter = (PhotoGridAdapter) mRecyclerView.getAdapter();
        if (photo.select) {
            if (mMaxCount == 1 && mSelectPhotos.size() == 1) {
                Photo lastPhoto = mSelectPhotos.get(0);
                lastPhoto.select = false;
                mSelectPhotos.remove(0);
            }
            mLastPosition = currentItem;
            mSelectPhotos.add(photo);
        } else {
            mSelectPhotos.remove(photo);
            mLastPosition = -1;
        }
        adapter.setCheckEnabled(mSelectPhotos.size() < mMaxCount || mMaxCount == 1);
        adapter.notifyDataSetChanged();
        mCompleteBtn.setText(mSelectPhotos.size() == 0 ? "完成" : String.format("完成(%d/%d)".toLowerCase(), mSelectPhotos.size(), mMaxCount));
    }

    @Override
    public void onScrolled(int position) {
        mPositionTV.setText(String.format("%d/%d".toLowerCase(), position + 1, isNeedCamera ? mPhotos.size() - 1 : mPhotos.size()));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing() && !isDestroyed) {
            destroy();
            isDestroyed = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing() && !isDestroyed) {
            destroy();
            isDestroyed = true;
        }
    }

    @Override
    protected void onDestroy() {
        if (!isDestroyed) {
            destroy();
            isDestroyed = true;
        }
        super.onDestroy();
    }

    protected void destroy() {
        mImageCaptureManager.onDestroy();
        MediaStoreHelper.onDestroy(this);
        Glide.get(this).clearMemory();
//        System.exit(0);
//        System.gc();
    }
}
