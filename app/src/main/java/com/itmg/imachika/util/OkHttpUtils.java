package com.itmg.imachika.util;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtils {
    private static OkHttpUtils okHttpUtils;
    private OkHttpClient client;
    private Handler handler;

    private OkHttpUtils(){
        client = new OkHttpClient.Builder().connectTimeout(25, TimeUnit.SECONDS)
                .writeTimeout(25, TimeUnit.SECONDS)
                .readTimeout(25, TimeUnit.SECONDS).build();
        handler=new Handler(Looper.getMainLooper());
    }
    public static OkHttpUtils getInstance(){
        if (okHttpUtils==null){
            okHttpUtils= new OkHttpUtils();
        }
        return okHttpUtils;
    }

    //封装普通post请求  携带 header和cookie
    public void okHttpPost(String url, final String uid, Map<String,String> params, final RequestCallBack callBack){
        FormBody.Builder builder= new FormBody.Builder();
        if (params != null) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
        String data = GsonUtil.mapToJson(params);
        builder.add("data",data).build();
        RequestBody body = builder.build();
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
        Request request = new Request.Builder().addHeader("Platform","android").url(url).post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                         callBack.onFail();
                    }
                });
            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String result=response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(result);
                    }
                });
            }
        });
    }

    //封装普通post请求
    public void okHttpPost(String url, Map<String,String> params, final RequestCallBack callBack){
        FormBody.Builder builder= new FormBody.Builder();
        if (params != null) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
        String data = GsonUtil.mapToJson(params);
        builder.add("data",data).build();
        RequestBody body = builder.build();
        Request request = new Request.Builder().addHeader("Platform","android").url(url).post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFail();
                    }
                });
            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String result=response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(result);
                    }
                });
            }
        });
    }

    //post请求传json字符串
//    public void okHttpPostJson(String url, String json, final RequestCallBack callBack) {
//
//        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
//        Request request = new Request.Builder().url(url).post(body).build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        callBack.onFail();
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, final Response response) throws IOException {
//                final String result = response.body().string();
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        callBack.onSuccess(result);
//                    }
//                });
//            }
//        });
//    }

    //封装普通get请求 携带 header和cookie
    public void okHttpGet(String url, final String uid, Map<String,String> params, String versionName, final RequestCallBack callBack){
        FormBody.Builder builder= new FormBody.Builder();
        if (params != null) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }

        Request.Builder ReqBuilder = new Request.Builder().url(attachHttpGetParams(url,params));
        ReqBuilder.addHeader("platform","android");  //将请求头以键值对形式添加，可添加多个请求头
        ReqBuilder.addHeader("appVersion", versionName);
        ReqBuilder.addHeader("uuid","12341235");

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
        Request request = ReqBuilder.get().build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFail();
                    }
                });
            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String result = response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(result);
                    }
                });
            }
        });
    }

    //封装普通get请求
    public void okHttpGet(String url, Map<String,String> params,String versionName, final RequestCallBack callBack){
        FormBody.Builder builder= new FormBody.Builder();
        if (params != null) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }

        Request.Builder ReqBuilder = new Request.Builder().url(attachHttpGetParams(url,params));
        ReqBuilder.addHeader("platform","android");  //将请求头以键值对形式添加，可添加多个请求头
        ReqBuilder.addHeader("appVersion", versionName);
        ReqBuilder.addHeader("uuid","12341235");

        Request request = ReqBuilder.get().build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFail();
                    }
                });
            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String result = response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(result);
                    }
                });
            }
        });
    }

    public static String attachHttpGetParams(String url, Map<String,String> params){

        Iterator<String> keys = params.keySet().iterator();
        Iterator<String> values = params.values().iterator();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("?");

        for (int i=0;i<params.size();i++ ) {
            String value=null;
            try {
                value= URLEncoder.encode(values.next(),"utf-8");
            }catch (Exception e){
                e.printStackTrace();
            }

            stringBuffer.append(keys.next()+"="+value);
            if (i!=params.size()-1) {
                stringBuffer.append("&");
            }
            Log.i("stringBuffer","stringBuffer === "+stringBuffer.toString());
        }

        return url + stringBuffer.toString();
    }

    //上传文件和参数
