package com.itmg.imachika.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.itmg.imachika.R;
import com.itmg.imachika.adapter.ShopAdapter;
import com.itmg.imachika.adapter.SortAdapter;
import com.itmg.imachika.adapter.TitleAdapter;
import com.itmg.imachika.application.APP;
import com.itmg.imachika.model.Shop;
import com.itmg.imachika.model.Classifal;
import com.itmg.imachika.model.SortData;
import com.itmg.imachika.util.GlideUtil;
import com.itmg.imachika.util.GsonUtil;
import com.itmg.imachika.util.OkHttpUtils;
import com.itmg.imachika.util.PreferencesUtils;
import com.itmg.imachika.util.RequestCallBack;
import com.itmg.imachika.util.URLInfo;
import com.itmg.imachika.view.SelectDialog;
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

public class ShopListActivity extends Activity implements OnMapReadyCallback
        ,GoogleMap.OnCameraIdleListener
        ,GoogleMap.OnCameraMoveStartedListener{
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.title)
    RelativeLayout titleRl;
    @BindView(R.id.title_list)
    RecyclerView titleList;
    @BindView(R.id.recyclerView)
    PullToRefreshListView mListView;
    @BindView(R.id.shop_list_no_data)
    LinearLayout mNoDataView;
    @BindView(R.id.no_data_cat)
    TextView mNoDataTv;
    Intent intent;
    String id;
    String name;
    String all;
    StringBuffer location = new StringBuffer();
    StringBuffer mapLocation = new StringBuffer();
    TitleAdapter titleAdapter;
    List<Classifal> alls = new ArrayList<>();
    List<Classifal> childClassifal = new ArrayList<>();
    APP app = APP.getMyApplication();
    @BindView(R.id.tv_map)
    TextView tvMap;
    @BindView(R.id.layout_list)
    LinearLayout layoutList;
    @BindView(R.id.layout_map)
    LinearLayout layoutMap;
    @BindView(R.id.bussiness_list_sort_lv)
    ListView sortLv;
    @BindView(R.id.img_more)
    ImageView imgMore;
    @BindView(R.id.tv_list)
    TextView tvList;
    GoogleMap map;
    private static final String TAG = "ShopListActivity";
    private WaitDialog mWaitDialog;
    List<Shop.Data.Info> mShopList = new ArrayList();
    List<Shop.Data.Info> mapsList = new ArrayList();
    ShopAdapter mShopAdapter;
    private SelectDialog selectDialog;
    private SortAdapter sortAdapter;
    private static final int START_PAGE = 0;
    private int index = 0;
    private boolean isMapMove;
//    private boolean isBack;
    private boolean isFrash = false;
    private String sortParam = "";
    private String shareUrl = "";
    private String address = "";
    private int zoomSize = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        ButterKnife.bind(this);
        Log.i(TAG, "onCreate ------------ ");
        app.addActivity(this);
//        if(app.getActivityNum() >= 10 ){
//            app.finishActivity(2);
//        }

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        Log.i(TAG, "location === " + location);
        tvMap.setVisibility(View.VISIBLE);
        intent = getIntent();
        id = intent.getStringExtra("id");
        name = intent.getStringExtra("name");
        all = intent.getStringExtra("all");
        location.append(PreferencesUtils.getString(this, "location", ""));
        if(!"".equals(intent.getStringExtra("address"))){
            address = intent.getStringExtra("address");
            Log.i(TAG,"onCreate   -----  address == "+address);
        }
        Map<String, Classifal> map = new Gson().fromJson(all, new TypeToken<HashMap<String, Classifal>>() {
        }.getType());
        Log.i(TAG, map.toString());
        for (Classifal classifal : map.values()) {
            alls.add(classifal);
        }
        for (Classifal classifal : alls) {
            if (id.equals(classifal.getParent_id())) {
                childClassifal.add(classifal);
            }
        }
        if (childClassifal.size() != 0) {
            Log.i(TAG, childClassifal.toString());
            //对User按照age降序排序
            Collections.sort(childClassifal, new Comparator<Classifal>() {
                @Override
                public int compare(Classifal o1, Classifal o2) {
                    return o1.getWeight()>o2.getWeight()?-1:1;
                }
            });
        } else {
            titleList.setVisibility(View.GONE);
        }
        tvTitle.setText(name);
        mWaitDialog = new WaitDialog(ShopListActivity.this, R.style.loading_dialog);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        titleList.setLayoutManager(linearLayoutManager);
        titleAdapter = new TitleAdapter(this, childClassifal);
        titleList.setAdapter(titleAdapter);
        titleAdapter.setOnItemClickListener(new TitleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                titleAdapter.setPosition(position);
                id = childClassifal.get(position).get_id();
                mShopList.clear();
                mNoDataView.setVisibility(View.GONE);
                mNoDataTv.setText(childClassifal.get(position).getName());
                index = START_PAGE;
                getInfoRequest();//true
            }
        });
        initView();
