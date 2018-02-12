package com.itmg.imachika.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.itmg.imachika.R;
import com.itmg.imachika.adapter.DetailVpAdapter;
import com.itmg.imachika.application.APP;
import com.itmg.imachika.util.Utils;
import com.itmg.imachika.view.WrapContentHeightViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageDetailActivity extends Activity{
    @BindView(R.id.detail_img_vp)
    WrapContentHeightViewPager mViewPager;
    @BindView(R.id.detail_banner_point)
    LinearLayout mViewPagerPoint;

    private static final String TAG = "ImageDetailActivity";
    private APP app = APP.getMyApplication();
    private Intent intent;
    List<String> mImgs = new ArrayList<>();

    //设置当前 第几个图片 被选中

    private ImageView[] mBottomImages;//底部只是当前页面的小圆点

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        ButterKnife.bind(this);
        app.addActivity(this);

        intent = getIntent();
        mImgs = intent.getStringArrayListExtra("data");
        if(null != mImgs && mImgs.size() > 0){//图片轮播
            DetailVpAdapter adapter = new DetailVpAdapter(this ,mImgs,1);
            mViewPager.setAdapter(adapter);
            if(mImgs.size() > 1){
                setViewPagerPoint();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setViewPagerPoint() {
        mViewPagerPoint.removeAllViews();
        mBottomImages = new ImageView[mImgs.size()];
        for (int i = 0; i < mBottomImages.length; i++) {
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Utils.dip2px(this, 5), Utils.dip2px(this, 5));
            params.setMargins(Utils.dip2px(this, 5), Utils.dip2px(this, 5), Utils.dip2px(this, 5), Utils.dip2px(this, 5));
            imageView.setLayoutParams(params);
            if (i == 0) {
                imageView.setBackgroundResource(R.drawable.orange_point);
            } else {
                imageView.setBackgroundResource(R.drawable.gray_point);
            }

            mBottomImages[i] = imageView;
            //把指示作用的原点图片加入底部的视图中
            mViewPagerPoint.addView(mBottomImages[i]);
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                int total = mBottomImages.length;
                for (int j = 0; j < total; j++) {
                    if (j == position) {
                        mBottomImages[j].setBackgroundResource(R.drawable.orange_point);
                    } else {
                        mBottomImages[j].setBackgroundResource(R.drawable.gray_point);
                    }
                }
                //设置全局变量，currentIndex为选中图标的 index
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

}
