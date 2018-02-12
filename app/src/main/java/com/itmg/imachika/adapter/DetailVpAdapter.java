package com.itmg.imachika.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.itmg.imachika.R;
import com.itmg.imachika.ui.ImageDetailActivity;
import com.itmg.imachika.ui.ShopDetailActivity;
import com.itmg.imachika.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/25 0025.
 */

public class DetailVpAdapter extends PagerAdapter {
    List<String> mImgs = new ArrayList<>();
    Context mContext;
    int type;

    public DetailVpAdapter(Context context , List<String> imgs){
        this.mImgs = imgs;
        this.mContext = context;
    }

    public DetailVpAdapter(Context context , List<String> imgs,int type){
        this.mImgs = imgs;
        this.mContext = context;
        this.type = type;
    }
    @Override
    public int getCount() {
        //返回实际要显示的图片数+2
//        if(mImgs.size() == 1){
//            return 1;
//        }
        return mImgs.size();// + 2
//        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    // 这个方法是 销毁划出屏幕的图片 避免内存溢出
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_detail_vp, null);
        ImageView imageView = view.findViewById(R.id.item_vp_img);

        //设置网络判断
        if(null != mImgs){
            if(1 == type){
                Glide.with(mContext).load(mImgs.get(position%mImgs.size()) != null ? mImgs.get(position%mImgs.size())
                        : "").into(imageView);//加载图片
            }else{
                Glide.with(mContext).load(mImgs.get(position%mImgs.size()) != null ? mImgs.get(position%mImgs.size())
                        : "").centerCrop().into(imageView);//加载图片
            }
        }

       imageView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Log.i("DetailVpAdapter","OnClickListener type == "+type);
               ShopDetailActivity.isStop = true;
               if(1 == type){
                   if(Activity.class.isInstance(mContext))
                   {
                       // 转化为activity，然后finish就行了
                       Activity activity = (Activity)mContext;
                       activity.finish();
                   }
               }else{
                   Utils.gotoNewAct(mContext, ImageDetailActivity.class,mImgs);
               }

           }
       });

        container.addView(view);//将图片加载到容器中
        return view;
    }
}
