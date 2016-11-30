package com.wang.imagepicker.interfaces;

import android.view.View;

public interface OnPhotoListener extends OnRecyclerViewClickListener{

    void onCheck(int position, boolean check);

    void onItemClick(View v, int position, boolean check);
}