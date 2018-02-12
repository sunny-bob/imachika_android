package com.itmg.imachika.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.itmg.imachika.R;
import com.itmg.imachika.adapter.ExpandAdapter;
import com.itmg.imachika.adapter.GridAdapter;
import com.itmg.imachika.adapter.MyExpandableListAdapter;
import com.itmg.imachika.contans.Contans;
import com.itmg.imachika.model.All;
import com.itmg.imachika.model.Rank;
import com.itmg.imachika.ui.ShopListActivity;
import com.itmg.imachika.ui.RegisterActivity;
import com.itmg.imachika.ui.SearchActivity;
import com.itmg.imachika.util.GsonUtil;
import com.itmg.imachika.util.Mytoast;
import com.itmg.imachika.util.OkHttpUtil;
import com.itmg.imachika.util.PreferencesUtils;
import com.itmg.imachika.util.URLInfo;
import com.itmg.imachika.view.MyExpandListView;
import com.itmg.imachika.view.MyGridView;
import com.itmg.imachika.view.PinnedHeaderExpListView;
import com.itmg.imachika.view.WaitDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/11/9 0009.
 * 首页
 */

public class HomeFragment extends Fragment implements OkHttpUtil.Result {
    View view;
    @BindView(R.id.grid_view)
    MyGridView gridView;
    Unbinder unbinder;
    Intent intent = new Intent();
    List<Rank> list = new ArrayList<>();
    List<All> groups = new ArrayList<>();
    List<List<All.Cat2s>> chlids = new ArrayList<>();
    GridAdapter adapter;
    ExpandAdapter expandAdapter;
//    @BindView(R.id.ex_listView)
//    ExpandableListView exListView;
    @BindView(R.id.ex_listView)
    MyExpandListView exListView;
    @BindView(R.id.home_scrollview)
    ScrollView mScrollView;
    @BindView(R.id.search_ll)
    LinearLayout mSearchLl;

    private static final String TAG = "HomeFragment";
    private WaitDialog mWaitDialog;
    SharedPreferences sp;
    String rank,all,alls;
    boolean isLogin;
    LinearLayout mTitleView;
//    // 标志位，标志已经初始化完成。
//    private boolean isPrepared;

//    MyExpandableListAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home, null);
        unbinder = ButterKnife.bind(this, view);
        exListView.setGroupIndicator(null);
        adapter = new GridAdapter(list, getActivity());
        gridView.setAdapter(adapter);
        mTitleView = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.include,null);
        mWaitDialog = new WaitDialog(getActivity(),R.style.loading_dialog,1);
        expandAdapter = new ExpandAdapter(getActivity(), groups, chlids);
        exListView.setAdapter(expandAdapter);
//        mAdapter = new MyExpandableListAdapter(getActivity(), groups, chlids);
//        exListView.setAdapter(mAdapter);
//        View h = LayoutInflater.from(getActivity()).inflate(R.layout.include, null, false);
//        exListView.setPinnedHeaderView(h);
//        exListView.setOnScrollListener((AbsListView.OnScrollListener) mAdapter);
//        exListView.setDividerHeight(0);

        exListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Log.i(TAG,"gridView.getHeight() === "
                        +gridView.getHeight());
                mScrollView.scrollTo(0,gridView.getHeight()+30);
                for (int i = 0; i < groups.size(); i++) {
                    if (groupPosition != i) {
                        exListView.collapseGroup(i);
                    }else{

                    }
                }
            }

        });

        exListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                intent.setClass(getActivity(), ShopListActivity.class);
                intent.putExtra("id",chlids.get(i).get(i1).get_id());
                intent.putExtra("name",chlids.get(i).get(i1).getName());
                intent.putExtra("all",alls);
                getActivity().startActivity(intent);
                return true;
            }
        });

//        isPrepared = true;
//        lazyLoad();
        isLogin = PreferencesUtils.getBoolean(getActivity(),"isLogin",false);
        Log.i(TAG,"---- onResume  ----- list.size() == "+list.size());
