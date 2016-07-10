package com.xiaojinzi.autolayout.utils;

import android.view.View;
import android.view.ViewGroup;

import com.xiaojinzi.autolayout.config.AutoLayoutConifg;
import com.yoursecondworld.adaptationdemo.R;

/**
 * Created by zhy on 15/12/4.
 * 自带适配宽和高
 * 适配内边距
 * 适配外边距
 */
public class AutoUtils {

    /**
     * 会直接将view的LayoutParams上设置的width，height直接进行百分比处理
     *
     * @param view
     */
    public static void auto(View view) {
        autoSize(view);
        autoPadding(view);
        autoMargin(view);
    }

    /**
     * 适配外边距
     *
     * @param view
     */
    public static void autoMargin(View view) {
        if (!(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams))
            return;

        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (lp == null) return;

        Object tag = view.getTag(R.id.id_tag_autolayout_margin);
        if (tag != null) return;
        view.setTag(R.id.id_tag_autolayout_margin, "Just Identify");

        lp.leftMargin = getPercentWidthSize(lp.leftMargin);
        lp.topMargin = getPercentHeightSize(lp.topMargin);
        lp.rightMargin = getPercentWidthSize(lp.rightMargin);
        lp.bottomMargin = getPercentHeightSize(lp.bottomMargin);

    }

    /**
     * 适配内边距
     *
     * @param view
     */
    public static void autoPadding(View view) {
        Object tag = view.getTag(R.id.id_tag_autolayout_padding);
        if (tag != null) return;
        //做一个标识而已,防止二次计算
        view.setTag(R.id.id_tag_autolayout_padding, "Just Identify");

        int l = view.getPaddingLeft();
        int t = view.getPaddingTop();
        int r = view.getPaddingRight();
        int b = view.getPaddingBottom();

        l = getPercentWidthSize(l);
        t = getPercentHeightSize(t);
        r = getPercentWidthSize(r);
        b = getPercentHeightSize(b);

        view.setPadding(l, t, r, b);
    }

    /**
     * 适配宽和高
     *
     * @param view
     */
    public static void autoSize(View view) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();

        if (lp == null) return;

        Object tag = view.getTag(R.id.id_tag_autolayout_size);
        if (tag != null) return;

        //做一个标识而已,防止二次计算
        view.setTag(R.id.id_tag_autolayout_size, "Just Identify");

        if (lp.width > 0) { //如果直接写的是大小,不是包裹和填充那种的
            int screenWidth = AutoLayoutConifg.getInstance().getScreenWidth();
            int designWidth = AutoLayoutConifg.getInstance().getDesignWidth();
            lp.width = (int) (lp.width * 1.0f / designWidth * screenWidth);
        }

        if (lp.height > 0) { //如果直接写的是大小,不是包裹和填充那种的
            int screenHeight = AutoLayoutConifg.getInstance().getScreenHeight();
            int designHeight = AutoLayoutConifg.getInstance().getDesignHeight();
            lp.height = (int) (lp.height * 1.0f / designHeight * screenHeight);
        }
    }

    /**
     * 调整val的值适配本机的分辨率
     *
     * @param val 传入的尺寸
     * @return
     */
    public static int getPercentWidthSize(int val) {
        int screenWidth = AutoLayoutConifg.getInstance().getScreenWidth();
        int designWidth = AutoLayoutConifg.getInstance().getDesignWidth();

        return (int) (val * 1.0f / designWidth * screenWidth);
    }


    /**
     * 同上
     *
     * @param val
     * @return
     */
    public static int getPercentWidthSizeBigger(int val) {
        int screenWidth = AutoLayoutConifg.getInstance().getScreenWidth();
        int designWidth = AutoLayoutConifg.getInstance().getDesignWidth();

        int res = val * screenWidth;
        if (res % designWidth == 0) {
            return res / designWidth;
        } else {
            return res / designWidth + 1;
        }

    }


    /**
     * 调整val的值适配本机的分辨率
     *
     * @param val 传入的尺寸
     * @return
     */
    public static int getPercentHeightSize(int val) {
        int screenHeight = AutoLayoutConifg.getInstance().getScreenHeight();
        int designHeight = AutoLayoutConifg.getInstance().getDesignHeight();

        return (int) (val * 1.0f / designHeight * screenHeight);
    }

    /**
     * 同上
     *
     * @param val
     * @return
     */
    public static int getPercentHeightSizeBigger(int val) {
        int screenHeight = AutoLayoutConifg.getInstance().getScreenHeight();
        int designHeight = AutoLayoutConifg.getInstance().getDesignHeight();

        int res = val * screenHeight;
        if (res % designHeight == 0) {
            return res / designHeight;
        } else {
            return res / designHeight + 1;
        }
    }
}
