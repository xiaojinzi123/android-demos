package com.example.cxj.mvpdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.cxj.mvpdemo.recyclerView.CommonRecyclerViewAdapter;
import com.example.cxj.mvpdemo.recyclerView.CommonRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //下拉刷新控件
    private CommonRefreshLayout crl;

    //列表控件
    private RecyclerView rv = null;

    //显示的数据
    private List<String> data = new ArrayList<>();

    private HeaderReFresh HeaderReFresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        crl = (CommonRefreshLayout) findViewById(R.id.crl);
        rv = (RecyclerView) findViewById(R.id.rv);

        HeaderReFresh = new HeaderReFresh(findViewById(R.id.rl_refresh), crl);

        //数据造假
        for (int i = 0; i < 100; i++) {
            data.add("测试" + i);
        }

        //设置下拉刷新监听
        crl.setOnRefreshListener(HeaderReFresh);

        //初始化列表控件的布局管理器
        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setOrientation(LinearLayoutManager.VERTICAL);

        //设置布局管理器
        rv.setLayoutManager(layout);

        //设置适配器
        rv.setAdapter(new CommonRecyclerViewAdapter<String>(this, data) {

            @Override
            public void convert(CommonRecyclerViewHolder h, String entity, int position) {
                h.setText(android.R.id.text1, entity);
            }

            @Override
            public int getLayoutViewId(int viewType) {
                return android.R.layout.simple_list_item_1;
            }

        });

    }

}
