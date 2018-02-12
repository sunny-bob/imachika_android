package com.itmg.imachika.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.itmg.imachika.R;
import com.itmg.imachika.model.All;
import com.itmg.imachika.util.GlideUtil;
import com.itmg.imachika.util.URLInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/11/10 0010.
 */

public class ExpandAdapter extends BaseExpandableListAdapter {
    Context context;
    List<All> groupList;
    List<List<All.Cat2s>> childList;

    public ExpandAdapter(Context context, List<All> groupList, List<List<All.Cat2s>> childList) {
        this.context = context;
        this.groupList = groupList;
        this.childList = childList;
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return childList.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return groupList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return childList.get(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        GroupViewHolder groupViewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.expand_group, null);
            groupViewHolder = new GroupViewHolder(view);
            view.setTag(groupViewHolder);
        }else{
            groupViewHolder = (GroupViewHolder) view.getTag();
        }
        All item = groupList.get(i);
        groupViewHolder.name.setText(item.getName());
        String img = URLInfo.logoUrl+"fl_"+item.getTop_id()+".png";
        GlideUtil.setImg(context,groupViewHolder.img,img);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildViewHolder childViewHolder;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.expand_child,null);
            childViewHolder = new ChildViewHolder(view);
            view.setTag(childViewHolder);
        }else{
            childViewHolder = (ChildViewHolder) view.getTag();
        }
        All.Cat2s item = childList.get(i).get(i1);
        childViewHolder.tv.setText(item.getName());
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    static class GroupViewHolder {
        @BindView(R.id.img)
        ImageView img;
        @BindView(R.id.name)
        TextView name;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
    static class ChildViewHolder {
        @Nullable@BindView(R.id.tv)
        TextView tv;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
