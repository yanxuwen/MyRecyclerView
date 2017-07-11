package com.yanxuwen.PinnedHeader.IndexBar.helper;

import com.yanxuwen.PinnedHeader.IndexBar.bean.BaseIndexPinyinBean;

import java.util.List;

/**
 * 介绍：IndexBar 的 数据相关帮助类
 * 1 将汉语转成拼音
 * 2 填充indexTag
 * 3 排序源数据源
 * 4 根据排序后的源数据源indexBar的数据源
 * 作者：yanxuwen
 * 邮箱：420255048@qq.com
 * 时间： 2016/11/28.
 */

public interface IIndexBarDataHelper {
    //汉语-》拼音
    IIndexBarDataHelper convert(List<? extends BaseIndexPinyinBean> data);

    //拼音->tag
    IIndexBarDataHelper fillInexTag(List<? extends BaseIndexPinyinBean> data);

    //对源数据进行排序（RecyclerView）
    IIndexBarDataHelper sortSourceDatas(List<? extends BaseIndexPinyinBean> datas);

    //对IndexBar的数据源进行排序(右侧栏),在 sortSourceDatas 方法后调用
    IIndexBarDataHelper getSortedIndexDatas(List<? extends BaseIndexPinyinBean> sourceDatas, List<String> datas);
}
