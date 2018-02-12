package com.itmg.imachika.adapter;

import android.app.Activity;
import android.content.ClipboardManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.itmg.imachika.R;
import com.itmg.imachika.model.ChatMessage;
import com.itmg.imachika.util.GlideUtil;

import java.io.File;
import java.util.List;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by Administrator on 2017/7/18 0018.
 * 聊天界面的消息
 */

public class ChatAdapter extends BaseAdapter {
    Activity context;
    List<ChatMessage> list;
    long lastTime = 0,currentTime;
    public ChatAdapter(Activity context, List<ChatMessage> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        final ChatMessage item = list.get(position);
        convertView = item.isLeft()?LayoutInflater.from(context).inflate(R.layout.chat_left_item, parent, false)
                :LayoutInflater.from(context).inflate(R.layout.chat_right_item, parent, false);
        vh = new ViewHolder();
        vh.tvText = (TextView) convertView.findViewById(R.id.tv_text);
        vh.headView = (ImageView) convertView.findViewById(R.id.head_view);
        vh.tvText.setText(item.getData());
//        GlideUtil.setImg(context,vh.headView,item.getHeadImg());
        return convertView;
    }

    static class ViewHolder {
        ImageView headView;
        TextView tvText;
        TextView tvDesc;
        ImageView imgMsg;
    }
}
