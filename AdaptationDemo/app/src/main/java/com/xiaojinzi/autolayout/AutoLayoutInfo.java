package com.xiaojinzi.autolayout;

import android.view.View;

import com.xiaojinzi.autolayout.attr.AutoAttr;

import java.util.ArrayList;
import java.util.List;

public class AutoLayoutInfo {

    /**
     * 自动适配的属性集合
     */
    private List<AutoAttr> autoAttrs = new ArrayList<>();

    /**
     * 添加适配的属性
     *
     * @param autoAttr
     */
    public void addAttr(AutoAttr autoAttr) {
        autoAttrs.add(autoAttr);
    }


    /**
     * 适配
     *
     * @param view
     */
    public void fillAttrs(View view) {
        for (AutoAttr autoAttr : autoAttrs) {
            autoAttr.apply(view);
        }
    }

    @Override
    public String toString() {
        return "AutoLayoutInfo{" +
                "autoAttrs=" + autoAttrs +
                '}';
    }
}