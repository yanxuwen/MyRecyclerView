package com.example.xrecyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.example.xrecyclerview.Utils.MyRecyclerViewUtils;
import com.yanxuwen.MyRecyclerview.MyBaseAdapter;
import com.yanxuwen.MyRecyclerview.MyRecyclerView;
import com.yanxuwen.MyRecyclerview.MySwipeRefreshLayout;

import java.util.ArrayList;

/**
 * Created by yanxuwen on 2018/1/29.
 */

public class LinearActivity3 extends AppCompatActivity implements MyRecyclerView.LoadingListener{
    private MySwipeRefreshLayout mMySwipeRefreshLayout;
    private MyRecyclerView mRecyclerView;
    private ArrayList<String> listData= new  ArrayList<String>();
    MyRecyclerViewUtils mMyRecyclerViewUtils;
    private int refreshTime = 0;
    private int times = 0;
    View head_view;
    MyBaseAdapter mAdapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview2);
        mMySwipeRefreshLayout=(MySwipeRefreshLayout)findViewById(R.id.refreshLayout);
        mRecyclerView = (MyRecyclerView)this.findViewById(R.id.recyclerview);
        mAdapter = new MyChildAdapter(this,listData);
        mMyRecyclerViewUtils= new MyRecyclerViewUtils(this,mMySwipeRefreshLayout,mRecyclerView,mAdapter);
        head_view=   LayoutInflater.from(this).inflate(R.layout.recyclerview_header,null);
        mMyRecyclerViewUtils.setRecyclerView();
        mRecyclerView.addHeaderView(head_view);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLoadingListener(this);
        mMySwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onRefresh() {
            refreshTime ++;
            times = 0;
            new Handler().postDelayed(new Runnable(){
                public void run() {
                    listData.clear();
                    for(int i = 0; i < 20 ;i++){
                        listData.add("item" + i );
                    }
                    mMyRecyclerViewUtils.notifyDataSetChanged(mMyRecyclerViewUtils.getLimit(),true);
                }

            }, 3000);
    }

    @Override
    public void onLoadMore() {
        if(times < 2){
            new Handler().postDelayed(new Runnable(){
                public void run() {
                    for(int i = 0; i < 20 ;i++){
                        listData.add("item" + (i + listData.size()) );
                    }
                    mMyRecyclerViewUtils.notifyDataSetChanged(mMyRecyclerViewUtils.getLimit(),false);
                }
            }, 2000);
        } else {
            mMyRecyclerViewUtils.notifyDataSetChanged(mMyRecyclerViewUtils.getLimit(),false);
        }
        times ++;
    }
}
