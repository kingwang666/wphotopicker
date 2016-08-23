package com.wang.imagepicker.utils;

import android.support.v7.util.DiffUtil;
import android.util.Log;

import com.wang.imagepicker.model.Photo;

import java.util.List;

/**
 * Created on 2016/8/23.
 * Author: wang
 */

public class PhotoChangeCallback extends DiffUtil.Callback {

    private List<Photo> mOldList;
    private List<Photo> mNewList;

    public PhotoChangeCallback(List<Photo> oldList, List<Photo> newList) {
        mOldList = oldList;
        mNewList = newList;
    }

    @Override
    public int getOldListSize() {
        return mOldList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldList.get(oldItemPosition).id == mNewList.get(newItemPosition).id;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Log.e("test", (mOldList.get(oldItemPosition).select ? "true  " : "false  ") + (mNewList.get(newItemPosition).select ? "true" : "false"));
        return mOldList.get(oldItemPosition).select == mNewList.get(newItemPosition).select;
    }
}
