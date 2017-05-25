package com.wang.imagepicker.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.OnOutsidePhotoTapListener;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.wang.imagepicker.R;
import com.wang.imagepicker.adapter.ShowPicPagerAdapter;



/**
 * Created on 2016/6/30.
 * Author: wang
 */
public class ShowPicFragment extends BaseShowPicFragment {


    private PhotoView mItemImg;


    private String mUrl;
    private ShowPicPagerAdapter.OnPhotoViewClickListener mListener;

    private int mDefaultImg = R.mipmap.default_image;

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

        mItemImg.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
                if (mListener != null){
                    mListener.onClick();
                }
            }
        });

        mItemImg.setOnOutsidePhotoTapListener(new OnOutsidePhotoTapListener() {
            @Override
            public void onOutsidePhotoTap(ImageView imageView) {
                if (mListener != null){
                    mListener.onClick();
                }
            }
        });
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }

    @Override
    protected void afterView() {
        mItemImg.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mItemImg != null && getActivity() != null){
                    Glide.with(ShowPicFragment.this).load(mUrl).apply(new RequestOptions().error(mDefaultImg)).into(mItemImg);
                }
            }
        }, 200);

    }

    @Override
    protected void onInvisible() {
        if ( mItemImg == null || TextUtils.isEmpty(mUrl) || getActivity() == null){
            return;
        }
        Glide.with(this).load(mUrl).apply(new RequestOptions().error(mDefaultImg)).into(mItemImg);
    }

    public ShowPicFragment setOnPhotoViewClickListener(ShowPicPagerAdapter.OnPhotoViewClickListener listener){
        mListener = listener;
        return this;
    }

    public ShowPicFragment setDefaultImg(int defaultImg) {
        if (defaultImg != 0) {
            mDefaultImg = defaultImg;
        }
        return this;
    }
}
