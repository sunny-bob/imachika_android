package com.itmg.imachika.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/11/9 0009.
 */

public class Mytoast {
    public static void show(Context context,String s){
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }
    public static void show(Context context,int resId){
        Toast.makeText(context,context.getResources().getString(resId),Toast.LENGTH_SHORT).show();
    }
}
