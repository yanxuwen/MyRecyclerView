package com.yanxuwen.PinnedHeader.IndexBar.bean;

import com.yanxuwen.PinnedHeader.suspension.ISuspensionInterface;

/**
 * 介绍：索引类的标志位的实体基类
 * 作者：zhangxutong
 * 邮箱：mcxtzhang@163.com
 * CSDN：http://blog.csdn.net/zxt0601
 * 时间： 16/09/04.
 */

public abstract class BaseIndexBean implements ISuspensionInterface {
    private String baseIndexTag;//所属的分类（城市的汉语拼音首字母）
    private String suspensionTag;////悬停的title
    public String getBaseIndexTag() {
        return baseIndexTag;
    }

    /**
     * 设置索引值
     */
    public BaseIndexBean setBaseIndexTag(String baseIndexTag) {
        this.baseIndexTag = baseIndexTag;
        return this;
    }

    @Override
    public String getSuspensionTag() {
        return suspensionTag==null?baseIndexTag:suspensionTag;
    }

    /**
     * 设置/悬停的title，如果没有设置，则会自动去索引的值
     */
    public BaseIndexBean setSuspensionTag(String suspensionTag) {
        this.suspensionTag = suspensionTag;
        return this;
    }
    /**
     * 是否要显示，头部
     */
    @Override
    public boolean isShowSuspension() {
        return true;
    }
}
