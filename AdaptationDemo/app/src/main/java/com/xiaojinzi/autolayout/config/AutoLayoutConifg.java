package com.xiaojinzi.autolayout.config;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.xiaojinzi.autolayout.utils.L;
import com.xiaojinzi.autolayout.utils.ScreenUtils;

/**
 * Created by zhy on 15/11/18.
 * 自动布局的配置
 */
public class AutoLayoutConifg {

    /**
     * 声明一个自己
     */
    private static AutoLayoutConifg sIntance = new AutoLayoutConifg();

    /**
     * 清单文件中必须生命的设计图的宽
     */
    private static final String KEY_DESIGN_WIDTH = "design_width";

    /**
     * 清单文件中必须生命的设计图的高
     */
    private static final String KEY_DESIGN_HEIGHT = "design_height";

    //屏幕的宽和高,这里是指分辨率的宽和高
    private int mScreenWidth;
    private int mScreenHeight;

    //设计图的宽和高
    private int mDesignWidth;
    private int mDesignHeight;

    private boolean useDeviceSize;

    private AutoLayoutConifg() {
    }

    /**
     * 检查设计图的宽和高是否读取到了
     */
    public void checkParams() {
        if (mDesignHeight <= 0 || mDesignWidth <= 0) {
            throw new RuntimeException(
                    "you must set " + KEY_DESIGN_WIDTH + " and " + KEY_DESIGN_HEIGHT + "  in your manifest file.");
        }
    }

    public AutoLayoutConifg useDeviceSize() {
        useDeviceSize = true;
        return this;
    }


    /**
     * 返回自己的实例对象
     *
     * @return
     */
    public static AutoLayoutConifg getInstance() {
        return sIntance;
    }


    public int getScreenWidth() {
        return mScreenWidth;
    }

    public int getScreenHeight() {
        return mScreenHeight;
    }

    public int getDesignWidth() {
        return mDesignWidth;
    }

    public int getDesignHeight() {
        return mDesignHeight;
    }

    /**
     * 初始化屏幕的宽和高
     *
     * @param context 上下文
     */
    public void init(Context context) {
        getMetaData(context);
        int[] screenSize = ScreenUtils.getScreenSize(context, useDeviceSize);
        mScreenWidth = screenSize[0];
        mScreenHeight = screenSize[1];
        L.e(" screenWidth =" + mScreenWidth + " ,screenHeight = " + mScreenHeight);
    }

    /**
     * 获取清单文件中的设计图的宽和高
     *
     * @param context
     */
    private void getMetaData(Context context) {
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(context
                    .getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                mDesignWidth = (int) applicationInfo.metaData.get(KEY_DESIGN_WIDTH);
                mDesignHeight = (int) applicationInfo.metaData.get(KEY_DESIGN_HEIGHT);
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(
                    "you must set " + KEY_DESIGN_WIDTH + " and " + KEY_DESIGN_HEIGHT + "  in your manifest file.", e);
        }
        L.e(" designWidth =" + mDesignWidth + " , designHeight = " + mDesignHeight);
    }


}
