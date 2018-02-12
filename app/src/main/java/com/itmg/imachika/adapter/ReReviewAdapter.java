package com.itmg.imachika.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itmg.imachika.R;
import com.itmg.imachika.model.ReReview;
import com.itmg.imachika.model.Recommend;

import java.util.List;

/**
 * Created by Administrator on 2017/11/10 0010.
 */

public class ReReviewAdapter extends BaseAdapter {
    String TAG = "ReReviewAdapter";
    Context context;
    List<ReReview> list;

    public ReReviewAdapter(Context context, List<ReReview> list) {
        this.context = context;
        this.list = list;
    }

    public void setData( List<ReReview> list){
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
            view = LayoutInflater.from(context).inflate(R.layout.item_re_review,null);
            holder.reReUserTv = view.findViewById(R.id.item_re_review_user_tv);
            holder.reReTv = view.findViewById(R.id.item_re_review_tv);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        ReReview data = list.get(i);
        holder.reReUserTv.setText(data.getUser_name()+":");
        holder.reReTv.setText(data.getContent());
        return view;
    }


    class ViewHolder {
        TextView reReUserTv;
        TextView reReTv;
    }

}
