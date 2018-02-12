package com.itmg.imachika;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.itmg.imachika.application.APP;
import com.itmg.imachika.fragment.AddFragment;
import com.itmg.imachika.fragment.ChatFragment;
import com.itmg.imachika.fragment.HomeFragment;
import com.itmg.imachika.fragment.MeFragment;
import com.itmg.imachika.ui.MeActivity;
import com.itmg.imachika.ui.RegisterActivity;
import com.itmg.imachika.util.Constant;
import com.itmg.imachika.util.PreferencesUtils;
import com.itmg.imachika.util.Utils;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.yuyh.library.imgsel.utils.StatusBarCompat.getStatusBarHeight;

public class MainActivity extends Activity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.content)
    LinearLayout content;
    @BindView(R.id.bottom)
    RadioGroup bottom;

    HomeFragment homeFragment;
    ChatFragment chatFragment;
    MeFragment meFragment;
    AddFragment addFragment;

    FragmentManager fm;
    FragmentTransaction ft;

    private String TAG = "MainActivity";
    //    SharedPreferences sp;
    boolean isLogin;
    Intent intent = new Intent();
    @BindView(R.id.radio_btn1)
    RadioButton radioBtn1;
    APP app = APP.getMyApplication();
    public static int table = -1;
//    public static io.socket.client.Socket socket;
//    LocationListener locationListener;
    //实例化监听对象
    private static PermissionListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Log.i(TAG,"onCreate ------- ");
        app.addActivity(this);
//        addStatusViewWithColor(this,R.color.title_text);
//        Log.i(TAG,"key ==== "+ app.getKey());
//        getKey();
        homeFragment = new HomeFragment();
        replaceFragment(homeFragment);
        tvTitle.setText(getResources().getString(R.string.home));
