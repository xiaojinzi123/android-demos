package com.yoursecondworld.recyclerviewdemo.entity;

/**
 * Created by cxj on 2016/7/14.
 */
public class DemoEntity {

    //如果有这个说明需要使用tag条目
    private String tag;

    //如果有这个说明要使用item条目
    private String name;

    public DemoEntity(String tag, String name) {
        this.tag = tag;
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
