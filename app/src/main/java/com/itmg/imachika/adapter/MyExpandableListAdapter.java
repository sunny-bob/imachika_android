package com.itmg.imachika.adapter;

/**
 * Created by Administrator on 2018/2/10 0010.
 */

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.itmg.imachika.R;
import com.itmg.imachika.model.All;
import com.itmg.imachika.util.GlideUtil;
import com.itmg.imachika.util.URLInfo;
import com.itmg.imachika.view.PinnedHeaderExpListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple adapter which maintains an ArrayList of photo resource Ids.
 * Each photo is displayed as an image. This adapter supports clearing the
 * list of photos and adding a new photo.
 *
 */
public class MyExpandableListAdapter extends BaseExpandableListAdapter implements PinnedHeaderExpListView.PinnedHeaderAdapter, AbsListView.OnScrollListener {
    // Sample data set.  children[i] contains the children (String[]) for groups[i].
    List<All> groupList;
    List<List<All.Cat2s>> childList;
    private Context context;

    private int mPinnedHeaderBackgroundColor;
    private int mPinnedHeaderTextColor;
    public MyExpandableListAdapter(Context context, List<All> groupList, List<List<All.Cat2s>> childList){
        this.context = context;
        this.groupList = groupList;
        this.context = context;
        mPinnedHeaderBackgroundColor = context.getResources().getColor(android.R.color.black);
        mPinnedHeaderTextColor = context.getResources().getColor(android.R.color.white);

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
        @Nullable
        @BindView(R.id.tv)
        TextView tv;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * размытие/пропадание хэдера
     */
    public void configurePinnedHeader(View v, int position, int alpha) {
        TextView header = (TextView) v;
        final String title = (String) getGroup(position);

        header.setText(title);
        if (alpha == 255) {
            header.setBackgroundColor(mPinnedHeaderBackgroundColor);
            header.setTextColor(mPinnedHeaderTextColor);
        } else {
            header.setBackgroundColor(Color.argb(alpha,
                    Color.red(mPinnedHeaderBackgroundColor),
                    Color.green(mPinnedHeaderBackgroundColor),
                    Color.blue(mPinnedHeaderBackgroundColor)));
            header.setTextColor(Color.argb(alpha,
                    Color.red(mPinnedHeaderTextColor),
                    Color.green(mPinnedHeaderTextColor),
                    Color.blue(mPinnedHeaderTextColor)));
        }
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (view instanceof PinnedHeaderExpListView) {
            ((PinnedHeaderExpListView) view).configureHeaderView(firstVisibleItem);
        }

    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // TODO Auto-generated method stub

    }

}
