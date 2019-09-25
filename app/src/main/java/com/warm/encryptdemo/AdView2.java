package com.warm.encryptdemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class AdView2 extends FrameLayout {
    private static final String TAG = "AdView2";

    private ViewDragHelper mViewDragHelper;

    private PagerAdapter mPagerAdapter;

    private ViewConfiguration mViewConfiguration;

    public AdView2(@NonNull Context context) {
        this(context, null);
    }

    public AdView2(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    int mIndex;


    int[] imgs = {R.mipmap.img1, R.mipmap.img2, R.mipmap.img3/*, R.mipmap.img4, R.mipmap.img5*/};

    private List<ImageView> mViews = new ArrayList<>(3);


    public void setPagerAdapter(PagerAdapter pagerAdapter) {
        mPagerAdapter = pagerAdapter;


    }

    private ImageView getLeftView(View curView) {
        int index = mViews.indexOf(curView);
        if (index == 0) {
            return mViews.get(2);
        } else if (index == 1) {
            return mViews.get(0);
        } else {
            return mViews.get(1);
        }

    }

    private ImageView getRightView(View curView) {
        int index = mViews.indexOf(curView);
        if (index == 0) {
            return mViews.get(1);
        } else if (index == 1) {
            return mViews.get(2);
        } else {
            return mViews.get(0);
        }
    }

    private int getNext() {
        if (mIndex == imgs.length - 1) {
            return 0;
        }

        return mIndex + 1;
    }

    private int getPrevious() {
        if (mIndex == 0) {
            return imgs.length - 1;
        }
        return mIndex - 1;
    }

    public AdView2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mViewConfiguration = ViewConfiguration.get(context);
        ImageView view = (ImageView) LayoutInflater.from(context).inflate(R.layout.item, this, false);
        addView(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mViews.add(view);
        ImageView view1 = (ImageView) LayoutInflater.from(context).inflate(R.layout.item, this, false);
        addView(view1, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mViews.add(view1);
        ImageView view2 = (ImageView) LayoutInflater.from(context).inflate(R.layout.item, this, false);
        addView(view2, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mViews.add(view2);


        mViewDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            private int left;
            private int top;
            private View mReleasedChild;
            private int xl;

            boolean next;

            @Override
            public void onViewDragStateChanged(int state) {
                super.onViewDragStateChanged(state);

                if (state == ViewDragHelper.STATE_IDLE) {
                    if (!next) {
                        return;
                    }
                    if (mReleasedChild == null) {
                        return;
                    }

                    if (xl < 0) {
                        getLeftView(mReleasedChild).setImageResource(imgs[getNext()]);
                    } else if (xl > 0) {
                        getRightView(mReleasedChild).setImageResource(imgs[getPrevious()]);
                    }

                    Log.d(TAG, "onViewDragStateChanged: " + mIndex);

                }

            }

            @Override
            public void onViewCaptured(@NonNull View capturedChild, int activePointerId) {
                super.onViewCaptured(capturedChild, activePointerId);
                left = capturedChild.getLeft();
                top = capturedChild.getTop();
            }

            @Override
            public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                if (left < 0) {
                    //向右
                    getRightView(changedView).offsetLeftAndRight(getWidth() + left - getRightView(changedView).getLeft());
                    getLeftView(changedView).offsetLeftAndRight(-getWidth());
                } else if (left > 0) {
                    getLeftView(changedView).offsetLeftAndRight(-getWidth() + left - getLeftView(changedView).getLeft());
                    getRightView(changedView).offsetLeftAndRight(getWidth());
                } else {
                    getLeftView(changedView).offsetLeftAndRight(-getWidth());

                    getRightView(changedView).offsetLeftAndRight(getWidth());

                }
            }

            @Override
            public boolean tryCaptureView(@NonNull View view, int i) {
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
                Log.d(TAG, "clampViewPositionHorizontal: " + mIndex);
                this.xl = left;
                return left;
            }

            @Override
            public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
                return 0;
            }

            @Override
            public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                Log.d(TAG, "onViewReleased: ");
                next = Math.abs(xvel) > 1000 || Math.abs((int) releasedChild.getX()) > (releasedChild.getWidth() / 2);
                if (next) {
                    if (xvel < 0) {
                        mViewDragHelper.settleCapturedViewAt(-releasedChild.getWidth(), top);
                        mIndex = getNext();
                    } else {
                        mViewDragHelper.settleCapturedViewAt(releasedChild.getWidth(), top);
                        mIndex = getPrevious();
                    }
                } else {
                    mViewDragHelper.settleCapturedViewAt(left, top);
                }
                invalidate();
                this.mReleasedChild = releasedChild;
            }
        });
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d(TAG, "onLayout: left=" + left + "top=" + top + "right=" + right + "bottom=" + bottom);

//        getChildAt(1).offsetLeftAndRight(getWidth());
//        getChildAt(2).offsetLeftAndRight(-getWidth());

    }

    // 2, 在onInterceptTouchEvent和onTouchEvent中调用VDH的方法
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 通过使用mDragHelper.shouldInterceptTouchEvent(ev)来决定我们是否应该拦截当前的事件
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 通过mDragHelper.processTouchEvent(event)来处理事件
        mViewDragHelper.processTouchEvent(event);
        return true; // 返回 true，表示事件被处理了。
    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

}
