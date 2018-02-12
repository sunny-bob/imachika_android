package com.itmg.imachika.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.itmg.imachika.R;
import com.itmg.imachika.adapter.ShopAdapter;
import com.itmg.imachika.application.APP;
import com.itmg.imachika.base.BaseActivity;
import com.itmg.imachika.model.Shop;
import com.itmg.imachika.model.Collection;
import com.itmg.imachika.util.Constant;
import com.itmg.imachika.util.GsonUtil;
import com.itmg.imachika.util.OkHttpUtils;
import com.itmg.imachika.util.PreferencesUtils;
import com.itmg.imachika.util.RequestCallBack;
import com.itmg.imachika.util.URLInfo;
import com.itmg.imachika.view.WaitDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 收藏列表
 */
public class CollectionActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView)
    ListView mListView;
    @BindView(R.id.collection_shop_null)
    TextView nullTv;

    private static final String TAG = "CollectionActivity";
    private WaitDialog mWaitDialog;
    ShopAdapter adapter;
    List<Shop.Data.Info> list = new ArrayList<>();
    APP app = APP.getMyApplication();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_shop);
        ButterKnife.bind(this);
        app.addActivity(this);

        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        tvTitle.setText(getResources().getString(R.string.me_collect));
//        //设置可上拉刷新和下拉刷新
//        mListView.setMode(PullToRefreshBase.Mode.BOTH);
//        //设置刷新时显示的文本
//        ILoadingLayout startLayout = mListView.getLoadingLayoutProxy(false, true);
//        startLayout.setPullLabel("正在下拉刷新...");
//        startLayout.setRefreshingLabel("正在玩命加载中...");
//        startLayout.setReleaseLabel("放开以刷新");
//        ILoadingLayout endLayout = mListView.getLoadingLayoutProxy(false, true);
//        endLayout.setPullLabel("正在上拉刷新...");
//        endLayout.setRefreshingLabel("正在玩命加载中...");
//        endLayout.setReleaseLabel("放开以刷新");
//        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//                //下拉刷新
////                bussinessList.clear();
////                index = START_PAGE;
////                getInfoRequest();//false
//
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//                //上拉加载更多
////                index++;
////                getInfoRequest();//false
//
//            }
//        });
        adapter = new ShopAdapter(this,list);
        mListView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCollectRequest();
    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        finish();
    }

    private void getCollectRequest(){
        mWaitDialog = new WaitDialog(this, R.style.loading_dialog,1);
        mWaitDialog.show();
        String url = URLInfo.meCollection;
        Map<String, String> params = new HashMap<>();
        params.put("location", PreferencesUtils.getString(this,"location",""));//   "594cd4d723679c0e4d69dcfc"
        Log.i(TAG,"getInfoRequest url === " + url+"  params == "+params.toString());
        String userId = PreferencesUtils.getString(this, Constant.SP_USER_ID,"");
        Log.i(TAG,"getInfoRequest userId === " + userId );
        OkHttpUtils.getInstance().okHttpGet(url,userId, params,PreferencesUtils.getString(this,"Version","1.0"), new RequestCallBack() {
            @Override
            public void onSuccess(String result) {
                mWaitDialog.dismiss();
                if(null != result){
                    Log.i(TAG,"result = "+result);
                    Collection collection = (Collection) GsonUtil.praseJsonToModel(result, Collection.class);
                    if(null != collection.getData() && collection.getData().size() > 0){
                        Log.i(TAG,"collection.getData() = "+collection.getData().size());
                        adapter.setData(collection.getData());
                    }else{
                        nullTv.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFail() {
                mWaitDialog.dismiss();
                Toast.makeText(CollectionActivity.this,getResources().getString(R.string.net_bad_retry), Toast.LENGTH_LONG).show();
            }

        });
    }
}
