package com.wang.wphotopicker;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wang.imagepicker.Extra;
import com.wang.imagepicker.model.Photo;
import com.wang.imagepicker.utils.PhotoPager;
import com.wang.imagepicker.utils.PhotoPicker;
import com.wang.imagepicker.utils.VideoPicker;

import java.io.File;
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
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Photo");
        file.mkdirs();
        PhotoPicker.builder()
                .setGridColumnCount(3)
                .setPhotoCount(9)
                .setCrop(true)
                .setCropCircle(true)
                .setCropDirPath(file.getAbsolutePath())
                .setPreviewEnabled(true)
                .setSelected(mPhotos)
                .start(this, 100);

    }

    public void doClick2(View view) {
        VideoPicker.builder()
                .setGridColumnCount(3)
                .setPhotoCount(1)
                .setSelected(mPhotos)
                .start(this, 101);
    }

    @Override
    public void onClick(int position) {
        PhotoPager.builderActivity()
                .setPosition(position)
                .setPhotos(mPhotos)
                .setShowTop(true)
                .setShowDelete(true)
                .setShowCrop(true)
                .setFullscreen(true)
                .start(this, 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 100) {
            ArrayList<Photo> photos = data.getParcelableArrayListExtra(Extra.EXTRA_SELECTED_PHOTOS);
            mPhotos.clear();
            mPhotos.addAll(photos);
            mRecyclerView.getAdapter().notifyDataSetChanged();

        } else if (requestCode == 101) {
            ArrayList<Photo> photos = data.getParcelableArrayListExtra(Extra.EXTRA_SELECTED_VIDEOS);
            mPhotos.clear();
            mPhotos.addAll(photos);
            mRecyclerView.getAdapter().notifyDataSetChanged();
        } else if (requestCode == 200) {
            ArrayList<Photo> photos = data.getParcelableArrayListExtra(Extra.EXTRA_PHOTOS);
            mPhotos.clear();
            mPhotos.addAll(photos);
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }
}
