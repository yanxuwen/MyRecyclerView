package com.example.xrecyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.yanxuwen.MyRecyclerview.MyBaseAdapter;
import com.yanxuwen.MyRecyclerview.MyRecyclerView;
import com.yanxuwen.MyRecyclerview.ProgressStyle;

import java.util.ArrayList;

public class GridActivity extends AppCompatActivity {

    private MyRecyclerView mRecyclerView;
    private MyChildAdapter mAdapter;
    private ArrayList<String> listData;
    private int refreshTime = 0;
    private int times = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //////////////////////////////////开始处理列表//////////////////////////////////////////////////////////
        mRecyclerView = (MyRecyclerView)this.findViewById(R.id.recyclerview);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mRecyclerView.setItemAnimator(MyRecyclerView.ItemType.FadeInLeft);
        //只有调用setPullRefreshEnabled才会打开下来刷新
        mRecyclerView.setPullRefreshEnabled(true);
        //只有调用setLoadingMoreEnabled才会打开上啦加载
        mRecyclerView.setLoadingMoreEnabled(true);
        View header =   LayoutInflater.from(this).inflate(R.layout.recyclerview_header, (ViewGroup)findViewById(android.R.id.content),false);
        mRecyclerView.addHeaderView(header);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("xxx", "头部");
            }
        });
        mRecyclerView.setLoadingListener(new MyRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refresh();
            }

            @Override
            public void onLoadMore() {
                load();
            }
        });

        listData = new  ArrayList<String>();
        mAdapter = new MyChildAdapter(this,listData);

        mRecyclerView.setAdapter(mAdapter);
        //不同于谷歌的SwipeRefreshLayout，SwipeRefreshLayout.setRefreshing(true);只是单纯的打开加载样式，但不会调用onRefresh方法
        //而这里会自动调用
        mRecyclerView.setRefreshing(true);
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
                holder.expand();
                Log.e("xxx", position + "");
            }
        });
        mAdapter.setOnItemLongClickListener(new MyBaseAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(MyBaseAdapter.BaseViewHolder holder,View view, int position) {
                if(position==1){
                    holder.expand();
                }
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
    public void refresh(){
        refreshTime ++;
        times = 0;
        new Handler().postDelayed(new Runnable(){
            public void run() {

                listData.clear();
                for(int i = 0; i < 20 ;i++){
                    listData.add("item" + i );
                }
                mAdapter.notifyDataSetChanged();
                mAdapter.setFirstOnly(true);
                mRecyclerView.refreshComplete();
            }

        }, 1000);            //refresh data here
    }
    public void load(){
        if(times < 2){
            new Handler().postDelayed(new Runnable(){
                public void run() {
                    mRecyclerView.loadMoreComplete();
                    for(int i = 0; i < 20 ;i++){
                        listData.add("item" + (i + listData.size()) );
                    }
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.refreshComplete();
                }
            }, 1000);
        } else {
            new Handler().postDelayed(new Runnable() {
                public void run() {

                    mRecyclerView.noMoreLoading();
                }
            }, 1000);
        }
        times ++;
    }

}
