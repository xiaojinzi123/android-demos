package com.example.cxj.mvpdemo;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by cxj on 2016/11/22.
 * 抽取出下拉刷新更新刷新视图ui的代码
 */
public class HeaderReFresh implements CommonRefreshLayout.OnRefreshListener {

    private ImageView iv = null;

    private TextView tv = null;

    private ProgressBar pb;

    private CommonRefreshLayout crl;

    private View contentView;

    public HeaderReFresh(View contentView, CommonRefreshLayout crl) {
        this.contentView = contentView;
        this.crl = crl;
        iv = (ImageView) contentView.findViewById(R.id.iv);
        tv = (TextView) contentView.findViewById(R.id.tv_tip);
        pb = (ProgressBar) contentView.findViewById(R.id.pb);
    }

    private Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //设置刷新控件完成刷新
            crl.setOnRefreshComplete();
            //还原ui
            pb.setVisibility(View.GONE);
            iv.setVisibility(View.VISIBLE);
            iv.setRotation(0);
        }
    };

    @Override
    public void onPullPercentage(float percent) {
        if (percent < 0) {
            percent = 0f;
        }
        if (percent > 1) {
            percent = 1f;
        }
        if (percent > 0.7f) {
            percent = (percent - 0.7f) * 10 / 3;
            iv.setRotation(180 * percent);
        }else{
            iv.setRotation(0);
        }

    }

    @Override
    public void onHeaderRefresh() {
        tv.setText("正在刷新.......");
        iv.setVisibility(View.INVISIBLE);
        pb.setVisibility(View.VISIBLE);
        //模拟加载数据,延迟0.5秒发送消息
        h.sendEmptyMessageDelayed(0, 500);
    }

    @Override
    public void onHeaderPrepareRefresh() {
        tv.setText("释放立即刷新");
    }

    @Override
    public void onHeaderCamcelPrepareRefresh() {
        tv.setText("下拉刷新");
    }

}
