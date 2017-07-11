package com.example.xrecyclerview.PinnedHeader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xrecyclerview.R;
import com.yanxuwen.MyRecyclerview.MyBaseAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by zhangxutong .
 * Date: 16/08/28
 */

public class CityAdapter extends MyBaseAdapter {
    protected Context mContext;
    protected List<CityBean> mDatas;
        protected LayoutInflater mInflater;
    public CityAdapter(Context mContext, List<CityBean> mDatas) {
        super(mContext, mDatas);
        this.mContext = mContext;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(mContext);
    }
    public List<CityBean> getDatas() {
        return mDatas;
    }

    public CityAdapter setDatas(List<CityBean> datas) {
        mDatas = datas;
        return this;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        addExpand(R.layout.expand_default);
        return new ViewHolder(setLayout(R.layout.item_city, parent));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        final ViewHolder mViewHolder = (ViewHolder) holder;
        final CityBean cityBean = mDatas.get(position);
        mViewHolder.tvCity.setText(cityBean.getCity());
        mViewHolder.tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewHolder.expand();
            }
        });
        mViewHolder.avatar.setImageResource(R.drawable.friend);
        super.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    class ViewHolder extends BaseViewHolder {
        @Bind(R.id.tvCity)
        TextView tvCity;
        @Bind(R.id.ivAvatar)
        ImageView avatar;
        @Bind(R.id.content)
        View content;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
