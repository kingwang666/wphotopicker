package com.wang.imagepicker.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.wang.imagepicker.R;
import com.wang.imagepicker.interfaces.OnMediaListener;
import com.wang.imagepicker.model.Photo;
import com.wang.imagepicker.model.Video;

import java.io.File;
import java.util.List;

/**
 * Created by wang
 * on 2017/4/27
 */

public class VideoGridAdapter extends RecyclerView.Adapter<VideoGridAdapter.VideoViewHolder> {

    private boolean mCheckEnabled;
    private List<Video> mVideos;
    private OnMediaListener mListener;

    private Context mContext;

    private RequestOptions mOptions;

    public VideoGridAdapter(List<Video> videos, boolean checkEnabled, OnMediaListener listener) {
        mCheckEnabled = checkEnabled;
        mVideos = videos;
        mListener = listener;
        mOptions = new RequestOptions();
        mOptions.dontAnimate();
        mOptions.dontTransform();
        mOptions.centerCrop();
        mOptions.placeholder(R.mipmap.default_image);
        mOptions.error(R.mipmap.default_image);
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder vh, int position) {
        Video video = mVideos.get(position);
        Glide.with(mContext)
                .load(video.path)
                .apply(mOptions)
                .into(vh.mVideoImg);
        vh.mDurTV.setText(video.getDuration());
        vh.mSizeTV.setText(video.getSize());
        if (video.select) {
            vh.mMaskerView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.percent50BlackColor));
            vh.mSelectedImg.setSelected(true);
            vh.mSelectedImg.setEnabled(true);
        } else {
            vh.mMaskerView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.percent10BlackColor));
            vh.mSelectedImg.setSelected(false);
            vh.mSelectedImg.setEnabled(mCheckEnabled);
        }
    }

    public void setCheckEnabled(boolean checkEnabled) {
        if (mCheckEnabled != checkEnabled) {
            mCheckEnabled = checkEnabled;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {

        private ImageView mVideoImg;
        private View mMaskerView;
        private ImageView mSelectedImg;
        private TextView mDurTV;
        private TextView mSizeTV;

        public VideoViewHolder(View itemView) {
            super(itemView);
            mVideoImg = (ImageView) itemView.findViewById(R.id.video_img);
            mMaskerView = itemView.findViewById(R.id.masker_view);
            mSelectedImg = (ImageView) itemView.findViewById(R.id.selected_img);
            mDurTV = (TextView) itemView.findViewById(R.id.duration_tv);
            mSizeTV = (TextView) itemView.findViewById(R.id.size_tv);
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
}
