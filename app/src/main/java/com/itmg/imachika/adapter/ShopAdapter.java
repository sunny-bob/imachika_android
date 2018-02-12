package com.itmg.imachika.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.itmg.imachika.R;
import com.itmg.imachika.model.Shop;
import com.itmg.imachika.ui.ShopDetailActivity;
import com.itmg.imachika.util.GlideUtil;

import java.util.List;


/**
 * Created by Administrator on 2017/11/10 0010.
 */

public class ShopAdapter extends BaseAdapter {
    String TAG = "ShopAdapter";
    Context context;
    List<Shop.Data.Info> list;
//    private LruCache<String, BitmapDrawable> mImageCache;
    private OnItemClickListener mOnItemClickListener = null;
    public ShopAdapter(Context context, List<Shop.Data.Info> list) {
        this.context = context;
        this.list = list;

//        int maxCache = (int) Runtime.getRuntime().maxMemory();
//        int cacheSize = maxCache / 8;
//        mImageCache = new LruCache<String, BitmapDrawable>(cacheSize) {
//            @Override
//            protected int sizeOf(String key, BitmapDrawable value) {
//                return value.getBitmap().getByteCount();
//            }
//        };
    }

    public void setData( List<Shop.Data.Info> list){
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
            view = LayoutInflater.from(context).inflate(R.layout.list_item,null);
            holder.img = view.findViewById(R.id.img);
            holder.imgNull = view.findViewById(R.id.img_null);
            holder.tvName = view.findViewById(R.id.tv_name);
            holder.tvAddress = view.findViewById(R.id.tv_address);
            holder.tvTime = view.findViewById(R.id.tv_time);
            holder.ratingBar = view.findViewById(R.id.ratingBar);
            holder.ratingBarFull = view.findViewById(R.id.ratingBarFull);
            holder.tvRange = view.findViewById(R.id.tv_range);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        final Shop.Data.Info item = list.get(i);
        float rating = Double.valueOf(item.getRating()).floatValue();
        holder.tvAddress.setText(item.getAddress());
        holder.tvName.setText(item.getContent());
        if(rating >= 4){
            holder.ratingBarFull.setRating(rating);
            holder.ratingBarFull.setVisibility(View.VISIBLE);
            holder.ratingBar.setVisibility(View.GONE);
        }else{
            holder.ratingBarFull.setVisibility(View.GONE);
            holder.ratingBar.setVisibility(View.VISIBLE);
        }
        holder.ratingBar.setRating(rating);
        holder.tvRange.setText(item.getDistance());

        if (null != item.getSmall_img() || null != item.getBig_img()){// && !imgUrl.isEmpty()
            Log.i(TAG," item.getBig_img() != null i == " + i + "---- item.getBig_img() === "+item.getBig_img());
            holder.img.setVisibility(View.VISIBLE);
            GlideUtil.setImg(context,holder.img,item.getSmall_img() != null ? item.getSmall_img() :
                    item.getBig_img() != null ? item.getBig_img() : "");
            holder.imgNull.setVisibility(View.GONE);
        }else{
            holder.img.setVisibility(View.GONE);
            holder.imgNull.setVisibility(View.VISIBLE);
            Log.i(TAG," item.getBig_img() == null i == " +i);
            if(0 == i%3){
                holder.imgNull.setBackgroundColor(context.getResources().getColor(R.color.weChat_bg));
            }else if(1 == i%3){
                holder.imgNull.setBackgroundColor(context.getResources().getColor(R.color.yellow));
            }else{
                holder.imgNull.setBackgroundColor(context.getResources().getColor(R.color.blue));
            }
        }

        holder.imgNull.setText(item.getContent());
        if (item.getOrigin_end_time_format()!= null){
            holder.tvTime.setVisibility(View.VISIBLE);
            holder.tvTime.setText(item.getOrigin_start_time_format()+" ～ "+item.getOrigin_end_time_format());
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(context, ShopDetailActivity.class);
                intent.putExtra("data", item);
                context.startActivity(intent);
            }
        });
        return view;
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    class ViewHolder {
//        @BindView(R.id.img)
        ImageView img;
//        @BindView(R.id.img_null)
        TextView imgNull;
//        @BindView(R.id.tv_name)
        TextView tvName;
//        @BindView(R.id.tv_address)
        TextView tvAddress;
//        @BindView(R.id.tv_time)
        TextView tvTime;
//        @BindView(R.id.ratingBar)
        RatingBar ratingBar;
        RatingBar ratingBarFull;
//        @BindView(R.id.tv_range)
        TextView tvRange;

    }

}