//        getInfoRequest();
        getSortData();
    }

    private void initView() {
        //设置可上拉刷新和下拉刷新
        mListView.setMode(PullToRefreshBase.Mode.BOTH);

        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新
                mShopList.clear();
                index = START_PAGE;
                sortParam = "";
                SortAdapter.selectPosition = 1;
                isFrash = true;
                getInfoRequest();//false

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载更多
                index++;
                getInfoRequest();//false

            }
        });

        mShopAdapter = new ShopAdapter(ShopListActivity.this, mShopList);
        mListView.setAdapter(mShopAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume ------------ ");
        if (mShopList.size() == 0) {
            getInfoRequest();//true
        }
    }

    @OnClick({R.id.img_back, R.id.img_more, R.id.tv_map, R.id.tv_list})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_more:
                showDialog(sortAdapter);
                Log.i(TAG, "onViewClicked img_more is clicked === sortLv.isShown() == "+sortLv.isShown());
                break;
            case R.id.tv_map:
                layoutMap.setVisibility(View.VISIBLE);
                layoutList.setVisibility(View.GONE);
                imgMore.setVisibility(View.GONE);
                tvList.setVisibility(View.VISIBLE);
                tvMap.setVisibility(View.GONE);
//                isBack = false;
                break;
            case R.id.tv_list:
                tvMap.setVisibility(View.VISIBLE);
                tvList.setVisibility(View.GONE);
                layoutList.setVisibility(View.VISIBLE);
                layoutMap.setVisibility(View.GONE);
                imgMore.setVisibility(View.VISIBLE);
