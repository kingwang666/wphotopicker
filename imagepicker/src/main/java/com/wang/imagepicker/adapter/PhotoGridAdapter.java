package com.wang.imagepicker.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.wang.imagepicker.R;
import com.wang.imagepicker.interfaces.OnMediaListener;
import com.wang.imagepicker.model.Photo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class PhotoGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_PHOTO = 1;
    public static final int TYPE_CAMERA = 2;
    private boolean mShowCamera;

    private boolean mCheckEnabled;
    private Context mContext;
    private List<Photo> mPhotos;
    private OnMediaListener mListener;

    private RequestOptions mOptions;

    public PhotoGridAdapter(List<Photo> photos, boolean showCamera, boolean checkEnabled, OnMediaListener listener) {
        mPhotos = new ArrayList<>();
        mPhotos = photos;
        mShowCamera = showCamera;
        mCheckEnabled = checkEnabled;
        mListener = listener;
        mOptions = new RequestOptions();
        mOptions.dontAnimate();
        mOptions.dontTransform();
        mOptions.centerCrop();
        mOptions.placeholder(R.mipmap.default_image);
        mOptions.error(R.mipmap.default_image);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        switch (viewType) {
            case TYPE_CAMERA: {
                View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_camera, parent, false);
                return new CameraViewHolder(itemView);
            }
            case TYPE_PHOTO: {
                final View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_photo, parent, false);
                return new PhotoViewHolder(itemView);
            }
        }
        return null;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_CAMERA:
                break;
            case TYPE_PHOTO: {
                PhotoViewHolder vh = (PhotoViewHolder) holder;
                Photo photo = mPhotos.get(position);
                Glide.with(mContext).load(photo.path)
                        .apply(mOptions)
                        .into(vh.mPhotoImg);
                if (photo.select) {
                    vh.mMaskerView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.percent50BlackColor));
                    vh.mSelectedImg.setSelected(true);
                    vh.mSelectedImg.setEnabled(true);
                } else {
                    vh.mMaskerView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.percent10BlackColor));
                    vh.mSelectedImg.setSelected(false);
                    vh.mSelectedImg.setEnabled(mCheckEnabled);
                }
                break;
            }

        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        if (holder.getItemViewType() == TYPE_PHOTO) {
            Glide.with(mContext).clear(((PhotoViewHolder) holder).mPhotoImg);
        }
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mShowCamera && position == 0 ? TYPE_CAMERA : TYPE_PHOTO;
    }


    public void setCheckEnabled(boolean checkEnabled) {
        if (mCheckEnabled != checkEnabled) {
            mCheckEnabled = checkEnabled;
            notifyDataSetChanged();
        }
    }

    public void setShowCamera(boolean showCamera) {
        mShowCamera = showCamera;
        Photo photo = mPhotos.get(0);
        if (showCamera && !photo.path.equals("camera")) {
            mPhotos.add(0, new Photo(-1, "camera"));
        } else if (!showCamera && photo.path.equals("camera")) {
            mPhotos.remove(0);
        }
    }


    class PhotoViewHolder extends RecyclerView.ViewHolder {

        private ImageView mPhotoImg;
        private View mMaskerView;
        private ImageView mSelectedImg;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            mPhotoImg = (ImageView) itemView.findViewById(R.id.photo_img);
            mMaskerView = itemView.findViewById(R.id.masker_view);
            mSelectedImg = (ImageView) itemView.findViewById(R.id.selected_img);
            mSelectedImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onCheck(getAdapterPosition(), !mSelectedImg.isSelected());
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(v, getAdapterPosition(), mSelectedImg.isSelected());
                }
            });
        }
    }

    class CameraViewHolder extends RecyclerView.ViewHolder {

        public CameraViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(v, getAdapterPosition(), false);
                }
            });
        }
    }


}
