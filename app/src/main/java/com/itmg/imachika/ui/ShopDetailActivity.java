package com.itmg.imachika.ui;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itmg.imachika.MainActivity;
import com.itmg.imachika.R;
import com.itmg.imachika.adapter.CommendAdapter;
import com.itmg.imachika.adapter.DetailVpAdapter;
import com.itmg.imachika.adapter.ReviewAdapter;
import com.itmg.imachika.adapter.SortAdapter;
import com.itmg.imachika.application.APP;
import com.itmg.imachika.model.Shop;
import com.itmg.imachika.model.ShopDetail;
import com.itmg.imachika.model.Classifal;
import com.itmg.imachika.model.Recommend;
import com.itmg.imachika.util.Constant;
import com.itmg.imachika.util.GsonUtil;
import com.itmg.imachika.util.Mytoast;
import com.itmg.imachika.util.OkHttpUtils;
import com.itmg.imachika.util.PreferencesUtils;
import com.itmg.imachika.util.RequestCallBack;
import com.itmg.imachika.util.Share;
import com.itmg.imachika.util.URLInfo;
import com.itmg.imachika.util.Utils;
import com.itmg.imachika.view.MyListView;
import com.itmg.imachika.view.ObservableScrollView;
import com.itmg.imachika.view.SelectDialog;
import com.itmg.imachika.view.WaitDialog;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopDetailActivity extends Activity implements OnMapReadyCallback {
    @BindView(R.id.tv_title)
    TextView mTitleTv;
    @BindView(R.id.title)
    RelativeLayout titleRl;
    @BindView(R.id.detail_scroolview)
    ObservableScrollView mScrollview;
//    @BindView(R.id.bussiness_detail_more)
//    ListView mMoreView;
    @BindView(R.id.detail_vp_view)
    RelativeLayout mVpView;
    @BindView(R.id.detail_main_view)
    RelativeLayout mMainView;
    @BindView(R.id.detail_img_vp)
    ViewPager mViewPager;
    @BindView(R.id.detail_tv_name)
    TextView mNameTv;
    @BindView(R.id.detail_hint_tv)
    TextView mHintTv;
    @BindView(R.id.detail_ratingBar)
    AppCompatRatingBar mRating;
    @BindView(R.id.detail_category_tv)
    TextView mClassTv;
    @BindView(R.id.detail_sub_category_tv)
    TextView mSubClassTv;
    @BindView(R.id.detail_sub1_category_tv)
    TextView mSub1ClassTv;
    @BindView(R.id.detail_sub2_category_tv)
    TextView mSub2ClassTv;
    @BindView(R.id.detail_sub3_category_tv)
    TextView mSub3ClassTv;
    @BindView(R.id.detail_class_view)
    LinearLayout mClassView;
    @BindView(R.id.detail_phone_view)
    LinearLayout mPhoneView;
    @BindView(R.id.detail_phone_tv)
    TextView mPhoneTv;
    @BindView(R.id.detail_address_tv)
    TextView mAddressTv;
    @BindView(R.id.detail_bottom_phone_tv)
    RelativeLayout mBottomPhoneTv;
//    @BindView(R.id.detail_bottom_collect_iv)
//    ImageView mCollectionIv;
    @BindView(R.id.detail_bottom_collect_tv)
    TextView mCollectionTv;
    @BindView(R.id.detail_time_tv)
    TextView mTimeTv;
    @BindView(R.id.detail_url_tv)
    TextView mUrlTv;
    @BindView(R.id.detail_address_view)
    LinearLayout mAddView;
    @BindView(R.id.detail_time_view)
    LinearLayout mTimeView;
    @BindView(R.id.detail_url_view)
    LinearLayout mUrlView;
    @BindView(R.id.detail_tag_ll)
    LinearLayout mTagLl;
    @BindView(R.id.detail_tag_view)
    LinearLayout mTagView;

    @BindView(R.id.detail_opening_time_tv)
    TextView mOpeningTv;
    @BindView(R.id.detail_desc_wv)
    WebView mDescWb;
    @BindView(R.id.detail_desc_view)
    LinearLayout mDescView;
    @BindView(R.id.detail_review_lv)
    RecyclerView mReviewLv;
    @BindView(R.id.detail_review_view)
    LinearLayout mReviewView;
    @BindView(R.id.detail_opening_time)
    LinearLayout mOpeningView;
    @BindView(R.id.detail_commend_lv)
    MyListView mCommendLv;
    @BindView(R.id.detail_banner_point)
    LinearLayout mViewPagerPoint;
//    @BindView(R.id.recyclerView)
//    RecyclerView recyclerView;
//    SharedPreferences sp;
    GoogleMap map;
    private static final String TAG = "ShopDetailActivity";
    private WaitDialog mWaitDialog;
    private APP app = APP.getMyApplication();
    private Intent intent;
    private Shop.Data.Info mInfo;
    private ShopDetail mDetBussiness;
//    private ReviewAdapter mReviewAdapter;
    private boolean mIsCollection;
    //设置当前 第几个图片 被选中
    private int autoCurrIndex = 0;
    private ImageView[] mBottomImages;//底部只是当前页面的小圆点
    private String classStr = "";
    private String subClassStr = "";
    private String classId = "";
    private String subClassId = "";
    private String sub1ClassStr = "";
    private String sub1ClassId = "";
    private String sub2ClassStr = "";
    private String sub2ClassId = "";
    private ArrayList<Recommend> commendDatas;
    private String detailId = "";
    ArrayList<String> moreDatas;
    ArrayList<String> reDatas;
    SortAdapter moreAdapter;
    ReviewAdapter reviewAdapter;
//    SortAdapter reDialogAdapter;
    private SelectDialog selectDialog;
//    private SelectDialog reviewDialog;
    private boolean mGetInfo;
    public static boolean isStop;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    setRequestInfo();
                    break;
                case 2:
//                    if (autoCurrIndex == mInfo.getImgs().size()) {
//                        autoCurrIndex = 1;
//                        mViewPager.setCurrentItem(autoCurrIndex);
//                    }
                    if (autoCurrIndex == mInfo.getImgs().size() - 1) {
                        autoCurrIndex = -1;
                    }
                    if(!isStop){
                        mViewPager.setCurrentItem(++autoCurrIndex);
                        mHandler.sendEmptyMessageDelayed(2, 3000);
                    }

                    break;
                case 3:
                    if(null != mInfo){
                        String locationStr = mInfo.getLocation();
                        if(null !=  mDetBussiness){
                            locationStr = mDetBussiness.getData().getData().getLocation();
                        }
                        String location [] = locationStr.split(",");
                        LatLng sydney = new LatLng(Float.parseFloat(location[0]), Float.parseFloat(location[1]));
                        if(null != map){
                            Log.i(TAG,"mInfo.getContent() === "+mInfo.getContent());
                            map.addMarker(new MarkerOptions().position(sydney).title(mInfo.getContent()));
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14));
                            Log.i(TAG,"mHandler 3 === "+locationStr);

                        }
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);
        ButterKnife.bind(this);
        Log.i(TAG,"onCreate ------------ ");
        app.addActivity(this);
        isStop = false;
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        mCommendLv.setFocusable(false);
        intent = getIntent();

        if(intent.hasExtra("data")){
            mInfo = (Shop.Data.Info)intent.getSerializableExtra("data");
            if(null != mInfo){
                setInfo();
            }
        }
