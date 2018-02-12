package com.itmg.imachika.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatRatingBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.itmg.imachika.R;
import com.itmg.imachika.adapter.ExpandAdapter;
import com.itmg.imachika.adapter.GridAdapter;
import com.itmg.imachika.adapter.ShopAdapter;
import com.itmg.imachika.application.APP;
import com.itmg.imachika.model.All;
import com.itmg.imachika.model.Shop;
import com.itmg.imachika.model.Rank;
import com.itmg.imachika.util.GlideUtil;
import com.itmg.imachika.util.GsonUtil;
import com.itmg.imachika.util.OkHttpUtils;
import com.itmg.imachika.util.PreferencesUtils;
import com.itmg.imachika.util.RequestCallBack;
import com.itmg.imachika.util.URLInfo;
import com.itmg.imachika.view.MyExpandListView;
import com.itmg.imachika.view.MyGridView;
import com.itmg.imachika.view.WaitDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class SearchActivity extends Activity implements OnMapReadyCallback
        ,GoogleMap.OnCameraIdleListener
        ,GoogleMap.OnCameraMoveStartedListener{// implements OkHttpUtil.Result

    @BindView(R.id.grid_view)
    MyGridView gridView;
    @BindView(R.id.search_list)
    TextView listTv;
    @BindView(R.id.ex_listView)
    MyExpandListView exListView;
    @BindView(R.id.search_old_view)
    LinearLayout mOldView;
    @BindView(R.id.search_list_view)
    LinearLayout layoutList;
    @BindView(R.id.layout_map)
    LinearLayout layoutMap;
    @BindView(R.id.shop_list_no_data)
    LinearLayout mNoDataView;
    @BindView(R.id.search_lv)
    PullToRefreshListView mListView;
    @BindView(R.id.title)
    RelativeLayout title;
    @BindView(R.id.search_map_view)
    LinearLayout mapView;
    @BindView(R.id.layout_search)
    LinearLayout layoutSearch;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.no_data_cat)
    TextView mNoDataTv;
//    @BindView(R.id.search_tv_now)
//    TextView nowAddTv;
    @BindView(R.id.search_delete_iv)
    ImageView deleteIv;
    @BindView(R.id.search_scrollview)
    ScrollView mScrollView;

    List<Rank> list = new ArrayList<>();
    List<All> groups = new ArrayList<>();
    List<List<All.Cat2s>> chlids = new ArrayList<>();
    GridAdapter adapter;
    ExpandAdapter expandAdapter;
    Intent intent;
    String rank, all, alls;
    GoogleMap map;
    APP app = APP.getMyApplication();
    private static final String TAG = "SearchActivity";
    private WaitDialog mWaitDialog;
    List<Shop.Data.Info> bussinessList = new ArrayList();
    List<Shop.Data.Info> mapsList = new ArrayList();
    ShopAdapter mBussinessAdapter;
    private static final int START_PAGE = 0;
    private int index = 0;
    StringBuffer location = new StringBuffer();
    private boolean isMapMove;
//    private boolean isBack;
    private boolean isFrash = false;
    private String searchStr = "";
    private String searchAddStr = "";
    private String shareUrl = "";
    private int zoomSize = 13;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        app.addActivity(this);
        exListView.setGroupIndicator(null);//去掉箭头
