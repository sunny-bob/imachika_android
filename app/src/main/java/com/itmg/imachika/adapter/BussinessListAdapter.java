package com.itmg.imachika.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.itmg.imachika.R;
import com.itmg.imachika.contans.Contans;
import com.itmg.imachika.model.Shop;
import com.itmg.imachika.util.GlideUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/11/10 0010.
 */

public class BussinessListAdapter extends RecyclerView.Adapter<BussinessListAdapter.ViewHolder> {
    Context context;
    List<Shop.Data.Info> list;
   private OnItemClickListener mOnItemClickListener = null;
    public BussinessListAdapter(Context context, List<Shop.Data.Info> list) {
        this.context = context;
        this.list = list;
    }
    public static interface OnItemClickListener {
        void onItemClick(View view,int position);
    }

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null){
                    mOnItemClickListener.onItemClick(view, position);
                }
            }
        });
        Shop.Data.Info item = list.get(position);
        float rating = Double.valueOf(item.getRating()).floatValue();
//        Log.i(Contans.TAG, String.valueOf(rating));
//        Log.i(Contans.TAG,item.toString());
        holder.tvAddress.setText(item.getAddress());
        holder.tvName.setText(item.getContent() != null ? item.getContent():"");
        holder.ratingBar.setRating(rating);
        holder.tvRange.setText(item.getDistance());
        String imgUrl = item.getBig_img();
        if (imgUrl != null){// && !imgUrl.isEmpty()
            Log.i(Contans.TAG,imgUrl);
            holder.img.setVisibility(View.VISIBLE);
            GlideUtil.setImg(context,holder.img,item.getBig_img());
            holder.imgNull.setVisibility(View.GONE);
        }

        holder.imgNull.setText(item.getContent());

        if (item.getOrigin_end_time_format()!= null){
            holder.tvTime.setVisibility(View.VISIBLE);
            holder.tvTime.setText(item.getOrigin_start_time_format()+"～"+item.getOrigin_end_time_format());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.img)
        ImageView img;
        @BindView(R.id.img_null)
        TextView imgNull;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.ratingBar)
        RatingBar ratingBar;
        @BindView(R.id.tv_range)
        TextView tvRange;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        @Override
        public void onClick(View view) {
           if (mOnItemClickListener != null){
               mOnItemClickListener.onItemClick(view,getLayoutPosition());
           }
        }
    }
}
