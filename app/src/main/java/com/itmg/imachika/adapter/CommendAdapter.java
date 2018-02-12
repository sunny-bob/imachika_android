package com.itmg.imachika.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.itmg.imachika.R;
import com.itmg.imachika.model.Shop;
import com.itmg.imachika.model.Recommend;

import java.util.List;

/**
 * Created by Administrator on 2017/11/10 0010.
 */

public class CommendAdapter extends BaseAdapter {
    String TAG = "CommendAdapter";
    Context context;
    List<Recommend> list;
    private MyClickListener mListener;
    public CommendAdapter(Context context, List<Recommend> list,MyClickListener listener) {
        this.context = context;
        this.list = list;
        this.mListener = listener;
    }

    public void setData( List<Recommend> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder ;
        if(null == view){
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_detail_recommend,null);
            holder.oneView = view.findViewById(R.id.item_commend_one);
            holder.twoView = view.findViewById(R.id.item_commend_two);
            holder.threeView = view.findViewById(R.id.item_commend_three);
            holder.viewClassify = view.findViewById(R.id.item_commend_classify_view);
            holder.tvClassify = view.findViewById(R.id.itme_recommend_classify);
            holder.ratingBar = view.findViewById(R.id.item_commend_rb);
            holder.ratingBar1 = view.findViewById(R.id.item_commend_rb_2);
            holder.ratingBar2 = view.findViewById(R.id.item_commend_rb_3);
            holder.ratingBarF = view.findViewById(R.id.item_commend_rb_f);
            holder.ratingBarF1 = view.findViewById(R.id.item_commend_rb_f1);
            holder.ratingBarF2 = view.findViewById(R.id.item_commend_rb_f2);
            holder.ivImg = view.findViewById(R.id.item_commend_iv);
            holder.ivImg1 = view.findViewById(R.id.item_commend_iv_2);
            holder.ivImg2 = view.findViewById(R.id.item_commend_iv_3);
            holder.tvName = view.findViewById(R.id.item_commend_name);
            holder.tvName1 = view.findViewById(R.id.item_commend_name_2);
            holder.tvName2 = view.findViewById(R.id.item_commend_name_3);
            holder.tvDes = view.findViewById(R.id.item_commend_destance);
            holder.tvDes1 = view.findViewById(R.id.item_commend_destance_2);
            holder.tvDes2 = view.findViewById(R.id.item_commend_destance_3);
            holder.tvIvName = view.findViewById(R.id.item_commend_iv_name);
            holder.tvIvName1 = view.findViewById(R.id.item_commend_iv_name_2);
            holder.tvIvName2 = view.findViewById(R.id.item_commend_iv_name_3);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        Recommend data = list.get(i);
        holder.tvClassify.setText(data.getTitleStr());
        if(data.getAdapterData().size() <= 2){
            holder.threeView.setVisibility(View.GONE);
        }
        if(data.getAdapterData().size() <= 1){
            holder.twoView.setVisibility(View.GONE);
            holder.threeView.setVisibility(View.GONE);
        }
        for (int j = 0; j < data.getAdapterData().size(); j++) {
            if(null != data.getAdapterData().get(j)){
                Shop.Data.Info detailInfo = data.getAdapterData().get(j);
                if(0 == j){
                    holder.oneView.setVisibility(View.VISIBLE);
                    if(null == detailInfo.getSmall_img() && null == detailInfo.getBig_img()){
                        holder.tvIvName.setVisibility(View.VISIBLE);
                    }else{
                        Glide.with(context).load(detailInfo.getSmall_img() != null ? detailInfo.getSmall_img()
                                : (detailInfo.getBig_img() != null ? detailInfo.getBig_img()
                                : "")).into(holder.ivImg);//加载图片
                        holder.tvIvName.setVisibility(View.GONE);
                    }
                    holder.tvIvName.setText(detailInfo.getContent());
                    Float rating = Double.valueOf(detailInfo.getRating()).floatValue();
                    if(rating >= 4){
                        holder.ratingBarF.setRating(rating);
                        holder.ratingBarF.setVisibility(View.VISIBLE);
                        holder.ratingBar.setVisibility(View.GONE);
                    }else{
                        holder.ratingBarF.setVisibility(View.GONE);
                        holder.ratingBar.setVisibility(View.VISIBLE);
                    }
                    holder.ratingBar.setRating(rating);
                    holder.tvDes.setText(detailInfo.getDistance());
                    holder.tvName.setText(detailInfo.getContent());
                }else if(1 == j){
                    holder.twoView.setVisibility(View.VISIBLE);
                    if(null == detailInfo.getSmall_img() && null == detailInfo.getBig_img()){
                        holder.tvIvName1.setVisibility(View.VISIBLE);
                    }else{
                        Glide.with(context).load(detailInfo.getSmall_img() != null ? detailInfo.getSmall_img()
                                : (detailInfo.getBig_img() != null ? detailInfo.getBig_img()
                                : "")).into(holder.ivImg1);//加载图片
                        holder.tvIvName1.setVisibility(View.GONE);
                    }
                    holder.tvIvName1.setText(detailInfo.getContent());
                    Float rating = Double.valueOf(detailInfo.getRating()).floatValue();
                    if(rating >= 4){
                        holder.ratingBarF1.setRating(rating);
                        holder.ratingBarF1.setVisibility(View.VISIBLE);
                        holder.ratingBar1.setVisibility(View.GONE);
                    }else{
                        holder.ratingBarF1.setVisibility(View.GONE);
                        holder.ratingBar1.setVisibility(View.VISIBLE);
                    }
                    holder.ratingBar1.setRating(Double.valueOf(detailInfo.getRating()).floatValue());
                    holder.tvDes1.setText(detailInfo.getDistance());
                    holder.tvName1.setText(detailInfo.getContent());
                }else if(2 == j){
                    holder.threeView.setVisibility(View.VISIBLE);
                    if(null == detailInfo.getSmall_img() && null == detailInfo.getBig_img()){
                        holder.tvIvName2.setVisibility(View.VISIBLE);
                    }else{
                        Glide.with(context).load(detailInfo.getSmall_img() != null ? detailInfo.getSmall_img()
                                : (detailInfo.getBig_img() != null ? detailInfo.getBig_img()
                                : "")).into(holder.ivImg2);//加载图片
                        holder.tvIvName2.setVisibility(View.GONE);
                    }
                    holder.tvIvName2.setText(detailInfo.getContent());
                    Float rating = Double.valueOf(detailInfo.getRating()).floatValue();
                    if(rating >= 4){
                        holder.ratingBarF2.setRating(rating);
                        holder.ratingBarF2.setVisibility(View.VISIBLE);
                        holder.ratingBar2.setVisibility(View.GONE);
                    }else{
                        holder.ratingBarF2.setVisibility(View.GONE);
                        holder.ratingBar2.setVisibility(View.VISIBLE);
                    }
                    holder.ratingBar2.setRating(rating);
                    holder.tvDes2.setText(detailInfo.getDistance());
                    holder.tvName2.setText(detailInfo.getContent());
                }
            }else{
                Log.i(TAG,"data.getAdapterData().get(j) ==== null   jjj  ====  "+ j);
            }

        }

        holder.viewClassify.setOnClickListener(mListener);
        holder.viewClassify.setTag(i);
        holder.oneView.setOnClickListener(mListener);
        holder.oneView.setTag(i);
        holder.twoView.setOnClickListener(mListener);
        holder.twoView.setTag(i);
        holder.threeView.setOnClickListener(mListener);
        holder.threeView.setTag(i);

        return view;
    }

    public static abstract class MyClickListener implements View.OnClickListener {
        /**
         * 基类的onClick方法
         */
        @Override
        public void onClick(View v) {
            myOnClick((Integer) v.getTag(), v);
        }
        public abstract void myOnClick(int position, View v);
    }

    class ViewHolder {
        RelativeLayout viewClassify;
        LinearLayout oneView;
        LinearLayout twoView;
        LinearLayout threeView;
        TextView tvClassify;
        RatingBar ratingBar;
        RatingBar ratingBarF;
        RatingBar ratingBar1;
        RatingBar ratingBarF1;
        RatingBar ratingBar2;
        RatingBar ratingBarF2;
        TextView tvName;
        TextView tvName1;
        TextView tvName2;
        TextView tvDes;
        TextView tvDes1;
        TextView tvDes2;
        TextView tvIvName;
        TextView tvIvName1;
        TextView tvIvName2;
        ImageView ivImg;
        ImageView ivImg1;
        ImageView ivImg2;
    }

}
