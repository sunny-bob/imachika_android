package com.itmg.imachika.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.itmg.imachika.R;

import java.util.List;

public class SortAdapter extends BaseAdapter {
    Context context;
    List<String> list;
    private MyClickListener mOnItemClickListener = null;
    public static int selectPosition = 1;
    private int type = -1;
    public SortAdapter(Context context, List<String> list,MyClickListener listener) {
        this.context = context;
        this.list = list;
        this.mOnItemClickListener = listener;
    }

    public SortAdapter(Context context, List<String> list,MyClickListener listener,int type) {
        this.context = context;
        this.list = list;
        this.mOnItemClickListener = listener;
        this.type = type;
    }

    public void setData( List<String> list){
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
            view = LayoutInflater.from(context).inflate(R.layout.sort_item,null);
            holder.tvName = view.findViewById(R.id.tv_name);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        if(-1 != type ){
            if(i == 0 ){
                holder.tvName.setTextColor(context.getResources().getColor(R.color.white));
            }else if(selectPosition == i){
                Log.i("SortAdapter","selectPosition === "+selectPosition);
                holder.tvName.setTextColor(context.getResources().getColor(R.color.blue));
            }
        }

        holder.tvName.setText(list.get(i));
        holder.tvName.setOnClickListener(mOnItemClickListener);
        holder.tvName.setTag(i);

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

//    public static interface OnItemClickListener {
//        void onItemClick(View view, int position);
//    }
//
//    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
//        this.mOnItemClickListener = mOnItemClickListener;
//    }

    class ViewHolder {
        TextView tvName;
    }
}
