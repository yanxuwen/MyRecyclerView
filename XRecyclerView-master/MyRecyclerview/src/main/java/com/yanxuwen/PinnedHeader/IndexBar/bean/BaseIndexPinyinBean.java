package com.yanxuwen.PinnedHeader.IndexBar.bean;

/**
 * 介绍：索引类的汉语拼音的接口
 * 作者：zhangxutong
 * 邮箱：mcxtzhang@163.com
 * CSDN：http://blog.csdn.net/zxt0601
 * 时间： 16/09/04.
 */

public abstract class BaseIndexPinyinBean extends BaseIndexBean {
    private String baseIndexPinyin;//城市的拼音

    public String getBaseIndexPinyin() {
        return baseIndexPinyin;
    }

    public BaseIndexPinyinBean setBaseIndexPinyin(String baseIndexPinyin) {
        this.baseIndexPinyin = baseIndexPinyin;
        return this;
    }

    /**
     * @return
     * 是否需要被转化成拼音，
     * true的话，头部跟索引都会将getTarget值转换为拼音
     */
    public boolean isNeedToPinyin() {
        return true;
    }

    /**
     * 前提【isNeedToPinyin为false,才能是全文，不然都getTarget的首拼】
     * 头部跟索引的值，isNeedToPinyin为true则会自动转换为拼音首字母，false的话则原文显示
     */
    public abstract String getTarget();
    /**
     * 前提【isNeedToPinyin为false,才能自定义，不然无线】
     * 自定义索引值，也就是替换getTarget的索引，但是不替换头部标题，，主要用于一些特殊
     * 如：头部为“热门城市”
     * 但是索引要为“热门”
     * null的时候则取getTarget
     */
    public String getIndexString() {
        return null;
    }


}
