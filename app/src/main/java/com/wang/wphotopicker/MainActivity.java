package com.wang.wphotopicker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wang.imagepicker.model.Photo;
import com.wang.imagepicker.utils.PhotoPager;
import com.wang.imagepicker.utils.PhotoPicker;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnRecyclerViewClickListener {

    private ArrayList<Photo> mPhotos;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPhotos = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setAdapter(new PhotoAdapter(mPhotos, this));
    }

    public void doClick(View view) {
        PhotoPicker.builder()
                .setGridColumnCount(3)
                .setPhotoCount(9)
                .setPreviewEnabled(true)
                .setSelected(mPhotos)
                .start(this, 100);
    }

    @Override
    public void onClick(int position) {
        PhotoPager.builder()
                .setPosition(position)
                .setPhotos(mPhotos)
                .start(this, 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 100) {
            ArrayList<Photo> photos = data.getParcelableArrayListExtra(PhotoPicker.EXTRA_SELECTED_PHOTOS);
            mPhotos.clear();
            mPhotos.addAll(photos);
            mRecyclerView.getAdapter().notifyDataSetChanged();

        } else if (requestCode == 200) {
            ArrayList<Photo> photos = data.getParcelableArrayListExtra(PhotoPager.PHOTOS);
            mPhotos.clear();
            mPhotos.addAll(photos);
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }
}
