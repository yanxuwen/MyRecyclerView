package com.yanxuwen.MyRecyclerview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.yanxuwen.MyRecyclerview.animators.animators.BaseItemAnimator;
import com.yanxuwen.MyRecyclerview.animators.animators.FadeInAnimator;
import com.yanxuwen.MyRecyclerview.animators.animators.FadeInDownAnimator;
import com.yanxuwen.MyRecyclerview.animators.animators.FadeInLeftAnimator;
import com.yanxuwen.MyRecyclerview.animators.animators.FadeInRightAnimator;
import com.yanxuwen.MyRecyclerview.animators.animators.FadeInUpAnimator;
import com.yanxuwen.MyRecyclerview.animators.animators.FlipInBottomXAnimator;
import com.yanxuwen.MyRecyclerview.animators.animators.FlipInLeftYAnimator;
import com.yanxuwen.MyRecyclerview.animators.animators.FlipInRightYAnimator;
import com.yanxuwen.MyRecyclerview.animators.animators.FlipInTopXAnimator;
import com.yanxuwen.MyRecyclerview.animators.animators.LandingAnimator;
import com.yanxuwen.MyRecyclerview.animators.animators.NullAnimator;
import com.yanxuwen.MyRecyclerview.animators.animators.OvershootInLeftAnimator;
import com.yanxuwen.MyRecyclerview.animators.animators.OvershootInRightAnimator;
import com.yanxuwen.MyRecyclerview.animators.animators.ScaleInAnimator;
import com.yanxuwen.MyRecyclerview.animators.animators.ScaleInBottomAnimator;
import com.yanxuwen.MyRecyclerview.animators.animators.ScaleInLeftAnimator;
import com.yanxuwen.MyRecyclerview.animators.animators.ScaleInRightAnimator;
import com.yanxuwen.MyRecyclerview.animators.animators.ScaleInTopAnimator;
import com.yanxuwen.MyRecyclerview.animators.animators.SlideInDownAnimator;
import com.yanxuwen.MyRecyclerview.animators.animators.SlideInLeftAnimator;
import com.yanxuwen.MyRecyclerview.animators.animators.SlideInRightAnimator;
import com.yanxuwen.MyRecyclerview.animators.animators.SlideInUpAnimator;
import com.yanxuwen.MyRecyclerview.progressindicator.materialprogressview.MaterialProgressView;
import com.yanxuwen.swipelibrary.SwipeLayout;

import java.util.ArrayList;

public class MyRecyclerView extends RecyclerView {
    /*添加跟删除动画*/
    public enum ItemType {
        FadeIn(new FadeInAnimator(new OvershootInterpolator(1f))),
        FadeInDown(new FadeInDownAnimator(new OvershootInterpolator(1f))),
        FadeInUp(new FadeInUpAnimator(new OvershootInterpolator(1f))),
        FadeInLeft(new FadeInLeftAnimator(new OvershootInterpolator(1f))),
        FadeInRight(new FadeInRightAnimator(new OvershootInterpolator(1f))),
        Landing(new LandingAnimator(new OvershootInterpolator(1f))),
        ScaleIn(new ScaleInAnimator(new OvershootInterpolator(1f))),
        ScaleInTop(new ScaleInTopAnimator(new OvershootInterpolator(1f))),
        ScaleInBottom(new ScaleInBottomAnimator(new OvershootInterpolator(1f))),
        ScaleInLeft(new ScaleInLeftAnimator(new OvershootInterpolator(1f))),
        ScaleInRight(new ScaleInRightAnimator(new OvershootInterpolator(1f))),
        FlipInTopX(new FlipInTopXAnimator(new OvershootInterpolator(1f))),
        FlipInBottomX(new FlipInBottomXAnimator(new OvershootInterpolator(1f))),
        FlipInLeftY(new FlipInLeftYAnimator(new OvershootInterpolator(1f))),
        FlipInRightY(new FlipInRightYAnimator(new OvershootInterpolator(1f))),
        SlideInLeft(new SlideInLeftAnimator(new OvershootInterpolator(1f))),
        SlideInRight(new SlideInRightAnimator(new OvershootInterpolator(1f))),
        SlideInDown(new SlideInDownAnimator(new OvershootInterpolator(1f))),
        SlideInUp(new SlideInUpAnimator(new OvershootInterpolator(1f))),
        OvershootInRight(new OvershootInRightAnimator(1.0f)),
        OvershootInLeft(new OvershootInLeftAnimator(1.0f));

