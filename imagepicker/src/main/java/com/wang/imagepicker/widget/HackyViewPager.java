package com.wang.imagepicker.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.wang.imagepicker.R;

public class HackyViewPager extends ViewPager {

    private boolean isScrollEnable = false;

    public HackyViewPager(Context context) {
        super(context);
        init(context, null);
    }

    public HackyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.HackyViewPager, 0, 0);
        isScrollEnable = a.getBoolean(R.styleable.HackyViewPager_hvp_scrollEnable, false);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isScrollEnable) {
            try {
                return super.onInterceptTouchEvent(ev);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return isScrollEnable && super.onTouchEvent(event);
    }

    public void toggleScrollEnable() {
        isScrollEnable = !isScrollEnable;
    }

    public void setScrollEnable(boolean enable) {
        this.isScrollEnable = enable;
    }

    public boolean isScrollEnable() {
        return isScrollEnable;
    }

}