//        bottom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                switch (i) {
//                    case R.id.radio_btn1://首页
//                        tvTitle.setText(getResources().getString(R.string.home));
//                        if (homeFragment == null) {
//                            homeFragment = new HomeFragment();
//                        }
//                        replaceFragment(homeFragment);
//                        break;
//                    case R.id.radio_btn2://消息
//                        if (isLogin) {
//                            tvTitle.setText(getResources().getString(R.string.chat));
//                            if (chatFragment == null) {
//                                chatFragment = new ChatFragment();
//                            }
//                            replaceFragment(chatFragment);
//                        } else {
//                            goLogin();
//                        }
//                        break;
//                    case R.id.radio_btn3://发稿
//                        if (isLogin) {
//                            tvTitle.setText(getResources().getString(R.string.add));
//                            if (addFragment == null) {
//                                addFragment = new AddFragment();
//                            }
//                            replaceFragment(addFragment);
//                            break;
//                        } else {
//                            goLogin();
//                        }
//
//                    case R.id.radio_btn4://我
//                        if (isLogin) {
//                            tvTitle.setText(getResources().getString(R.string.me));
//                            if (meFragment == null) {
//                                meFragment = new MeFragment();
//                            }
//                            replaceFragment(meFragment);
//                        } else {
//                            goLogin();
//                        }
//                        break;
//                }
//            }
//        });
        getPermission();
        getLanguage();
    }

    /**
     * 添加状态栏占位视图
     *
     * @param activity
     */
    private void addStatusViewWithColor(Activity activity, int color) {
        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        View statusBarView = new View(activity);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(activity));
        statusBarView.setBackgroundColor(color);
        contentView.addView(statusBarView, lp);
    }

    private void getLanguage() {
        Locale locale = getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        int type = Constant.LANGUAGE_TYPE_JP;
        if (language.endsWith("zh")){
            type =  Constant.LANGUAGE_TYPE_CN;
        }else if (language.endsWith("en")){
            type =  Constant.LANGUAGE_TYPE_EN;
        }else if (language.endsWith("ja")){
            type =  Constant.LANGUAGE_TYPE_JP;
        }
        Log.i(TAG,"languageType === "+type);
        app.languageType =  type;
    }

    @OnClick({R.id.title_me})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_me://me页面
                if (isLogin) {
                    Utils.gotoNewAct(this, MeActivity.class);
//                    tvTitle.setText(getResources().getString(R.string.me));
//                    if (meFragment == null) {
//                        meFragment = new MeFragment();
//                    }
//                    replaceFragment(meFragment);
                } else {
                    goLogin();
                }
                break;
            default:
                break;
        }
    }

    private void getKey(){
        try {
            int i = 0;
            PackageInfo info = getPackageManager().getPackageInfo( getPackageName(),  PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                i++;
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String KeyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.i(TAG,"KeyHash ==== "+KeyHash);
            }
        }
        catch (PackageManager.NameNotFoundException e) {

        }
        catch (NoSuchAlgorithmException e) {

        }
    }

    private void getPermission(){
        requestRuntimePermission(new String[]{
                Manifest.permission.CALL_PHONE,//打电话权限
                Manifest.permission.ACCESS_FINE_LOCATION,//精准定位权限
                Manifest.permission.ACCESS_COARSE_LOCATION,//精准定位权限
                Manifest.permission.CAMERA,//相机权限
                Manifest.permission.WRITE_EXTERNAL_STORAGE,//写数据权限
                Manifest.permission.READ_EXTERNAL_STORAGE,//读数据权限
//                Manifest.permission.RECORD_AUDIO//录音权限
        }, new PermissionListener() {
            //授权后的回调方法
            @Override
            public void onGranted() {
//                Toast.makeText(MainActivity.this, "所有权限都同意了", Toast.LENGTH_SHORT).show();
            }

            //权限被拒绝的回调方法
            @Override
            public void onDenied(List<String> deniedPermission) {
//                for (String permission : deniedPermission) {
//                    Toast.makeText(MainActivity.this, "被拒绝权限：" + permission, Toast.LENGTH_SHORT).show();
//                }
            }
        });

    }

    //权限请求的方法
    public static void requestRuntimePermission(String[] permissions, PermissionListener listener) {
        Activity topActivity = APP.getTopActivity();
        if (topActivity == null) {
            return;
        }
        mListener = listener;
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(topActivity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(topActivity, permissionList.toArray(new String[permissionList.size()]), 1);
        } else {
            mListener.onGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    List<String> deniedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        int grantResult = grantResults[i];
                        String permission = permissions[i];
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            deniedPermissions.add(permission);
                        }
                    }
                    if (deniedPermissions.isEmpty()) {
                        mListener.onGranted();
                    } else {
                        mListener.onDenied(deniedPermissions);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void getLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);//低精度，如果设置为高精度，依然获取不了location。
        criteria.setAltitudeRequired(false);//不要求海拔
        criteria.setBearingRequired(false);//不要求方位
        criteria.setCostAllowed(false);//允许有花费
        criteria.setPowerRequirement(Criteria.POWER_LOW);//低功耗

        //从可用的位置提供器中，匹配以上标准的最佳提供器
        String locationProvider = locationManager.getBestProvider(criteria, true);

