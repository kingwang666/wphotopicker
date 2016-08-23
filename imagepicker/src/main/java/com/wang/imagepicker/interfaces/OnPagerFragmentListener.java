package com.wang.imagepicker.interfaces;

import com.wang.imagepicker.model.Photo;

/**
 * Created on 2016/8/22.
 * Author: wang
 */

public interface OnPagerFragmentListener {

    void onDelete(int currentItem);

    void onScrolled(int position);

    void onSelect(Photo photo, int currentItem);
}
