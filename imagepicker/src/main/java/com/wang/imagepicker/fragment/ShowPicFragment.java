package com.wang.imagepicker.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.wang.imagepicker.R;
import com.wang.imagepicker.adapter.ShowPicPagerAdapter;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Created on 2016/6/30.
 * Author: wang
 */
public class ShowPicFragment extends BaseShowPicFragment {


    private PhotoView mItemImg;


    private String mUrl;
    private ShowPicPagerAdapter.OnPhotoViewClickListener mListener;

    public ShowPicFragment() {
    }


    public static ShowPicFragment getInstance(String url) {
        ShowPicFragment fragment = new ShowPicFragment();
        Bundle arg = new Bundle();
        arg.putString("url", url);
        fragment.setArguments(arg);
        return fragment;
    }


    @Override
    protected void initData(Bundle arg) {
        mUrl = arg.getString("url");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_show_pic;
    }

    @Override
    protected void initListener(View rootView) {
        mItemImg = (PhotoView) rootView.findViewById(R.id.item_img);

        mItemImg.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                if (mListener != null){
                    mListener.onClick();
                }
            }

            @Override
            public void onOutsidePhotoTap() {
                if (mListener != null){
                    mListener.onClick();
                }
            }
        });
    }


    @Override
    protected void afterView() {
        mItemImg.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mItemImg != null && getActivity() != null){
                    Glide.with(ShowPicFragment.this).load(mUrl).error(R.mipmap.default_image).into(mItemImg);
                }
            }
        }, 200);

    }

    @Override
    protected void onInvisible() {
        if ( mItemImg == null || TextUtils.isEmpty(mUrl) || getActivity() == null){
            return;
        }
        Glide.with(this).load(mUrl).error(R.mipmap.default_image).into(mItemImg);
    }

    public ShowPicFragment setOnPhotoViewClickListener(ShowPicPagerAdapter.OnPhotoViewClickListener listener){
        mListener = listener;
        return this;
    }
}