//        if (locationManager
//                .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
//            Toast.makeText(this, "GPS模块正常", Toast.LENGTH_SHORT).show();
//            return;
//        }

        if (!locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            Log.d(TAG, "onCreate: 没有权限 ");
            //无法定位：1、提示用户打开定位服务；2、跳转到设置界面
            Toast.makeText(this, getResources().getString(R.string.toast_location), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);//ACTION_SETTINGS
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                startActivity(intent);
            } catch(ActivityNotFoundException ex) {
                // The Android SDK doc says that the location settings activity
                // may not be found. In that case show the general settings.
                // General settings activity
                intent.setAction(Settings.ACTION_SETTINGS);
                try {
                    startActivity(intent);
                } catch (Exception e) {
                }
            }
            return;
        }else{
//            Toast.makeText(this, "GPS模块正常", Toast.LENGTH_SHORT).show();
        }

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
        Location location = locationManager.getLastKnownLocation(locationProvider);
        Log.d(TAG, "onCreate: " + (location == null) + "..");
        if (location != null) {
            Log.d(TAG, "onCreate: location");
            //不为空,显示地理位置经纬度
            showLocation(location);
        }
        //监视地理位置变化
        locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);
    }

    /**
     * LocationListern监听器
     * 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
     */

    LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "onProviderEnabled: " + provider + ".." + Thread.currentThread().getName());
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled: " + provider + ".." + Thread.currentThread().getName());
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "onLocationChanged: " + ".." + Thread.currentThread().getName());
            //如果位置发生变化,重新显示
            showLocation(location);
        }
    };

    private void showLocation(Location location) {
        Log.e("showLocation", location.toString());
        String positon = location.getLatitude() + "," + location.getLongitude();
        Log.i("showLocation", "location ==== "+ positon);
        List<Address> addList  = new ArrayList<>();
        Geocoder ge = new Geocoder(getApplicationContext());
        try {
            addList  = ge.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String latLongString = "";
        if (addList != null && addList.size() > 0) {
            for (int i = 0; i < addList.size(); i++) {
                Address ad = addList.get(i);
                latLongString = ad.getLocality();
            }
        }

        Log.i("showLocation", "latLongString ==== "+ latLongString);
        PreferencesUtils.putString(MainActivity.this,"city", latLongString);

        PreferencesUtils.putString(MainActivity.this,"location", positon);//35.6377623682076,139.787390936822 东京经纬度
//        Log.d(TAG,"定位成功------->"+"location------>经度为：" + location.getLatitude() + "\n纬度为" + location.getLongitude());
    }

    private void goLogin() {
        intent.setClass(getApplicationContext(), RegisterActivity.class);//  WebViewActivity.class
        intent.putExtra("state",3);
        startActivity(intent);
        radioBtn1.setChecked(true);
    }

    //切换界面
    private void replaceFragment(Fragment fragment) {
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.content, fragment).addToBackStack(null).commitAllowingStateLoss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"onResume ------- ");
//        sp = getSharedPreferences("login", MODE_PRIVATE);
        isLogin = PreferencesUtils.getBoolean(this,Constant.SP_IS_LOGIN, false);

//        PreferencesUtils.putString(this,"location", "35.702069,139.775327");
        getLocation();

//        if(0 == table){
//            radioBtn1.setChecked(true);
//        }
//        table = -1;
        getAppVersionName(this);
//        createScoket();
    }

    //获取当前版本号
    private  void getAppVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo("com.itmg.imachika", 0);
            String versionName = packageInfo.versionName;
            PreferencesUtils.putString(context,"Version",versionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/*    private void createScoket() {
        try {
            socket = IO.socket(URLInfo.chat);
            final String cookie = "uid=123";
            Log.i("cookie",cookie);
            socket.io().on(Manager.EVENT_TRANSPORT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Transport transport = (Transport)args[0];
                    transport.on(Transport.EVENT_REQUEST_HEADERS, new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            Map<String, List<String>> headers = (Map<String, List<String>>)args[0];
                            // modify request headers
                            headers.put("Cookie", Arrays.asList(cookie));
                        }
                    });
                }
            });
            Log.e("cookie","准备连接");
            socket.connect();
            Log.e("cookie","已连接");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Log.e("cookie",e.toString());
        }

    }*/

    private long exitTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                if(System.currentTimeMillis() - exitTime > 2000){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_close_app),
                            Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                    return false;
                } else{
                    app.exit();
                }
            }
            return true;
        }
        return false;
    }

    public interface PermissionListener {

        void onGranted();

        void onDenied(List<String> deniedPermission);

    }

}
