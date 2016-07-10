/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xiaojinzi.autolayout.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.xiaojinzi.autolayout.AutoLayoutInfo;
import com.xiaojinzi.autolayout.attr.HeightAttr;
import com.xiaojinzi.autolayout.attr.MarginAttr;
import com.xiaojinzi.autolayout.attr.MarginBottomAttr;
import com.xiaojinzi.autolayout.attr.MarginLeftAttr;
import com.xiaojinzi.autolayout.attr.MarginRightAttr;
import com.xiaojinzi.autolayout.attr.MarginTopAttr;
import com.xiaojinzi.autolayout.attr.MaxHeightAttr;
import com.xiaojinzi.autolayout.attr.MaxWidthAttr;
import com.xiaojinzi.autolayout.attr.MinHeightAttr;
import com.xiaojinzi.autolayout.attr.MinWidthAttr;
import com.xiaojinzi.autolayout.attr.PaddingAttr;
import com.xiaojinzi.autolayout.attr.PaddingBottomAttr;
import com.xiaojinzi.autolayout.attr.PaddingLeftAttr;
import com.xiaojinzi.autolayout.attr.PaddingRightAttr;
import com.xiaojinzi.autolayout.attr.PaddingTopAttr;
import com.xiaojinzi.autolayout.attr.TextSizeAttr;
import com.xiaojinzi.autolayout.attr.WidthAttr;
import com.xiaojinzi.autolayout.config.AutoLayoutConifg;
import com.yoursecondworld.adaptationdemo.R;


public class AutoLayoutHelper {
    private final ViewGroup mHost;

    /**
     * 目前支持的属性数组
     */
    private static final int[] LL = new int[]
            { //
                    android.R.attr.textSize,
                    android.R.attr.padding,//
                    android.R.attr.paddingLeft,//
                    android.R.attr.paddingTop,//
                    android.R.attr.paddingRight,//
                    android.R.attr.paddingBottom,//
                    android.R.attr.layout_width,//
                    android.R.attr.layout_height,//
                    android.R.attr.layout_margin,//
                    android.R.attr.layout_marginLeft,//
                    android.R.attr.layout_marginTop,//
                    android.R.attr.layout_marginRight,//
                    android.R.attr.layout_marginBottom,//
                    android.R.attr.maxWidth,//
                    android.R.attr.maxHeight,//
                    android.R.attr.minWidth,//
                    android.R.attr.minHeight,//16843072


            };

    private static final int INDEX_TEXT_SIZE = 0;
    private static final int INDEX_PADDING = 1;
    private static final int INDEX_PADDING_LEFT = 2;
    private static final int INDEX_PADDING_TOP = 3;
    private static final int INDEX_PADDING_RIGHT = 4;
    private static final int INDEX_PADDING_BOTTOM = 5;
    private static final int INDEX_WIDTH = 6;
    private static final int INDEX_HEIGHT = 7;
    private static final int INDEX_MARGIN = 8;
    private static final int INDEX_MARGIN_LEFT = 9;
    private static final int INDEX_MARGIN_TOP = 10;
    private static final int INDEX_MARGIN_RIGHT = 11;
    private static final int INDEX_MARGIN_BOTTOM = 12;
    private static final int INDEX_MAX_WIDTH = 13;
    private static final int INDEX_MAX_HEIGHT = 14;
    private static final int INDEX_MIN_WIDTH = 15;
    private static final int INDEX_MIN_HEIGHT = 16;


    /**
     * move to other place?
     */
    private static AutoLayoutConifg mAutoLayoutConifg;

    public AutoLayoutHelper(ViewGroup host) {
        mHost = host;
        if (mAutoLayoutConifg == null) {
            initAutoLayoutConfig(host);
        }

    }

    private void initAutoLayoutConfig(ViewGroup host) {
        mAutoLayoutConifg = AutoLayoutConifg.getInstance();
        mAutoLayoutConifg.init(host.getContext());
    }

