package com.yanxuwen;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.yanxuwen.MyRecyclerview.MyBaseAdapter;
import com.yanxuwen.swipelibrary.SwipeLayout;

/**
 * Created by yanxuwen on 2018/6/21.
 */

public class MySwipeLayout extends SwipeLayout {
    public static boolean isIntercept=false;
    public MySwipeLayout(Context context) {
        super(context);
    }

    public MySwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySwipeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(isIntercept&&MyBaseAdapter.swipe_holder!=null){
            boolean isreturn= inRangeOfView(MyBaseAdapter.swipe_holder.getSwipeView(),ev)?super.onInterceptTouchEvent(ev):true;
            return this==MyBaseAdapter.swipe_holder.getSwipeLayout()?isreturn:true;

        }
        return isIntercept?true:super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(isIntercept&&MyBaseAdapter.swipe_holder!=null){
            if(event.getAction()==MotionEvent.ACTION_DOWN) {
                MyBaseAdapter.swipe_holder.swipe.close();
            }
            return true;
        }else{

        }
        return super.onTouchEvent(event);
    }


    private boolean inRangeOfView(View view, MotionEvent ev){
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if(ev.getX() < x || ev.getRawX() > (x + view.getWidth()) || ev.getRawY() < y || ev.getY() > (y + view.getHeight())){
            return false;
        }
        return true;
    }
}
