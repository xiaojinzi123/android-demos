package com.yoursecondworld.recyclerviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.yoursecondworld.recyclerviewdemo.entity.DemoEntity;

import java.util.ArrayList;
import java.util.List;

import xiaojinzi.base.android.adapter.recyclerView.CommonRecyclerViewAdapter;
import xiaojinzi.base.android.adapter.recyclerView.CommonRecyclerViewHolder;

/**
 * 多布局的demo
 */
public class MultiLayoutActivity extends AppCompatActivity {

    //展示数据的列表
    private RecyclerView rv = null;

    //需要展示的数据
    private List<DemoEntity> data = new ArrayList<DemoEntity>();

    private CommonRecyclerViewAdapter<DemoEntity> adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //展示的数据造假
        data.add(new DemoEntity("A", null));
        data.add(new DemoEntity(null, "阿大"));
        data.add(new DemoEntity(null, "阿姨1"));
        data.add(new DemoEntity(null, "阿姨2"));
        data.add(new DemoEntity(null, "阿姨3"));
        data.add(new DemoEntity(null, "阿姨4"));
        data.add(new DemoEntity("C", null));
        data.add(new DemoEntity(null, "陈旭金1"));
        data.add(new DemoEntity(null, "陈旭金2"));
        data.add(new DemoEntity(null, "陈旭金3"));
        data.add(new DemoEntity(null, "陈旭金4"));

        //寻找控件
        rv = (RecyclerView) findViewById(R.id.rv);

        //创建一个线性的布局管理器并设置
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);

        adapter = new CommonRecyclerViewAdapter<DemoEntity>(this, data) {

            @Override
            public void convert(CommonRecyclerViewHolder h, DemoEntity entity, int position) {
                int itemViewType = getItemType(position);
                if (itemViewType == 1) {
                    h.setText(R.id.tv_tag, entity.getTag());
                } else {
                    h.setText(R.id.tv_name, entity.getName());
                }
            }

            //返回item布局的id
            @Override
            public int getLayoutViewId(int viewType) {
                if (viewType == 1) {
                    return R.layout.tag;
                } else {
                    return R.layout.item;
                }
            }

            //默认是返回0,所以你可以定义返回1表示使用tag,2表示使用item,
            //这里返回的值将在getLayoutViewId方法中出现
            @Override
            public int getItemType(int position) {
                //根据实体对象中的属性来返回view的类型
                DemoEntity demoEntity = data.get(position);
                if (demoEntity.getTag() != null) { //如果是tag,应该返回1
                    return 1;
                } else {
                    return 2;
                }
            }
        };

        adapter.addHeaderView(View.inflate(this, R.layout.header, null));

        //设置适配器
        rv.setAdapter(adapter);

//        //全部的item都起作用
//        adapter.setOnRecyclerViewItemClickListener(new CommonRecyclerViewAdapter.OnRecyclerViewItemClickListener() {
//            @Override
//            public void onItemClick(View v, int position) {
//                Toast.makeText(MultiLayoutActivity.this, "你点击了第" + position + "个item", Toast.LENGTH_SHORT).show();
//            }
//        });

        //只针对显示name的Item
        adapter.setOnRecyclerViewItemClickListener(new CommonRecyclerViewAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Toast.makeText(MultiLayoutActivity.this, "你点击了第" + position + "个item", Toast.LENGTH_SHORT).show();
            }
        }, 2);

        //添加item中控件监听
        adapter.setOnViewInItemClickListener(new CommonRecyclerViewAdapter.OnViewInItemClickListener() {
            @Override
            public void onViewInItemClick(View v, int position) {
                DemoEntity demoEntity = data.get(position);
                Toast.makeText(MultiLayoutActivity.this, "你点击了第" + position + "个item,name = " + demoEntity.getName(), Toast.LENGTH_SHORT).show();
            }
        }, R.id.bt);

    }

}