//        sp = getSharedPreferences("login",MODE_PRIVATE);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        location.append(PreferencesUtils.getString(this,"location",""));
        intent = getIntent();
        rank = (String) intent.getSerializableExtra("rank");
        all = (String) intent.getSerializableExtra("all");
        alls = (String) intent.getSerializableExtra("alls");
        mWaitDialog = new WaitDialog(SearchActivity.this,R.style.loading_dialog);
        if (null !=rank && null != all && !rank.isEmpty() && !all.isEmpty()) {
            List<Object> objectList = GsonUtil.praseJsonToList(rank, Rank.class);
            for (Object o : objectList) {
                Rank rank1 = (Rank) o;
                list.add(rank1);
            }
            adapter = new GridAdapter(list, this);
            gridView.setAdapter(adapter);
            List<Object> allList = GsonUtil.praseJsonToList(all, All.class);
            for (Object o : allList) {
                All all1 = (All) o;
                groups.add(all1);
                chlids.add(all1.getCat2s());
            }
            expandAdapter = new ExpandAdapter(this, groups, chlids);
            exListView.setAdapter(expandAdapter);
            exListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                @Override
                public void onGroupExpand(int groupPosition) {
                    Log.i(TAG,"gridView.getHeight() === "
                            +(gridView.getHeight()));
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
                    intent.setClass(SearchActivity.this, ShopListActivity.class);
                    intent.putExtra("id", chlids.get(i).get(i1).get_id());
                    intent.putExtra("name", chlids.get(i).get(i1).getName());
                    intent.putExtra("all", alls);
                    Log.i(TAG,"   putExtra  searchAddStr == "+etAddress.getText().toString().trim());
                    intent.putExtra("address", etAddress.getText().toString().trim());
                    startActivity(intent);
                    return true;
                }
            });
        }

        initView();
        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("--setOnClickListener--", "mOldView.isShown() == "+mOldView.isShown());
                if(!mOldView.isShown()){
                    if(!"".equals(searchStr)){
                        etSearch.setText(searchStr);
                    }else{
                        etSearch.setHint(getResources().getString(R.string.search_hint_content));
                    }
                    mOldView.setVisibility(View.VISIBLE);
                    mapView.setVisibility(View.GONE);
                    if(!"".equals(etAddress.getText().toString().trim())){
                        deleteIv.setVisibility(View.VISIBLE);
                    }else{
                        etAddress.setHint(getResources().getString(R.string.search_hint_add_now));
                    }
                    mListView.setVisibility(View.GONE);
                }
            }
        });

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.i(TAG, "搜索操作执行");
                    searchStr = etSearch.getText().toString().trim();
                    searchAddStr = etAddress.getText().toString().trim();
                    etSearch.setHint(searchStr+"  "+etAddress.getText());
                    bussinessList.clear();
                    index = START_PAGE;
                    getInfoRequest();
                    mListView.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Log.i(TAG,"-- OnFocusChangeListener --" + "mOldView.isShown() == "+mOldView.isShown());
                deleteIv.setVisibility(View.VISIBLE);
                mapView.setVisibility(View.GONE);
                if(!"".equals(etAddress.getText().toString().trim())){

                }else{
                    etAddress.setHintTextColor(getResources().getColor(R.color.blue));
                    etAddress.setHint(getResources().getString(R.string.search_hint_add_now));
                }

            }
        });
        etAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if("".equals(etAddress.getText().toString().trim())){
                    etAddress.setHintTextColor(getResources().getColor(R.color.title_text));
                    etAddress.setHint(getResources().getString(R.string.search_hint_add));
                    deleteIv.setVisibility(View.GONE);
                }else{
                    deleteIv.setVisibility(View.VISIBLE);
                }

            }
        });

        etAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.i("--etAddress--", "搜索操作执行");
                    searchStr = etSearch.getText().toString().trim();
                    searchAddStr = etAddress.getText().toString().trim();
                    etSearch.setHint(searchStr+"  "+etAddress.getText());
                    bussinessList.clear();
                    index = START_PAGE;
                    getInfoRequest();
                    mListView.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        etAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!"".equals(etAddress.getText().toString().trim())){
                    deleteIv.setVisibility(View.VISIBLE);
                } else{
                    deleteIv.setVisibility(View.GONE);
                }
            }
        });

    }

    private void initView(){
        //设置可上拉刷新和下拉刷新
        mListView.setMode(PullToRefreshBase.Mode.BOTH);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新
                bussinessList.clear();
                index = START_PAGE;
                isFrash = true;
                getInfoRequest();

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载更多
                index++;
                getInfoRequest();
            }
        });

        mBussinessAdapter = new ShopAdapter(SearchActivity.this, bussinessList);
        mListView.setAdapter(mBussinessAdapter);
    }

    Shop bussiness;
    private void getInfoRequest(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        mWaitDialog.show();
        String url = URLInfo.infoList;
        Map<String, String> params = new HashMap<>();
        if("".equals(etSearch.getText().toString().trim())){//搜索关键字为空
            params.put("catid", "store" );
        }else{
            params.put("q", searchStr );
        }

        if(isMapMove){
            index = 0;
            params.put("page", index+"");
            params.put("location", location.toString());
//        }else if(isBack){
//            index = 0;
//            params.put("page", index+"");
//            if(isFrash){//地图返回刷新列表
//                params.put("location", PreferencesUtils.getString(this, "location", ""));
//            }else{
//                params.put("location", !location.toString().equals("") ?
//                        location.toString() : PreferencesUtils.getString(this, "location", ""));
//            }
//            isFrash = false;
        }else{
            params.put("page", index + "");
            params.put("location", location.toString());
//            params.put("sort", sortParam);
        }

        if(!"".equals(searchAddStr)){//地址为空
            params.put("address", searchAddStr );
        }
        params.put("location", location.toString() );

        params.put("page", index+"" );

        if(0 != index){
            params.put("es", bussiness.getData().getResultinfo().getEs()+"" );
            params.put("g", bussiness.getData().getResultinfo().getG()+"" );
            params.put("y", bussiness.getData().getResultinfo().getY()+"" );
            params.put("limit", "32" );
            params.put("g_next_page_token", bussiness.getData().getResultinfo().getG_next_page_token()+"" );
            params.put("is_fulfil", bussiness.getData().getIs_fulfil()+"" );
        }
//        if(isBack){
//            Log.i(TAG, " -- isBack --  getInfoRequest url === " + url + params.toString());
//        }
        Log.i(TAG, "getInfoRequest url === " + url + params.toString());
        shareUrl = OkHttpUtils.attachHttpGetParams(URLInfo.shareListUrl,params);
        OkHttpUtils.getInstance().okHttpGet(url, params, PreferencesUtils.getString(this,"Version","1.0"), new RequestCallBack() {
            @Override
            public void onSuccess(String result) {
                mWaitDialog.dismiss();
                mListView.onRefreshComplete();
                if(null != result){
                    Log.i(TAG,"result = "+result);
                    mOldView.setVisibility(View.GONE);
                    if(!isMapMove){
                        mapView.setVisibility(View.VISIBLE);
                    }
                    try {
                        bussiness = (Shop) GsonUtil.praseJsonToModel(result, Shop.class);
                        if(layoutMap.isShown()){
                            Log.i(TAG,"layoutMap.isShown() ---------------- ");
                            if (null != bussiness.getData().getData()
                                    && bussiness.getData().getData().size() > 0) {
                                mapsList.addAll(removeContent(bussiness.getData().getData()));
                                mBussinessAdapter.setData(removeContent(bussiness.getData().getData()));
                                Log.i(TAG, "layoutMap.isShown() mapsList.size() ==== " + mapsList.size());
                            }
                        }else {
                            if (START_PAGE != index) {
                                Log.i(TAG, "bussiness.getData().getData().size() ==== " + bussiness.getData().getData().size());
                                if (null != bussiness.getData().getData()
                                        && bussiness.getData().getData().size() > 0) {
                                    bussinessList.addAll(removeContent(bussiness.getData().getData()));
                                    Log.i(TAG, "bussinessList.size() ==== " + bussinessList.size());
                                    mBussinessAdapter.setData(bussinessList);
                                }
                            } else {
                                if (null != bussiness.getData().getData()
                                        && bussiness.getData().getData().size() == 0) {
                                    mListView.setVisibility(View.GONE);
                                    mNoDataView.setVisibility(View.VISIBLE);
                                    mNoDataTv.setText(searchStr);
                                } else {
//                                    mapsList.clear();
                                    mListView.setVisibility(View.VISIBLE);
                                    bussinessList.addAll(removeContent(bussiness.getData().getData()));
                                    mBussinessAdapter.setData(bussinessList);
//                                    isBack = false;
                                    mListView.getRefreshableView().setSelection(0);
                                }

                            }
                            mapsList.addAll(bussinessList);
                        }
                        if(null != mapsList && mapsList.size() > 0){
                            setMapZoom(mapsList);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFail() {
                mWaitDialog.dismiss();
                mListView.onRefreshComplete();
                Toast.makeText(SearchActivity.this,getResources().getString(R.string.net_bad_retry), Toast.LENGTH_LONG).show();
            }

        });

    }

    private void setMapZoom(List<Shop.Data.Info> datas){//latLng
        ArrayList<Double> lats = new ArrayList<>();
        ArrayList<Double> lngs = new ArrayList<>();
        for(int i = 0 ; i < datas.size() ; i ++){
            String location [] = datas.get(i).getLocation().split(",");
            lats.add(Double.parseDouble(location[0]));
            lngs.add(Double.parseDouble(location[1]));
        }

        lats = getSortLocation(lats);
        lngs = getSortLocation(lngs);

//        setAutoZoom(lngs.get(0),lngs.get(lats.size()-1),lats.get(0),lats.get(lats.size()-1));
        setAutoZoom(lngs.get(0),lngs.get(lats.size()-1),lats.get(0),lats.get(lats.size()-1));
    }

    //根据经纬极值计算绽放级别。
    private void setAutoZoom (double maxLng,double minLng, double maxLat,double minLat) {
        double [] zooms = {50,100,200,500,1000,2000,5000,10000,20000,25000,50000,100000,200000,500000,1000000,2000000};//级别18到3。
//        LatLng pointA = new LatLng(maxLng,maxLat);  // 创建点坐标A
//        LatLng pointB = new LatLng(minLng,minLat);  // 创建点坐标B
//        LatLngBounds boundA = new LatLngBounds(pointA,pointB);
//        let distance:Double = location1.distance(from:location2)
        int zoomSize = 14;
        double distance = getDistanceBetweenTwoPoints(maxLat,maxLng,minLat,minLng);//获取两点距离,保留小数点后两位
//        map.setLatLngBoundsForCameraTarget(boundA);
        Log.i(TAG,"setAutoZoom  distance ==== "+distance);
        for (int i = 0,zoomLen = zooms.length; i < zoomLen; i++) {
            if(zooms[i] - distance > 0){
                zoomSize = 18-i+3;
                break;
            }
        }
        Log.i(TAG,"setAutoZoom  zoomSize ==== "+zoomSize);
        setMaps(mapsList);//设置地图maker
    }

    public Double getDistanceBetweenTwoPoints(Double latitude1, Double longitude1, Double latitude2, Double longitude2) {
        final int RADIUS_EARTH = 6371;

        double dLat = getRad(latitude2 - latitude1);
        double dLong = getRad(longitude2 - longitude1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(getRad(latitude1)) * Math.cos(getRad(latitude2)) * Math.sin(dLong / 2) * Math.sin(dLong / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return (RADIUS_EARTH * c) * 1000;
    }

    private Double getRad(Double x) {
        return x * Math.PI / 180;
    }

    private ArrayList<Double> getSortLocation(ArrayList<Double> datas){
        Collections.sort(datas, new Comparator<Double>() {
            @Override
            public int compare(Double o1, Double o2) {
                return o1 > o2 ? -1:1;
            }
        });
        return datas;
    }

    private List<Shop.Data.Info> removeContent(List<Shop.Data.Info> datas){
        for (int i = 0; i < datas.size() - 1; i++) {
            for (int j = datas.size() - 1; j > i; j--) {
                if ((datas.get(j).get_id() != null ? datas.get(j).get_id() : "0")
                        .equals((datas.get(i).get_id() != null ? datas.get(i).get_id() : "1"))
                        || (datas.get(j).getY_id() != null ? datas.get(j).getY_id() : "")
                        .equals((datas.get(i).getY_id() != null ? datas.get(i).getY_id() : "1"))
                        || (datas.get(j).getG_id() != null ? datas.get(j).getG_id() : "")
                        .equals((datas.get(i).getG_id() != null ? datas.get(i).getG_id() : "1"))) {
                    datas.remove(j);
                }
            }
        }
        return datas;
    }

    @OnItemClick(R.id.grid_view)
    void onItemClick(int position) {
        intent = new Intent();
        intent.setClass(this, ShopListActivity.class);
        intent.putExtra("id", list.get(position).get_id());
        intent.putExtra("name", list.get(position).getName());
        intent.putExtra("all", alls);
        intent.putExtra("address", etAddress.getText().toString().trim());
        startActivity(intent);
    }

    @OnClick({R.id.tv_cancel,R.id.search_delete_iv,R.id.search_share,R.id.search_map,R.id.search_list})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.search_delete_iv:
//                if(deleteIv.isShown()){
//                    deleteIv.setVisibility(View.GONE);
//                }

                etAddress.setText("");
                etAddress.setHintTextColor(getResources().getColor(R.color.title_text));
                etAddress.setHint(getResources().getString(R.string.search_hint_add));
                break;
            case R.id.search_share:
                Intent intent1=new Intent(Intent.ACTION_SEND);
                intent1.putExtra(Intent.EXTRA_TEXT,shareUrl);
                intent1.setType("text/plain");
                startActivity(Intent.createChooser(intent1,"share"));
                Log.i(TAG,"  sortLv  onItemClick share is clicked ");
                break;
            case R.id.search_map:
                layoutMap.setVisibility(View.VISIBLE);
                layoutList.setVisibility(View.GONE);
                mapView.setVisibility(View.GONE);
                listTv.setVisibility(View.VISIBLE);
//                isBack = false;
                break;
            case R.id.search_list:
                layoutMap.setVisibility(View.GONE);
                layoutList.setVisibility(View.VISIBLE);
                mapView.setVisibility(View.VISIBLE);
                listTv.setVisibility(View.GONE);
//                isBack = true;
//                index = START_PAGE;
//                if(isBack && isMapMove){
//                    Log.i(TAG,"search_list is Clicked --- ");
//                    bussinessList.clear();
//                    getInfoRequest();
//                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        Log.i(TAG,"onMapReady map is created ------ ");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setOnCameraIdleListener(this);
        map.setOnCameraMoveStartedListener(this);
        map.setMyLocationEnabled(true);
//        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(true);

    }

    private void setMaps(List<Shop.Data.Info> dateInfos){
        Log.i(TAG,"  setMaps  dateInfos.size ==="+dateInfos.size());
        for(Shop.Data.Info dateInfo : dateInfos){
            String locationStr = dateInfo.getLocation();
            if(!"".equals(locationStr)){
//                Log.i(TAG,"setMaps  locationStr == "+locationStr);
                String location [] = locationStr.split(",");

                LatLng latLng = new LatLng(Float.parseFloat(location[0]), Float.parseFloat(location[1]));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoomSize));
                map.addMarker(new MarkerOptions().position(latLng).title(dateInfo.getContent()));//.title(mShopList.get(i).getContent()

                map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) {
                        LayoutInflater inflater = SearchActivity.this.getLayoutInflater();
                        View view = inflater.inflate(R.layout.map_title_view,null);
                        ImageView img = view.findViewById(R.id.img);
                        TextView name = view.findViewById(R.id.tv_name);
                        TextView time = view.findViewById(R.id.tv_time);
                        TextView add = view.findViewById(R.id.tv_address);
                        AppCompatRatingBar rant = view.findViewById(R.id.ratingBar);
                        for(Shop.Data.Info data : mapsList){
                            if(marker.getTitle().equals(data.getContent())) {
                                if(null != data.getSmall_img() || null != data.getBig_img()){
                                    img.setVisibility(View.VISIBLE);
                                    GlideUtil.setImg(SearchActivity.this,img,data.getSmall_img()
                                            != null ? data.getSmall_img() : (data.getBig_img() != null ?
                                            data.getBig_img() :""));
                                }
                                name.setText(data.getContent());
                                add.setText(data.getAddress());
                                if(null != data.getOrigin_start_time_format() && null != data.getOrigin_end_time_format()){
                                    time.setVisibility(View.VISIBLE);
                                    time.setText(data.getOrigin_start_time_format()+" ～ "+data.getOrigin_end_time_format());
                                }
                                time.setText(data.getAddress());
                                rant.setRating(Double.valueOf(data.getRating()).floatValue());
                            }
                        }

                        return view;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        return null;
                    }
                });

                map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker)
                    {
                        for(Shop.Data.Info data : mapsList){
                            if(marker.getTitle().equals(data.getContent())){
                                Intent intent = new Intent();
                                intent.setClass(SearchActivity.this, ShopDetailActivity.class);
                                intent.putExtra("data", data);
                                SearchActivity.this.startActivity(intent);
                                break;
                            }
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onCameraIdle() {
        CameraPosition cameraPosition = map.getCameraPosition();
        if(layoutMap.isShown()){
            if(isMapMove){
                location = new StringBuffer();
                location.append(cameraPosition.target.latitude);
                location.append(",");
                location.append(cameraPosition.target.longitude);
                Log.i(TAG," ---- onCameraIdle ---- layoutMap.isShown --- isMapMove " +
                        "mapLocation == "+location);
                getInfoRequest();
            }
        }
    }

    @Override
    public void onCameraMoveStarted(int reason) {

        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            isMapMove = true;
        } else if (reason == GoogleMap.OnCameraMoveStartedListener
                .REASON_API_ANIMATION) {

            isMapMove = false;
            Log.i(TAG," ---- onCameraMoveStarted ---- The user tapped something on the map == "+location);
        } else if (reason == GoogleMap.OnCameraMoveStartedListener
                .REASON_DEVELOPER_ANIMATION) {
            isMapMove = false;
            Log.i(TAG," ---- onCameraMoveStarted ---- The app moved the camera == "+location);
        }
    }

}
