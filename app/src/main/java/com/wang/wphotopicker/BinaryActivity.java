package com.wang.wphotopicker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;

import com.wang.imagepicker.Extra;
import com.wang.imagepicker.model.Photo;
import com.wang.imagepicker.utils.PhotoPicker;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 2017/8/12.
 * Author: wang
 */

public class BinaryActivity extends AppCompatActivity {

    @BindView(R.id.source_img)
    AppCompatImageView mSourceImg;
    @BindView(R.id.grey_img)
    AppCompatImageView mGreyImg;
    @BindView(R.id.binary_img)
    AppCompatImageView mBinaryImg;

    private int[] mPixels;
    private int mWidth;
    private int mHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binary);
        ButterKnife.bind(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 100) {
            ArrayList<Photo> photos = data.getParcelableArrayListExtra(Extra.EXTRA_SELECTED_PHOTOS);
            Flowable.just(photos.get(0).path)
                    .subscribeOn(Schedulers.io())
                    .map(new Function<String, Bitmap>() {
                        @Override
                        public Bitmap apply(@NonNull String s) throws Exception {
                            return GlideApp.with(BinaryActivity.this).asBitmap().load(s).submit(1080, 1080).get();
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Bitmap>() {
                        @Override
                        public void accept(Bitmap bitmap) throws Exception {
                            mSourceImg.setImageBitmap(bitmap);
                            mWidth = bitmap.getWidth();
                            mHeight = bitmap.getHeight();
                            mPixels = new int[mWidth * mHeight];
                            bitmap.getPixels(mPixels, 0, mWidth, 0 , 0, mWidth, mHeight);
                        }
                    });
        }
    }


    @OnClick(R.id.get_btn)
    public void onMGetBtnClicked() {
        PhotoPicker.builder()
                .setGridColumnCount(3)
                .setPhotoCount(1)
                .setCrop(false)
                .setPreviewEnabled(true)
                .start(this, 100);
    }

    @OnClick(R.id.grey_btn)
    public void onMGreyBtnClicked() {
        if (mPixels != null){
            Flowable.just(mPixels)
                    .subscribeOn(Schedulers.io())
                    .map(new Function<int[], Bitmap>() {
                        @Override
                        public Bitmap apply(@NonNull int[] ints) throws Exception {
                            int[] greyPixels = ImageUtil.grey(mPixels);
                            return Bitmap.createBitmap(greyPixels, mWidth, mHeight, Bitmap.Config.ARGB_8888);
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Bitmap>() {
                        @Override
                        public void accept(Bitmap bitmap) throws Exception {
                            mGreyImg.setImageBitmap(bitmap);
                        }
                    });
        }
    }

    @OnClick(R.id.binary_btn)
    public void onMBinaryBtnClicked() {
        if (mPixels != null){
            Flowable.just(mPixels)
                    .subscribeOn(Schedulers.io())
                    .map(new Function<int[], Bitmap>() {
                        @Override
                        public Bitmap apply(@NonNull int[] ints) throws Exception {
                            int[] binary = new int[mWidth * mHeight];
                            ImageUtil.OTSU(mPixels, null, binary);
                            return Bitmap.createBitmap(binary, mWidth, mHeight, Bitmap.Config.ARGB_8888);
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Bitmap>() {
                        @Override
                        public void accept(Bitmap bitmap) throws Exception {
                            mBinaryImg.setImageBitmap(bitmap);
                        }
                    });
        }
    }
}
