package com.wang.imagepicker.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
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
import android.widget.Toast;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.wang.imagepicker.Extra;
import com.wang.imagepicker.R;
import com.wang.imagepicker.adapter.ShowPicPagerAdapter;
import com.wang.imagepicker.interfaces.OnPagerFragmentListener;
import com.wang.imagepicker.model.Photo;
import com.wang.imagepicker.utils.CropUtil;
import com.wang.imagepicker.widget.HackyViewPager;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;


public class ShowPicPagerFragment extends Fragment implements View.OnClickListener, ShowPicPagerAdapter.OnPhotoViewClickListener {

    public final static long ANIM_DURATION = 200L;

    private HackyViewPager mViewPager;
    private View mTopView;
    private View mBottomView;
    private ImageView mCropImg;
    private CheckBox mSelectCB;
    private ImageView mDeleteImg;
    private TextView mTitleTV;

    private int thumbnailTop = 0;
    private int thumbnailLeft = 0;
    private int thumbnailWidth = 0;
    private int thumbnailHeight = 0;

    private boolean hasAnim = false;
    private boolean isPhoto;
    private boolean isShowTop;
    private boolean isShowDelete;
    private boolean isShowBottom;
    private boolean isAlwaysShow;
    private boolean isShowCrop;
    private boolean needCamera;
    private boolean fullscreen = false;


    private int mStartItem = 0;
    private int mCurrentItem = 0;

    private int mDefaultImg;

    private int mCropToolbarColor;
    private boolean mCropFullPath;
    private String mCropDestPath;
    private boolean mIsCircle;
    private float mAspectRatioX;
    private float mAspectRatioY;
    private int mMaxWidth;
    private int mMaxHeight;
    private boolean mFreeStyle;

    private final ColorMatrix colorizerMatrix = new ColorMatrix();
    private ArrayList<String> mPaths;
    private ArrayList<Photo> mPhotos;

    private OnPagerFragmentListener mListener;