        private BaseItemAnimator mAnimator;

        ItemType(BaseItemAnimator animator) {
            mAnimator = animator;
        }

        public BaseItemAnimator getAnimator() {
            return mAnimator;
        }
    }

    private int DefaultAddDuration = 200;
    private int DefaultRemoveDuration = 200;
    private Context mContext;
    private boolean isLoadingData = false;
    private boolean isnomore = false;
    private int mRefreshProgressStyle = ProgressStyle.SysProgress;
    private int mLoadingMoreProgressStyle = ProgressStyle.SysProgress;
    private ArrayList<View> mHeaderViews = new ArrayList<>();
    private ArrayList<View> mFootViews = new ArrayList<>();
    private Adapter mAdapter;
    private Adapter mWrapAdapter;
    private float mLastY = -1;
    private float mMoveY = -1;
    private static final float DRAG_RATE = 3;
    private LoadingListener mLoadingListener;
    private BaseArrowRefreshHeader mRefreshHeader;
    private boolean pullRefreshEnabled = false;
    private boolean loadingMoreEnabled = false;
    private boolean isGoogleRefresh;
    private static final int TYPE_REFRESH_HEADER = -5;
    private static final int TYPE_HEADER = -4;
    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_FOOTER = -3;

    private int previousTotal = 0;
    private int mPageCount = 0;
    //adapter没有数据的时候显示,类似于listView的emptyView
    private View mEmptyView;
    private SwipeLayout mTouchView;

    public MyRecyclerView(Context context) {
        this(context, null);
        mContext = context;
    }

