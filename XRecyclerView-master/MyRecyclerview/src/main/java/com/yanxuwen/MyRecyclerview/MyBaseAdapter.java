package com.yanxuwen.MyRecyclerview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.RelativeLayout;

import com.yanxuwen.MyRecyclerview.animators.internal.ViewHelper;
import com.yanxuwen.expandable.ExpandableLinearLayout;
import com.yanxuwen.swipelibrary.SwipeLayout;

import java.util.List;

/**
 * Created by yanxuwen on 2016/05/4.
 * 如果是使用系统的RecyclerView,一定要调用setRecyclerView
 */
public class MyBaseAdapter extends RecyclerView.Adapter<MyBaseAdapter.BaseViewHolder> {
    boolean horizontal = false;
    boolean isAnimate=true;
    public static int NOOPENID = -1;
    private Context mContext;
    private List<?> mDataSet;
    public BaseViewHolder swipe_holder;
    public BaseViewHolder expand_holder;
    /**
     * 展开position
     */
    private int expand_position = -1;

    private int mDuration = 3000;//只适合透明度才3秒，如果是移动或者之类的，时间最好1秒之类
    private Interpolator mInterpolator = new LinearInterpolator();
    private int mLastPosition = -1;
    private boolean isFirstOnly = true;
    //下面为滑动菜单所用到的
    private int swipe_layout;//滑动菜单的试图
    public SwipeLayout.DragEdge mDragEdge = SwipeLayout.DragEdge.Right;
    public SwipeLayout.ShowMode mShowMode = SwipeLayout.ShowMode.LayDown;
    private boolean isBounces = false;
    //下面为展开列表所用到的
    private int expand_layout;
    private View mainview;
    private MyRecyclerView mMyRecyclerView;
    private RecyclerView mRecyclerView;


