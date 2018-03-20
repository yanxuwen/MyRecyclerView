package com.example.xrecyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.yanxuwen.DensityUtil;
import com.yanxuwen.MyRecyclerview.MyBaseAdapter;
import com.yanxuwen.MyRecyclerview.MyRecyclerView;
import com.yanxuwen.MyRecyclerview.MySwipeRefreshLayout;
import com.yanxuwen.MyRecyclerview.ProgressStyle;

import java.util.ArrayList;

public class LinearActivity2 extends AppCompatActivity implements MyRecyclerView.LoadingListener{
    private MySwipeRefreshLayout mMySwipeRefreshLayout;
    private MyRecyclerView mRecyclerView;
    private MyChildAdapter mAdapter;
    private ArrayList<String> listData;
    private int refreshTime = 0;
    private int times = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //-------------------------------mSwipeRefreshLayout初始化--------------------------------------------//
        mMySwipeRefreshLayout=(MySwipeRefreshLayout)findViewById(R.id.refreshLayout);
        mMySwipeRefreshLayout.setColorSchemeResources(R.color.black);
        mMySwipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);;
        mMySwipeRefreshLayout.setProgressBackgroundColor(R.color.white);
        //swipeRefreshLayout.setPadding(20, 20, 20, 20);
        //代表支持缩放，0为start, 200为end
        mMySwipeRefreshLayout.setProgressViewOffset(false, -DensityUtil.dip2px(this,50), DensityUtil.dip2px(this,30));
        //// 设置手指在屏幕下拉多少距离会触发下拉刷新
//        mSwipeRefreshLayout.setDistanceToTriggerSync(DensityUtil.dip2px(this,50));
        mMySwipeRefreshLayout.setProgressViewEndTarget(false, DensityUtil.dip2px(this,30));
        //////////////////////////////////开始处理列表//////////////////////////////////////////////////////////
        mRecyclerView = (MyRecyclerView)this.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(MyRecyclerView.ItemType.FadeInLeft);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.MaterialDesign);
        //注意设置MaterialDesign类型的时候，要单独的在设置下颜色，最好以SwipeRefreshLayout颜色拼配
        if(mRecyclerView!=null&&mRecyclerView.getMaterialProgressView()!=null){
            mRecyclerView.getMaterialProgressView().setColorSchemeResources(R.color.black);
        }
        //注意由于下来刷新用的是谷歌的，，则要设置下谷歌刷新标识
        mRecyclerView.setGoogleRefresh(true,mMySwipeRefreshLayout);
        //只有调用setLoadingMoreEnabled才会打开上啦加载
        mRecyclerView.setLoadingMoreEnabled(true);
        View head_view=   LayoutInflater.from(this).inflate(R.layout.recyclerview_header,null);
        mRecyclerView.addHeaderView(head_view);
        mRecyclerView.setLoadingListener(this);

        listData = new  ArrayList<String>();
        mAdapter = new MyChildAdapter(this,listData);

        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.setRefreshing(true);
        //主要setRefreshing(true)只是打开刷新图标出来而已，并不会自动调用onRefresh，所以还要在调用一次
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mMySwipeRefreshLayout.setRefreshing(true);
            }
        },100);
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.add("newly added item", 2);
            }
        });

        findViewById(R.id.del).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mAdapter.remove(2);
            }
        });
        mAdapter.setOnItemClickListener(new MyBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MyBaseAdapter.BaseViewHolder holder,View view, int position) {
                Log.e("xxx", position + "");
            }
        });
        mAdapter.setOnItemLongClickListener(new MyBaseAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(MyBaseAdapter.BaseViewHolder holder,View view, int position) {
                Log.e("xxx", position + "long");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        {
            refreshTime ++;
            times = 0;
            new Handler().postDelayed(new Runnable(){
                public void run() {
                    mMySwipeRefreshLayout.setRefreshing(false);
                    listData.clear();
                    for(int i = 0; i < 20 ;i++){
                        listData.add("item" + i );
                    }
                    mAdapter.notifyDataSetChanged();
                    mAdapter.setFirstOnly(true);
                    mRecyclerView.refreshComplete();
                }

            }, 3000);            //refresh data here
        }
    }

    @Override
    public void onLoadMore() {
        if(times < 2){
            new Handler().postDelayed(new Runnable(){
                public void run() {
                    mRecyclerView.loadMoreComplete();
                    for(int i = 0; i < 20 ;i++){
                        listData.add("item" + (i + listData.size()) );
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }, 2000);
        } else {
            new Handler().postDelayed(new Runnable() {
                public void run() {

                    mRecyclerView.noMoreLoading();
                }
            }, 2000);
        }
        times ++;
    }
}
