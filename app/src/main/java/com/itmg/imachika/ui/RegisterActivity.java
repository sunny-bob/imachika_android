package com.itmg.imachika.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.itmg.imachika.MainActivity;
import com.itmg.imachika.R;
import com.itmg.imachika.application.APP;
import com.itmg.imachika.model.LoginUser;
import com.itmg.imachika.model.User;
import com.itmg.imachika.util.GoogleLogin;
import com.itmg.imachika.util.GsonUtil;
import com.itmg.imachika.util.Mytoast;
import com.itmg.imachika.util.OkHttpUtils;
import com.itmg.imachika.util.PreferencesUtils;
import com.itmg.imachika.util.RequestCallBack;
import com.itmg.imachika.util.URLInfo;
import com.itmg.imachika.view.WaitDialog;
import com.linecorp.linesdk.auth.LineLoginApi;
import com.linecorp.linesdk.auth.LineLoginResult;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener , GoogleLogin.GoogleSignListener {

    @BindView(R.id.checkbox)
    CheckBox checkbox;

    private static final String TAG = "RegisterActivity";
    private WaitDialog mWaitDialog;
    Intent intent = new Intent();
    APP app = APP.getMyApplication();
    CallbackManager callbackManager;
    private LoginManager loginManager;
    private GoogleLogin googleLogin;
    private LoginUser loginUser;
    private static final int FB_TYPE = 0;
    private static final int GG_TYPE = 1;
    private static final int LINE_TYPE = 2;
    private static final String CHANNELID = "1559243726";
    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        app.addActivity(this);

        //初始化谷歌登录服务
        googleLogin = new GoogleLogin(this,this) ;
        googleLogin.setGoogleSignListener(this);

        initFb();
    }

    private void initFb(){
        //这里要初始化FB
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        this.loginManager = LoginManager.getInstance();

        loginManager.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        // App code
                        Log.e("test","success");
                        //取得自己的信息
                        Log.e("personProfile","User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + loginResult.getAccessToken().getToken());

                        getFacebookInfo(loginResult.getAccessToken());
//                        getUserFacebookBasicInfo();//获取facebook登录用户的基本信息的方法

                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.e("test","cancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.e("test","error");
                    }
                });



    }

    public void getFacebookInfo(final AccessToken accessToken) {
        String userId = accessToken.getUserId();
        final String access_token = accessToken.getToken();
        Log.e("test","userId == "+userId);
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                if (object != null) {
                    loginUser = new LoginUser();
                    loginUser.setId(object.optString("id"));
                    loginUser.setName(object.optString( "name"));
                    loginUser.setEmail(object.optString( "email"));
                    loginUser.setAccess_token(access_token);

                    //获取用户头像
                    JSONObject object_pic = object.optJSONObject( "picture" ) ;
                    JSONObject object_data = object_pic.optJSONObject( "data" ) ;
                    loginUser.setImage(object_data.optString( "url" ));

                    upLoadUserInfo(FB_TYPE);

                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,gender,birthday,email,picture,locale,updated_time,timezone,age_range,first_name,last_name");
        request.setParameters(parameters);
        request.executeAsync() ;
    }

    @OnClick({R.id.img_back, R.id.tv_facebook, R.id.tv_google, R.id.tv_line, R.id.tv_mail, R.id.tv1, R.id.tv2, R.id.tv_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_facebook:
                if(!checkbox.isChecked()){
                    Mytoast.show(this,getResources().getString(R.string.login_check_tost));
                    return;
                }
                //设置登录权限 此处如果不添加 "user_friends" 你是不能访问好友信息的
                loginManager.logInWithReadPermissions(
                        this, Arrays.asList(
                                new String[]{
                                        "public_profile",
                                        "email",
                                        "user_about_me"}));

                break;
            case R.id.tv_google:
                if(!checkbox.isChecked()){
                    Mytoast.show(this,getResources().getString(R.string.login_check_tost));
                    return;
                }
                //登录
                googleLogin.signIn();
                break;
            case R.id.tv_line:
                if(!checkbox.isChecked()){
                    Mytoast.show(this,getResources().getString(R.string.login_check_tost));
                    return;
                }

                Intent loginIntent = LineLoginApi.getLoginIntent(this, CHANNELID);
                startActivityForResult(loginIntent, REQUEST_CODE);

                break;
            case R.id.tv_mail:
                intent.setClass(getApplicationContext(),EmailRegActivity.class);
                startActivity(intent);
                break;
            case R.id.tv1:
                intent.setClass(getApplicationContext(),WebViewActivity.class);
                intent.putExtra("state",1);
                startActivity(intent);
                break;
            case R.id.tv2:
                intent.setClass(getApplicationContext(),WebViewActivity.class);
                intent.putExtra("state",2);
                startActivity(intent);
                break;
            case R.id.tv_login:
                intent.setClass(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                this.finish();
                break;
        }
    }

    private void upLoadUserInfo(int type){
        mWaitDialog = new WaitDialog(this, R.style.loading_dialog);
        mWaitDialog.show();
        String url = "";
        switch (type){
            case FB_TYPE:
                url = URLInfo.login_fb;
                break;
            case GG_TYPE:
                url = URLInfo.login_gg;
                break;
            case LINE_TYPE:
                url = URLInfo.login_line;
                break;
            default:
                break;
        }
        Map<String, String> params = new HashMap<>();

        params.put("email", loginUser.getEmail());
        params.put("id", loginUser.getId());
        params.put("name", loginUser.getName());
        params.put("image", loginUser.getImage());
        params.put("access_token", loginUser.getAccess_token());
        params.put("location", PreferencesUtils.getString(RegisterActivity.this,"location",""));
        Log.i(TAG,"getInfoRequest url === " + url+"  params == "+params.toString());

        OkHttpUtils.getInstance().okHttpPost(url, params, new RequestCallBack() {
            @Override
            public void onSuccess(String result) {
                mWaitDialog.dismiss();
                if(null != result){
                    Log.i(TAG,"result = "+result);
                    User user = (User) GsonUtil.praseJsonToModel(result,User.class);
                    if (user.getStatus().equals("ok")) {
                        String userInfo = GsonUtil.objectToJson(user.getUser());
                        PreferencesUtils.putBoolean(RegisterActivity.this, "isLogin", true);
                        PreferencesUtils.putString(RegisterActivity.this, "userInfo", userInfo);
                        PreferencesUtils.putString(RegisterActivity.this, "id", user.getUser().get_id());
                        intent.setClass(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        String toastStr = "";
                        switch (user.getType()){
                            case 1:
                                toastStr = getResources().getString(R.string.toast_login_psd_error);
                                break;
                            case 2:
                                toastStr = getResources().getString(R.string.toast_login_not_reg);
                                break;
                            case 3:
                                toastStr = getResources().getString(R.string.toast_login_email_error);
                                break;
                            case 4:
                                toastStr = getResources().getString(R.string.toast_login_user_registered);
                                break;
                        }
                        Mytoast.show(getApplicationContext(),toastStr);
                    }
                }
            }

            @Override
            public void onFail() {
                mWaitDialog.dismiss();
                Toast.makeText(RegisterActivity.this,getResources().getString(R.string.net_bad_retry), Toast.LENGTH_LONG).show();
            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        //谷歌登录成功回调
        if (requestCode == googleLogin.requestCode ) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            googleLogin.handleSignInResult( result ) ;
        }

        //line登录成功回调
        LineLoginResult result = LineLoginApi.getLoginResultFromIntent(data);
        switch (result.getResponseCode()) {
            case SUCCESS:
                loginUser = new LoginUser();
                loginUser.setName(result.getLineProfile().getDisplayName());
                loginUser.setId(result.getLineProfile().getUserId());
                Uri pictureUrl = result.getLineProfile().getPictureUrl();
                if (pictureUrl != null) {
                    String imgUrl = pictureUrl.toString();
                    Log.i(TAG," line ---- imgUrl == "+imgUrl);
                    loginUser.setImage(imgUrl);
                }else{
                    loginUser.setImage("");
                }
                loginUser.setEmail("");
                String accessToken = result.getLineCredential().getAccessToken().getAccessToken();
                Log.i(TAG,"AccessToken == "+accessToken);
                loginUser.setAccess_token(accessToken);
                upLoadUserInfo(LINE_TYPE);
                break;
            case CANCEL:
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void googleLoginSuccess(LoginUser user) {
        loginUser = user;
        upLoadUserInfo(GG_TYPE);
    }

    @Override
    public void googleLoginFail() {

    }

    @Override
    public void googleLogoutSuccess() {

    }

    @Override
    public void googleLogoutFail() {

    }
}
