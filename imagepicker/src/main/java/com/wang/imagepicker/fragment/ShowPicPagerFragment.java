package com.wang.imagepicker.fragment;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.wang.imagepicker.R;
import com.wang.imagepicker.adapter.ShowPicPagerAdapter;
import com.wang.imagepicker.interfaces.OnPagerFragmentListener;
import com.wang.imagepicker.model.Photo;
import com.wang.imagepicker.widget.HackyViewPager;

import java.util.ArrayList;
import java.util.List;


public class ShowPicPagerFragment extends Fragment implements View.OnClickListener, ShowPicPagerAdapter.OnPhotoViewClickListener {

    public final static long ANIM_DURATION = 200L;

    private final static String ARG_PATH = "PATHS";
    private final static String ARG_START_ITEM = "ARG_START_ITEM";
    private final static String ARG_IS_PHOTOS = "IS_PHOTOS";
    private final static String ARG_THUMBNAIL_TOP = "THUMBNAIL_TOP";
    private final static String ARG_THUMBNAIL_LEFT = "THUMBNAIL_LEFT";
    private final static String ARG_THUMBNAIL_WIDTH = "THUMBNAIL_WIDTH";
    private final static String ARG_THUMBNAIL_HEIGHT = "THUMBNAIL_HEIGHT";
    private final static String ARG_NEED_CAMERA = "NEED_CAMERA";
    private final static String ARG_HAS_ANIM = "HAS_ANIM";
    private final static String ARG_SHOW_TOP = "SHOW_TOP";
    private final static String ARG_SHOW_DELETE = "SHOW_DELETE";
    private final static String ARG_SHOW_BOTTOM = "SHOW_BOTTOM";
    private final static String ARG_SHOW_DEFAULT = "SHOW_DEFAULT";
    private final static String ARG_FULLSCREEN = "FULLSCREEN";


    private HackyViewPager mViewPager;
    private View mTopView;
    private View mBottomView;
    private CheckBox mSelectCB;
    private ImageView mDeleteImg;
    private TextView mTitleTV;

    private int thumbnailTop = 0;
    private int thumbnailLeft = 0;
    private int thumbnailWidth = 0;
    private int thumbnailHeight = 0;

    private boolean hasAnim = false;
    private boolean isPhotos;
    private boolean isShowTop;
    private boolean isShowDelete;
    private boolean isShowBottom;
    private boolean needCamera;
    private boolean fullscreen = false;

    private int mStartItem = 0;
    private int mCurrentItem = 0;

    private int mDefaultImg;

    private final ColorMatrix colorizerMatrix = new ColorMatrix();
    private ArrayList<String> mPaths;
    private ArrayList<Photo> mPhotos;

    private OnPagerFragmentListener mListener;


    public static ShowPicPagerFragment newInstance(List<String> paths, boolean fullscreen, boolean needCamera, int currentItem, boolean isShowTop, boolean isShowDelete, boolean isShowBottom) {

        ShowPicPagerFragment f = new ShowPicPagerFragment();

        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PATH, (ArrayList<String>) paths);
        args.putInt(ARG_START_ITEM, currentItem);
        args.putBoolean(ARG_HAS_ANIM, false);
        args.putBoolean(ARG_IS_PHOTOS, false);
        args.putBoolean(ARG_SHOW_TOP, isShowTop);
        args.putBoolean(ARG_SHOW_DELETE, isShowDelete);
        args.putBoolean(ARG_SHOW_BOTTOM, isShowBottom);
        args.putBoolean(ARG_NEED_CAMERA, needCamera);
        args.putBoolean(ARG_FULLSCREEN, fullscreen);
        f.setArguments(args);

