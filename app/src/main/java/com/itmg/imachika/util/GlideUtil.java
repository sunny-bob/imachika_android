package com.itmg.imachika.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by lenovo on 2017/11/13.
 */

public class GlideUtil {
    public static void setImg(Context context, ImageView iv,String url){
        Glide.with(context).load(url).fitCenter().into(iv);
    }
}
