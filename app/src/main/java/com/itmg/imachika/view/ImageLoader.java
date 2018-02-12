package com.itmg.imachika.view;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


public class ImageLoader implements com.yuyh.library.imgsel.ImageLoader {

    @Override
    public void displayImage(Context context, String path, ImageView imageView) {
        Glide.with(context).load(path).into(imageView);
    }
}