//    public void okHttpPostAndFile(String url, Map<String, String> params, List<String> paths, final RequestCallBack callBack) {
//        MultipartBody.Builder builder=new MultipartBody.Builder();
//        builder.setType(MultipartBody.FORM);
//        if(null != params){
//            for (String key:params.keySet()){
//                builder.addFormDataPart(key,params.get(key));
//            }
//        }
//        File file = null;
//        for (String string : paths) {                                      //MediaType.parse("application/octet-stream")
//            file = BitmapUtils.compress(string);
//            builder.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
//        }
//        RequestBody body=builder.build();
//        Request request=new Request.Builder().url(url).post(body).build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        callBack.onFail();
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, final Response response) throws IOException {
//                final String result=response.body().string();
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        callBack.onSuccess(result);
//                    }
//                });
//            }
//        });
//    }

    //上传文件和参数
//    public void okHttpPostAndFile(String url, Map<String, String> params, List<String> imgPath, List<String> paths, final RequestCallBack callBack) {
//        MultipartBody.Builder builder = new MultipartBody.Builder();
//        builder.setType(MultipartBody.FORM);
//        for (String key : params.keySet()) {
//            builder.addFormDataPart(key, params.get(key));
//        }
//        File photoFile = null;
//        for (String string : imgPath) {                                      //MediaType.parse("application/octet-stream")
//            photoFile = BitmapUtils.compress(string);
//            builder.addFormDataPart("file", photoFile.getName(), RequestBody.create(MediaType.parse("image/png"), photoFile));
//        }
//        File file = null;
//        for (String string : paths) {                                      //MediaType.parse("application/octet-stream")
//            file = BitmapUtils.compress(string);
//            builder.addFormDataPart("fileList", file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
//        }
//        RequestBody body = builder.build();
//        Request request = new Request.Builder().url(url).post(body).build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        callBack.onFail();
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, final Response response) throws IOException {
//                final String result = response.body().string();
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        callBack.onSuccess(result);
//                    }
//                });
//            }
//        });
//    }

    //拿到输入流(PDF)
//    public void okHttpGetInputStream(String url, final RequestStreamCallBack callBack) {
//        Request request = new Request.Builder().url(url).get().build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        callBack.onFail();
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                final InputStream is = response.body().byteStream();
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        callBack.onSuccess(is);
//                    }
//                });
//            }
//        });
//    }

    //下载文件(更新APP)
//    public void okHttpUpdateAPP(String url, Map<String, String> params, final File filePath, final RequestDownloadCallBack callBack) {
//        MultipartBody.Builder builder = new MultipartBody.Builder();
//        builder.setType(MultipartBody.FORM);
//        for (String key : params.keySet()) {
//            builder.addFormDataPart(key, params.get(key));
//        }
//        RequestBody body = builder.build();
//        Request request = new Request.Builder().url(url).post(body).build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                callBack.onDownloadFailed();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                InputStream is = null;
//                byte[] buffer = new byte[1024];
//                int len = 0;
//                FileOutputStream fos = null;
//                try {
//                    is = response.body().byteStream();
//                    long total = response.body().contentLength();
//                    fos = new FileOutputStream(filePath);
//                    long sum = 0;
//                    while ((len = is.read(buffer)) != -1) {
//                        fos.write(buffer, 0, len);
//                        sum += len;
//                        final int progress = (int) (sum * 1.0f / total * 100);
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                callBack.onDownloading(progress);
//                            }
//                        });
//                    }
//                    fos.flush();
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            callBack.onDownloadSuccess();
//                        }
//                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    callBack.onDownloadFailed();
//                } finally {
//                    try {
//                        if (is != null)
//                            is.close();
//                    } catch (IOException e) {
//                    }
//                    try {
//                        if (fos != null)
//                            fos.close();
//                    } catch (IOException e) {
//                    }
//                }
//            }
//        });
//    }
}
