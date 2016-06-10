package com.example.cxj.retrofitdemo;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by cxj on 2016/6/10.
 * 所有的网络请求
 */
public interface NetWorkService {

    /**
     * 一个get请求的请求接口,返回是字符串
     *
     * @return
     */
    @GET("https://publicobject.com/helloworld.txt")
    public Call<String> get();

    /**
     * post提交数据
     *
     * @param name
     * @param pass
     * @return
     */
    @FormUrlEncoded
    @POST("test/register")
    public Call<String> register(@Field("name") String name, @Field("pass") String pass);

    /**
     * 测试提交一个文件和两个普通的键值对
     *
     * @param fileBody
     * @param nameBody
     * @param passBody
     * @return
     */
    @Multipart
    @POST("test/uploadFile")
    public Call<String> postFile(@Part("file\"; filename=\"avatar.db") RequestBody fileBody, @Part("name") RequestBody nameBody, @Part("pass") RequestBody passBody);

    @POST("test/uploadFiles")
    public Call<String> postFiles(@Body MultipartBody multipartBody );

}
