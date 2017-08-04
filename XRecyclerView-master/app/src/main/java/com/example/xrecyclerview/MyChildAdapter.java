package com.example.xrecyclerview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanxuwen.DensityUtil;
import com.yanxuwen.MyRecyclerview.MyBaseAdapter;
import com.yanxuwen.swipelibrary.SwipeLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者：严旭文 on 2016/5/11 14:36
 * 邮箱：420255048@qq.com
 */
public class MyChildAdapter extends MyBaseAdapter {
    private List<String> mDataSet;
    private Context mContext;
    boolean isStaggered=false;

    public MyChildAdapter(Context context, List<String> dataSet) {
        super(context, dataSet);
        this.mDataSet = dataSet;
        this.mContext = context;
    }
    public MyChildAdapter(Context context, List<String> dataSet, boolean isStaggered) {
        super(context, dataSet);
        this.mDataSet = dataSet;
        this.mContext = context;
        this.isStaggered = isStaggered;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        addSwipe(R.layout.swipe_default2, SwipeLayout.ShowMode.LayDown, SwipeLayout.DragEdge.Right, true);
        addExpand(R.layout.expand_default);
        return new ViewHolder(setLayout(R.layout.item, parent));
    }

    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        final ViewHolder mViewHolder = (ViewHolder) holder;
        int adpterposition = holder.getCurPosition();
        mViewHolder.text.setText(mDataSet.get(position));
        android.widget.LinearLayout.LayoutParams lp = new android.widget.LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        if (isStaggered && position % 2 == 0) {
            lp.height = DensityUtil.dip2px(mContext,80);
            lp.width = DensityUtil.dip2px(mContext,50);
            lp.leftMargin= DensityUtil.dip2px(mContext,5);
            lp.rightMargin=DensityUtil.dip2px(mContext,5);
            lp.topMargin=DensityUtil.dip2px(mContext,5);
            lp.bottomMargin=DensityUtil.dip2px(mContext,5);


        }else{
            lp.height = DensityUtil.dip2px(mContext,50);
            lp.width = DensityUtil.dip2px(mContext,50);
            lp.leftMargin= DensityUtil.dip2px(mContext,5);
            lp.rightMargin=DensityUtil.dip2px(mContext,5);
            lp.topMargin=DensityUtil.dip2px(mContext,5);
            lp.bottomMargin=DensityUtil.dip2px(mContext,5);
        }

        mViewHolder.v_expand.setLayoutParams(lp);
        super.onBindViewHolder(holder, position);

    }

    class ViewHolder extends BaseViewHolder implements View.OnClickListener {
        private ViewHolder mViewHolder;
        @Bind(R.id.text)
        TextView text;
        @Bind(R.id.v_expand)
        View v_expand;
        @Bind(R.id.swipe_delete)
        View swipeDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mViewHolder = this;
            //设置自动展开按钮，
//            setExpandView(v_expand);
            swipeDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.swipe_delete:
                    remove(mViewHolder.getCurPosition());
                    break;

            }
        }
    }

    public void add(String text, int position) {
        mDataSet.add(position, text);
        super.add(position);
    }
}
