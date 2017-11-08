package com.example.xrecyclerview.PinnedHeader;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.xrecyclerview.R;
import com.yanxuwen.MyRecyclerview.MyBaseAdapter;
import com.yanxuwen.PinnedHeader.DividerItemDecoration;
import com.yanxuwen.PinnedHeader.HeaderRecyclerAndFooterWrapperAdapter;
import com.yanxuwen.PinnedHeader.IndexBar.widget.IndexBar;
import com.yanxuwen.PinnedHeader.ViewHolder;
import com.yanxuwen.PinnedHeader.suspension.SuspensionDecoration;

import java.util.ArrayList;
import java.util.List;

public class PinnedHeaderActivity extends Activity {
    private static final String TAG = "zxt";
    private RecyclerView mRv;
    private CityAdapter mAdapter;
    private HeaderRecyclerAndFooterWrapperAdapter mHeaderAdapter;
    private LinearLayoutManager mManager;
    private List<CityBean> mDatas;

    private SuspensionDecoration mDecoration;
    private EditText et_search;
    /**
     * 右侧边栏导航区域
     */
    private IndexBar mIndexBar;

    /**
     * 显示指示器DialogText
     */
    private TextView mTvSideBarHint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinnedheader);
        et_search=(EditText)findViewById(R.id.et_search);
        mRv = (RecyclerView) findViewById(R.id.rv);
        mRv.setLayoutManager(mManager = new LinearLayoutManager(this));
        mAdapter = new CityAdapter(this, mDatas);
        mHeaderAdapter = new HeaderRecyclerAndFooterWrapperAdapter(mAdapter) {
            @Override
            protected void onBindHeaderHolder(ViewHolder holder, int headerPos, int layoutId, Object o) {
                holder.setText(R.id.tvCity, (String) o);
            }
        };
        mHeaderAdapter.addHeaderView(R.layout.item_city, "测试头部");
        mHeaderAdapter.addHeaderView(R.layout.item_city, "测试头部2");
        mRv.setAdapter(mHeaderAdapter);
        mRv.addItemDecoration(mDecoration = new SuspensionDecoration(this, mDatas).setHeaderViewCount(mHeaderAdapter.getHeaderViewCount()));

        //如果add两个，那么按照先后顺序，依次渲染。
        mRv.addItemDecoration(new DividerItemDecoration(PinnedHeaderActivity.this, LinearLayoutManager.VERTICAL));

        //使用indexBar
        mTvSideBarHint = (TextView) findViewById(R.id.tvSideBarHint);//HintTextView
        mIndexBar = (IndexBar) findViewById(R.id.indexBar);//IndexBar

        mIndexBar.setPressedShowTextView(mTvSideBarHint)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setLayoutManager(mManager);//设置RecyclerView的LayoutManager

        initDatas(getResources().getStringArray(R.array.provinces));
        mAdapter.setOnItemClickListener(new MyBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MyBaseAdapter.BaseViewHolder holder, View view, int position) {
                Log.e("xxx", position + "");
                holder.expand();
            }
        });
        mAdapter.setOnItemLongClickListener(new MyBaseAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(MyBaseAdapter.BaseViewHolder holder, View view, int position) {
                if (position == 1) {
                    holder.expand();
                }
                Log.e("xxx", position + "long");
            }
        });
    }

    /**
     * 组织数据源
     *
     * @param data
     * @return
     */
    private void initDatas(final String[] data) {
        //延迟200ms 模拟加载数据中....
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDatas = new ArrayList<>();
                for (int i = 0; i < data.length; i++) {
                    CityBean cityBean = new CityBean();
                    cityBean.setCity(data[i]);//设置城市名称
                    mDatas.add(cityBean);
                }

                mIndexBar
                        .setSuspensionDecoration(mDecoration)
                        .setNeedRealIndex(true)
                        .setIsSort(false)
                        .setSourceDatas(mDatas)//设置数据
                        .setHeaderViewCount(mHeaderAdapter.getHeaderViewCount())//设置HeaderView数量
                        .invalidate();

                mAdapter.setDatas(mDatas);
                mHeaderAdapter.notifyDataSetChanged();
            }
        }, 200);

    }

    /**
     * 更新数据源
     *
     * @param view
     */
    public void updateDatas(View view) {
        for (int i = 0; i < 5; i++) {
            mDatas.add(new CityBean("东京"));
            mDatas.add(new CityBean("大阪"));
        }
        mIndexBar.setSourceDatas(mDatas)
                .invalidate();
        mHeaderAdapter.notifyDataSetChanged();
    }
}
