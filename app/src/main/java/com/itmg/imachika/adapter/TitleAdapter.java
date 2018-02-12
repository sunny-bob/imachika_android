package com.itmg.imachika.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.itmg.imachika.R;
import com.itmg.imachika.model.Classifal;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/11/10 0010.
 */

public class TitleAdapter extends RecyclerView.Adapter<TitleAdapter.ViewHolder>{
    private String TAG = "TitleAdapter";
    Context context;
    List<Classifal> list;
    private int mPosition = -1;
    private OnItemClickListener mListener; // Item点击事件

    public TitleAdapter(Context context, List<Classifal> list) {
        this.context = context;
        this.list = list;
    }

    public void setPosition(int position) {
        mPosition = position;
//        Log.i(TAG," setPosition  ----  position   === "+position+"   mPosition === " + mPosition);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.title_item, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mListener = mOnItemClickListener;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Classifal item = list.get(position);
        holder.tvTitle.setText(item.getName());
//        Log.i(TAG,"position   === "+position+"   mPosition === " + mPosition);
        if (mPosition == position){
            holder.tvTitle.setTextColor(context.getResources().getColor(R.color.title_text_select));
            holder.line.setVisibility(View.VISIBLE);
        } else {
            holder.tvTitle.setTextColor(context.getResources().getColor(R.color.title_text));
            holder.line.setVisibility(View.INVISIBLE);
        }

        //判断是否设置了监听器
        if(mListener != null){
            //为ItemView设置监听器
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition(); // 1
                    mListener.onItemClick(holder.itemView,position); // 2
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.title_line)
        View line;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
}