    public MyRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
    }

    public MyRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        if (attrs != null) {
//            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MyRecyclerView);
            //展示不支持xml设置
//            pullRefreshEnabled = a.getBoolean(R.styleable.MyRecyclerView_pullRefreshEnabled, true);
//            a.recycle();
        }
        init(context);
    }

    private BaseArrowRefreshHeader mArrowRefreshHeader;

    /**
     * 设置自定义头部。
     */
    public void setArrowRefreshHeader(BaseArrowRefreshHeader mArrowRefreshHeader) {
        this.mArrowRefreshHeader = mArrowRefreshHeader;
        pullRefreshEnabled = false;
        init(mContext);
    }

    public BaseArrowRefreshHeader getArrowRefreshHeader() {
        if (mArrowRefreshHeader == null) return new ArrowRefreshHeader(mContext);
        return mArrowRefreshHeader;
    }

    private void init(Context context) {
            setItemAnimator(ItemType.FadeInLeft, DefaultAddDuration, DefaultRemoveDuration);
    }

    public void addHeaderView(View view) {
        mHeaderViews.add(view);
    }

    public void addHeaderView(int position, View view) {
        mHeaderViews.add(position, view);
    }

    public ArrayList<View> getHeaderViews() {
        return mHeaderViews;
    }

    public View getHeaderView(int position) {
        return mHeaderViews.get(position);
    }

    public void removeHeaderView(int position) {
        if (mHeaderViews != null && mHeaderViews.size() > position) {
            mHeaderViews.remove(position);
        }
    }

    public void addFootView(final View view) {
        if (getFootView()!=null) {
            mFootViews.add(mFootViews.size() - 1, view);
        } else {
            mFootViews.add(view);
        }
    }

    public int getHeaderViewsCount() {
        return mHeaderViews != null ? mHeaderViews.size() : 0;
    }

    public int getFootersViewsCount() {
        return mFootViews != null ? mFootViews.size() : 0;
    }

    public void loadMoreComplete() {
        MyBaseAdapter baseadapter = (MyBaseAdapter) mAdapter;
        MyBaseAdapter.BaseViewHolder swipe_holder = baseadapter.swipe_holder;
        baseadapter.swipe_holder = null;
        isLoadingData = false;
        View footView = getFootView();
        if(footView==null)return;
//        if(previousTotal <  getLayoutManager().getItemCount()) {
        if (!isnomore) {
            if (footView instanceof LoadingMoreFooter) {
                ((LoadingMoreFooter) footView).setState(LoadingMoreFooter.STATE_COMPLETE);
            } else {
                footView.setVisibility(View.GONE);
            }
        } else {
            if (footView instanceof LoadingMoreFooter) {
                ((LoadingMoreFooter) footView).setState(LoadingMoreFooter.STATE_NOMORE);
            } else {
                footView.setVisibility(View.GONE);
            }
            isnomore = true;
        }
        previousTotal = getLayoutManager().getItemCount();
    }

    public void setIsnoMore(boolean isnomore) {
        this.isnomore = isnomore;
    }

    public void setLoadTextPadding(int paddingleft, int paddingtop, int paddingright, int paddingbottom) {
            if (getFootView()!=null) {
                LoadingMoreFooter mLoadingMoreFooter = getFootView();
                mLoadingMoreFooter.setStateTextPadding(paddingleft, paddingtop, paddingright, paddingbottom);
        }
    }

    public void noMoreLoading() {
        noMoreLoading(null, 0);
    }

    public void noMoreLoading(int corlor) {
        noMoreLoading(null, corlor);
    }

    public void noMoreLoading(String state_nomore) {
        noMoreLoading(state_nomore, 0);
    }

    public void noMoreLoading(String state_nomore, int color) {
        isLoadingData = false;
        isnomore = true;
        View footView = getFootView();
        if(footView==null)return;
        if (mAdapter.getItemCount() == 0) {
            state_nomore = "";
        }
        if (footView instanceof LoadingMoreFooter) {
            LoadingMoreFooter mLoadingMoreFooter = (LoadingMoreFooter) footView;
            mLoadingMoreFooter.setStateNoMoreText(state_nomore, color);
            mLoadingMoreFooter.setState(LoadingMoreFooter.STATE_NOMORE);
        } else {
            footView.setVisibility(View.GONE);
        }
    }

    public void refreshComplete() {
        MyBaseAdapter baseadapter = (MyBaseAdapter) mAdapter;
        MyBaseAdapter.BaseViewHolder swipe_holder = baseadapter.swipe_holder;
        baseadapter.swipe_holder = null;
        isnomore = false;
        previousTotal = 0;
        if (mRefreshHeader != null) {
            mRefreshHeader.refreshComplate();
        }
    }

    public void setRefreshHeader(BaseArrowRefreshHeader refreshHeader) {
        mRefreshHeader = refreshHeader;
    }

    /**
     * 初始化的时候为true才可以随意修改值
     */
    public void setPullRefreshEnabled(boolean enabled) {
        pullRefreshEnabled = enabled;
        if(enabled){
            BaseArrowRefreshHeader refreshHeader = getArrowRefreshHeader();
            mHeaderViews.clear();
            mHeaderViews.add(0, refreshHeader);
            mRefreshHeader = refreshHeader;
            mRefreshHeader.setProgressStyle(mRefreshProgressStyle);
            setArrowImageView(ArrowImageView);
        }else{
            if (mHeaderViews != null && mHeaderViews.size() > 0) mHeaderViews.remove(0);
        }
    }

    public void setLoadingMoreEnabled(boolean enabled) {
        loadingMoreEnabled = enabled;
        if (enabled&&!isHorizontal()) {
            LoadingMoreFooter footView = new LoadingMoreFooter(mContext);
            footView.setProgressStyle(mLoadingMoreProgressStyle);
            addFootView(footView);
            getFootView().setVisibility(GONE);

        }
        else{
            if (mFootViews != null && mFootViews.size() > 0) mFootViews.remove(mFootViews.size()-1);

        }
    }

    private MySwipeRefreshLayout mMySwipeRefreshLayout;

    /**
     * @param enabled               用于判断是否设置谷歌的下拉刷新
     * @param mMySwipeRefreshLayout 谷歌下拉刷新的控件,自己定义的刷新控件，其实就是修改了setRefreshing会触发onRefresh，谷歌自带的不触发onRefresh
     *                              设置谷歌下拉刷新，去除原来的下拉刷新
     */
    public void setGoogleRefresh(boolean enabled, MySwipeRefreshLayout mMySwipeRefreshLayout) {
        isGoogleRefresh = enabled;
        this.mMySwipeRefreshLayout = mMySwipeRefreshLayout;
        mMySwipeRefreshLayout.setOnMyRefreshListener(new MySwipeRefreshLayout.OnMyRefreshListener() {
            @Override
            public void onRefresh() {
                if (mLoadingListener != null) {
                    mLoadingListener.onRefresh();
                    isnomore = false;
                    previousTotal = 0;
                }
            }
        });
    }

    public boolean isGoogleRefresh() {
        return isGoogleRefresh;
    }

    public void setRefreshProgressStyle(int style) {
        mRefreshProgressStyle = style;
        if (mRefreshHeader != null) {
            mRefreshHeader.setProgressStyle(style);
        }
    }

    public void setLoadingMoreProgressStyle(int style) {
        mLoadingMoreProgressStyle = style;
        if (getFootView()!=null) {
            getFootView().setProgressStyle(style);
        }
    }

    public MaterialProgressView getMaterialProgressView() {
        if (getFootView()!=null) {
            return getFootView().getMaterialProgressView();
        }
        return null;
    }
    int ArrowImageView;
    public void setArrowImageView(int resid) {
        this.ArrowImageView=resid;
        if (mRefreshHeader != null) {
            mRefreshHeader.setArrowImageView(resid);
        }
    }

    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
        mDataObserver.onChanged();
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mAdapter = adapter;
        try {
            ((MyBaseAdapter) mAdapter).setMyRecyclerView(this);
        } catch (Exception e) {
        }
        mWrapAdapter = new WrapAdapter(mHeaderViews, mFootViews, adapter);
        super.setAdapter(mWrapAdapter);
        mAdapter.registerAdapterDataObserver(mDataObserver);
        mDataObserver.onChanged();
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);

        if (state == RecyclerView.SCROLL_STATE_IDLE && mLoadingListener != null && !isLoadingData && loadingMoreEnabled) {
            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition;
            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                lastVisibleItemPosition = findMax(into);
            } else {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            //1.注释部分暂时隐藏，该判断是，数组大小要大于当前可视的大小，才能加载，但是这样就造成。
            //当前数组只有1个的时候就无法加载问题
            //2、修改第二个判断，lastVisibleItemPosition为最后的可见视图的position,由于footview隐藏的话，就会少一个尾部position,所以就不会>=
            //所以lastVisibleItemPosition要减去头部，layoutManager.getItemCount()要减去头部跟尾部，
            //什么时候footview会隐藏呢，在设置没有更多的时候是，文字设置成""空的话，就会隐藏掉尾部分
            if (layoutManager.getChildCount() > 0
                    && lastVisibleItemPosition - getHeaderViewsCount() >= layoutManager.getItemCount() - 1 - getHeaderViewsCount() - getFootersViewsCount() && /** layoutManager.getItemCount() > layoutManager.getChildCount() && */!isnomore) {
                if (mRefreshHeader == null || (mRefreshHeader != null && mRefreshHeader.getState() < BaseArrowRefreshHeader.STATE_REFRESHING)) {
                    //如果是谷歌的下拉刷新，则在刷新的时候不能加载
                    if (!isGoogleRefresh || (isGoogleRefresh && mMySwipeRefreshLayout != null && !mMySwipeRefreshLayout.isRefreshing())) {
                        //如果没有数据的时候，禁止加载
                        if (mAdapter != null) {
                            int itemCount = ((MyBaseAdapter) mAdapter).getItemCount();
                            if (itemCount == 0) return;
                        }

                        View footView = getFootView();
                        if(footView==null){mLoadingListener.onLoadMore();return;}
                        isLoadingData = true;
                        if (footView instanceof LoadingMoreFooter) {
                            ((LoadingMoreFooter) footView).setState(LoadingMoreFooter.STATE_LOADING);
                            //防止一些没有滑动到最下面，影响美观，自动拉动到最下面
                            smoothScrollBy(500, 500);
                        } else {
                            footView.setVisibility(View.VISIBLE);
                        }
                        mLoadingListener.onLoadMore();
                    }
                }
            }
        }
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            MyBaseAdapter adapter = null;
            if (mAdapter != null)
                adapter = (MyBaseAdapter) mAdapter;
            if (adapter != null && adapter.swipe_holder != null) {
                if (adapter.swipe_holder.swipe.getOpenStatus() != SwipeLayout.Status.Close) {
                    return super.onTouchEvent(ev);
                }
            }
        } catch (Exception e) {
        }
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }
        // 进行与操作是为了判断多点触摸
        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN://在第一个点被按下时触发
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_POINTER_DOWN://当屏幕上已经有一个点被按住，此时再按下其他点时触发。
                mLastY = -1; // reset
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (isOnTop() && pullRefreshEnabled) {
                    float DRAG_RATE2 = (float) mRefreshHeader.getVisiableHeight() / 50;
                    if (DRAG_RATE2 <= DRAG_RATE) DRAG_RATE2 = DRAG_RATE;
                    mRefreshHeader.onMove(deltaY / DRAG_RATE2);
                    if (mRefreshHeader.getVisiableHeight() > 0 && mRefreshHeader.getState() < BaseArrowRefreshHeader.STATE_REFRESHING) {
                        Log.i("getVisiableHeight", "getVisiableHeight = " + mRefreshHeader.getVisiableHeight());
                        Log.i("getVisiableHeight", " mRefreshHeader.getState() = " + mRefreshHeader.getState());
                        return false;
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP://当屏幕上有多个点被按住，松开其中一个点时触发（即非最后一个点被放开时）。
                mLastY = -1; // reset
                break;
            case MotionEvent.ACTION_UP://当屏幕上唯一的点被放开时触发
                mLastY = -1; // reset
                if (isOnTop() && pullRefreshEnabled) {
                    if (mRefreshHeader.releaseAction()) {
                        if (mLoadingListener != null) {
                            mLoadingListener.onRefresh();
                            isnomore = false;
                            previousTotal = 0;
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private int findMin(int[] firstPositions) {
        int min = firstPositions[0];
        for (int value : firstPositions) {
            if (value < min) {
                min = value;
            }
        }
        return min;
    }

    private boolean isOnTop() {
        if (mHeaderViews == null || mHeaderViews.isEmpty()) {
            return false;
        }

        View view = getHeaderView();
        if (view!=null&&view.getParent() != null) {
            return true;
        } else {
            return false;
        }
//        LayoutManager layoutManager = getLayoutManager();
//        int firstVisibleItemPosition;
//        if (layoutManager instanceof GridLayoutManager) {
//            firstVisibleItemPosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
//        } else if ( layoutManager instanceof StaggeredGridLayoutManager ) {
//            int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
//            ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(into);
//            firstVisibleItemPosition = findMin(into);
//        } else {
//            firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
//        }
//        if ( firstVisibleItemPosition <= 1 ) {
//             return true;
//        }
//        return false;
    }

    private final RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            Adapter<?> adapter = getAdapter();
            if (adapter != null && mEmptyView != null) {
                int emptyCount = 0;
                if (pullRefreshEnabled) {
                    emptyCount++;
                }
                if (loadingMoreEnabled) {
                    emptyCount++;
                }
                if (adapter.getItemCount() == emptyCount) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    MyRecyclerView.this.setVisibility(View.GONE);
                } else {
                    mEmptyView.setVisibility(View.GONE);
                    MyRecyclerView.this.setVisibility(View.VISIBLE);
                }
            }
            if (mWrapAdapter != null) {
                mWrapAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    };

    private class WrapAdapter extends RecyclerView.Adapter<ViewHolder> {

        private RecyclerView.Adapter adapter;

        private ArrayList<View> mHeaderViews;

        private ArrayList<View> mFootViews;

        private int headerPosition = 1;
        private int loadPosition = 0;

        public WrapAdapter(ArrayList<View> headerViews, ArrayList<View> footViews, RecyclerView.Adapter adapter) {
            this.adapter = adapter;
            this.mHeaderViews = headerViews;
            this.mFootViews = footViews;
            if (isGoogleRefresh||getHeaderView()==null) headerPosition = 0;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            if (manager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) manager);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        if (mAdapter instanceof MyBaseAdapter) {
                            return (isHeader(position) || isFooter(position))
                                    ? gridManager.getSpanCount() : (((MyBaseAdapter) mAdapter).getSpanCount(position - mHeaderViews.size()));
                        } else {
                            return (isHeader(position) || isFooter(position))
                                    ? gridManager.getSpanCount() : 1;
                        }

                    }
                });
            }
        }

        @Override
        public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null
                    && lp instanceof StaggeredGridLayoutManager.LayoutParams
                    && (isHeader(holder.getLayoutPosition()) || isFooter(holder.getLayoutPosition()))) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }

        public boolean isHeader(int position) {
            return position >= 0 && position < mHeaderViews.size();
        }

        public boolean isFooter(int position) {
            return position < getItemCount() && position >= getItemCount() - mFootViews.size();
        }

        public boolean isRefreshHeader(int position) {
            return position == 0&&getHeaderView()!=null;
        }

        public int getHeadersCount() {
            return mHeaderViews.size();
        }

        public int getFootersCount() {
            return mFootViews.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_REFRESH_HEADER && mHeaderViews != null && mHeaderViews.size() > 0) {
                return new SimpleViewHolder(getHeaderView());
            } else if (viewType == TYPE_HEADER && headerPosition < mHeaderViews.size()) {
                return new SimpleViewHolder(mHeaderViews.get(headerPosition++));
            } else if (viewType == TYPE_FOOTER && mFootViews != null && mFootViews.size() > 0) {
                return new SimpleViewHolder(mFootViews.get(loadPosition++));

            }
            return adapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (isHeader(position)) {
                return;
            }
            int adjPosition = position - getHeadersCount();
            int adapterCount;
            if (adapter != null) {
                adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    adapter.onBindViewHolder(holder, adjPosition);
                    return;
                }
            }
        }

        @Override
        public int getItemCount() {
            if (adapter != null) {
                return getHeadersCount() + getFootersCount() + adapter.getItemCount();
            } else {
                return getHeadersCount() + getFootersCount();
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (isRefreshHeader(position) && !isGoogleRefresh) {
                return TYPE_REFRESH_HEADER;
            }
            if (isHeader(position)) {
                return TYPE_HEADER;
            }
            if (isFooter(position)) {
                return TYPE_FOOTER;
            }
            int adjPosition = position - getHeadersCount();
            int adapterCount;
            if (adapter != null) {
                adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    return adapter.getItemViewType(adjPosition);
                }
            }
            return TYPE_NORMAL;
        }

        @Override
        public long getItemId(int position) {
            if (adapter != null && position >= getHeadersCount()) {
                int adjPosition = position - getHeadersCount();
                int adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    return adapter.getItemId(adjPosition);
                }
            }
            return -1;
        }

        @Override
        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            if (adapter != null) {
                adapter.unregisterAdapterDataObserver(observer);
            }
        }

        @Override
        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            if (adapter != null) {
                adapter.registerAdapterDataObserver(observer);
            }
        }

        private class SimpleViewHolder extends RecyclerView.ViewHolder {
            public SimpleViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

    public void setLoadingListener(LoadingListener listener) {
        mLoadingListener = listener;
    }

    public interface LoadingListener {

        void onRefresh();

        void onLoadMore();
    }

    public void setRefreshing(boolean refreshing) {
        if (refreshing && pullRefreshEnabled && mLoadingListener != null) {
            mRefreshHeader.setState(BaseArrowRefreshHeader.STATE_REFRESHING);
            mRefreshHeader.onMove(mRefreshHeader.mMeasuredHeight);
            mLoadingListener.onRefresh();
            isnomore = false;
            previousTotal = 0;
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
    public void setItemAnimator(ItemType type, int addduration, int removeduration) {
        setItemAnimator(type.getAnimator());
        getItemAnimator().setAddDuration(addduration);
        getItemAnimator().setRemoveDuration(removeduration);
        this.getItemAnimator().setMoveDuration(DefaultAddDuration);
        this.getItemAnimator().setRemoveDuration(DefaultAddDuration);
    }

    public void setItemAnimator(ItemType type) {
        setItemAnimator(type.getAnimator());
        getItemAnimator().setAddDuration(DefaultAddDuration);
        getItemAnimator().setRemoveDuration(DefaultRemoveDuration);
        this.getItemAnimator().setMoveDuration(DefaultAddDuration);
        this.getItemAnimator().setRemoveDuration(DefaultAddDuration);
    }

    /**
     * 关闭默认局部刷新动画
     */
    public void closeDefaultAnimator() {
        setItemAnimator(new NullAnimator());
        this.getItemAnimator().setAddDuration(0);
        this.getItemAnimator().setChangeDuration(0);
        this.getItemAnimator().setMoveDuration(0);
        this.getItemAnimator().setRemoveDuration(0);
        ((SimpleItemAnimator) this.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    /**
     * 获取第一条展示的位置
     *
     * @return
     */
    public int getFirstVisiblePosition() {
        int position;
        if (getLayoutManager() instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        } else if (getLayoutManager() instanceof GridLayoutManager) {
            position = ((GridLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        } else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) getLayoutManager();
            int[] lastPositions = layoutManager.findFirstVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMinPositions(lastPositions);
        } else {
            position = 0;
        }
        return position;
    }

    /**
     * 获得当前展示最小的position
     *
     * @param positions
     * @return
     */
    public int getMinPositions(int[] positions) {
        int size = positions.length;
        int minPosition = Integer.MAX_VALUE;
        for (int i = 0; i < size; i++) {
            minPosition = Math.min(minPosition, positions[i]);
        }
        return minPosition;
    }

    /**
     * 获取最后一条展示的位置
     *
     * @return
     */
    public int getLastVisiblePosition() {
        int position;
        if (getLayoutManager() instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
        } else if (getLayoutManager() instanceof GridLayoutManager) {
            position = ((GridLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
        } else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) getLayoutManager();
            int[] lastPositions = layoutManager.findLastVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMaxPosition(lastPositions);
        } else {
            position = getLayoutManager().getItemCount() - 1;
        }
        return position;
    }

    /**
     * 获得最大的位置
     *
     * @param positions
     * @return
     */
    public int getMaxPosition(int[] positions) {
        int size = positions.length;
        int maxPosition = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            maxPosition = Math.max(maxPosition, positions[i]);
        }
        return maxPosition;
    }

    /**
     * 是否是横向的，如果是则不显示加载样式
     */
    public boolean isHorizontal() {
        LayoutManager mLayoutManager = getLayoutManager();
        if (mLayoutManager instanceof LinearLayoutManager) {
            if (((LinearLayoutManager) mLayoutManager).getOrientation() == LinearLayoutManager.HORIZONTAL) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取加载视图，如果没有则获取空
     */
    private LoadingMoreFooter getFootView() {
        if (mFootViews != null && !mFootViews.isEmpty()) {
            View footView = mFootViews.get(mFootViews.size()-1);
            if (footView != null && footView instanceof LoadingMoreFooter) {
                return (LoadingMoreFooter)footView;
            }
        }
        return null;
    }
    /**
     * 获取刷新视图，如果没有则获取空
     */
    private BaseArrowRefreshHeader getHeaderView() {
        if (mHeaderViews != null && !mHeaderViews.isEmpty()) {
            View headView = mHeaderViews.get(0);
            if (headView != null && headView instanceof BaseArrowRefreshHeader) {
                return (BaseArrowRefreshHeader)headView;
            }
        }
        return null;
    }
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        if(getFootView()!=null&&isHorizontal()){
            mFootViews.remove(mFootViews.size()-1);
        }
    }
}
