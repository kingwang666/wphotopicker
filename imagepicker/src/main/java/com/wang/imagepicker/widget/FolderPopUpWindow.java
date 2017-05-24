package com.wang.imagepicker.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.wang.imagepicker.R;
import com.wang.imagepicker.adapter.PhotoDirectoryAdapter;
import com.wang.imagepicker.interfaces.OnRecyclerViewListener;
import com.wang.imagepicker.model.PhotoDirectory;

import java.util.List;


public class FolderPopUpWindow extends PopupWindow implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private final View masker;
    private final View marginView;
    private int marginPx;

    public FolderPopUpWindow(Context context, List<PhotoDirectory> photoDirectories, OnRecyclerViewListener listener) {
        super(context);

        final View view = View.inflate(context, R.layout.pop_folder, null);
        masker = view.findViewById(R.id.masker);
        masker.setOnClickListener(this);
        marginView = view.findViewById(R.id.margin);
        marginView.setOnClickListener(this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(new PhotoDirectoryAdapter(photoDirectories, listener));
        mRecyclerView.setHasFixedSize(true);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);  //如果不设置，就是 AnchorView 的宽度
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(0));
        setAnimationStyle(0);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int maxHeight = view.getHeight() * 5 / 8;
                int realHeight = mRecyclerView.getHeight();
                ViewGroup.LayoutParams listParams = mRecyclerView.getLayoutParams();
                listParams.height = realHeight > maxHeight ? maxHeight : realHeight;
                mRecyclerView.setLayoutParams(listParams);
                LinearLayout.LayoutParams marginParams = (LinearLayout.LayoutParams) marginView.getLayoutParams();
                marginParams.height = marginPx;
                marginView.setLayoutParams(marginParams);
                enterAnimator();
            }
        });
    }

    private void enterAnimator() {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(masker, "alpha", 0, 1);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(mRecyclerView, "translationY", mRecyclerView.getHeight(), 0);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(400);
        set.playTogether(alpha, translationY);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.start();
    }

    @Override
    public void dismiss() {
        exitAnimator();
    }

    private void exitAnimator() {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(masker, "alpha", 1, 0);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(mRecyclerView, "translationY", 0, mRecyclerView.getHeight());
        AnimatorSet set = new AnimatorSet();
        set.setDuration(300);
        set.playTogether(alpha, translationY);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                FolderPopUpWindow.super.dismiss();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        set.start();
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setSelection(int selection) {
        mRecyclerView.scrollToPosition(selection);
    }

    public void setMargin(int marginPx) {
        this.marginPx = marginPx;
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

}
