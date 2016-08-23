package com.wang.imagepicker.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created on 2016/8/19.
 * Author: wang
 */

public abstract class BaseShowPicFragment extends Fragment {
    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible;

    /**
     * 是否视图已经初始化
     */
    protected boolean isPrepared = false;
    /**
     * 是否二次加载
     */
    protected boolean isTwo = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutId(), container, false);
        initListener(rootView);
        isPrepared = true;
        afterView();
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    protected abstract void initData(Bundle arg);

    protected abstract int getLayoutId();

    protected abstract void initListener(View rootView);

    protected abstract void afterView();



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    /**
     * 可见
     */
    protected void onVisible() {
        delayLoad();
    }


    /**
     * 不可见
     */
    protected void onInvisible() {


    }


    /**
     * 延迟加载
     */
    protected boolean delayLoad() {
        if (!isPrepared || !isVisible || isTwo) {
            return false;
        }
        isTwo = true;
        return true;
    }

    @Override
    public void onDestroyView() {
        isPrepared = false;
        super.onDestroyView();
    }
}
