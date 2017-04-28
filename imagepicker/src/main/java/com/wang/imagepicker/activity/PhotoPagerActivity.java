package com.wang.imagepicker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.wang.imagepicker.Extra;
import com.wang.imagepicker.R;
import com.wang.imagepicker.fragment.ShowPicPagerFragment;
import com.wang.imagepicker.interfaces.OnPagerFragmentListener;
import com.wang.imagepicker.model.Photo;
import com.wang.imagepicker.utils.PhotoPager;

import java.util.ArrayList;

/**
 * Created on 2016/8/23.
 * Author: wang
 */

public class PhotoPagerActivity extends AppCompatActivity implements OnPagerFragmentListener {

    private boolean isPhoto;

    private ArrayList<Photo> photos;
    private ArrayList<String> paths;
    private int resultCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_pager);
        resultCode = RESULT_CANCELED;
        Intent intent = getIntent();
        if (intent != null) {
            isPhoto = intent.getBooleanExtra(Extra.EXTRA_IS_PHOTO, true);
            if (isPhoto) {
                photos = intent.getParcelableArrayListExtra(Extra.EXTRA_PHOTOS);
            } else {
                paths = intent.getStringArrayListExtra(Extra.EXTRA_PHOTOS);
            }
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, ShowPicPagerFragment.newInstance(intent.getExtras())).commit();
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        if (isPhoto) {
            intent.putParcelableArrayListExtra(Extra.EXTRA_PHOTOS, photos);
        } else {
            intent.putStringArrayListExtra(Extra.EXTRA_PHOTOS, paths);
        }
        setResult(resultCode, intent);
        super.onBackPressed();
    }

    @Override
    public void onCrop(int position, String path) {
        resultCode = RESULT_OK;
    }

    @Override
    public void onDelete(int currentItem) {
        resultCode = RESULT_OK;
    }

    @Override
    public void onScrolled(int position) {

    }

    @Override
    public void onSelect(Photo photo, int currentItem) {

    }
}
