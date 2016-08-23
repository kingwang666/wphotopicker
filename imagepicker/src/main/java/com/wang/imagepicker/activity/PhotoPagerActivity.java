package com.wang.imagepicker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_pager);

        Intent intent = getIntent();
        if (intent != null){
            isPhoto = intent.getBooleanExtra(PhotoPager.IS_PHOTO, true);
            boolean showDelete = intent.getBooleanExtra(PhotoPager.SHOW_DELETE, true);
            boolean showTop = intent.getBooleanExtra(PhotoPager.SHOW_TOP, true);
            boolean haveCamera = intent.getBooleanExtra(PhotoPager.HAVE_CAMERA, false);
            int position = intent.getIntExtra(PhotoPager.POSITION, 0);
            if (isPhoto){
                photos = intent.getParcelableArrayListExtra(PhotoPager.PHOTOS);
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, ShowPicPagerFragment.newInstance(photos, haveCamera, position, showTop, showDelete, false)).commit();
            }
            else {
                paths = intent.getStringArrayListExtra(PhotoPager.PHOTOS);
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, ShowPicPagerFragment.newInstance(paths, haveCamera, position, showTop, showDelete, false)).commit();
            }
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        if (isPhoto){
            intent.putParcelableArrayListExtra(PhotoPager.PHOTOS, photos);
        }
        else {
            intent.putStringArrayListExtra(PhotoPager.PHOTOS, paths);
        }
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    @Override
    public void onDelete(int currentItem) {

    }

    @Override
    public void onScrolled(int position) {

    }

    @Override
    public void onSelect(Photo photo, int currentItem) {

    }
}
