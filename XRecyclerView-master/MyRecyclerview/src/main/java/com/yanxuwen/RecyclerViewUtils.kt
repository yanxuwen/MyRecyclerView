package com.example.app.myapplication.Utils

import android.content.Context
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.yanxuwen.DensityUtil
import com.yanxuwen.MyRecyclerview.*

/**
 * Created by yanxuwen on 2018/1/26.
 */
open class RecyclerViewUtils(context: Context, mSwipeRefreshLayout: MySwipeRefreshLayout, mRecyclerView: MyRecyclerView, mAdapter: MyBaseAdapter) {
    open var limit = 10
    open var context: Context?=null
    open var mSwipeRefreshLayout: MySwipeRefreshLayout?=null
    open var mRecyclerView:MyRecyclerView?=null
    open var mAdapter:MyBaseAdapter?=null
    init {
        this.context = context
        this.mSwipeRefreshLayout = mSwipeRefreshLayout
        this.mRecyclerView = mRecyclerView
        this.mAdapter = mAdapter
        this.mAdapter?.setIsAnimate(false)
    }
    open fun getContentView(): ViewGroup? {
        return  mSwipeRefreshLayout
    }
    /**
     * 默认LinearLayoutManager，然后竖方向
     */
    open fun setRecyclerView() {
        setRecyclerView(LinearLayoutManager.VERTICAL, android.R.color.black )
    }
    /**
     * @param isGoogle 是否设置谷歌样式
     * @param orientation 设置方向
     * @param color 设置颜色。
     */
    open fun setRecyclerView(orientation: Int,color:Int) {
        if (mRecyclerView == null) return
        val layoutManager: LinearLayoutManager = LinearLayoutManager(context)
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout?.setColorSchemeResources(color)
            mSwipeRefreshLayout?.setSize(MySwipeRefreshLayout.DEFAULT)
            mSwipeRefreshLayout?.setProgressBackgroundColor(android.R.color.white)
            //swipeRefreshLayout.setPadding(20, 20, 20, 20);
            //代表支持缩放，0为start, 200为end
            mSwipeRefreshLayout?.setProgressViewOffset(false, -DensityUtil.dip2px(context, 40f), DensityUtil.dip2px(context, 60f))
            //// 设置手指在屏幕下拉多少距离会触发下拉刷新
            //        MySwipeRefreshLayout.setDistanceToTriggerSync(DensityUtil.dip2px(this,50));
            mSwipeRefreshLayout?.setProgressViewEndTarget(false, DensityUtil.dip2px(context, 60f))
        }
        //////////////////////////////////开始处理列表//////////////////////////////////////////////////////////
        layoutManager.orientation = orientation
        layoutManager.isSmoothScrollbarEnabled = true
        layoutManager.isAutoMeasureEnabled = true
        mRecyclerView?.layoutManager = layoutManager
        mRecyclerView?.setHasFixedSize(true)
        mRecyclerView?.setItemAnimator(MyRecyclerView.ItemType.FadeInLeft)
        mRecyclerView?.setLoadingMoreProgressStyle(ProgressStyle.MaterialDesign)

        //注意设置MaterialDesign类型的时候，要单独的在设置下颜色，最好以MySwipeRefreshLayout颜色拼配
        if (mRecyclerView != null && mRecyclerView?.materialProgressView != null) {
            mRecyclerView?.materialProgressView?.setColorSchemeResources(color)
        }
        //注意由于下来刷新用的是谷歌的，，则要设置下谷歌刷新标识
        try {
            mRecyclerView?.setGoogleRefresh(true, mSwipeRefreshLayout)
        } catch (e: Exception) {
        }

