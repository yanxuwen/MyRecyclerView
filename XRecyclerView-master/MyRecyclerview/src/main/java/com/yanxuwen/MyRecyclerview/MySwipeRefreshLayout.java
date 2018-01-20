package com.yanxuwen.MyRecyclerview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * Created by yanxuwen on 2017/11/17.
 */

public class MySwipeRefreshLayout extends SwipeRefreshLayout {
    public interface OnMyRefreshListener{
        public void onRefresh();
    }
    public OnMyRefreshListener mOnMyRefreshListener;
    public void setOnMyRefreshListener(OnMyRefreshListener mOnMyRefreshListener){
        this.mOnMyRefreshListener=mOnMyRefreshListener;
    }
    public MySwipeRefreshLayout(Context context) {
        super(context);
    }
    public MySwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 由于谷歌刷新不执行onRefresh，所以我们自己弄了一个监听
     */
    @Override
    public void setRefreshing(boolean refreshing) {
        super.setRefreshing(refreshing);
        if(refreshing) {
            if (mOnMyRefreshListener != null) mOnMyRefreshListener.onRefresh();
        }
    }

    /**
     * @param refreshing
     * @param delayMillis  延迟几秒
     */
    public void setRefreshing(final boolean refreshing, long delayMillis) {
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                setRefreshing(refreshing);
            }
        },delayMillis);
    }
}
