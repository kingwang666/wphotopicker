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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wang.imagepicker.Extra;
import com.wang.imagepicker.R;
import com.wang.imagepicker.adapter.VideoGridAdapter;
import com.wang.imagepicker.interfaces.OnMediaListener;
import com.wang.imagepicker.model.Video;
import com.wang.imagepicker.utils.MediaStoreHelper;
import com.wang.imagepicker.utils.PermissionUtil;
import com.wang.imagepicker.utils.VideoPicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wang
 * on 2017/4/27
 */

public class VideoPickerActivity extends AppCompatActivity implements OnMediaListener, View.OnClickListener {

    private static final int GET_VIDEOS = 23;
    private RecyclerView mRecyclerView;
    private Button mCompleteBtn;

    private ArrayList<Video> mSelectVideos;
    private ArrayList<Video> mVideos;

    private int mMaxCount = 9;
    private int mColumn = 3;

    private int mToolbarBg;
    private int mCompleteBg;

    private boolean isDestroyed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDestroyed = false;
        setContentView(R.layout.activity_video_picker);
        mVideos = new ArrayList<>();
        initData(getIntent());
        initView();
        requestPermissions(GET_VIDEOS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void getVideos(){
        Bundle mediaStoreArgs = new Bundle();
        mediaStoreArgs.putParcelableArrayList(Extra.EXTRA_SELECTED_VIDEOS, mSelectVideos);
        MediaStoreHelper.getVideos(this, mediaStoreArgs,
                new MediaStoreHelper.OnVideoResultCallback() {
                    @Override
                    public void onResultCallback(List<Video> videos) {
                        mVideos.clear();
                        mVideos.addAll(videos);
                        if (mRecyclerView != null) {
                            mRecyclerView.getAdapter().notifyDataSetChanged();
                        }
                    }
                });
    }

    private void initData(Intent intent) {
        if (intent != null) {
            if (mSelectVideos == null) {
                mSelectVideos = intent.getParcelableArrayListExtra(Extra.EXTRA_SELECTED_VIDEOS);
                if (mSelectVideos == null) {
                    mSelectVideos = new ArrayList<>();
                }
            }
            mMaxCount = intent.getIntExtra(Extra.EXTRA_MAX_COUNT, mMaxCount);
            mColumn = intent.getIntExtra(Extra.EXTRA_GRID_COLUMN, mColumn);
            mToolbarBg = intent.getIntExtra(Extra.EXTRA_TOOLBAR_BG, -1);
            mCompleteBg = intent.getIntExtra(Extra.EXTRA_COMPLETE_BG, -1);
        }
    }

    private void initView() {

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, mColumn));
        mRecyclerView.setAdapter(new VideoGridAdapter(mVideos, mSelectVideos.size() < mMaxCount, this));
        mRecyclerView.setHasFixedSize(true);
        mCompleteBtn = (Button) findViewById(R.id.complete_btn);
        if (mMaxCount == 1){
            mCompleteBtn.setVisibility(View.GONE);
        }
        else if (mSelectVideos.size() > 0) {
            mCompleteBtn.setText(String.format("完成(%d/%d)".toLowerCase(), mSelectVideos.size(), mMaxCount));
        }
        findViewById(R.id.back_img).setOnClickListener(this);
        if (mToolbarBg != -1) {
            findViewById(R.id.toolbar_view).setBackgroundColor(mToolbarBg);
        }
        if (mCompleteBg != -1) {
            mCompleteBtn.setBackgroundResource(mCompleteBg);
        }
        mCompleteBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.complete_btn) {
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra(Extra.EXTRA_SELECTED_VIDEOS, mSelectVideos);
            setResult(RESULT_OK, intent);
            finish();
        } else if (i == R.id.back_img) {
            onBackPressed();
        }
    }

    @Override
    public void onClick(int position, Object data) {

    }

    @Override
    public void onCheck(int position, boolean check) {
        Video video = mVideos.get(position);
        video.select = check;
        VideoGridAdapter adapter = (VideoGridAdapter) mRecyclerView.getAdapter();
        if (check) {
            if (mMaxCount == 1 && mSelectVideos.size() == 1) {
                Video lastVideo = mSelectVideos.get(0);
                lastVideo.select = false;
                mSelectVideos.remove(0);
            }
            mSelectVideos.add(video);
        } else {
            mSelectVideos.remove(video);
        }
        adapter.setCheckEnabled(mSelectVideos.size() < mMaxCount || mMaxCount == 1);
        adapter.notifyDataSetChanged();
        if (mMaxCount > 1) {
            mCompleteBtn.setText(mSelectVideos.size() == 0 ? "完成" : String.format("完成(%d/%d)".toLowerCase(), mSelectVideos.size(), mMaxCount));
        }
        else if (check){
            onClick(mCompleteBtn);
        }
    }

    @Override
    public void onItemClick(View v, int position, boolean check) {
        onCheck(position, !check);
    }

    public void requestPermissions(int requestCode, String... permissions) {

        if (PermissionUtil.checkSelfPermission(this, permissions)) {
            ActivityCompat.requestPermissions(this, permissions, requestCode);
        } else {
            requestPermissionsEnd(requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        List<String> allPermissions = Arrays.asList(permissions);
        if (!PermissionUtil.verifyPermissions(allPermissions, grantResults)) {
          if (requestCode == GET_VIDEOS){
                Toast.makeText(this, "权限请求失败，相册图片", Toast.LENGTH_SHORT).show();
          }
        } else {
            requestPermissionsEnd(requestCode);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void requestPermissionsEnd(int requestCode) {
        if (requestCode == GET_VIDEOS) {
            getVideos();
        }
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
        MediaStoreHelper.onDestroy(this);
        Glide.get(this).clearMemory();
    }

}
