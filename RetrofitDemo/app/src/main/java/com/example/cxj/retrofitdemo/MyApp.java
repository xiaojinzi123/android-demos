package com.example.cxj.retrofitdemo;

import android.app.Application;


import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


/**
 * Created by cxj on 2016/6/10.
 */
public class MyApp extends Application {

    /**
     * 声明请求的接口
     */
    public static NetWorkService netWorkService;

    /**
     * 网络请求框架
     */
    public static Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.102:8080/Test/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        //让框架自动实现我们的请求接口,让我们的请求接口可以被调用
        netWorkService = retrofit.create(NetWorkService.class);

    }
}