    /**
     * 对孩子的px属性进行调整适配
     */
    public void adjustChildren() {

        //检查是否有设计图的宽和高
        AutoLayoutConifg.getInstance().checkParams();

        for (int i = 0, n = mHost.getChildCount(); i < n; i++) {
            View view = mHost.getChildAt(i);
            ViewGroup.LayoutParams params = view.getLayoutParams();

            if (params instanceof AutoLayoutParams) {
                AutoLayoutInfo info =
                        ((AutoLayoutParams) params).getAutoLayoutInfo();
                if (info != null) {
                    info.fillAttrs(view);
                }
            }
        }

    }

    /**
     * 获取布局的信息
     *
     * @param context
     * @param attrs
     * @return
     */
    public static AutoLayoutInfo getAutoLayoutInfo(Context context,
                                                   AttributeSet attrs) {

        //创建一个自动布局的信息
        AutoLayoutInfo info = new AutoLayoutInfo();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AutoLayout_Layout);
        int baseWidth = a.getInt(R.styleable.AutoLayout_Layout_layout_auto_basewidth, 0);
        int baseHeight = a.getInt(R.styleable.AutoLayout_Layout_layout_auto_baseheight, 0);
        a.recycle();

        //获取支持的属性的集合
        TypedArray array = context.obtainStyledAttributes(attrs, LL);

        //获取集合的长度
        int n = array.getIndexCount();

        //循环集合,针对属性里面单位是px的进行适配
        for (int i = 0; i < n; i++) {

            int index = array.getIndex(i);
//            String val = array.getString(index);
//            if (!isPxVal(val)) continue;

            //如果不是px为单位的,就跳过,下一个
            if (!isPxVal(array.peekValue(index))) continue;

            int pxVal = 0;
            try {
                //获取填写的数值
                pxVal = array.getDimensionPixelOffset(index, 0);
            } catch (Exception ignore) {//not dimension
                continue;
            }
            //晒选下标,添加写了并且支持的属性的信息到集合中,供onMeasure方法中调整适配
            switch (index) {
                case INDEX_TEXT_SIZE:
                    info.addAttr(new TextSizeAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_PADDING:
                    info.addAttr(new PaddingAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_PADDING_LEFT:
                    info.addAttr(new PaddingLeftAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_PADDING_TOP:
                    info.addAttr(new PaddingTopAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_PADDING_RIGHT:
                    info.addAttr(new PaddingRightAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_PADDING_BOTTOM:
                    info.addAttr(new PaddingBottomAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_WIDTH:
                    info.addAttr(new WidthAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_HEIGHT:
                    info.addAttr(new HeightAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_MARGIN:
                    info.addAttr(new MarginAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_MARGIN_LEFT:
                    info.addAttr(new MarginLeftAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_MARGIN_TOP:
                    info.addAttr(new MarginTopAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_MARGIN_RIGHT:
                    info.addAttr(new MarginRightAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_MARGIN_BOTTOM:
                    info.addAttr(new MarginBottomAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_MAX_WIDTH:
                    info.addAttr(new MaxWidthAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_MAX_HEIGHT:
                    info.addAttr(new MaxHeightAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_MIN_WIDTH:
                    info.addAttr(new MinWidthAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_MIN_HEIGHT:
                    info.addAttr(new MinHeightAttr(pxVal, baseWidth, baseHeight));
                    break;
            }
        }
        //回收
        array.recycle();
//        L.e(" getAutoLayoutInfo " + info.toString());
        return info;
    }

    /**
     * 判断属性是不是px单位的
     *
     * @param val
     * @return
     */
    private static boolean isPxVal(TypedValue val) {
        if (val != null && val.type == TypedValue.TYPE_DIMENSION &&
                getComplexUnit(val.data) == TypedValue.COMPLEX_UNIT_PX) {
            return true;
        }
        return false;
    }

    private static int getComplexUnit(int data) {
        return TypedValue.COMPLEX_UNIT_MASK & (data >> TypedValue.COMPLEX_UNIT_SHIFT);
    }

    /**
     * 当属性是字符串的时候,判断是不是px结尾的
     *
     * @param val
     * @return
     */
    private static boolean isPxVal(String val) {
        if (val.endsWith("px")) {
            return true;
        }
        return false;
    }

    public interface AutoLayoutParams {
        AutoLayoutInfo getAutoLayoutInfo();
    }
}