        //只有调用setLoadingMoreEnabled才会打开上啦加载
        mRecyclerView?.setLoadingMoreEnabled(true)
    }

    /**
     * @param isNestedScrollView 是否有NestedScrollView进行嵌套
     */
    open fun setRecyclerView(isNestedScrollView: Boolean, orientation: Int,color:Int) {
        if (mRecyclerView == null) return
        val layoutManager: LinearLayoutManager = LinearLayoutManager(context)
        //////////////////////////////////开始处理列表//////////////////////////////////////////////////////////
        layoutManager.orientation = orientation
        layoutManager.isSmoothScrollbarEnabled = true
        mRecyclerView?.layoutManager = layoutManager
        mRecyclerView?.setHasFixedSize(true)
        mRecyclerView?.isNestedScrollingEnabled = !isNestedScrollView
        mRecyclerView?.setItemAnimator(MyRecyclerView.ItemType.FadeInLeft)
        mRecyclerView?.setLoadingMoreProgressStyle(ProgressStyle.MaterialDesign)
        //注意设置MaterialDesign类型的时候，要单独的在设置下颜色，最好以MySwipeRefreshLayout颜色拼配
        if (mRecyclerView != null && mRecyclerView?.materialProgressView != null) {
            mRecyclerView?.materialProgressView?.setColorSchemeResources(color)
        }
        //只有调用setLoadingMoreEnabled才会打开上啦加载
        mRecyclerView?.setLoadingMoreEnabled(true)
    }

    /**
     * 设置GridLayoutManager
     */
    open fun setRecyclerViewGrid(spanCount: Int,color:Int) {
        if (mSwipeRefreshLayout == null || mRecyclerView == null) return
        val layoutManager: GridLayoutManager = GridLayoutManager(context, spanCount)
        mSwipeRefreshLayout?.setColorSchemeResources(color)
        mSwipeRefreshLayout?.setSize(MySwipeRefreshLayout.DEFAULT)
        mSwipeRefreshLayout?.setProgressBackgroundColor(android.R.color.white)
        //swipeRefreshLayout.setPadding(20, 20, 20, 20);
        //代表支持缩放，0为start, 200为end
        mSwipeRefreshLayout?.setProgressViewOffset(false, -DensityUtil.dip2px(context, 40f), DensityUtil.dip2px(context, 60f))
        //// 设置手指在屏幕下拉多少距离会触发下拉刷新
        //        MySwipeRefreshLayout.setDistanceToTriggerSync(DensityUtil.dip2px(this,50));
        mSwipeRefreshLayout?.setProgressViewEndTarget(false, DensityUtil.dip2px(context, 60f))
        //////////////////////////////////开始处理列表//////////////////////////////////////////////////////////
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        layoutManager.isSmoothScrollbarEnabled = true
        layoutManager.isAutoMeasureEnabled = true
        mRecyclerView?.layoutManager = layoutManager
        mRecyclerView?.setHasFixedSize(true)
        mRecyclerView?.setItemAnimator(MyRecyclerView.ItemType.FadeInLeft)
        mRecyclerView?.setLoadingMoreProgressStyle(ProgressStyle.MaterialDesign)
        //注意设置MaterialDesign类型的时候，要单独的在设置下颜色，最好以MySwipeRefreshLayout颜色拼配
        if (mRecyclerView != null && mRecyclerView?.materialProgressView != null) {
            mRecyclerView?.materialProgressView?.setColorSchemeResources(color)
        }
        //注意由于下来刷新用的是谷歌的，，则要设置下谷歌刷新标识
        mRecyclerView?.setGoogleRefresh(true, mSwipeRefreshLayout)
        //只有调用setLoadingMoreEnabled才会打开上啦加载
        mRecyclerView?.setLoadingMoreEnabled(true)
    }

    /**

     * @param count 当前刷新的数量
     * @param nomore 上啦加载提示文字
     * @param timeout 超时
     * @param emptyView 空内容
     * @param emptyView_network 空网络
     */
    open fun notifyDataSetChanged(count: Int, nomore: String, timeout: Boolean, emptyView: View?, emptyView_network: View?) {
        mRecyclerView?.refreshComplete()
        mSwipeRefreshLayout?.setRefreshing(false, 500)
        mRecyclerView?.loadMoreComplete()
        if (count < limit && !timeout) {
            mRecyclerView?.noMoreLoading(nomore)
        }
        mAdapter?.notifyDataSetChanged()
        //超时,如果当前没有数据的时候，则显示网络异常的图片。如果当前有数据的话，则不做任何处理
        if (timeout) {
            setEmptyView(emptyView_network)
            //由于emptyView_network有的是NestedScrollView 导致不能点击，所以用他的子类来点击
            if (!(emptyView_network is NestedScrollView)) {
                emptyView_network?.setOnClickListener { v ->
                    if (mSwipeRefreshLayout != null) {
                        mSwipeRefreshLayout?.isRefreshing = true
                    }
                }
            }
            emptyView?.visibility = View.GONE
        }
        //没有数据
        else {
            setEmptyView(emptyView)
            emptyView_network?.visibility = View.GONE
        }
    }

    open fun setEmptyView(v: View?) {
        if (v != null) {
            if (mAdapter?.itemCount == 0) {
                v.visibility = View.VISIBLE
                val height= mRecyclerView?.headerViews?.sumBy { it.height }
                val para1= v.layoutParams as RelativeLayout.LayoutParams
                para1.topMargin=height?:0
            } else {
                v.visibility = View.GONE
            }
        }
    }
}