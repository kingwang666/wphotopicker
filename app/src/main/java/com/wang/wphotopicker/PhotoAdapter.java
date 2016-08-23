package com.wang.wphotopicker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wang.imagepicker.model.Photo;

import java.util.ArrayList;

/**
 * Created on 2016/8/23.
 * Author: wang
 */
public class PhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Photo> mPhotos;
    private OnRecyclerViewClickListener mListener;
    private Context mContext;

    public PhotoAdapter(ArrayList<Photo> photos, OnRecyclerViewClickListener listener) {
        mPhotos = photos;
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageViewHolder vh = (ImageViewHolder) holder;
        Photo photo = mPhotos.get(position);
        Glide.with(mContext).load(photo.path).into(vh.mImageView);
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.image);
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onClick(getAdapterPosition());
                }
            });
        }
    }
}
