package com.yanxuwen.MyRecyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class BaseArrowRefreshHeader extends LinearLayout implements BaseRefreshHeader{
    public int mState = STATE_NORMAL;
    public int mMeasuredHeight;
    public View mContainer;
    public BaseArrowRefreshHeader(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public BaseArrowRefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onMove(float delta) {

    }

    @Override
    public boolean releaseAction() {
        return false;
    }

    @Override
    public void refreshComplate() {

    }
    public void setProgressStyle(int style) {}

    public void setArrowImageView(int resid){}

    public int getState() {
        return mState;
    }
    public void setState(int state){}

    public int getVisiableHeight() {
        int height = 0;
        LayoutParams lp = (LayoutParams) mContainer
                .getLayoutParams();
        height = lp.height;
        return height;
    }
}
