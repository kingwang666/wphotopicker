package com.wang.imagepicker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wang.imagepicker.R;
import com.wang.imagepicker.interfaces.OnPhotoListener;
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
    private OnPhotoListener mListener;


    public PhotoGridAdapter(List<Photo> photos, boolean showCamera, boolean checkEnabled, OnPhotoListener listener) {
        mPhotos = new ArrayList<>();
        mPhotos = photos;
        mShowCamera = showCamera;
        mCheckEnabled = checkEnabled;
        mListener = listener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        switch (viewType) {
            case TYPE_CAMERA: {
                View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_camera, parent, false);
                return new CameraViewHolder(itemView);
            }
            case TYPE_PHOTO: {
                View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_photo, parent, false);
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
                Glide.with(mContext)
                        .load(new File(photo.path))
                        .dontAnimate()
                        .dontTransform()
                        .centerCrop()
                        .thumbnail(0.1f)
                        .placeholder(R.mipmap.default_image)
                        .error(R.mipmap.default_image)
                        .into(vh.mPhotoImg);
                if (photo.select) {
                    vh.mMaskerView.setVisibility(View.VISIBLE);
                    vh.mSelectedCB.setChecked(true);
                    vh.mSelectedCB.setEnabled(true);
                } else {
                    vh.mMaskerView.setVisibility(View.GONE);
                    vh.mSelectedCB.setChecked(false);
                    vh.mSelectedCB.setEnabled(mCheckEnabled);
                }
                break;
            }

        }
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mShowCamera && position == 0 ? TYPE_CAMERA : TYPE_PHOTO;
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        if (holder.getItemViewType() == TYPE_PHOTO) {
            Glide.clear(((PhotoViewHolder) holder).mPhotoImg);
        }
        super.onViewRecycled(holder);
    }

    public void setCheckEnabled(boolean checkEnabled) {
        if (mCheckEnabled != checkEnabled){
            mCheckEnabled = checkEnabled;
            notifyDataSetChanged();
        }
    }

    public void setShowCamera(boolean showCamera) {
        mShowCamera = showCamera;
        Photo photo = mPhotos.get(0);
        if (showCamera && !photo.path.equals("camera")){
            mPhotos.add(new Photo(-1, "camera"));
        }
        else if (!showCamera && photo.path.equals("camera")){
            mPhotos.remove(0);
        }
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {

        private ImageView mPhotoImg;
        private View mMaskerView;
        private CheckBox mSelectedCB;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            mPhotoImg = (ImageView) itemView.findViewById(R.id.photo_img);
            mMaskerView = itemView.findViewById(R.id.masker_view);
            mSelectedCB = (CheckBox) itemView.findViewById(R.id.selected_cb);
            mSelectedCB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onCheck(getAdapterPosition(), mSelectedCB.isChecked());
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(v, getAdapterPosition(), !mSelectedCB.isChecked());
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