//        getInfoRequest();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"onResume ------------ ");
        getInfoRequest();
    }

    private void setMoreView(ShopDetail.Data.Info info){
        moreDatas = new ArrayList<>();
        reDatas = new ArrayList<>();
        reDatas.add(getResources().getString(R.string.jubao));
        reDatas.add(getResources().getString(R.string.hint_item));
        moreDatas.add(getResources().getString(R.string.sort_share));
        if(!info.isIs_collection()){
            moreDatas.add(getResources().getString(R.string.collect));
        }else{
            moreDatas.add(getResources().getString(R.string.cancle_collect));
        }
        moreDatas.add(getResources().getString(R.string.hint_item));
        moreDatas.add(getResources().getString(R.string.detail_jubao));
        moreDatas.add("Home");
        moreAdapter = new SortAdapter(ShopDetailActivity.this,moreDatas,mSortItemClickListener);

    }

    private void shareOut(){
        Intent intent1 = new Intent(Intent.ACTION_SEND);
        intent1.putExtra(Intent.EXTRA_TEXT,URLInfo.shareDetailUrl+mInfo.get_id());
        intent1.setType("text/plain");
//        Uri u = Uri.parse("android.resource://"+this.getPackageName() +"/drawable/logo.png");
//        intent1.putExtra(Intent.EXTRA_STREAM, u);
//        intent1.putExtra(Intent.EXTRA_SUBJECT, mInfo.getContent());
        startActivity(Intent.createChooser(intent1,getResources().getString(R.string.share)));
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void setInfo(){
        mWaitDialog = new WaitDialog(this, R.style.loading_dialog);
        mTitleTv.setText(mInfo.getContent() != null ? mInfo.getContent() : "");
        mNameTv.setText(mInfo.getContent() != null ? mInfo.getContent() : "");
        if(null != mInfo.getImgs() && mInfo.getImgs().size() > 0){//图片轮播
            mVpView.setVisibility(View.VISIBLE);
            DetailVpAdapter adapter = new DetailVpAdapter(this ,mInfo.getImgs());
            //设置当前页码值--一开始就在某位置
            mViewPager.setCurrentItem(10000*mInfo.getImgs().size());
            mViewPager.setAdapter(adapter);
            mViewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    isStop = true;
                    return false;
                }
            });

            if(mInfo.getImgs().size() > 1){
                setViewPagerPoint();
            }
        }

        if(null != mInfo.getCategory()){//分类
            mClassView.setVisibility(View.VISIBLE);
            Log.i(TAG,"setInfo 分类 ==  "+ mInfo.getCategory().get(0));
            String all = PreferencesUtils.getString(this,"all","");
            Map<String, Classifal> mapCla = new Gson().fromJson(all, new TypeToken<HashMap<String, Classifal>>() {
            }.getType());
//            Log.i(TAG, mapCla.toString());
            String [] result = mInfo.getCategory().get(0).split(",");
            Log.i(TAG,"setInfo result ==  "+ result.toString());

            for (Classifal classifal : mapCla.values()) {
                for(int j = 0 ; j < result.length; j ++){
                    if(result[j].equals(classifal.get_id())){
                        if(j == 0){
                            classStr = classifal.getName();
                            classId = classifal.get_id();
                            mClassTv.setText(classStr);
                        }else{
                            switch (j){
                                case 1:
                                    subClassStr =  classifal.getName();
                                    subClassId = classifal.get_id();
                                    mSubClassTv.setVisibility(View.VISIBLE);
                                    mSubClassTv.setText(">  " + subClassStr);
                                    break;
                                case 2:
                                    sub1ClassStr =  classifal.getName();
                                    sub1ClassId = classifal.get_id();
                                    mSub1ClassTv.setVisibility(View.VISIBLE);
                                    mSub1ClassTv.setText(">  " + sub1ClassStr);
                                    break;
                                case 3:
                                    sub2ClassStr =  classifal.getName();
                                    sub2ClassId = classifal.get_id();
                                    mSub2ClassTv.setVisibility(View.VISIBLE);
                                    mSub2ClassTv.setText(">  " + sub2ClassStr);
                                    break;
//                                case 4:
////                                    subClassId = classifal.get_id();
//                                    mSub3ClassTv.setVisibility(View.VISIBLE);
//                                    mSub3ClassTv.setText(">  " + subClassStr);
//                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            }

        }

        if(null != mInfo.getLocation()){

            mHandler.sendEmptyMessage(3);
        }

        mRating.setRating((mInfo.getRating() != 0 ? (float)mInfo.getRating() : 0));

        if(null != mInfo.getPhone()){
            mPhoneView.setVisibility(View.VISIBLE);
            mBottomPhoneTv.setVisibility(View.VISIBLE);
            mPhoneTv.setText(mInfo.getPhone() != null ? mInfo.getPhone() : "");
        }

        if(null != mInfo.getAddress() && !"".equals(mInfo.getAddress())){
            mAddView.setVisibility(View.VISIBLE);
            mAddressTv.setText(mInfo.getAddress() != null ? mInfo.getAddress() : "");
        }

        if(null != mInfo.getOrigin_start_time_format() && null != mInfo.getOrigin_end_time_format()){
            mTimeView.setVisibility(View.VISIBLE);
            mTimeTv.setText(mInfo.getOrigin_start_time_format()+" ～ "+mInfo.getOrigin_end_time_format());
        }

        if(null != mInfo.getOpening_weekday()){
            mOpeningView.setVisibility(View.VISIBLE);
            StringBuffer timeStr = new StringBuffer();
            for(int i = 0 ; i < mInfo.getOpening_weekday().size(); i ++){
                if(i == 0){
                    timeStr.append(mInfo.getOpening_weekday().get(i));
                }else{
                    timeStr.append("\n"+mInfo.getOpening_weekday().get(i));
                }
            }
            mOpeningTv.setText(timeStr);
        }

        if(null != mInfo.getCategory() &&  mInfo.getCategory().get(0).startsWith("event")){
            if(null != mInfo.getWeb_url()){
                mUrlView.setVisibility(View.VISIBLE);
                mUrlTv.setText(mInfo.getWeb_url());
            }
        }

        if(null != mInfo.getTag() && mInfo.getTag().size() > 1){
            mTagView.setVisibility(View.VISIBLE);
            for(int i = 0 ; i < mInfo.getTag().size(); i++){
                LinearLayout view  = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_tag,null);
                TextView tagTv = view.findViewById(R.id.item_tag_tv);
                Log.i(TAG,"mInfo.getTag() ==== "+ mInfo.getTag().get(i));
                tagTv.setText(mInfo.getTag().get(i));
                mTagLl.addView(view);
            }
        }

        if(null != mInfo.getDesc()){
            WebSettings webSettings = mDescWb.getSettings();//获取webview设置属性
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//把html中的内容放大webview等宽的一列中
            webSettings.setJavaScriptEnabled(true);//支持js
            webSettings.setBuiltInZoomControls(true); // 显示放大缩小
            webSettings.setSupportZoom(true); // 可以缩放
            mDescView.setVisibility(View.VISIBLE);
            mDescWb.loadDataWithBaseURL(null, mInfo.getDesc(), "text/html" , "utf-8", null);
        }

//        mScrollview.setOnScollChangedListener(new ObservableScrollView.OnScollChangedListener() {
//            @Override
//            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
//                Log.i(TAG,"mScrollview   onScrollChange --- ");
//                View view = mScrollview.getChildAt(mScrollview.getChildCount()-1);
//                int d = view.getBottom();
//                d -= (mScrollview.getHeight()+mScrollview.getScrollY());
//                if(d==0){
//                    //you are at the end of the list in scrollview
//                    //do what you wanna do here
//                    Log.i(TAG,"mScrollview   onScrollChange ScrollView.FOCUS_DOWN --- ");
//                    if(!mGetInfo){//滚动到底部且未加载完数据
//                        mWaitDialog.show();
//                    }else{
//                        mWaitDialog.dismiss();
//                    }
//                }
//            }
//        });

    }

    private void setRequestInfo(){//设置请求数据
       ShopDetail.Data.Info info =  mDetBussiness.getData().getData();

        setMoreView(info);

        if(null != info.getLocation()){
            mHandler.sendEmptyMessage(3);
        }

       if(null != info.getReviews() && info.getReviews().size() > 0){//商品评论列表
           mReviewView.setVisibility(View.VISIBLE);
           mReviewLv.setHasFixedSize(true);
           RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
           mReviewLv.setLayoutManager(layoutManager);
           reviewAdapter = new ReviewAdapter(this,info.getReviews(),mReviewListener);
           mReviewLv.setAdapter(reviewAdapter);
       }
        if(null != info.getCategory() &&  info.getCategory().get(0).getName().equals("event")){//.sta
            if(null != info.getWeb_url()){
                mUrlTv.setVisibility(View.VISIBLE);
                mUrlTv.setText(info.getWeb_url());
            }
        }
        if(null != info.getOpening_weekday() && info.getOpening_weekday().size() > 0){//营业时间
            mOpeningView.setVisibility(View.VISIBLE);
            StringBuffer timeStr = new StringBuffer();
            for(int i = 0 ; i < info.getOpening_weekday().size(); i ++){
                if(i == 0){
                    timeStr.append(info.getOpening_weekday().get(i));
                }else{
                    timeStr.append("\n"+info.getOpening_weekday().get(i));
                }
            }
            mOpeningTv.setText(timeStr);
        }

        if(null != info.getRecommend_items()){//设置推荐商家
            Recommend data = info.getRecommend_items();
            commendDatas = new ArrayList<>();
            if(null != data.getEvent()){
                Recommend model = new Recommend();
                model.setTitleId("event");
                model.setTitleStr(getResources().getString(R.string.commend_one));
                model.setAdapterData(data.getEvent());
                commendDatas.add(model);
            }
            if(null != data.getHoliday_attraction()){
                Recommend model = new Recommend();
                model.setTitleId("holiday_attraction");
                model.setTitleStr(getResources().getString(R.string.commend_two));
                model.setAdapterData(data.getHoliday_attraction());
                commendDatas.add(model);
            }
            if(null != data.getRestaurant()){
                Recommend model = new Recommend();
                model.setTitleId("restaurant");
                model.setTitleStr(getResources().getString(R.string.commend_three));
                model.setAdapterData(data.getRestaurant());
                commendDatas.add(model);
            }
            if(null != data.getCafe()){
                Recommend model = new Recommend();
                model.setTitleId("cafe");
                model.setTitleStr(getResources().getString(R.string.commend_four));
                model.setAdapterData(data.getCafe());
                commendDatas.add(model);
            }
            if(null != data.getShopping()){
                Recommend model = new Recommend();
                model.setTitleId("shopping");
                model.setTitleStr(getResources().getString(R.string.commend_five));
                model.setAdapterData(data.getShopping());
                commendDatas.add(model);
            }

            CommendAdapter mComAdapter = new CommendAdapter(this,commendDatas,mCommendListener);
            mCommendLv.setAdapter(mComAdapter);
            mScrollview.scrollTo(0,0);
        }

        mIsCollection = info.isIs_collection();
        if(mIsCollection){
//            mCollectionIv.setBackgroundResource(R.drawable.collection1);
            mCollectionTv.setText(getResources().getString(R.string.cancle_collect));
        }

    }

    private String getCatName(String str){
        String catStr = "";
        String all = PreferencesUtils.getString(this,"all","");
        Map<String, Classifal> mapCla = new Gson().fromJson(all, new TypeToken<HashMap<String, Classifal>>() {
        }.getType());
//            Log.i(TAG, mapCla.toString());
        String [] result = mInfo.getCategory().get(0).split(",");
        Log.i(TAG,"setInfo result ==  "+ result.toString());

        for (Classifal classifal : mapCla.values()) {
            if(str.equals(classifal.get_id())){
                catStr = classifal.getName();
            }
        }
        Log.i(TAG,"getCatName catStr ==  "+ catStr);

        return catStr;
    }

    /**
     * 实现类，响应按钮点击事件
     */
    private SortAdapter.MyClickListener mSortItemClickListener = new SortAdapter.MyClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            selectDialog.dismiss();
            switch (position){
                case 0://分享
                    shareOut();
                    Log.i(TAG,"  sortLv  onItemClick share is clicked ");
                    break;
                case 1://收藏、取消收藏
                    if("".equals(PreferencesUtils.getString(ShopDetailActivity.this, Constant.SP_USER_ID,
                            ""))){
                        Utils.gotoNewAct(ShopDetailActivity.this,RegisterActivity.class);
                        return;
                    }
                    collectRequest();
                    Log.i(TAG,"  sortLv  onItemClick share 1 is clicked ");
                    break;
                case 2://隐藏显示主页面
                    if(mMainView.isShown()){
                        mMainView.setVisibility(View.GONE);
                        mHintTv.setVisibility(View.VISIBLE);
                        moreDatas.set(2,getResources().getString(R.string.show_item));
                        moreAdapter.setData(moreDatas);
                    }else{
                        mMainView.setVisibility(View.VISIBLE);
                        mHintTv.setVisibility(View.GONE);
                        moreDatas.set(2,getResources().getString(R.string.hint_item));
                        moreAdapter.setData(moreDatas);
                    }
                    Log.i(TAG,"  sortLv  onItemClick share 2 is clicked ");
                    break;
                case 3://通报页面跳转
                    if("".equals(PreferencesUtils.getString(ShopDetailActivity.this,Constant.SP_USER_ID,
                            ""))){
                        Utils.gotoNewAct(ShopDetailActivity.this,RegisterActivity.class);
                        return;
                    }
                    Utils.gotoNewAct(ShopDetailActivity.this,
                            ReportActivity.class,mDetBussiness.getData().getData().get_id());
                    Log.i(TAG,"  sortLv  onItemClick share 3 is clicked ");
                    break;
                case 4://home页面跳转
                    Utils.gotoNewAct(ShopDetailActivity.this, MainActivity.class);
                    Log.i(TAG,"  sortLv  onItemClick share 4 is clicked ");
                    break;
                default:
                    break;
            }
        }
    };

//    /**
//     * 实现类，响应按钮点击事件
//     */
//    private SortAdapter.MyClickListener reClickListener = new SortAdapter.MyClickListener() {
//        @Override
//        public void myOnClick(int position, View v) {
//            selectDialog.dismiss();
//            switch (position){
//                case 0://投诉
//                    Utils.gotoNewAct(ShopDetailActivity.this,
//                            ReportActivity.class,mDetBussiness.getData().getData().get_id());
//                    Log.i(TAG,"  reClickListener  onItemClick is clicked  position ==== "+position);
//                    break;
//                case 1://隐藏、显示评论
//                    Log.i(TAG,"  reClickListener  onItemClick  1 is clicked ");
//                    Log.i(TAG,"  reClickListener  onItemClick is clicked  position ==== "+ position);
//                    break;
//                default:
//                    break;
//            }
//        }
//    };

    /**
     * 实现类，响应按钮点击事件
     */
    private ReviewAdapter.MyClickListener mReviewListener = new ReviewAdapter.MyClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            Log.i(TAG,"ReviewAdapter  mListener  ");
            switch (v.getId()){
                case R.id.item_detail_review_like://点赞
                    Log.i(TAG," mReviewLv  onItemClick item_detail_review_like is clicked");

                    if("".equals(PreferencesUtils.getString(ShopDetailActivity.this,Constant.SP_USER_ID,
                            ""))){
                        Utils.gotoNewAct(ShopDetailActivity.this,RegisterActivity.class);
                        return;
                    }
                    if(1 == mDetBussiness.getData().getData().getReviews().get(position).getIs_liked()){
                        return;
                    }
                    addLikeRequest(mDetBussiness.getData().getData().getReviews().get(position).get_id(),position);
                    break;
                case R.id.item_more_one://投诉
                    if("".equals(PreferencesUtils.getString(ShopDetailActivity.this,Constant.SP_USER_ID,
                            ""))){
                        Utils.gotoNewAct(ShopDetailActivity.this,RegisterActivity.class);
                        return;
                    }
                    Utils.gotoNewAct(ShopDetailActivity.this,ReportActivity.class,
                            mDetBussiness.getData().getData().get_id());
                    Log.i(TAG," mReviewLv  onItemClick item_more_one is clicked");
                    break;
                case R.id.item_more_two://隐藏、显示该条评论
                    if(mDetBussiness.getData().getData().getReviews().get(position).isHint()){
                        mDetBussiness.getData().getData().getReviews().get(position).setHint(false);
                    }else{
                        mDetBussiness.getData().getData().getReviews().get(position).setHint(true);
                    }
                    reviewAdapter.setData(mDetBussiness.getData().getData().getReviews());
                    Log.i(TAG," mReviewLv  onItemClick item_more_two is clicked");
                    break;
                case R.id.item_detail_review_user_tv:
                    Log.i(TAG," mReviewLv  onItemClick item_detail_review_user_tv is clicked");
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 实现类，响应按钮点击事件
     */
    private CommendAdapter.MyClickListener mCommendListener = new CommendAdapter.MyClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            Log.i(TAG,"CommendAdapter  mListener position == "+position);
            String catId = "";
            String catName = "";
            Log.i(TAG," CommendAdapter  onItemClick commendDatas.size === "+commendDatas.size() );
            if(null != commendDatas.get(position).getTitleId()){
                if("event".equals(commendDatas.get(position).getTitleId())){
                    catId = "event";
                    catName = getCatName(catId);
                }else if("holiday_attraction".equals(commendDatas.get(position).getTitleId())){
                    catId = "holiday_attraction";
                    catName = getCatName(catId);
                }else if("restaurant".equals(commendDatas.get(position).getTitleId())){
                    catId = "restaurant";
                    catName = getCatName(catId);
                }else if("cafe".equals(commendDatas.get(position).getTitleId())){
                    catId = "cafe";
                    catName = getCatName(catId);
                }else if("shopping".equals(commendDatas.get(position).getTitleId())){
                    catId = "shopping";
                    catName = getCatName(catId);
                }
            }

            switch (v.getId()){
                case R.id.item_commend_one://第一条数据
                    Log.i(TAG," CommendAdapter  onItemClick item_commend_one is clicked ");
                    Utils.gotoNewAct(ShopDetailActivity.this,
                            ShopDetailActivity.class,commendDatas.get(position).getAdapterData().get(0));
                    break;
                case R.id.item_commend_two://第二条数据
                    Log.i(TAG," CommendAdapter  onItemClick item_commend_two is clicked ");
                    Utils.gotoNewAct(ShopDetailActivity.this,
                            ShopDetailActivity.class,commendDatas.get(position).getAdapterData().get(1));
                    break;
                case R.id.item_commend_three://第三条数据
                    Log.i(TAG," CommendAdapter  onItemClick item_commend_three is clicked " );
                    Utils.gotoNewAct(ShopDetailActivity.this,
                            ShopDetailActivity.class,commendDatas.get(position).getAdapterData().get(2));
                    break;
                case R.id.item_commend_classify_view://标题

                    Log.i(TAG," CommendAdapter  onItemClick item_commend_classify_view is clicked");
                    Log.i(TAG,"CommendAdapter  catId == "+catId);
                    Log.i(TAG,"CommendAdapter  catName == "+catName);
                    intent.setClass(ShopDetailActivity.this, ShopListActivity.class);
                    intent.putExtra("id",catId);
                    intent.putExtra("name",catName);
                    intent.putExtra("all", PreferencesUtils.getString(ShopDetailActivity.this,"all",""));

                    startActivity(intent);
                    break;
                default:
                    break;
            }

        }
    };

    private void showDialog(SortAdapter adapter){
        selectDialog = new SelectDialog(this,R.style.dialog,adapter);//创建Dialog并设置样式主题
        Window win = selectDialog.getWindow();
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.gravity = Gravity.RIGHT|Gravity.TOP;
        params.x = 10;//设置x坐标
        Log.i(TAG,"titleRl.getHeight() === "+titleRl.getHeight());
        params.y = titleRl.getHeight()+10;//设置y坐标
        win.setAttributes(params);
        selectDialog.setCanceledOnTouchOutside(true);//设置点击Dialog外部任意区域关闭Dialog
        selectDialog.show();
    }

    private void callPhone(String phoneNum){
        Intent phoneIntent = new Intent("android.intent.action.CALL",
        Uri.parse("tel:" + phoneNum));
        //启动
        startActivity(phoneIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick({R.id.img_back, R.id.img_more,R.id.detail_map_view, R.id.detail_phone_tv, R.id.detail_bottom_phone_tv,
            R.id.detail_open_address, R.id.detail_go_review, R.id.detail_bottom_collect_view,
            R.id.detail_share_view,R.id.detail_category_tv,R.id.detail_sub_category_tv,
            R.id.detail_sub1_category_tv,R.id.detail_sub2_category_tv,R.id.detail_sub3_category_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.detail_map_view://地图详情
                Utils.gotoNewAct(ShopDetailActivity.this,MapsActivity.class,mInfo);
                Log.i(TAG,"OnClick detail_map_view ");
                break;
            case R.id.detail_open_address://打开地图
                Uri mUri = Uri.parse("geo:" + PreferencesUtils.getString(this,"location",""));//latitude + "," + longitude + "?q=" + zoneName
                Intent mIntent = new Intent(Intent.ACTION_VIEW, mUri);
                startActivity(mIntent);

                Log.i(TAG,"OnClick detail_open_address ");
                break;
            case R.id.img_more://title更多按钮
                showDialog(moreAdapter);
                Log.i(TAG,"OnClick img_more ");
                break;
            case R.id.detail_share_view://分享
//                Share.shareMsg(ShopDetailActivity.this,mInfo.getContent(),URLInfo.shareDetailUrl+mInfo.get_id());
                shareOut();
//                Share.shareImage(this,URLInfo.shareDetailUrl+mInfo.get_id());
//                share(URLInfo.shareDetailUrl+mInfo.get_id());
                Log.i(TAG,"OnClick detail_share_view ");
                break;
            case R.id.detail_go_review://评价
                if("".equals(PreferencesUtils.getString(this,Constant.SP_USER_ID,""))){
                    Utils.gotoNewAct(this,RegisterActivity.class);
                    return;
                }
                Utils.gotoNewAct(ShopDetailActivity.this,EvaluateActivity.class,mInfo);
                Log.i(TAG,"OnClick detail_go_review ");
                break;
            case R.id.detail_bottom_collect_view://添加、取消收藏
                Log.i(TAG,"OnClick detail_bottom_collect_tv ");
                if("".equals(PreferencesUtils.getString(ShopDetailActivity.this,Constant.SP_USER_ID,""))){
                    Utils.gotoNewAct(ShopDetailActivity.this,RegisterActivity.class);
                    return;
                }
                collectRequest();
                break;
            case R.id.detail_category_tv://分类
               gotoListAct(classId,classStr);
                Log.i(TAG,"OnClick detail_category_tv ");
                break;
            case R.id.detail_sub_category_tv://子分类
                gotoListAct(subClassId,subClassStr);
                break;
            case R.id.detail_sub1_category_tv://子分类
                gotoListAct(sub1ClassId,sub1ClassStr);
                break;
            case R.id.detail_sub2_category_tv://子分类
                gotoListAct(sub2ClassId,sub2ClassStr);
                break;
            case R.id.detail_phone_tv:
            case R.id.detail_bottom_phone_tv:
                if(!"".equals(mPhoneTv.getText().toString().trim())){
                    String phoneNum = mPhoneTv.getText().toString().trim().replace(" ", "");
                    Log.i(TAG,"OnClick detail_phone_tv phoneNum ===== "
                            + phoneNum);
                    callPhone(phoneNum);
                }
                break;
        }
    }

    private void gotoListAct(String classId,String classStr){
        intent.setClass(this, ShopListActivity.class);
        intent.putExtra("id",classId);//分类id
        intent.putExtra("name",classStr);//分类名称
        intent.putExtra("all", PreferencesUtils.getString(this,"all",""));
//                Utils.gotoNewAct(this,ShopListActivity.class);
        startActivity(intent);
    }

    private void getInfoRequest(){
        mWaitDialog.show();
        String url = URLInfo.infoDetail;
        Map<String, String> params = new HashMap<>();
        Log.i(TAG,"getInfoRequest detailId ==  "+detailId);
//        if(!"".equals(detailId)){
//            params.put("item_id", detailId);
//        }else{
            params.put("item_id", mInfo.get_id());//   "594cd4d723679c0e4d69dcfc"
//        }
        Log.i(TAG,"getInfoRequest url === " + url+"  params == "+params.toString());
        String userId = PreferencesUtils.getString(this,Constant.SP_USER_ID,"");
        Log.i(TAG,"getInfoRequest userId === " + userId );
        OkHttpUtils.getInstance().okHttpGet(url,userId, params,PreferencesUtils.getString(this,"Version","1.0"), new RequestCallBack() {
            @Override
            public void onSuccess(String result) {
                mWaitDialog.dismiss();
                if(null != result){
                    Log.i(TAG,"result = "+result);
                    mDetBussiness = (ShopDetail) GsonUtil.praseJsonToModel(result, ShopDetail.class);
                    Log.i(TAG,"mDetBussiness = "+mDetBussiness);
                    mHandler.sendEmptyMessage(1);

                }
            }

            @Override
            public void onFail() {
                mWaitDialog.dismiss();
                Toast.makeText(ShopDetailActivity.this,getResources().getString(R.string.net_bad_retry), Toast.LENGTH_LONG).show();
            }

        });

    }

    private void collectRequest(){
        mWaitDialog.show();
        String url = URLInfo.addCollect;
        Map<String, String> params = new HashMap<>();
        params.put("item_id", mInfo.get_id());//   "594cd4d723679c0e4d69dcfc"
        String status = "";
        if(mIsCollection){
            status = "0";
        }else{
            status = "1";
        }
        params.put("status", status);//1 收藏 2 取消收藏
        Log.i(TAG,"collectRequest url === " + url+"  params == "+params.toString());
        String userId = PreferencesUtils.getString(this,Constant.SP_USER_ID,"");

        Log.i(TAG,"collectRequest userId === " + userId );
        OkHttpUtils.getInstance().okHttpGet(url,userId, params,PreferencesUtils.getString(this,"Version","1.0"), new RequestCallBack() {
            @Override
            public void onSuccess(String result) {
                mWaitDialog.dismiss();
                if(null != result){
                    Log.i(TAG,"result = "+result);
                    if(null != result && result.contains("\"status\": \"ok\"")){
                        mGetInfo = true;
                        mIsCollection = !mIsCollection;
                        Log.i(TAG,"result mIsCollection === "+mIsCollection);
                        if(mIsCollection){
                            moreDatas.set(1,getResources().getString(R.string.cancle_collect));
                            mCollectionTv.setText(getResources().getString(R.string.cancle_collect));
//                            mCollectionIv.setBackgroundResource(R.drawable.collection1);
                        }else{
                            moreDatas.set(1,getResources().getString(R.string.collect));
                            mCollectionTv.setText(getResources().getString(R.string.collect));
//                            mCollectionIv.setBackgroundResource(R.drawable.menu_collection);
                        }
                        moreAdapter.setData(moreDatas);
                    }
                }
            }

            @Override
            public void onFail() {
                mWaitDialog.dismiss();
                Toast.makeText(ShopDetailActivity.this,getResources().getString(R.string.net_bad_retry), Toast.LENGTH_LONG).show();
            }

        });

    }

    private void setViewPagerPoint() {
        mViewPagerPoint.removeAllViews();
        Log.i(TAG,"mInfo.getImgs().size() === "+mInfo.getImgs().size());
        mBottomImages = new ImageView[mInfo.getImgs().size()];
        for (int i = 0; i < mInfo.getImgs().size(); i++) {
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Utils.dip2px(this, 5), Utils.dip2px(this, 5));
            params.setMargins(Utils.dip2px(this, 5), Utils.dip2px(this, 5), Utils.dip2px(this, 5), Utils.dip2px(this, 5));
            imageView.setLayoutParams(params);
            if (i == 0) {
                imageView.setBackgroundResource(R.drawable.orange_point);
            } else {
                imageView.setBackgroundResource(R.drawable.gray_point);
            }

            mBottomImages[i] = imageView;
            //把指示作用的原点图片加入底部的视图中
            mViewPagerPoint.addView(mBottomImages[i]);
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int total = mBottomImages.length;
                for (int j = 0; j < total; j++) {
                    if (j == position) {
                        mBottomImages[j].setBackgroundResource(R.drawable.orange_point);
                    } else {
                        mBottomImages[j].setBackgroundResource(R.drawable.gray_point);
                    }
                }
                //设置全局变量，currentIndex为选中图标的 index
                autoCurrIndex = position;
            }

            int preState;
            @Override
            public void onPageScrollStateChanged(int state) {
                if (preState == 1 && state == 0 && autoCurrIndex == 0) {
                   mViewPager.setCurrentItem(mBottomImages.length-1,false);
                }else if(preState == 1 && state == 0 && autoCurrIndex == mBottomImages.length-1){
                    mViewPager.setCurrentItem(0,false);
                }
                preState = state;
            }
        });
        mHandler.sendEmptyMessageDelayed(2, 3000);


