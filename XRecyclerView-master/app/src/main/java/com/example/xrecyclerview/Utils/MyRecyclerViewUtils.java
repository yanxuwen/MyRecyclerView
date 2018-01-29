package com.example.xrecyclerview.Utils;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.example.app.myapplication.Utils.RecyclerViewUtils;
import com.example.xrecyclerview.R;
import com.yanxuwen.MyRecyclerview.MyBaseAdapter;
import com.yanxuwen.MyRecyclerview.MyRecyclerView;
import com.yanxuwen.MyRecyclerview.MySwipeRefreshLayout;

/**
 * Created by yanxuwen on 2018/1/26.
 */

public class MyRecyclerViewUtils extends RecyclerViewUtils {
    public MyRecyclerViewUtils(Context context, MySwipeRefreshLayout mSwipeRefreshLayout, MyRecyclerView mRecyclerView, MyBaseAdapter mAdapter) {
        super(context, mSwipeRefreshLayout, mRecyclerView, mAdapter);
    }

    /**
     * @param count 当前刷新的数量
     * @param timeout 超时
     */
    public void notifyDataSetChanged(int count,boolean timeout ){
        LinearLayout emptyView_network = null;
        LinearLayout empty_data= null;
        try{emptyView_network = (LinearLayout) getContentView().findViewById(R.id.emptyview_network);}catch (Exception e){}
        try{ empty_data = (LinearLayout) getMSwipeRefreshLayout().findViewById(R.id.empty_data);}catch (Exception e){}
        super.notifyDataSetChanged(count,"没有更多了",timeout,empty_data,emptyView_network);
    }

    /**
     * @param count 当前刷新的数量
     * @param timeout 超时
     * @param empty_data 由于会有很多界面的空数据都不一样，，所以要单独传
     */
    public void notifyDataSetChanged(int count,boolean timeout ,View empty_data){
        LinearLayout emptyView_network = null;
        try{emptyView_network = (LinearLayout) getContentView().findViewById(R.id.emptyview_network);}catch (Exception e){}
        super.notifyDataSetChanged(count,"没有更多了",timeout,empty_data,emptyView_network);
    }
}