        return f;
    }

    public static ShowPicPagerFragment newInstance(List<String> paths, boolean fullscreen, boolean needCamera, int currentItem, boolean isShowTop, boolean isShowDelete, boolean isShowBottom, int defaultId) {
        ShowPicPagerFragment f = newInstance(paths, fullscreen, needCamera, currentItem, isShowTop, isShowDelete, isShowBottom);
        Bundle arg = f.getArguments();
        arg.putInt(ARG_SHOW_DEFAULT, defaultId);
        return f;
    }


    public static ShowPicPagerFragment newInstance(List<String> paths, boolean fullscreen, boolean needCamera, int currentItem, boolean isShowTop, boolean isShowDelete, boolean isShowBottom, int[] screenLocation, int thumbnailWidth, int thumbnailHeight) {
        ShowPicPagerFragment f = newInstance(paths, fullscreen, needCamera, currentItem, isShowTop, isShowDelete, isShowBottom);
        Bundle arg = f.getArguments();
        arg.putInt(ARG_THUMBNAIL_LEFT, screenLocation[0]);
        arg.putInt(ARG_THUMBNAIL_TOP, screenLocation[1]);
        arg.putInt(ARG_THUMBNAIL_WIDTH, thumbnailWidth);
        arg.putInt(ARG_THUMBNAIL_HEIGHT, thumbnailHeight);
        arg.putBoolean(ARG_HAS_ANIM, true);
        return f;
    }

    public static ShowPicPagerFragment newInstance(List<String> paths, boolean fullscreen, boolean needCamera, int currentItem, boolean isShowTop, boolean isShowDelete, boolean isShowBottom, int[] screenLocation, int thumbnailWidth, int thumbnailHeight, int defaultId) {
        ShowPicPagerFragment f = newInstance(paths, fullscreen, needCamera, currentItem, isShowTop, isShowDelete, isShowBottom, screenLocation, thumbnailWidth, thumbnailHeight);
        Bundle arg = f.getArguments();
        arg.putInt(ARG_SHOW_DEFAULT, defaultId);
        return f;
    }

    public static ShowPicPagerFragment newInstance(ArrayList<Photo> photos, boolean fullscreen, boolean needCamera, int currentItem, boolean isShowTop, boolean isShowDelete, boolean isShowBottom) {
        ShowPicPagerFragment f = new ShowPicPagerFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PATH, photos);
        args.putInt(ARG_START_ITEM, currentItem);
        args.putBoolean(ARG_HAS_ANIM, false);
        args.putBoolean(ARG_IS_PHOTOS, true);
        args.putBoolean(ARG_SHOW_TOP, isShowTop);
        args.putBoolean(ARG_SHOW_DELETE, isShowDelete);
        args.putBoolean(ARG_SHOW_BOTTOM, isShowBottom);
        args.putBoolean(ARG_NEED_CAMERA, needCamera);
        args.putBoolean(ARG_FULLSCREEN, fullscreen);
        f.setArguments(args);
        return f;
    }

    public static ShowPicPagerFragment newInstance(ArrayList<Photo> paths, boolean fullscreen, boolean needCamera, int currentItem, boolean isShowTop, boolean isShowDelete, boolean isShowBottom, int defaultId) {
        ShowPicPagerFragment f = newInstance(paths, fullscreen, needCamera, currentItem, isShowTop, isShowDelete, isShowBottom);
        Bundle arg = f.getArguments();
        arg.putInt(ARG_SHOW_DEFAULT, defaultId);
        return f;
    }

    public static ShowPicPagerFragment newInstance(ArrayList<Photo> photos, boolean fullscreen, boolean needCamera, int currentItem, boolean isShowTop, boolean isShowDelete, boolean isShowBottom, int[] screenLocation, int thumbnailWidth, int thumbnailHeight) {

        ShowPicPagerFragment f = newInstance(photos, fullscreen, needCamera, currentItem, isShowTop, isShowDelete, isShowBottom);
        Bundle arg = f.getArguments();
        arg.putInt(ARG_THUMBNAIL_LEFT, screenLocation[0]);
        arg.putInt(ARG_THUMBNAIL_TOP, screenLocation[1]);
        arg.putInt(ARG_THUMBNAIL_WIDTH, thumbnailWidth);
        arg.putInt(ARG_THUMBNAIL_HEIGHT, thumbnailHeight);
        arg.putBoolean(ARG_HAS_ANIM, true);
        return f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPagerFragmentListener) {
            mListener = (OnPagerFragmentListener) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        if (bundle != null) {
            isPhotos = bundle.getBoolean(ARG_IS_PHOTOS);
            if (isPhotos) {
                mPhotos = bundle.getParcelableArrayList(ARG_PATH);
            } else {
                mPaths = bundle.getStringArrayList(ARG_PATH);
            }
            hasAnim = bundle.getBoolean(ARG_HAS_ANIM);
            isShowTop = bundle.getBoolean(ARG_SHOW_TOP);
            isShowDelete = bundle.getBoolean(ARG_SHOW_DELETE);
            isShowBottom = bundle.getBoolean(ARG_SHOW_BOTTOM);
            needCamera = bundle.getBoolean(ARG_NEED_CAMERA, false);
            fullscreen = bundle.getBoolean(ARG_FULLSCREEN, false);
            mStartItem = bundle.getInt(ARG_START_ITEM);
            mCurrentItem = mStartItem;
            thumbnailTop = bundle.getInt(ARG_THUMBNAIL_TOP);
            thumbnailLeft = bundle.getInt(ARG_THUMBNAIL_LEFT);
            thumbnailWidth = bundle.getInt(ARG_THUMBNAIL_WIDTH);
            thumbnailHeight = bundle.getInt(ARG_THUMBNAIL_HEIGHT);
            mDefaultImg = bundle.getInt(ARG_SHOW_DEFAULT);
        }

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (fullscreen) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        View rootView = inflater.inflate(R.layout.fragment_show_pic_pager, container, false);

        mTopView = rootView.findViewById(R.id.top_view);
        mBottomView = rootView.findViewById(R.id.bottom_view);
        mSelectCB = (CheckBox) rootView.findViewById(R.id.select_cb);
        mSelectCB.setOnClickListener(this);
        mDeleteImg = (ImageView) rootView.findViewById(R.id.head_view_delete_btn);
        mDeleteImg.setOnClickListener(this);
        mTitleTV = (TextView) rootView.findViewById(R.id.head_view_title_tv);
        mViewPager = (HackyViewPager) rootView.findViewById(R.id.view_pager);
        if (isPhotos) {
            mViewPager.setAdapter(new ShowPicPagerAdapter(getChildFragmentManager(), needCamera, mPhotos, mDefaultImg, this));
        } else {
            mViewPager.setAdapter(new ShowPicPagerAdapter(getChildFragmentManager(), needCamera, mPaths, mDefaultImg, this));
        }
        mViewPager.setCurrentItem(needCamera ? mStartItem - 1 : mStartItem);
        if (isPhotos) {
            mSelectCB.setChecked(mPhotos.get(mStartItem).select);
            mTitleTV.setText(String.format("%d/%d".toLowerCase(), needCamera ? mStartItem : (mStartItem + 1), needCamera ? mPhotos.size() - 1 : mPhotos.size()));
        } else {
            mTitleTV.setText(String.format("%d/%d".toLowerCase(), needCamera ? mStartItem : (mStartItem + 1), needCamera ? mPaths.size() - 1 : mPaths.size()));
        }
        if (mListener != null) {
            mListener.onScrolled(needCamera ? mStartItem - 1 : mStartItem);
        }
        // Only run the animation if we're coming from the parent activity, not if
        // we're recreated automatically by the window manager (e.g., device rotation)
        if (savedInstanceState == null && hasAnim) {
            ViewTreeObserver observer = mViewPager.getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {

                    mViewPager.getViewTreeObserver().removeOnPreDrawListener(this);

                    // Figure out where the thumbnail and full size versions are, relative
                    // to the screen and each other
                    int[] screenLocation = new int[2];
                    mViewPager.getLocationOnScreen(screenLocation);
                    thumbnailLeft = thumbnailLeft - screenLocation[0];
                    thumbnailTop = thumbnailTop - screenLocation[1];

                    runEnterAnimation();

                    return true;
                }
            });
        } else if (!hasAnim) {
            mTopView.setVisibility(isShowTop ? View.VISIBLE : View.GONE);
            mDeleteImg.setVisibility(isShowDelete ? View.VISIBLE : View.GONE);
            mBottomView.setVisibility(isShowBottom ? View.VISIBLE : View.GONE);
        }


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentItem = needCamera ? position + 1 : position;
//                hasAnim = mStartItem == position;
                if (isPhotos) {
                    Photo photo = mPhotos.get(mCurrentItem);
                    mSelectCB.setChecked(photo.select);
                    mTitleTV.setText(String.format("%d/%d".toLowerCase(), position + 1, needCamera ? mPhotos.size() - 1 : mPhotos.size()));
                } else {
                    mTitleTV.setText(String.format("%d/%d".toLowerCase(), position + 1, needCamera ? mPaths.size() - 1 : mPaths.size()));
                }
                if (mListener != null) {
                    mListener.onScrolled(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return rootView;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.select_cb) {
            if (isPhotos) {
                Photo photo = mPhotos.get(mCurrentItem);
                photo.select = !photo.select;
                if (mListener != null) {
                    mListener.onSelect(photo, mCurrentItem);
                }
            }
        } else if (i == R.id.head_view_delete_btn) {
            ((ShowPicPagerAdapter) mViewPager.getAdapter()).remove(mCurrentItem);
            if (mListener != null) {
                mListener.onDelete(mCurrentItem);
            }
            if (isPhotos) {
                if (mPhotos.size() == 0) {
                    getActivity().onBackPressed();
                }
                if (mCurrentItem > mPhotos.size() - 1) {
                    mCurrentItem = mPhotos.size() - 1;
                } else {
                    if (mListener != null) {
                        mListener.onScrolled(needCamera ? mCurrentItem - 1 : mCurrentItem);
                    }
                    mSelectCB.setChecked(mPhotos.get(mCurrentItem).select);
                    mTitleTV.setText(String.format("%d/%d".toLowerCase(), needCamera ? mCurrentItem : mCurrentItem + 1, needCamera ? mPhotos.size() - 1 : mPhotos.size()));
                }
            } else {
                if (mPaths.size() == 0) {
                    getActivity().onBackPressed();
                }
                if (mCurrentItem > mPaths.size() - 1) {
                    mCurrentItem = mPaths.size() - 1;
                } else {
                    if (mListener != null) {
                        mListener.onScrolled(needCamera ? mCurrentItem - 1 : mCurrentItem);
                    }
                    mTitleTV.setText(String.format("%d/%d".toLowerCase(), needCamera ? mCurrentItem : mCurrentItem + 1, needCamera ? mPaths.size() - 1 : mPaths.size()));
                }
            }
            mViewPager.setCurrentItem(needCamera ? mCurrentItem - 1 : mCurrentItem);
        }
    }


    @Override
    public void onClick() {
        if (isShowTop || isShowBottom) {
            if (mTopView.getVisibility() == View.VISIBLE || mBottomView.getVisibility() == View.VISIBLE) {
                if (isShowTop) {
                    mTopView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.top_out));
                    mTopView.setVisibility(View.GONE);
                }
                if (isShowBottom) {
                    mBottomView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_out));
                    mBottomView.setVisibility(View.GONE);
                }
            } else {
                if (isShowTop) {
                    mTopView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.top_in));
                    mTopView.setVisibility(View.VISIBLE);
                }
                if (isShowBottom) {
                    mBottomView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));
                    mBottomView.setVisibility(View.VISIBLE);
                }
            }
        } else {
            getActivity().onBackPressed();
        }
    }


    /**
     * The enter animation scales the picture in from its previous thumbnail
     * size/location, colorizing it in parallel. In parallel, the background of the
     * activity is fading in. When the pictue is in place, the text description
     * drops down.
     */
    private void runEnterAnimation() {
        final long duration = ANIM_DURATION;

        // Set starting values for properties we're going to animate. These
        // values scale and position the full size version down to the thumbnail
        // size/location, from which we'll animate it back up
        ViewHelper.setPivotX(mViewPager, 0);
        ViewHelper.setPivotY(mViewPager, 0);
        ViewHelper.setScaleX(mViewPager, (float) thumbnailWidth / mViewPager.getWidth());
        ViewHelper.setScaleY(mViewPager, (float) thumbnailHeight / mViewPager.getHeight());
        ViewHelper.setTranslationX(mViewPager, thumbnailLeft);
        ViewHelper.setTranslationY(mViewPager, thumbnailTop);

        // Animate scale and translation to go from thumbnail to full size
        ViewPropertyAnimator.animate(mViewPager)
                .setDuration(duration)
                .scaleX(1)
                .scaleY(1)
                .translationX(0)
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (isShowTop) {
                            mTopView.setVisibility(View.VISIBLE);
                        }
                        if (isShowBottom) {
                            mBottomView.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

        // Fade in the black background
        ObjectAnimator bgAnim = ObjectAnimator.ofInt(mViewPager.getBackground(), "alpha", 0, 255);
        bgAnim.setDuration(duration);
        bgAnim.start();

        // Animate a color filter to take the image from grayscale to full color.
        // This happens in parallel with the image scaling and moving into place.
        ObjectAnimator colorizer = ObjectAnimator.ofFloat(ShowPicPagerFragment.this, "saturation", 0, 1);
        colorizer.setDuration(duration);
        colorizer.start();

    }


    /**
     * The exit animation is basically a reverse of the enter animation, except that if
     * the orientation has changed we simply scale the picture back into the center of
     * the screen.
     *
     * @param endAction This action gets run after the animation completes (this is
     *                  when we actually switch activities)
     */
    public void runExitAnimation(final Runnable endAction, boolean toOlderPosition) {

        if (!getArguments().getBoolean(ARG_HAS_ANIM, false) || !hasAnim) {
            if (fullscreen) {
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
            endAction.run();
            return;
        }

        final long duration = ANIM_DURATION;

        // Animate image back to thumbnail size/location
        ViewPropertyAnimator.animate(mViewPager)
                .setDuration(duration)
                .setInterpolator(new AccelerateInterpolator())
                .scaleX((float) thumbnailWidth / mViewPager.getWidth())
                .scaleY((float) thumbnailHeight / mViewPager.getHeight())
                .translationX(toOlderPosition ? thumbnailLeft : ((mViewPager.getWidth() - thumbnailWidth) / 2))
                .translationY(toOlderPosition ? thumbnailTop : ((mViewPager.getHeight() - thumbnailHeight) / 2))
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        mTopView.setVisibility(View.GONE);
                        mBottomView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (fullscreen) {
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        }
                        endAction.run();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });

        // Fade out background
        ObjectAnimator bgAnim = ObjectAnimator.ofInt(mViewPager.getBackground(), "alpha", 0);
        bgAnim.setDuration(duration);
        bgAnim.start();

        // Animate a color filter to take the image back to grayscale,
        // in parallel with the image scaling and moving into place.
        ObjectAnimator colorizer =
                ObjectAnimator.ofFloat(ShowPicPagerFragment.this, "saturation", 1, 0);
        colorizer.setDuration(duration);
        colorizer.start();
    }


    /**
     * This is called by the colorizing animator. It sets a saturation factor that is then
     * passed onto a filter on the picture's drawable.
     *
     * @param value saturation
     */
    public void setSaturation(float value) {
        colorizerMatrix.setSaturation(value);
        ColorMatrixColorFilter colorizerFilter = new ColorMatrixColorFilter(colorizerMatrix);
        mViewPager.getBackground().setColorFilter(colorizerFilter);
    }


    public HackyViewPager getViewPager() {
        return mViewPager;
    }


    public ArrayList<String> getPaths() {
        return mPaths;
    }


    public int getStartItem() {
        return mViewPager.getCurrentItem();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
