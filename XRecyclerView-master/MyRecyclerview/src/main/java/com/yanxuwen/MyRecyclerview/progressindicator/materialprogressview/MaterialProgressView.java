package com.yanxuwen.MyRecyclerview.progressindicator.materialprogressview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;

public class MaterialProgressView extends RelativeLayout {

    protected CircleImageView mCircleView;
    protected MaterialProgressDrawable mProgress;
    protected static final int CIRCLE_BG_LIGHT = 0xFFFAFAFA;
    protected static final int CIRCLE_DIAMETER = 40;
    protected int mCircleWidth;
    protected int mCircleHeight;
    protected int mExtraShadowSpace;

    public MaterialProgressView(Context context) {
        super(context);
        initProgressView();
    }

    public MaterialProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initProgressView();
    }

    public MaterialProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initProgressView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MaterialProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initProgressView();
    }

    protected void initProgressView() {
        createProgressView();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        mCircleHeight = mCircleWidth = (int) (CIRCLE_DIAMETER * metrics.density);
        mExtraShadowSpace = (int) ((0.1333 * CIRCLE_DIAMETER) * metrics.density);
        setVisibility(VISIBLE);
    }

    protected void createProgressView() {
        mCircleView = new CircleImageView(getContext(), CIRCLE_BG_LIGHT, CIRCLE_DIAMETER / 2);
        mProgress = new MaterialProgressDrawable(getContext(), this);
        mProgress.setBackgroundColor(CIRCLE_BG_LIGHT);
        mCircleView.setImageDrawable(mProgress);
        mCircleView.setVisibility(View.VISIBLE);
        mProgress.setAlpha(255);
        addView(mCircleView);
    }

    public void setColorSchemeColors(int[] colors) {
        mProgress.setColorSchemeColors(colors);
    }
    public void setColorSchemeResources(@ColorRes int... colorResIds) {
        final Resources res = getResources();
        int[] colorRes = new int[colorResIds.length];
        for (int i = 0; i < colorResIds.length; i++) {
            colorRes[i] = res.getColor(colorResIds[i]);
        }
        mProgress.setColorSchemeColors(colorRes);
    }
    @Override
    public void setVisibility(int visibility) {
        if (visibility == GONE || visibility == INVISIBLE) {
            mProgress.stop();
        } else {
            mProgress.start();
            mProgress.showArrow(true);
        }
        super.setVisibility(visibility);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int width = getMeasuredWidth();
        int circleWidth = mCircleView.getMeasuredWidth();
        int circleHeight = mCircleView.getMeasuredHeight();
        mCircleView.layout((width / 2 - circleWidth / 2), 0, (width / 2 + circleWidth / 2), circleHeight);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST || MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            super.onMeasure(MeasureSpec.makeMeasureSpec(mCircleWidth + mExtraShadowSpace, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mCircleHeight + mExtraShadowSpace, MeasureSpec.EXACTLY));
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
        mCircleView.measure(MeasureSpec.makeMeasureSpec(mCircleWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mCircleHeight, MeasureSpec.EXACTLY));
    }
}