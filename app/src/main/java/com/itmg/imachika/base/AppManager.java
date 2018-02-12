package com.itmg.imachika.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述： activity堆栈式管理
 */
public class AppManager {

    public static List<Activity> list=new ArrayList<>();
    private static AppManager instance;

    public static AppManager getInstance() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        list.add(activity);
    }
    /**
     * 结束指定的Activity
     */
    public void finishActivity(int i){
//        if(activity!=null){
        list.remove(list.get(i));
        list.get(i).finish();
//        }
    }
    //将activity移除容器
    public void removeActivity(Class<?> cls){
        for (int i=0;i<list.size();i++){
            if (list.get(i).getClass().equals(cls)){
                list.get(i).finish();
                list.remove(list.get(i));
            }
        }

    }

    // 遍历所有Activity并finish
    public void exit() {
        for (Activity activity : list) {
            if (activity!=null)
            activity.finish();
        }
        list.clear();
        System.exit(0);
    }


}