//        mViewPager.setOnTouchListener(new View.OnTouchListener() {
//            float startX;
//            float startY;
//            float endX;
//            float endY;
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                isStop = true;
//                switch (event.getAction()){
//                    case MotionEvent.ACTION_DOWN:
//                        startX=event.getX();
//                        startY=event.getY();
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        endX=event.getX();
//                        endY=event.getY();
//                        WindowManager windowManager= (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
//                        //获取屏幕的宽度
//                        Point size = new Point();
//                        windowManager.getDefaultDisplay().getSize(size);
//                        int width=size.x;
//                        //首先要确定的是，是否到了最后一页，然后判断是否向左滑动，并且滑动距离是否符合，我这里的判断距离是屏幕宽度的4分之一（这里可以适当控制）
//                        if(mViewPager.getCurrentItem() == (mInfo.getImgs().size()-1) && startX-endX > 0 && startX-endX >= (width/4)){
//                            Log.i(TAG,"进入了触摸");
//                            mViewPager.setCurrentItem(0,false);
//                        }
//                        break;
//                }
//                return false;
//            }
//        });

    }

    private void addLikeRequest(final String id,final int position){//点赞
        String url = URLInfo.addLike;
//        mWaitDialog.show();
        Map<String, String> params = new HashMap<>();
        params.put("review_id", id);
        Log.i(TAG,"addLikeRequest url === " + url+"  params == "+params.toString());
        String userId = PreferencesUtils.getString(this,Constant.SP_USER_ID,"");

        Log.i(TAG,"addLikeRequest userId === " + userId );
        OkHttpUtils.getInstance().okHttpPost(url,userId , params, new RequestCallBack() {
            @Override
            public void onSuccess(String result) {
//                mWaitDialog.dismiss();
                if(null != result){
                    Log.i(TAG,"addLikeRequest  result = "+result);
                    if(!result.contains("\"msg\": \"user liked!\"")){
                        Log.i(TAG,""+mDetBussiness.getData().getData().getReviews().size());
                       int likeCount = Integer.parseInt(
                               mDetBussiness.getData().getData().getReviews().get(position).getLike_count()
                               != null ? mDetBussiness.getData().getData().getReviews().get(position).getLike_count() : "0") + 1;
                        mDetBussiness.getData().getData().getReviews().get(position).setLike_count(likeCount+"");
                        mDetBussiness.getData().getData().getReviews().get(position).setIs_liked(1);
                        reviewAdapter.setData(mDetBussiness.getData().getData().getReviews());
//                        getInfoRequest();
                    }
                }
            }

            @Override
            public void onFail() {
//                mWaitDialog.dismiss();
                Toast.makeText(ShopDetailActivity.this,getResources().getString(R.string.net_bad_retry), Toast.LENGTH_LONG).show();
            }

        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        mHandler.sendEmptyMessage(3);
    }
}