    public MyBaseAdapter(Context context, List<?> dataSet) {
        mContext = context;
        mDataSet = dataSet;
    }
    /**
     * 如果是使用系统的RecyclerView,一定要调用该句
     */
    public void setRecyclerView(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
        try {
            RecyclerView.LayoutManager mLayoutManager = mRecyclerView.getLayoutManager();
            if (mLayoutManager instanceof LinearLayoutManager) {
                if (((LinearLayoutManager) mLayoutManager).getOrientation() == LinearLayoutManager.HORIZONTAL) {
                    horizontal = true;
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * 在 mRecyclerView.setAdapter(mAdapter);的时候，里面会调用该方法，
     */
    public void setMyRecyclerView(MyRecyclerView mMyRecyclerView) {
        this.mMyRecyclerView = mMyRecyclerView;
        try {
            RecyclerView.LayoutManager mLayoutManager = mMyRecyclerView.getLayoutManager();
            if (mLayoutManager instanceof LinearLayoutManager) {
                if (((LinearLayoutManager) mLayoutManager).getOrientation() == LinearLayoutManager.HORIZONTAL) {
                    horizontal = true;
                }
            }
        } catch (Exception e) {
        }
    }

    private boolean getHorizontal() {
        return horizontal;
    }

    private boolean isAnimate(){return  isAnimate;}
    public void setIsAnimate(boolean isAnimate){this.isAnimate=isAnimate;}

    public MyRecyclerView getMyRecyclerView() {
        return mMyRecyclerView;
    }
    public RecyclerView getRecyclerView() {
        if(mMyRecyclerView!=null)return mMyRecyclerView;
        return mRecyclerView;
    }

    private int mHeaderViewsCount;

    public int getHeaderViewsCount() {
        if (mMyRecyclerView != null) {
            return mMyRecyclerView.getHeaderViewsCount();
        }
        return mHeaderViewsCount;
    }

    /**
     * 如果RecyclerView不是自定义，也不是城市列表，则需要设置setHeaderViewsCount数量，
     * 城市列表会在每次addHeaderView的时候，会设置一遍setHeaderViewsCount数量
     */
    public void setHeaderViewsCount(int mHeaderViewsCount) {
        this.mHeaderViewsCount = mHeaderViewsCount;
    }


    /**
     * @return 获取主布局，也就是我们通常显示的布局，没有展开跟没有菜单
     */
    public View getMainLayout() {
        return mainview;
    }

    /**
     * 设置item布局
     */
    public View setLayout(int layout, ViewGroup parent) {
        View v;
        if (getHorizontal()) {
            v = LayoutInflater.from(mContext).inflate(layout, null);
        } else {
            v = LayoutInflater.from(mContext).inflate(layout, parent, false);
        }

        mainview = v;
        ViewGroup viewGroup;
        if (getHorizontal()) {
            viewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.listview_base, null);
        } else {
            viewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.listview_base, parent, false);
        }
        //添加滑动菜单
        ViewGroup layout_swipe = (ViewGroup) viewGroup.findViewById(R.id.layout_swipelayout);
        View view_swipe = null;
        if (swipe_layout != 0) {
            if (getHorizontal()) {
                view_swipe = (View) LayoutInflater.from(mContext).inflate(swipe_layout, null);
            } else {
                view_swipe = (View) LayoutInflater.from(mContext).inflate(swipe_layout, parent, false);
            }
        } else {
            if (getHorizontal()) {
                view_swipe = (View) LayoutInflater.from(mContext).inflate(R.layout.swipe_default, null);
            } else {
                view_swipe = (View) LayoutInflater.from(mContext).inflate(R.layout.swipe_default, parent, false);
            }
        }
        layout_swipe.addView(view_swipe);
        layout_swipe.addView(v);
        //添加展开列表
        if (expand_layout != 0) {
            RelativeLayout viewgroup_expand = (RelativeLayout) viewGroup.findViewById(R.id.layout_expandable);
            View view_expand;
            if (getHorizontal()) {
                view_expand = (ViewGroup) LayoutInflater.from(mContext).inflate(expand_layout, null);
            } else {
                view_expand = (ViewGroup) LayoutInflater.from(mContext).inflate(expand_layout, parent, false);
            }
            viewgroup_expand.addView(view_expand);
        }
        return viewGroup;
    }

    /**
     * 添加可滑动菜单
     * swipe_layout 为滑动菜单布局
     * mode 拖动模式
     * bounces 是否开起弹簧
     */
    public void addSwipe(int swipe_layout, SwipeLayout.ShowMode mode, SwipeLayout.DragEdge DragEdge, boolean bounces) {
        this.swipe_layout = swipe_layout;
        mShowMode = mode;
        mDragEdge = DragEdge;
        isBounces = bounces;

    }

    public void addExpand(int expand_layout) {
        this.expand_layout = expand_layout;
    }

    /**
     * 获取展开项
     */
    public int getExpandPosition() {
        return expand_position;
    }

    /**
     * 设置展开项
     */
    public void setExpandPostion(int expand_position) {
        this.expand_position = expand_position;
    }

    /**
     * 用于格子视图占据多少数量【用于设置不同规格布局】，
     * 由于onAttachedToRecyclerView被占用，所以无法重写onAttachedToRecyclerView来设置格子视图占据多少数量。
     */
    public int getSpanCount(int position) {
        return 1;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, final int position) {
        if (holder.swipe != null) {
            if (swipe_layout != 0) {
                holder.swipe.addSwipeListener(new SwipeLayout.SwipeListener() {

                    @Override
                    public void onClose(SwipeLayout swipeLayout) {
                    }

                    @Override
                    public void onUpdate(SwipeLayout swipeLayout, int i, int i1) {

                    }

                    @Override
                    public void onOpen(SwipeLayout swipeLayout) {
                        swipe_holder = holder;
                    }

                    @Override
                    public void onHandRelease(SwipeLayout swipeLayout, float v, float v1) {
                        swipe_holder = holder;
                    }
                });
            }
        }
        //
        int adapterPosition = holder.getCurPosition();
        //注意，，由于如果展开视图不固定，会导致展开后滑出界面再滑回来，又导致界面不一样，所以在适配器请自行判断
        if (holder.expand == null) {
            if (expand_position == adapterPosition) {
                expand_holder = holder;
                holder.getExpandView().expand(false);
            } else {
                holder.getExpandView().collapse(false);
            }
        }
        //显示动画
        if (isAnimate&&(!isFirstOnly || adapterPosition > mLastPosition)) {
            for (Animator anim : getAnimators(holder.itemView)) {
                anim.setDuration(mDuration).start();
                anim.setInterpolator(new OvershootInterpolator(.5f));
            }
            mLastPosition = adapterPosition;
        } else {
            ViewHelper.clear(holder.itemView);
        }
    }

    /**
     * 刷新的时候记得重置
     */
    public void setFirstOnly(boolean firstOnly) {
        isFirstOnly = firstOnly;
        if (isFirstOnly) {
            mLastPosition = -1;
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet != null ? mDataSet.size() : 0;
    }

    /**
     * 注意：
     * 传递为 curPosition  也就是不包含头部的的position
     * remove会自动移除掉list的position.不需要自己移除，add的话就要自行添加了
     */
    public void remove(int curPosition) {
        if (swipe_holder != null && swipe_layout != 0) {
            swipe_holder.swipe.setIsSwipe(true);
            swipe_holder = null;
        }
        mDataSet.remove(curPosition);
        getRecyclerView().setItemAnimator(getRecyclerView().getItemAnimator());
        //移除的的时候一定要算上头部【由于RecyclerView是没有头部的，所以自定义的时候加上去的，，所以会导致notifyItemInserted失效少了2个】
        notifyItemRemoved(curPosition + getHeaderViewsCount());
        if (mDataSet != null && mDataSet.isEmpty()) {
            notifyDataSetChanged();
            getMyRecyclerView().noMoreLoading("");
        }
    }

    /**
     * 注意：
     * 用于外部调用，则需要加上头部
     */
    public void add(int curPosition) {
        getRecyclerView().setItemAnimator(getRecyclerView().getItemAnimator());
        //添加的时候一定要算上头部【由于RecyclerView是没有头部的，所以自定义的时候加上去的，，所以会导致notifyItemInserted失效少了2个】
        notifyItemInserted(curPosition + getHeaderViewsCount());
//        notifyItemRangeChanged(position, getItemCount());
    }

    /**
     * 局部刷新
     */
    public void onItemChanged(int curPosition) {
        getRecyclerView().setItemAnimator(getRecyclerView().getItemAnimator());
        notifyItemChanged(curPosition + getHeaderViewsCount());
    }


    /**
     * 注意：
     * 用于onBindViewHolder调用，防止错乱，传递为 holder.getAdapterPosition();
     */
    public void addHolder(int adapterposition) {
        notifyItemInserted(adapterposition);
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnFocusChangeListener { //由于是lib工程，所以ButterKnife在这个类不管用
        boolean isSwipe = true;
        SwipeLayout swipe;
        View expand;
        private ExpandableLinearLayout expandableLayout;

        /**
         * 获取当前的position ,由于有头部问题，所以请不要使用getAdapterPosition ，，然而getPostion获取会有问题
         */
        public int getCurPosition() {
            return getAdapterPosition() - getHeaderViewsCount();
        }

        public BaseViewHolder(final View itemView) {
            super(itemView);
            View clickview = getMainLayout();
            swipe = (SwipeLayout) itemView.findViewById(R.id.layout_swipelayout);
            expandableLayout = (ExpandableLinearLayout) itemView.findViewById(R.id.expandable_layout);
            if (swipe != null && swipe_layout != 0) {
                swipe.setShowMode(mShowMode);
                swipe.setDragEdge(mDragEdge);
                swipe.setBounces(isBounces);
                clickview = swipe.getSurfaceView();
            } else {
                swipe.setIsSwipe(false);
            }
            clickview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null)
                        mOnItemClickListener.onItemClick(BaseViewHolder.this, itemView, getCurPosition());
                }
            });
            clickview.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mOnItemLongClickListener != null) {
                        mOnItemLongClickListener.onItemLongClick(BaseViewHolder.this, itemView, getCurPosition());
                        return true;
                    }
                    return false;
                }
            });
            clickview.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (!isSwipe) {
                        return false;
                    }
                    if (swipe_layout != 0) {
                        swipe.setIsSwipe(true);
                    }
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        boolean isreturn = false;
//                        //关闭展开线
//                        if(expand_holder!=null){
//                            expand_holder. expandableLayout.collapse();
//                           try{expand_holder. expand.clearFocus();}catch (Exception e){
//                               expand_holder. expandableLayout.clearFocus();
//                           }
//                            expand_holder=null;
//                            isreturn=true;
//                        }

                        //关闭滑动菜单
                        if (swipe_holder != null && swipe_layout != 0)
                            swipe_holder.swipe.setIsSwipe(true);
                        if (swipe_holder != null) {
                            View view = swipe_holder.swipe;
                            SwipeLayout mTouchView = null;
                            if (view instanceof SwipeLayout) {
                                mTouchView = (SwipeLayout) view;
                            }
                            if (mTouchView != null && mTouchView.getOpenStatus() != SwipeLayout.Status.Close) {
                                if (swipe_holder != null && swipe_layout != 0) {
                                    swipe.setIsSwipe(false);
                                    swipe_holder.swipe.setIsSwipe(false);
                                }
                                mTouchView.close();
                                mTouchView = null;
                                isreturn = true;
                            }
                        }
                        return isreturn;
                    } else {
                        return false;
                    }
                }
            });
        }

        /**
         * 判断是否展开
         */
        public boolean isExpanded() {
            if (expandableLayout != null) {
                return expandableLayout.isExpanded();
            }
            return false;
        }

        /**
         * 获取展开试图
         */
        public ExpandableLinearLayout getExpandView() {
            return expandableLayout;
        }

        /**
         * 获取滑动试图
         */
        public SwipeLayout getSwipeView() {
            return swipe;
        }

        public void setIsSwipe(boolean isSwipe) {
            this.isSwipe = isSwipe;
            getSwipeView().setIsSwipe(isSwipe);
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (expandableLayout != null) {
                if (hasFocus) {
                    expand_holder = this;
                    expandableLayout.expand();
                } else {
                    expandableLayout.collapse();
                }
            }
        }

        /**
         * 展开详情
         */
        public void expand() {
            closeExpand();

            if (expandableLayout != null) {
                expand_holder = this;
                /**如果展开项跟当前项一样的话，则代表要关闭掉，所以要清空为-1*/
                if (expand_position == getCurPosition()) {
                    expand_position = -1;
                } else {
                    if (!isExpanded())
                        expand_position = getCurPosition();
                }
                expandableLayout.toggle();
            }
        }

        /**
         * 关闭展开的详情
         */
        public void closeExpand() {
            if (expand_holder != null && expand_holder != this) {
                if (expandableLayout != null) {
                    /**如果关闭项跟展开项一样的话，则要清空为-1*/
                    if (expand_position == getCurPosition()) {
                        expand_position = -1;
                    }
                    expand_holder.expandableLayout.collapse();
                    expand_holder = null;
                }
            }
        }

        /**
         * 设置控件展开按钮【注意：该模式是全自动模式，也就是说每个item都会展开，并且该模式是焦点模式，一旦焦点消失，就会自动关闭展开】
         */
        public void setExpandView(View v) {
            expand = v;
            expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (expand != null) {
                        if (expand.isFocused()) {
                            expand.clearFocus();
                        } else {
                            expand.requestFocus();
                        }
                    }
                }
            });
            expand.setFocusableInTouchMode(true);
            expand.setOnFocusChangeListener(this);
        }
    }

    public void setDuration(int mDuration) {
        this.mDuration = mDuration;
    }

    public Animator[] getAnimators(View view) {
        return new Animator[]{
//                ObjectAnimator.ofFloat(view, "translationX", -view.getRootView().getWidth(), 0)
                ObjectAnimator.ofFloat(view, "alpha", 0, 1f)
        };
    }

    public interface OnItemClickListener {

        /**
         * Callback method to be invoked when an item in this RecyclerView.Adapter has
         * been clicked.
         * <p>
         * Implementers can call getPosition(position) if they need
         * to access the data associated with the selected item.
         *
         * @param view     The view within the RecyclerView.Adapter that was clicked (this
         *                 will be a view provided by the adapter)
         * @param position The position of the view in the adapter.
         */
        void onItemClick(BaseViewHolder holder, View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(BaseViewHolder holder, View view, int position);
    }

    public OnItemClickListener mOnItemClickListener = null;
    public OnItemLongClickListener mOnItemLongClickListener = null;

    /**
     * Register a callback to be invoked when an item in this AdapterView has
     * been clicked.
     *
     * @param listener The callback that will be invoked.
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }

}