//                isBack = true;
//                index = START_PAGE;
//                if(isBack && isMapMove){
//                    mShopList.clear();
//                    getInfoRequest();
//                }
                break;
        }
    }

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

    Shop bussiness;
    private void getInfoRequest() {//boolean isShow
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        mWaitDialog.show();
        String url = URLInfo.infoList;
        Map<String, String> params = new HashMap<>();
        params.put("catid", id);

        if(isMapMove){
            index = 0;
            params.put("page", index+"");
            params.put("location", mapLocation.toString());
//        }
//        else if(isBack){
//            index = 0;
//            params.put("page", index+"");
//            if(isFrash){//地图返回刷新列表
//                params.put("location", PreferencesUtils.getString(this, "location", ""));
//            }else{
//                params.put("location", !mapLocation.toString().equals("") ?
//                        mapLocation.toString() : PreferencesUtils.getString(this, "location", ""));
//            }
//            isFrash = false;
        }else{
            params.put("page", index + "");
            params.put("location", location.toString());
            params.put("sort", sortParam);
        }

        if(null != address && !"".equals(address)){
            params.put("address", address);
        }
        Log.i(TAG,"getInfoRequest   -----  address == "+address);

//        if(isBack){
//            Log.i(TAG, " -- isBack --  getInfoRequest url === " + url + params.toString());
//        }
        Log.i(TAG, "getInfoRequest url === " + url + params.toString());
        shareUrl = OkHttpUtils.attachHttpGetParams(URLInfo.shareListUrl,params);
        OkHttpUtils.getInstance().okHttpGet(url, params, PreferencesUtils.getString(this, "Version", "1.0"), new RequestCallBack() {
            @Override
            public void onSuccess(String result) {
                mWaitDialog.dismiss();
                mListView.onRefreshComplete();

                if (null != result) {
                    Log.i(TAG, "result = " + result);
                    try {
                        bussiness = (Shop) GsonUtil.praseJsonToModel(result, Shop.class);
                        Log.i(TAG, "bussiness.getData().getData().size() ==== " + bussiness.getData().getData().size());
                        if(layoutMap.isShown()){
                            Log.i(TAG,"layoutMap.isShown() ---------------- ");
                            if (null != bussiness.getData().getData()
                                    && bussiness.getData().getData().size() > 0) {
                                mapsList.addAll(removeContent(bussiness.getData().getData()));
                                mShopAdapter.setData(removeContent(bussiness.getData().getData()));
                                Log.i(TAG, "layoutMap.isShown() mapsList.size() ==== " + mapsList.size());
                            }
                        }else{
                            if (START_PAGE != index) {
                                if (null != bussiness.getData().getData()
                                        && bussiness.getData().getData().size() > 0) {
                                    mShopList.addAll(removeContent(bussiness.getData().getData()));
                                    Log.i(TAG, "mShopList.size() ==== " + mShopList.size());
                                    mapsList.addAll(mShopList);
                                    Log.i(TAG,"  START_PAGE != index mapsList.size ==="+mapsList);
                                    mShopAdapter.setData(removeContent(mShopList));
                                }
                            } else {
                                Log.i(TAG,"layoutMap.isHint() -------page === 0  --------- ");
//                                mapsList.clear();
                                if(null != map && !isMapMove){//地图不为空且地图没移动
                                    map.clear();
                                }
                                if (null != bussiness.getData().getData()
                                        && bussiness.getData().getData().size() == 0) {
                                    mListView.setVisibility(View.GONE);
                                    mNoDataView.setVisibility(View.VISIBLE);
                                } else {
                                    mListView.setVisibility(View.VISIBLE);
                                    mShopList.addAll(removeContent(bussiness.getData().getData()));
                                    mShopAdapter.setData(mShopList);
                                    mListView.getRefreshableView().setSelection(0);
                                }
                                mapsList.addAll(mShopList);
                                Log.i(TAG,"  START_PAGE == index mapsList.size ==="+mapsList.size());
                                index = 1;
                                sortParam = bussiness.getData().getSort() != null ?
                                        bussiness.getData().getSort() : sortParam;
//                                isBack = false;
                                getInfoRequest();
                            }
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
                Toast.makeText(ShopListActivity.this, getResources().getString(R.string.net_bad_retry), Toast.LENGTH_LONG).show();
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

    private void getSortData(){
        mWaitDialog.show();
        Map<String, String> params = new HashMap<>();
        OkHttpUtils.getInstance().okHttpGet(URLInfo.searchSortInfo ,PreferencesUtils.getString(this, "id", ""),params, PreferencesUtils.getString(this, "Version", "1.0"), new RequestCallBack() {
            @Override
            public void onSuccess(String result) {
                mWaitDialog.dismiss();
                mListView.onRefreshComplete();

                if (null != result) {
                    Log.i(TAG, " getSortData  result = " + result);
                    try {
                        SortData sortData = (SortData) GsonUtil.praseJsonToModel(result, SortData.class);
                        Log.i(TAG, " getSortData  sortData.getData().size() = " + sortData.getData().size());

                        ArrayList<String> datas = new ArrayList<>();
                        datas.add(getResources().getString(R.string.sort_share));
                        if("event".equals(id)){
                            if(null != sortData.getData().get(1).getText().getSort0()){
                                datas.add(sortData.getData().get(1).getText().getSort0());
                            }
                            if(null != sortData.getData().get(1).getText().getSort1()){
                                datas.add(sortData.getData().get(1).getText().getSort1());
                            }

                            if(null != sortData.getData().get(1).getText().getSort2()){
                                datas.add(sortData.getData().get(1).getText().getSort2());
                            }

                            if(null != sortData.getData().get(1).getText().getSort3()){
                                datas.add(sortData.getData().get(1).getText().getSort3());
                            }
                            if(null != sortData.getData().get(1).getText().getSort4()){
                                datas.add(sortData.getData().get(1).getText().getSort4());
                            }
                            if(null != sortData.getData().get(1).getText().getSort5()){
                                datas.add(sortData.getData().get(1).getText().getSort5());
                            }
                        }else{
                            if(null != sortData.getData().get(0).getText().getSort0()){
                                datas.add(sortData.getData().get(0).getText().getSort0());
                            }
                            if(null != sortData.getData().get(0).getText().getSort1()){
                                datas.add(sortData.getData().get(0).getText().getSort1());
                            }

                            if(null != sortData.getData().get(0).getText().getSort2()){
                                datas.add(sortData.getData().get(0).getText().getSort2());
                            }

                            if(null != sortData.getData().get(0).getText().getSort3()){
                                datas.add(sortData.getData().get(0).getText().getSort3());
                            }
                            if(null != sortData.getData().get(0).getText().getSort4()){
                                datas.add(sortData.getData().get(0).getText().getSort4());
                            }
                            if(null != sortData.getData().get(0).getText().getSort5()){
                                datas.add(sortData.getData().get(0).getText().getSort5());
                            }
                        }

                        sortAdapter = new SortAdapter(ShopListActivity.this,
                                datas,mSortItemClickListener,1);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFail() {
                mWaitDialog.dismiss();
                mListView.onRefreshComplete();
                Toast.makeText(ShopListActivity.this, getResources().getString(R.string.net_bad_retry), Toast.LENGTH_LONG).show();
            }

        });
    }

    /**
     * 实现类，响应按钮点击事件
     */
    private SortAdapter.MyClickListener mSortItemClickListener = new SortAdapter.MyClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            selectDialog.dismiss();
            switch (position){
                case 0:
                    Intent intent1=new Intent(Intent.ACTION_SEND);
                    intent1.putExtra(Intent.EXTRA_TEXT,shareUrl);
                    intent1.setType("text/plain");
                    startActivity(Intent.createChooser(intent1,"share"));
                    Log.i(TAG,"  sortLv  onItemClick share is clicked ");
                    break;
                case 1:
                    sortParam = "sort0";
                    break;
                case 2:
                    sortParam = "sort1";
                    break;
                case 3:
                    sortParam = "sort2";
                    break;
                case 4:
                    sortParam = "sort3";
                    break;
                case 5:
                    sortParam = "sort4";
                    break;
                case 6:
                    sortParam = "sort5";
                    break;
                default:
                    break;

            }
            if(0 != position){
                SortAdapter.selectPosition = position;
            }
            Log.i(TAG,"  SortAdapter.selectPosition  == "+SortAdapter.selectPosition);
            Log.i(TAG,"  sortLv  onItemClick sortParam  == "+sortParam);
            sortLv.setVisibility(View.GONE);
            mShopList.clear();
            index = START_PAGE;
            if(0 != position){
                getInfoRequest();
            }
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

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
                        LayoutInflater inflater = ShopListActivity.this.getLayoutInflater();
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
                                    GlideUtil.setImg(ShopListActivity.this,img,data.getSmall_img()
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
                                intent.setClass(ShopListActivity.this, ShopDetailActivity.class);
                                intent.putExtra("data", data);
                                ShopListActivity.this.startActivity(intent);
                                break;
                            }
                        }
                    }
                });
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
//            Log.i(TAG," ---- onCameraMoveStarted ---- The user tapped something on the map == "+location);
        } else if (reason == GoogleMap.OnCameraMoveStartedListener
                .REASON_DEVELOPER_ANIMATION) {
            isMapMove = false;
//            Log.i(TAG," ---- onCameraMoveStarted ---- The app moved the camera == "+location);
        }
    }

    @Override
    public void onCameraIdle() {
        CameraPosition cameraPosition = map.getCameraPosition();
        if(layoutMap.isShown()){
            if(isMapMove){
                mapLocation = new StringBuffer();
                mapLocation.append(cameraPosition.target.latitude);
                mapLocation.append(",");
                mapLocation.append(cameraPosition.target.longitude);
                Log.i(TAG," ---- onCameraIdle ---- layoutMap.isShown --- isMapMove " +
                        "mapLocation == "+mapLocation);
                getInfoRequest();
            }
        }
    }
}