    public static ShowPicPagerFragment newInstance(Bundle args) {
        ShowPicPagerFragment f = new ShowPicPagerFragment();
        f.setArguments(args);
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
            isPhoto = bundle.getBoolean(Extra.EXTRA_IS_PHOTO);
            if (isPhoto) {
                mPhotos = bundle.getParcelableArrayList(Extra.EXTRA_PHOTOS);
            } else {
                mPaths = bundle.getStringArrayList(Extra.EXTRA_PHOTOS);
            }
            hasAnim = bundle.getBoolean(Extra.EXTRA_HAS_ANIM);
            isShowTop = bundle.getBoolean(Extra.EXTRA_SHOW_TOP);
            isShowDelete = bundle.getBoolean(Extra.EXTRA_SHOW_DELETE);
            isShowBottom = bundle.getBoolean(Extra.EXTRA_SHOW_BOTTOM);
            isAlwaysShow = bundle.getBoolean(Extra.EXTRA_ALWAYS_SHOW);
            isShowCrop = bundle.getBoolean(Extra.EXTRA_SHOW_CROP);
            if (isShowCrop) {
                mCropToolbarColor = bundle.getInt(Extra.EXTRA_CROP_TOOLBAR_COLOR, -1);
                mCropFullPath = bundle.getBoolean(Extra.EXTRA_CROP_FULL_PATH, false);
                mCropDestPath = bundle.getString(Extra.EXTRA_CROP_DEST_PATH);
                mIsCircle = bundle.getBoolean(Extra.EXTRA_CROP_CIRCLE, false);
                if (TextUtils.isEmpty(mCropDestPath)) {
                    mCropFullPath = false;
                    mCropDestPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Photo";
                }
                mAspectRatioX = bundle.getFloat(Extra.EXTRA_CROP_ASPECT_RATIO_X, 0f);
                mAspectRatioY = bundle.getFloat(Extra.EXTRA_CROP_ASPECT_RATIO_Y, 0f);
                mMaxWidth = bundle.getInt(Extra.EXTRA_CROP_MAX_WIDTH, 0);
                mMaxHeight = bundle.getInt(Extra.EXTRA_CROP_MAX_HEIGHT, 0);
                mFreeStyle = bundle.getBoolean(Extra.EXTRA_CROP_FREE_STYLE, false);
            }

            needCamera = bundle.getBoolean(Extra.EXTRA_HAVE_CAMERA, false);
            fullscreen = bundle.getBoolean(Extra.EXTRA_FULLSCREEN, false);
            mStartItem = bundle.getInt(Extra.EXTRA_POSITION);
            mCurrentItem = mStartItem;
            thumbnailTop = bundle.getInt(Extra.EXTRA_THUMBNAIL_TOP);
            thumbnailLeft = bundle.getInt(Extra.EXTRA_THUMBNAIL_LEFT);
            thumbnailWidth = bundle.getInt(Extra.EXTRA_THUMBNAIL_WIDTH);
            thumbnailHeight = bundle.getInt(Extra.EXTRA_THUMBNAIL_HEIGHT);
            mDefaultImg = bundle.getInt(Extra.EXTRA_ERROR_IMG);
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
        mCropImg = (ImageView) rootView.findViewById(R.id.head_view_crop_btn);
        mCropImg.setOnClickListener(this);
        mDeleteImg = (ImageView) rootView.findViewById(R.id.head_view_delete_btn);
        mDeleteImg.setOnClickListener(this);
        mTitleTV = (TextView) rootView.findViewById(R.id.head_view_title_tv);
        mViewPager = (HackyViewPager) rootView.findViewById(R.id.view_pager);
        if (isPhoto) {
            mViewPager.setAdapter(new ShowPicPagerAdapter(getChildFragmentManager(), needCamera, mPhotos, mDefaultImg, this));
        } else {
            mViewPager.setAdapter(new ShowPicPagerAdapter(getChildFragmentManager(), needCamera, mPaths, mDefaultImg, this));
        }
        mViewPager.setCurrentItem(needCamera ? mStartItem - 1 : mStartItem);
        if (isPhoto) {
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
            mCropImg.setVisibility(isShowCrop ? View.VISIBLE : View.GONE);
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
                if (isPhoto) {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == Extra.REQUEST_CROP) {
            Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                String path = resultUri.getPath();
                if (isPhoto) {
                    Photo photo = mPhotos.get(mCurrentItem);
                    photo.id = Extra.CROP;
                    photo.type = 4;
                    photo.path = path;
                }else {
                    mPaths.set(mCurrentItem, path);
                }
                mViewPager.getAdapter().notifyDataSetChanged();
                if (mListener != null){
                    mListener.onCrop(mCurrentItem, path);
                }
            } else {
                Toast.makeText(getContext(), "裁剪发生不可预知的错误", Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            Throwable cropError = UCrop.getError(data);
            Toast.makeText(getContext(), "裁剪发生不可预知的错误", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.select_cb) {
            if (isPhoto) {
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
            if (isPhoto) {
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
        } else if (i == R.id.head_view_crop_btn) {
            UCrop.Options options = new UCrop.Options();
            options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
            options.setFreeStyleCropEnabled(mFreeStyle);
            if (mMaxHeight > 0 && mMaxWidth > 0) {
                options.withMaxResultSize(mMaxWidth, mMaxHeight);
            }
            if (mAspectRatioX > 0f && mAspectRatioY > 0f) {
                options.withAspectRatio(mAspectRatioX, mAspectRatioY);
            }
            options.setCircleDimmedLayer(mIsCircle);
            options.setCompressionQuality(100);
            if (mCropToolbarColor != -1) {
                options.setToolbarColor(mCropToolbarColor);
                options.setStatusBarColor(mCropToolbarColor);
            }
            CropUtil.onStartCrop(this, isPhoto ? mPhotos.get(mCurrentItem).path : mPaths.get(mCurrentItem), mCropDestPath, mCropFullPath, options, Extra.REQUEST_CROP);
        }
    }


    @Override
    public void onClick() {
        if (!isAlwaysShow && (isShowTop || isShowBottom)) {
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
                        mTopView.setVisibility(isShowTop ? View.VISIBLE : View.GONE);
                        mDeleteImg.setVisibility(isShowDelete ? View.VISIBLE : View.GONE);
                        mCropImg.setVisibility(isShowCrop ? View.VISIBLE : View.GONE);
                        mBottomView.setVisibility(isShowBottom ? View.VISIBLE : View.GONE);
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

        if (!hasAnim) {
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
