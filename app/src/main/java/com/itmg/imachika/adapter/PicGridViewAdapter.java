package com.itmg.imachika.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.itmg.imachika.R;

import java.util.List;


public class PicGridViewAdapter extends BaseAdapter {
    private List<String> list;
    private Context context;
    private int flag;       //flag=0代表单选 其他为多选

    public PicGridViewAdapter(Context context, List<String> list, int flag) {
        this.context=context;
        this.list=list;
        this.flag = flag;
    }

    @Override
    public int getCount() {
        if (flag == 0) {
            if (list.size() >= 1) {
                return 1;
            }
            return (list.size() + 1);
        } else {
            if (list.size() >= 10) {
                return 10;
            }
            return (list.size() + 1);
        }
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_picgridview, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.select_img);
            holder.del = (ImageView) convertView.findViewById(R.id.img_del);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == list.size()) {
            Glide.with(context).load(R.drawable.addimg).centerCrop().into(holder.imageView);
            holder.del.setVisibility(View.GONE);
        }else{
            Glide.with(context).load(list.get(position)).centerCrop().into(holder.imageView);
            holder.del.setVisibility(View.VISIBLE);
        }
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
        ImageView del;
    }

}
