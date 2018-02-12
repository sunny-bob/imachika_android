package com.itmg.imachika.application;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Base64;
import android.util.Log;

import com.itmg.imachika.contans.Contans;
import com.itmg.imachika.util.URLInfo;

import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import io.socket.client.IO;
import io.socket.engineio.client.transports.WebSocket;

/**
 * Created by lenovo on 2017/11/15.
 */

public class APP extends Application{
    // 创建集合，存放页面属性
    private static List<Activity> list = new LinkedList<Activity>();
    // 用静态变量保证MyApplication对象在这个程序中只存在一个
    private static APP app;
    public static int languageType = -1;
    //得到MyApplication对象的方法，并返回MyApplication对象
    public APP(){
    }
    private io.socket.client.Socket mSocket;
    {
        try {
            IO.Options options =new IO.Options();
            try {
                HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        Log.i(Contans.TAG, "Verifying host name ::: " + hostname);
                        return true;
                    }
                };
                options.hostnameVerifier = hostnameVerifier;
                options.sslContext = SSLContext.getDefault();
                options.secure = true;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            options.reconnection = false;
            options.secure = true;
            options.transports = new String [] {WebSocket.NAME,"polling"};
            mSocket = IO.socket(URLInfo.chat,options);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    public static Activity getTopActivity() {
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(list.size() - 1);
        }
    }
    public io.socket.client.Socket getSocket() {
        return mSocket;
    }
    public static APP getMyApplication(){
        if(app == null){
            app = new APP();//创建MyApplication对象
        }
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * 获取Activity数量
     */
    public int getActivityNum(){
        return list.size();
    }

//    public String getKey() {
//        String key = "";
//        try { //facebook获取测试包的SHA秘钥
//            PackageInfo info = getApplicationContext().getPackageManager().getPackageInfo("com.itmg.imachika", PackageManager.GET_SIGNATURES);
//            for (android.content.pm.Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                key = Base64.encodeToString(md.digest(), Base64.DEFAULT);
//                Log.i("APP", "KeyHash:" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }
//        return key;
//    }

    /**
     * 结束指定的Activity
     */
    public static void finishActivity(int i){
//        if(activity!=null){
        list.remove(list.get(i));
        list.get(i).finish();
//        }
    }
    public static void addActivity(Activity activity){
        Log.i("APP","activity == "+activity.getClass().getName()
                + " list.size() === " + list.size());
        list.add(activity);
//        if(list.size() >= 10){
//            finishActivity(2);
//        }
    }

    public static void exit(){
        for (Activity activity:list){
            activity.finish();
        }
        System.exit(0);
    }
}
