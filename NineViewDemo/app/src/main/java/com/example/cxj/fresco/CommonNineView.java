package com.example.cxj.fresco;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by cxj on 2016/3/26.
 * 显示任何View的九宫格控件
 * 这个控件将如何测量和排列孩子的逻辑给抽取了出来,针对有些时候需要使用九宫格形式来展示的效果
 * 特别说明:此控件的包裹效果和填充父容器的效果是一样的,因为在本测量方法中并没有处理包裹的形式,也不能处理
 * 针对在listview的条目item中的时候,传入的高度的测量模式为:{@link MeasureSpec#UNSPECIFIED},此时高度就就根本孩子的个数来决定了
 * 因为不同的孩子格式,孩子的排列方式不一样
 */
public class CommonNineView extends ViewGroup {

    public CommonNineView(Context context) {
        this(context, null);
    }

    public CommonNineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonNineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 上下文对象
     */
    private Context context = null;

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        this.context = context;
    }

    /**
     * 用于保存每一个孩子的在父容器的位置
     */
    private List<RectEntity> rectEntityList = new ArrayList<RectEntity>();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //获取推荐的宽高和计算模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int childCount = getChildCount();

        if (heightMode == MeasureSpec.UNSPECIFIED) { //出现在listView的item中
            if (childCount == 1 || childCount == 3 || childCount == 4 || childCount > 6) {
                heightSize = widthSize;
            }
            if (childCount == 2) {
                heightSize = widthSize / 2;
            }
            if (childCount == 5 || childCount == 6) {
                heightSize = widthSize * 2 / 3;
            }

        }

        setMeasuredDimension(widthSize, heightSize);


        if (childCount == 1) { //一个孩子的时候
            getChildAt(0).measure(MeasureSpec.makeMeasureSpec(widthSize - 2 * intervalDistance, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(heightSize - 2 * intervalDistance, MeasureSpec.EXACTLY));
        }

        if (childCount == 2) { //两个孩子的时候
            getChildAt(0).measure(MeasureSpec.makeMeasureSpec((widthSize - 3 * intervalDistance) / 2, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(heightSize - 2 * intervalDistance, MeasureSpec.EXACTLY));
            getChildAt(1).measure(MeasureSpec.makeMeasureSpec((widthSize - 3 * intervalDistance) / 2, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(heightSize - 2 * intervalDistance, MeasureSpec.EXACTLY));
        }

        if (childCount == 3 || childCount == 4) { //三个四个孩子的时候
            for (int i = 0; i < childCount; i++) {
                getChildAt(i).measure(MeasureSpec.makeMeasureSpec((widthSize - 3 * intervalDistance) / 2, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec((heightSize - 3 * intervalDistance) / 2, MeasureSpec.EXACTLY));
            }
        }

        if (childCount == 5 || childCount == 6) { //五个六个孩子的时候
            for (int i = 0; i < childCount; i++) {
                getChildAt(i).measure(MeasureSpec.makeMeasureSpec((widthSize - 4 * intervalDistance) / 3, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec((heightSize - 3 * intervalDistance) / 2, MeasureSpec.EXACTLY));
            }
        }

        if (childCount == 7 || childCount == 8 || childCount == 9) { //七个八个九个孩子的时候
            for (int i = 0; i < childCount; i++) {
                getChildAt(i).measure(MeasureSpec.makeMeasureSpec((widthSize - 4 * intervalDistance) / 3, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec((heightSize - 4 * intervalDistance) / 3, MeasureSpec.EXACTLY));
            }
        }

        if (childCount > 9) {
            throw new RuntimeException("the chlid count can not > 9");
        }

    }

    /**
     * 安排孩子的位置
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        computeViewsLocation();
        // 循环集合中的各个菜单的位置信息,并让孩子到这个位置上
        for (int i = 0; i < getChildCount(); i++) {
            // 循环中的位置
            RectEntity e = rectEntityList.get(i);
            // 循环中的孩子
            View v = getChildAt(i);
            // 让孩子到指定的位置
            v.layout(e.leftX, e.leftY, e.rightX, e.rightY);
        }
    }


    //========================私有的方法 start===================

    /**
     * 每一行显示三个图片
     */
    private int column = 3;

    /**
     * 图片之间的间隔距离
     */
    private int intervalDistance = 4;

    /**
     * 自身的宽和高
     */
    private int mWidth;
    private int mHeight;

    /**
     * 用于计算孩子们的位置信息
     */
    private void computeViewsLocation() {
        int childCount = getChildCount();
        if (childCount == 0) {
            return;
        }
        if (childCount == rectEntityList.size()) {
            return;
        }
        rectEntityList.clear();
        //获取到宽度和高度
        mWidth = getWidth();
        mHeight = getHeight();

        switch (childCount) {
            case 1:
                oneView();
                break;
            case 2:
                twoView();
                break;
            case 3:
                threeView();
                break;
            case 4:
                fourView();
                break;
            default:
                other();
                break;
        }

    }

    /**
     * 用于计算一个孩子的时候
     */
    private void oneView() {
        RectEntity r = new RectEntity();
        r.leftX = intervalDistance;
        r.leftY = intervalDistance;
        r.rightX = r.leftX + getChildAt(0).getMeasuredWidth();
        r.rightY = r.leftY + getChildAt(0).getMeasuredHeight();
        rectEntityList.add(r);
    }

    /**
     * 两个孩子的时候
     */
    private void twoView() {
        RectEntity one = new RectEntity();
        one.leftX = intervalDistance;
        one.leftY = intervalDistance;
        one.rightX = one.leftX + getChildAt(0).getMeasuredWidth();
        one.rightY = one.leftY + getChildAt(0).getMeasuredHeight();
        rectEntityList.add(one);

        RectEntity two = new RectEntity();
        two.leftX = intervalDistance + one.rightX;
        two.leftY = intervalDistance;
        two.rightX = two.leftX + getChildAt(1).getMeasuredWidth();
        two.rightY = two.leftY + getChildAt(1).getMeasuredHeight();
        rectEntityList.add(two);

    }

    private void threeView() {
        RectEntity one = new RectEntity();
        one.leftX = intervalDistance;
        one.leftY = intervalDistance;
        one.rightX = one.leftX + getChildAt(0).getMeasuredWidth();
        one.rightY = one.leftY + getChildAt(0).getMeasuredHeight();
        rectEntityList.add(one);

        RectEntity two = new RectEntity();
        two.leftX = intervalDistance + one.rightX;
        two.leftY = intervalDistance;
        two.rightX = two.leftX + getChildAt(1).getMeasuredWidth();
        two.rightY = two.leftY + getChildAt(1).getMeasuredHeight();
        rectEntityList.add(two);

        RectEntity three = new RectEntity();
        three.leftX = intervalDistance;
        three.leftY = intervalDistance + one.rightY;
        three.rightX = three.leftX + getChildAt(2).getMeasuredWidth();
        three.rightY = three.leftY + getChildAt(2).getMeasuredHeight();
        rectEntityList.add(three);

    }

    private void fourView() {
        RectEntity one = new RectEntity();
        one.leftX = intervalDistance;
        one.leftY = intervalDistance;
        one.rightX = one.leftX + getChildAt(0).getMeasuredWidth();
        one.rightY = one.leftY + getChildAt(0).getMeasuredHeight();
        rectEntityList.add(one);

        RectEntity two = new RectEntity();
        two.leftX = intervalDistance + one.rightX;
        two.leftY = intervalDistance;
        two.rightX = two.leftX + getChildAt(1).getMeasuredWidth();
        two.rightY = two.leftY + getChildAt(1).getMeasuredHeight();
        rectEntityList.add(two);

        RectEntity three = new RectEntity();
        three.leftX = intervalDistance;
        three.leftY = intervalDistance + one.rightY;
        three.rightX = three.leftX + getChildAt(2).getMeasuredWidth();
        three.rightY = three.leftY + getChildAt(2).getMeasuredHeight();
        rectEntityList.add(three);

        RectEntity four = new RectEntity();
        four.leftX = intervalDistance + three.rightX;
        four.leftY = intervalDistance + two.rightY;
        four.rightX = four.leftX + getChildAt(3).getMeasuredWidth();
        four.rightY = four.leftY + getChildAt(3).getMeasuredHeight();
        rectEntityList.add(four);
    }

    /**
     * 大于四个孩子的时候
     */
    private void other() {

        RectEntity one = new RectEntity();
        one.leftX = intervalDistance;
        one.leftY = intervalDistance;
        one.rightX = one.leftX + getChildAt(0).getMeasuredWidth();
        one.rightY = one.leftY + getChildAt(0).getMeasuredHeight();
        rectEntityList.add(one);

        RectEntity two = new RectEntity();
        two.leftX = intervalDistance + one.rightX;
        two.leftY = intervalDistance;
        two.rightX = two.leftX + getChildAt(1).getMeasuredWidth();
        two.rightY = two.leftY + getChildAt(1).getMeasuredHeight();
        rectEntityList.add(two);

        RectEntity three = new RectEntity();
        three.leftX = intervalDistance + two.rightX;
        three.leftY = intervalDistance;
        three.rightX = three.leftX + getChildAt(2).getMeasuredWidth();
        three.rightY = three.leftY + getChildAt(2).getMeasuredHeight();
        rectEntityList.add(three);

        RectEntity four = new RectEntity();
        four.leftX = intervalDistance;
        four.leftY = intervalDistance + one.rightY;
        four.rightX = four.leftX + getChildAt(3).getMeasuredWidth();
        four.rightY = four.leftY + getChildAt(3).getMeasuredHeight();
        rectEntityList.add(four);

        RectEntity five = new RectEntity();
        five.leftX = intervalDistance + four.rightX;
        five.leftY = intervalDistance + two.rightY;
        five.rightX = five.leftX + getChildAt(4).getMeasuredWidth();
        five.rightY = five.leftY + getChildAt(4).getMeasuredHeight();
        rectEntityList.add(five);

        if (getChildCount() > 5) { //第六个孩子的位置
            RectEntity six = new RectEntity();
            six.leftX = intervalDistance + five.rightX;
            six.leftY = intervalDistance + three.rightY;
            six.rightX = six.leftX + getChildAt(5).getMeasuredWidth();
            six.rightY = six.leftY + getChildAt(5).getMeasuredHeight();
            rectEntityList.add(six);


            if (getChildCount() > 6) { //第七个孩子的位置
                RectEntity seven = new RectEntity();
                seven.leftX = intervalDistance;
                seven.leftY = intervalDistance + four.rightY;
                seven.rightX = seven.leftX + getChildAt(6).getMeasuredWidth();
                seven.rightY = seven.leftY + getChildAt(6).getMeasuredHeight();
                rectEntityList.add(seven);

                if (getChildCount() > 7) { //第八个孩子的位置
                    RectEntity eight = new RectEntity();
                    eight.leftX = intervalDistance + seven.rightX;
                    eight.leftY = intervalDistance + five.rightY;
                    eight.rightX = eight.leftX + getChildAt(7).getMeasuredWidth();
                    eight.rightY = eight.leftY + getChildAt(7).getMeasuredHeight();
                    rectEntityList.add(eight);

                    if (getChildCount() > 8) { //第八个孩子的位置
                        RectEntity nine = new RectEntity();
                        nine.leftX = intervalDistance + eight.rightX;
                        nine.leftY = intervalDistance + six.rightY;
                        nine.rightX = nine.leftX + getChildAt(8).getMeasuredWidth();
                        nine.rightY = nine.leftY + getChildAt(8).getMeasuredHeight();
                        rectEntityList.add(nine);
                    }
                }
            }
        }

    }

    //========================私有的方法 end=====================


    //========================暴露的方法 start=====================

    /**
     * 填充父容器的布局对象
     */
    private LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

    public void addClildView(View v) { //fresco SimpleDraweeView
        this.addView(v);
        requestLayout();
    }

    //========================暴露的方法 end=====================


}
