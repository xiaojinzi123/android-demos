package com.example.cxj.retrofitdemo;

import android.net.Network;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickView(View view) {

        Call<String> call = MyApp.netWorkService.get();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                System.out.println("result = " + result);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("请求失败");
            }
        });

    }

    /**
     * 注册
     */
    public void register(View v) {

        Call<String> call = MyApp.netWorkService.register("cxj", "123");

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //拿到返回的json数据
                String result = response.body();
                //打印结果
                System.out.println("result = " + result);
                //利用Gson转化json为实体对象
                Gson gson = new GsonBuilder().create();
                //传化后的实体对象
                Msg msg = gson.fromJson(result, Msg.class);
                //提示实体对象中的信息
                Toast.makeText(MainActivity.this, msg.getMsgText(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("注册失败");
            }
        });
    }

    public void postFile(View v) {
        //需要上传的文件
        File f = new File(Environment.getExternalStorageDirectory(), "address.db");

        //创建文件部分的请求体对象
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), f);

        //普通键值对的请求体
        RequestBody nameBody = RequestBody.create(null, "小金子");
        RequestBody passBody = RequestBody.create(null, "123");

        Call<String> call = MyApp.netWorkService.postFile(fileBody, nameBody, passBody);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                System.out.println("上传成功" + response.body() + response.code() + response.errorBody());

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("上传文件失败");
            }
        });

    }

    /**
     * 多文件上传
     *
     * @param view
     */
    public void postFiles(View view) {

        //需要上传的文件
        File f1 = new File(Environment.getExternalStorageDirectory(), "address.db");
        File f2 = new File(Environment.getExternalStorageDirectory(), "1.apk");

        //创建文件部分的请求体对象
        RequestBody fileBody1 = RequestBody.create(MediaType.parse("application/octet-stream"), f1);
        RequestBody fileBody2 = RequestBody.create(MediaType.parse("application/octet-stream"), f2);

        MultipartBody multipartBody = new MultipartBody.Builder()
                .addFormDataPart("files", f1.getName(), fileBody1)
                .addFormDataPart("files", f2.getName(), fileBody2)
                .addFormDataPart("name", "小金子")
                .addFormDataPart("pass", "123")
                .build();

        Call<String> call = MyApp.netWorkService.postFiles(multipartBody);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                System.out.println("上传成功");

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("上传多文件失败");
            }
        });

    }

}
