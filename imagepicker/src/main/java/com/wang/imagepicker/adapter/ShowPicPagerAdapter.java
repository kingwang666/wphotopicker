package com.wang.imagepicker.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.wang.imagepicker.fragment.ShowPicFragment;
import com.wang.imagepicker.model.Photo;

import java.util.ArrayList;
import java.util.List;


/**
 * Created on 2016/3/11.
 * Author: wang
 */
public class ShowPicPagerAdapter extends FragmentStatePagerAdapter {

    private List<String> mPics;
    private List<Photo> mPhotos;
    private boolean isPhotos;
    private boolean needCamera;

    private int defaultImg;

    private OnPhotoViewClickListener mListener;

    public ShowPicPagerAdapter(FragmentManager fm, boolean needCamera, List<String> pics, int defaultId, OnPhotoViewClickListener listener) {
        super(fm);
        this.mPics = pics;
        mListener = listener;
        isPhotos = false;
        this.needCamera = needCamera;
        defaultImg = defaultId;
        notifyDataSetChanged();
    }

    public ShowPicPagerAdapter(FragmentManager fm, boolean needCamera, ArrayList<Photo> photos, int defaultId, OnPhotoViewClickListener listener) {
        super(fm);
        this.mPhotos = photos;
        mListener = listener;
        isPhotos = true;
        this.needCamera = needCamera;
        defaultImg = defaultId;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return ShowPicFragment.getInstance(isPhotos ? mPhotos.get(needCamera ? position + 1 : position).path : mPics.get(needCamera ? position + 1 : position)).setOnPhotoViewClickListener(mListener).setDefaultImg(defaultImg);
    }

    @Override
    public int getCount() {
        return (isPhotos ? mPhotos.size() : mPics.size()) + (needCamera ? -1 : 0);
    }

    public void remove(int position){
        if (isPhotos){
            mPhotos.remove(position);
        }
        else {
            mPics.remove(position);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }


    public interface OnPhotoViewClickListener{
        void onClick();
    }
}
