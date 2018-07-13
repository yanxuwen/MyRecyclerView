# 博客地址http://www.jianshu.com/p/dd552dfc891e
# 前言
现在下拉上拉控件非常多，但是网上的上拉下拉控件，只有这2种功能，有时候我们需要像QQ那样滑动item就出现了删除按钮，有时候我们需要点击item可以展开更多的信息，以便我们看更多的信息，有时候我们需要谷歌自带的下拉控件，有时候需要自己定义上下拉，切换来切换去很麻烦，于是我在某个大神（忘记是哪个了）的下拉上拉的控件的基础上，增加了谷歌自带下拉以及上拉样式模仿了谷歌下拉的那种样式，还有滑动item出现删除跟展开更多，和城市列表，，以上的功能都只需要简单的配置下就可以使用。
## 首先先看个效果图

![GIF.gif](http://upload-images.jianshu.io/upload_images/6835615-ecfac2f40e5dbae1.gif?imageMogr2/auto-orient/strip)


# 依赖：
compile'com.yanxuwen.MyRecyclerView:MyRecyclerview:1.5.8'
# 1.上下拉实现：
###    xml：

     <com.yanxuwen.MyRecyclerview.MyRecyclerView
     android:id="@+id/recyclerview"
     android:layout_width="fill_parent"
     android:layout_height="fill_parent"
    />
### java：
~~~
 mRecyclerView= (MyRecyclerView)this.findViewById(R.id.recyclerview);
 LinearLayoutManager layoutManager =newLinearLayoutManager(this);
  layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
  mRecyclerView.setLayoutManager(layoutManager);
//设置刷新样式，ProgressStyle里面设置了很多种样式，自行参考
mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
//设置加载样式，ProgressStyle里面设置了很多种样式，自行参考
mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
//设置下拉图标
mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
//设置动画
mRecyclerView.setItemAnimator(MyRecyclerView.ItemType.FadeInLeft);
//启动可以刷新
mRecyclerView.setPullRefreshEnabled(true);
//启动可以加载
mRecyclerView.setLoadingMoreEnabled(true);
//执行刷新
 mRecyclerView.setRefreshing(true);
//刷新跟加载监听
mRecyclerView.setLoadingListener(newMyRecyclerView.LoadingListener() {
public voidonRefresh() {
}
public voidonLoadMore() {
}
});
//执行添加item操作
findViewById(R.id.add).setOnClickListener(newView.OnClickListener() {
public voidonClick(View v) {
mAdapter.add("newly added item",2);
}
});
//执行删除item操作
findViewById(R.id.del).setOnClickListener(newView.OnClickListener() {
public voidonClick(View v) {
mAdapter.remove(2);
}
});
//执行点击item进行展开操作
mAdapter.setOnItemClickListener(newMyBaseAdapter.OnItemClickListener() {
public voidonItemClick(MyBaseAdapter.BaseViewHolder holder,View view, intposition) {
holder.expand();
Log.e("xxx",position +"");
}
});
~~~
### 刷新完后操作，记得调用
~~~
mAdapter.setFirstOnly(true);
mRecyclerView.refreshComplete();
~~~
### 加载完后操作：
~~~
mRecyclerView.refreshComplete();
mRecyclerView.loadMoreComplete();
~~~
### 如果没有数据了，调用
~~~
//也可以不传递参数，默认会显示空白
mRecyclerView.noMoreLoading("没有了");
~~~
### Adapter:
需要继承MyBaseAdapter，主要介绍onCreateViewHolder的功能
~~~
publicViewHolderonCreateViewHolder(ViewGroup parent, intviewType) {
//添加滑动item删除按钮，可根据具体情况是否添加
addSwipe(R.layout.swipe_default2,SwipeLayout.ShowMode.LayDown,SwipeLayout.DragEdge.Right, true);
//添加展开详情，可根据具体情况是否添加，如何展开上面已经有介绍了。
addExpand(R.layout.expand_default);
//item布局
return newViewHolder(setLayout(R.layout.item,parent));
}

~~~

# 2.谷歌自带刷新样式：

###  xml：其实就是在外层嵌入SwipeRefreshLayout
    <com.yanxuwen.MyRecyclerview.MySwipeRefreshLayout
        android:id="@+id/refreshLayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
        <com.yanxuwen.MyRecyclerview.MyRecyclerView
            android:id="@+id/recyclerview"
            android:layout_width="fill_parent"
            android:layout_height="fill_parent"
            >
        />
    />
###  java：代码几乎跟上面的写法是一样的，无非就是取消了下拉刷新用谷歌刷新替代，更改上拉加载样式，所以主要介绍变动的代码
~~~
//设置加载样式，为MaterialDesign样式，也就是类似谷歌刷新的样式
mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.MaterialDesign);
//注意设置MaterialDesign类型的时候，要单独的在设置下颜色，最好以SwipeRefreshLayout颜色拼配
if(mRecyclerView!=null&&mRecyclerView.getMaterialProgressView()!=null){
      mRecyclerView.getMaterialProgressView().setColorSchemeResources(R.color.black);
}
//【重点，一定要调用】注意由于下来刷新用的是谷歌的，，则要设置下谷歌刷新标识
mRecyclerView.setGoogleRefresh(true,mSwipeRefreshLayout);
//启动可以刷新，由于启动了谷歌刷新，该句不需要调用
//mRecyclerView.setPullRefreshEnabled(true);
//启动可以加载
mRecyclerView.setLoadingMoreEnabled(true);
//执行刷新，【注意,这里的谷歌刷新我们在封装了一层MySwipeRefreshLayout，如果使用的是MySwipeRefreshLayout，
 //mSwipeRefreshLayout.setRefreshing(true);会自动开启刷新，如果使用的是系统的MySwipeRefreshLayout，则还需要调
 //用onRefresh()，这些大家都指定，就不需要我多说了】
