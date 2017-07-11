package com.example.xrecyclerview.PinnedHeader;

import com.yanxuwen.PinnedHeader.IndexBar.bean.BaseIndexPinyinBean;

/**
 * Created by yanxuwen .
 * 在这里面可以设置显示或者不显示，是否转换拼音或者全文显示
 */

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