//        Log.d(TAG,"---- onResume  ----- groups.size() == "+groups.size());
//        Log.d(TAG,"---- onResume  ----- chlids.size() == "+chlids.size());
        if (list.size()==0){
            loadData();
        }
        return view;
    }

//    @Override
//    protected void lazyLoad() {
//        Log.d(TAG,"---- isPrepared == "+isPrepared  +"---- isVisible == "+isVisible);
//        if(!isPrepared || !isVisible) {
//            return;
//        }
//        //填充各控件的数据
//        sp = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
//        isLogin = sp.getBoolean("isLogin",false);
//        Log.d(TAG,"---- onResume  ----- list.size() == "+list.size());
//        Log.d(TAG,"---- onResume  ----- groups.size() == "+groups.size());
//        Log.d(TAG,"---- onResume  ----- chlids.size() == "+chlids.size());
//        if (list.size()==0){
//            loadData();
//        }
//    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        //填充各控件的数据
////        sp = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
//        isLogin = PreferencesUtils.getBoolean(getActivity(),"isLogin",false);
////        Log.d(TAG,"---- onResume  ----- list.size() == "+list.size());
////        Log.d(TAG,"---- onResume  ----- groups.size() == "+groups.size());
////        Log.d(TAG,"---- onResume  ----- chlids.size() == "+chlids.size());
//        if (list.size()==0){
//            loadData();
//        }
//    }
    /**从网络上获取数据*/
    private void loadData() {
        mWaitDialog.show();
        OkHttpUtil.get(URLInfo.home, Contans.home,this,getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    @OnItemClick(R.id.grid_view)
    void onItemClick(int position){
        if (position == list.size()-1){
            if (isLogin){
                intent.setClass(getActivity(), ShopListActivity.class);
                intent.putExtra("id",list.get(position).get_id());
                intent.putExtra("name",list.get(position).getName());
                intent.putExtra("all",alls);
            }else{
                intent.setClass(getActivity(), RegisterActivity.class);
            }
        }else{
            intent.setClass(getActivity(), ShopListActivity.class);
            intent.putExtra("id",list.get(position).get_id());
            intent.putExtra("name",list.get(position).getName());
            intent.putExtra("all",alls);
        }
        getActivity().startActivity(intent);
    }
    @OnClick(R.id.layout_search)
    public void onViewClicked() {
        Log.d(TAG,rank+"\n"+all);
        intent.setClass(getActivity(), SearchActivity.class);
        intent.putExtra("rank", rank);
        intent.putExtra("all", all);
        intent.putExtra("alls", alls);
        getActivity().startActivity(intent);
    }
    /**网络请求返回的数据接口*/
    @Override
    public void result(final String s, final int code) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (code){
                    case Contans.error:
                        mWaitDialog.dismiss();
                        Mytoast.show(getActivity(),s);
                        break;
                    case Contans.home:
                        Log.d(TAG,s);
                        mWaitDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String data = jsonObject.getString("data");
                            JSONObject object = new JSONObject(data);
                            rank = object.getString("rank_cats");
                            Log.d(TAG,rank);
                            List<Object> objectList = GsonUtil.praseJsonToList(rank,Rank.class);
                            for (Object o:objectList){
                                Rank rank1 = (Rank) o;
                                list.add(rank1);
                            }
                            adapter.notifyDataSetChanged();
                            all = object.getString("all_cats");
                            Log.d(TAG,all);
                            List<Object> allList = GsonUtil.praseJsonToList(all,All.class);
                            for (Object o:allList){
                                All all1 = (All) o;
                                groups.add(all1);
                                chlids.add(all1.getCat2s());
                            }
                            expandAdapter.notifyDataSetChanged();
                            alls = object.getString("all");
                            PreferencesUtils.putString(getActivity(),"all",alls);
                            Log.i(TAG,alls);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });
    }
}
