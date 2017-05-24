package com.wang.imagepicker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.wang.imagepicker.R;
import com.wang.imagepicker.interfaces.OnRecyclerViewListener;
import com.wang.imagepicker.model.PhotoDirectory;

import java.io.File;
import java.util.List;

/**
 * Created on 2016/8/18.
 * Author: wang
 */

public class PhotoDirectoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PhotoDirectory> mList;

    private OnRecyclerViewListener mListener;

    private Context mContext;

    public PhotoDirectoryAdapter(List<PhotoDirectory> list, OnRecyclerViewListener listener) {
        mList = list;
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_folder, parent, false);
        return new FolderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PhotoDirectory photoDirectory = mList.get(position);
        FolderViewHolder vh = (FolderViewHolder) holder;
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        Glide.with(mContext).load(new File(photoDirectory.coverPath))
                .apply(options)
                .into(vh.mCoverImg);
        vh.mSelectCB.setSelected(photoDirectory.select);
        vh.mNameTV.setText(photoDirectory.name);
        vh.mCountTV.setText(String.format("共%d张".toLowerCase(), photoDirectory.photos.size()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class FolderViewHolder extends RecyclerView.ViewHolder {

        ImageView mCoverImg;
        TextView mNameTV;
        TextView mCountTV;
        ImageView mSelectCB;

        public FolderViewHolder(View itemView) {
            super(itemView);
            mCoverImg = (ImageView) itemView.findViewById(R.id.cover_img);
            mNameTV = (TextView) itemView.findViewById(R.id.name_tv);
            mCountTV = (TextView) itemView.findViewById(R.id.count_tv);
            mSelectCB = (ImageView) itemView.findViewById(R.id.select_cb);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(getAdapterPosition(), null);
                }
            });
        }
    }
}