//执行刷新
mSwipeRefreshLayout.setRefreshing(true);
~~~   
   


# 3.城市列表：
### xml：
~~~
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <android.support.v7.widget.RecyclerView
        android:id="@+id/rv"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@android:color/white">

    </android.support.v7.widget.RecyclerView>

    <com.yanxuwen.PinnedHeader.IndexBar.widget.IndexBar
        android:id="@+id/indexBar"
        android:layout_width="24dp"
        android:layout_height="match_parent"
        android:layout_marginTop="150dp"
        android:layout_marginBottom="150dp"
        android:layout_alignParentRight="true"
        android:layout_centerVertical="true"
        app:indexBarPressBackground="#00000000"
        app:textColor="#3388FF"
        app:indexBarTextSize="12sp"/>

    <TextView
        android:id="@+id/tvSideBarHint"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:minWidth="80dp"
        android:minHeight="80dp"
        android:layout_gravity="center"
        android:background="@drawable/shape_side_bar_bg"
        android:gravity="center"
        android:layout_centerInParent="true"
        android:textColor="@android:color/white"
        android:textSize="48sp"
        android:visibility="gone"
        android:padding="10dp"
        tools:text="A"
        tools:visibility="visible"/>

</RelativeLayout>

~~~
### java：
~~~
mRv = (RecyclerView) findViewById(R.id.rv);
mRv.setLayoutManager(mManager = new LinearLayoutManager(this));
mAdapter = new CityAdapter(this, mDatas);
mHeaderAdapter = new HeaderRecyclerAndFooterWrapperAdapter(mAdapter) {
protected void onBindHeaderHolder(ViewHolder holder, int headerPos, int layoutId, Object o) {
                holder.setText(R.id.tvCity, (String) o);
        }
 };
mHeaderAdapter.addHeaderView(R.layout.item_city, "测试头部");
mHeaderAdapter.addHeaderView(R.layout.item_city, "测试头部2");
mRv.setAdapter(mHeaderAdapter);
//添加头部导航
mRv.addItemDecoration(mDecoration = new SuspensionDecoration(this, mDatas).setHeaderViewCount(mHeaderAdapter.getHeaderViewCount()));
mRv.addItemDecoration(new DividerItemDecoration(PinnedHeaderActivity.this, LinearLayoutManager.VERTICAL));
//使用indexBar
mTvSideBarHint = (TextView) findViewById(R.id.tvSideBarHint);//HintTextView
mIndexBar = (IndexBar) findViewById(R.id.indexBar);//IndexBar
mIndexBar.setPressedShowTextView(mTvSideBarHint)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setLayoutManager(mManager);//设置RecyclerView的LayoutManager
 initDatas(getResources().getStringArray(R.array.provinces));
~~~
### initDatas：
~~~
   mDatas = new ArrayList<>();
                for (int i = 0; i < data.length; i++) {
                    CityBean cityBean = new CityBean();
                    cityBean.setCity(data[i]);//设置城市名称
                    mDatas.add(cityBean);
                }

                mIndexBar
                       //设置悬停头部如A，B,C,D，跟索引进行关联
                        .setSuspensionDecoration(mDecoration)
                        //是否需要根据实际的数据来生成索引数据源（例如 只有 A B C 三种tag，那么索引栏就 A B C 三项），如果为false索引栏就会显示从A到Z的数据
                        .setNeedRealIndex(true)
                        //设置是否排序，默认true强制从A到Z进行排序，false则会按照给的数据顺序进行排序，而且会自动隐藏索引栏
                        .setIsSort(true)
                        .setSourceDatas(mDatas)//设置数据
                        .setHeaderViewCount(mHeaderAdapter.getHeaderViewCount())//设置HeaderView数量
                        .invalidate();

                mAdapter.setDatas(mDatas);
                mHeaderAdapter.notifyDataSetChanged();
~~~
### Adapter：跟上面的Adapter没什么区别，这里就不需要多介绍

### CityBean 重点，一定要继承BaseIndexPinyinBean 
~~~
public class CityBean extends BaseIndexPinyinBean {

    private String city;//城市名字

    public CityBean() {
    }

    public CityBean(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public CityBean setCity(String city) {
        this.city = city;
        return this;
    }

    /**
     * 是否需要被转化成拼音，
     * true的话，头部跟索引都会将getTarget值转换为拼音
     * 【这里给ab值做了特殊处理，如果是ab则不强制转换为拼音】
     */
    @Override
    public boolean isNeedToPinyin() {
        return  city.equals("ab")?false:true;
    }

    /**
     * 前提【isNeedToPinyin为false,才能显示是全文，不然都getTarget的首拼】
     * 头部跟索引的值，isNeedToPinyin为true则会自动转换为拼音首字母，false的话则原文显示
     * 【这里做了特殊处理，如果是ab值，则头部显示"&%%"标记，其他显示Aab】
     */
    @Override
    public String getTarget() {
        return  city.equals("ab")?"&%%":city;
    }


    /**
     *  前提【isNeedToPinyin为false,才能自定义，不然无线】
     * 自定义索引值
     */
    public String getIndexString() {
        return  city.equals("ab")?"&":null;
    }

    /**
     * 是否需要显示悬停title
     */
    @Override
    public boolean isShowSuspension() {
        return true;
    }
}

~~~


### github代码：https://github.com/yanxuwen/MyRecyclerView
### 微信公众号：
      
![qrcode_for_gh_8e99f824c0d6_344.jpg](http://upload-images.jianshu.io/upload_images/6835615-8b35ce64a1688c8b.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 喜欢就在github star下,非常感谢o(∩_∩)o~~~
