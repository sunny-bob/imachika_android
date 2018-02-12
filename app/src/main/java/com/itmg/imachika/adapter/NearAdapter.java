package com.itmg.imachika.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.itmg.imachika.R;
import com.itmg.imachika.model.NearPerson;
import com.itmg.imachika.util.GlideUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/11/10 0010.
 */

public class NearAdapter extends RecyclerView.Adapter<NearAdapter.ViewHolder> {
    Context context;
    List<NearPerson.Data> list;
    private NearAdapter.OnItemClickListener mOnItemClickListener = null;
    public NearAdapter(Context context, List<NearPerson.Data> list) {
        this.context = context;
        this.list = list;
    }
    public static interface OnItemClickListener {
        void onItemClick(View view,int position);
    }

    public void setmOnItemClickListener(NearAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view =  LayoutInflater.from(context).inflate(R.layout.recyclerview_item, parent, false);
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
        NearPerson.Data item = list.get(position);
        String gender = item.getGender();
        if (gender == null){
            holder.imgGender.setVisibility(View.GONE);
        }else{
            switch (gender){
                case "female":
                    holder.imgGender.setImageResource(R.drawable.head_women);
                    break;
                case "male":
                    holder.imgGender.setImageResource(R.drawable.head_man);
                    break;
            }
        }

        GlideUtil.setImg(context,holder.headImg,item.getImage());
        holder.tvName.setText(item.getUser_name());
        holder.tvRange.setText(item.getDistance());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.head_img)
        ImageView headImg;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.img_gender)
        ImageView imgGender;
        @BindView(R.id.tv_range)
        TextView tvRange;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null){
                mOnItemClickListener.onItemClick(view,getLayoutPosition());
            }
        }
    }
}
