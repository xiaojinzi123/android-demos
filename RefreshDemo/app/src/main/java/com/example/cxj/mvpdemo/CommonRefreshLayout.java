package com.example.cxj.mvpdemo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;


import static android.animation.ObjectAnimator.*;
import static android.animation.ValueAnimator.ofInt;

/**
 * Created by cxj on 2016/8/21.
 * 这是一个通用的头部刷新自定义控件
 * 必须有两个孩子!
 * 第一个孩子作为刷新的头部,头部里面的控件完全自己放置
 * 第二个孩子是你自己的界面
 */
public class CommonRefreshLayout extends ViewGroup {

    /**
     * 菜单没有完全显示
     */
    public static final int STATE_MENU_UNSHOW = 0;

    /**
     * 菜单完全显示
     */
    public static final int STATE_MENU_SHOWED = 1;


    /**
     * 不在刷新状态
     */
    public static final int STATE_MENU_REFRESH_NORMAL = 11;

    /**
     * 正在刷新
     */
    public static final int STATE_MENU_REFRESH_NOW = 12;

    public CommonRefreshLayout(Context context) {
        this(context, null);
    }

    public CommonRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mDragger = ViewDragHelper.create(this, callback);
    }

    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            if (child == getChildAt(1)) {
                return true;
            }
            return false;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            //返回0表示被拖动的View不进行滑动,呆在原地
            return 0;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {

            if (dy > 0) {
                int mScrollY = Math.abs(getScrollY());
                //计算滑动出来的高度和头部的高度的比值
                float percent = ((Number) mScrollY).floatValue() / (headerMenuHeight * 2);
                dy = (int) ((1 - (percent)) * dy);
            }

            //这一段是为了防止上下拖拽的时候,把列表往上拽了,所以有一个判断,如果成立我们就滚动回开始的位置(刷新视图看不见,列表滑动到最上面的状态)
            if (getScrollY() - dy > 0) {
                //执行滑动
                scrollTo(0, 0);
            } else {
                //执行滑动
                scrollBy(0, -dy);
            }

            float pullPercent = 1f;

            //如果菜单没有完全滑动出来
            if (getScrollY() > -headerMenuHeight) {
                //计算拉出来的百分比
                pullPercent = ((Number) Math.abs(getScrollY())).floatValue() / ((Number) Math.abs(headerMenuHeight)).floatValue();
                //如果之前是菜单整个显示的状态,切换为菜单不显示的状态,并通知监听者
                if (currentHeaderMenuState == STATE_MENU_SHOWED) {
                    currentHeaderMenuState = STATE_MENU_UNSHOW;
                    if (onRefreshListener != null) {
                        onRefreshListener.onHeaderCamcelPrepareRefresh();
                    }
                }
            } else { //菜单整个出来了
                //计算百分比
                pullPercent = 1f;
                //如果之前是菜单不显示的状态,切换为菜单显示的状态,并通知监听者
                if (currentHeaderMenuState == STATE_MENU_UNSHOW) {
                    currentHeaderMenuState = STATE_MENU_SHOWED;
                    if (onRefreshListener != null) {
                        onRefreshListener.onHeaderPrepareRefresh();
                    }
                }
            }

            if (onRefreshListener != null) {
                onRefreshListener.onPullPercentage(pullPercent);
            }

            //返回0表示被拖动的View不进行滑动,呆在原地
            return 0;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            //声明目标y
            int toValue;

            //如果没有滑出整个菜单,那么还原状态
            if (getScrollY() > -headerMenuHeight) {
                toValue = 0;
                currentHeaderMenuState = STATE_MENU_UNSHOW;
                currentHeaderMenuRefreshState = STATE_MENU_REFRESH_NORMAL;
            } else {
                //如果释放的时候整个菜单都滑动出来了,那么目标y就是菜单的top的位置
                toValue = -headerMenuHeight;
                currentHeaderMenuState = STATE_MENU_SHOWED;
                //记录正在刷新
                currentHeaderMenuRefreshState = STATE_MENU_REFRESH_NOW;
                //通知监听者
                if (onRefreshListener != null) {
                    onRefreshListener.onHeaderRefresh();
                }
            }
            smothTo(toValue);
        }

    };

    /**
     * 滑动的工具类
     */
    private ViewDragHelper mDragger;

    /**
     * 当前的头部菜单的状态
     * 0：菜单没有滑动出来
     * 1：菜单滑动出来了
     */
    private int currentHeaderMenuState = STATE_MENU_UNSHOW;

    /**
     * 当前的头部是不是在状态的状态记录
     */
    private int currentHeaderMenuRefreshState = STATE_MENU_REFRESH_NORMAL;

    /**
     * 列表的高度和刷新的View的高度比
     */
    private int headerRatio = 4;


    //按下的时候的坐标点
    private float downX;
    private float downY;

    /**
     * 是否拦截事件
     */
    private boolean isInterceptTouchEvent = true;

    public void setInterceptTouchEvent(boolean interceptTouchEvent) {
        isInterceptTouchEvent = interceptTouchEvent;
    }

    /**
     * 拦截子类事件,在按下的时候记录下坐标点
     * 移动的时候判断当前点的y是否比按下的时候的y大
     * 如果大了并且标识符isInterceptTouchEvent为true,就表示拦截事件,下拉出我们的菜单
     *
     * @param event
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        //如果正在刷新,那就什么都别说了,直接拦截,
        if (currentHeaderMenuRefreshState == STATE_MENU_REFRESH_NOW) {
            return true;
        }

        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) { //按下的时候记录下位置
            downY = event.getY();
            downX = event.getX();
        } else if (action == MotionEvent.ACTION_MOVE) {
            View view = getChildAt(1);
            //如果当前的y是大于按下时候的y,说明是在向下拉
            if (isInterceptTouchEvent && event.getY() > downY && !canChildScrollUp(view)) {
                //改成按下的事件
                event.setAction(MotionEvent.ACTION_DOWN);
                //传递给onTouchevent
                onTouchEvent(event);
                event.setAction(MotionEvent.ACTION_MOVE);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (currentHeaderMenuRefreshState == STATE_MENU_REFRESH_NOW) {
            return false;
        }
        mDragger.processTouchEvent(event);
        return true;
    }

    /**
     * 头部菜单的高度
     */
    private int headerMenuHeight;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //获取孩子个数
        int childCount = getChildCount();
        if (childCount != 2) {
            throw new RuntimeException("the child count must be 2");
        }

        //拿到推荐的宽和高
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //父容器给我多少我就要多少,不能放在列表中使用,因为列表中推荐过来的值heightSize为0
        setMeasuredDimension(widthSize, heightSize);

        //推荐给第二个孩子的宽和高,和自身宽高一样
        View mainView = getChildAt(1);
        int widthSpec = MeasureSpec.makeMeasureSpec(widthSize - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        mainView.measure(widthSpec, heightSpec);

        //推荐给第一个孩子的宽和高,宽度是自身的4分之一
        View menuView = getChildAt(0);
        headerMenuHeight = (heightSize) / headerRatio;
        heightSpec = MeasureSpec.makeMeasureSpec(headerMenuHeight, MeasureSpec.AT_MOST);
        menuView.measure(widthSpec, heightSpec);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //刷新控件View
        View reFreshView = getChildAt(0);
        int measuredWidth = reFreshView.getMeasuredWidth();
        int measuredHeight = reFreshView.getMeasuredHeight();
        //记录下刷新控件的高度，后续有用
        headerMenuHeight = measuredHeight;
        reFreshView.layout(0 + getPaddingLeft(), 0 - measuredHeight, measuredWidth, 0);

        View mainView = getChildAt(1);
        measuredWidth = mainView.getMeasuredWidth();
        measuredHeight = mainView.getMeasuredHeight();
        mainView.layout(0 + getPaddingLeft(), 0, measuredWidth, measuredHeight);
    }

    /**
     * 设置刷新完成
     */
    public void setOnRefreshComplete() {
        currentHeaderMenuRefreshState = STATE_MENU_REFRESH_NORMAL;
        smothTo(0);
    }

    /**
     * 判断这个View是不是可以向上滑动
     *
     * @param mTarget
     * @return
     */
    public boolean canChildScrollUp(View mTarget) {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTarget instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTarget;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(mTarget, -1) || mTarget.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mTarget, -1);
        }
    }

    /**
     * 平滑的滚动到某个位置,这里针对竖直方向
     *
     * @param toValue 目标y
     */
    private void smothTo(int toValue) {
        ValueAnimator objectAnimator = //
                ofInt(getScrollY(), toValue)//
                        .setDuration(300);
        //设置更新数据的监听
        objectAnimator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                scrollTo(0, value);
            }
        });

        objectAnimator.start();
    }


    /**
     * 刷新的监听
     */
    public interface OnRefreshListener {

        /**
         * 拉拽的时候,菜单拉出来的部分占用整个菜单高度的百分比
         *
         * @param percent
         */
        void onPullPercentage(float percent);

        /**
         * 头部刷新啦
         */
        void onHeaderRefresh();

        /**
         * 准备刷新,在菜单整个滑动出来被调用
         */
        void onHeaderPrepareRefresh();

        /**
         * 取消准备刷新,在菜单整个滑动出来之后,又滑动回去了
         */
        void onHeaderCamcelPrepareRefresh();

    }

    private OnRefreshListener onRefreshListener;

    /**
     * 设置监听
     *
     * @param onRefreshListener
     */
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }
}
