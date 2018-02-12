package com.itmg.imachika.util;

import android.content.Context;
import android.util.Log;

import com.itmg.imachika.R;
import com.itmg.imachika.contans.Contans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/5/15 0015.
 * 自己对okhttp网络请求进行的进一步处理，
 * 通过实现Result接口的方法来对网络请求的数据进行进一步处理，
 * 或者更新UI
 */

public  class OkHttpUtil {
    private static OkHttpClient client = new OkHttpClient();
//    static Result result;
    //get请求
    public static void get(String path, final int code, final Result result, final Context context){
        Request request = new Request.Builder().url(path).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(Contans.TAG,e.toString());
               result.result(context.getResources().getString(R.string.error), Contans.error);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    String s = response.body().string();
                    result.result(s,code);
                }catch (Exception e){
                    Log.e("netErr",e.toString());
                }

            }
        });
    }
    //get请求 header和cookie
    public static void get(String path, final String uid , final int code, final Result result, final Context context){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        final Request original = chain.request();

                        final Request authorized = original.newBuilder()
                                .addHeader("Cookie", "uid="+uid)
                                .build();

                        return chain.proceed(authorized);
                    }
                })
                .build();
        Request request = new Request.Builder().addHeader("Platform","android").url(path).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(Contans.TAG,e.toString());
                result.result(context.getResources().getString(R.string.error), Contans.error);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    String s = response.body().string();
                    result.result(s,code);
                }catch (Exception e){
                    Log.e("netErr",e.toString());
                }

            }
        });
    }
    //需要实现的接口,解析数据更新UI等操作,该方法是运行在子线程中
    public interface Result{
        void result(String s, int code);
    }
    //post请求
    public static void post(String path,  final int code,RequestBody body,final Result result,final Context context){
        Request request = new Request.Builder().addHeader("Platform","android").url(path).post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.result(context.getResources().getString(R.string.error),Contans.error);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                result.result(s,code);
            }
        });
    }
    //post请求 携带 header和cookie
    public static void post(String path, final String uid , final int code, RequestBody body, final Result result, final Context context){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        final Request original = chain.request();

                        final Request authorized = original.newBuilder()
                                .addHeader("Cookie", "uid="+uid)
                                .build();

                        return chain.proceed(authorized);
                    }
                })
                .build();
        Request request = new Request.Builder().addHeader("Platform","android").url(path).post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.result(context.getResources().getString(R.string.error),Contans.error);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                result.result(s,code);
            }
        });
    }
}
