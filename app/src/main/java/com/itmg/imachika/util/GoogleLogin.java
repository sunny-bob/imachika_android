package com.itmg.imachika.util;

/**
 * Created by Administrator on 2018/1/23 0023.
 */

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.itmg.imachika.R;
import com.itmg.imachika.model.LoginUser;

//import com.google.android.gms.auth.api.Auth;
//import com.google.android.gms.auth.api.signin.GoogleSignInResult;

public class GoogleLogin {

    public int requestCode = 10 ;
    private Activity activity ;
    public GoogleSignInOptions gso ;
    public GoogleApiClient mGoogleApiClient ;
    public GoogleApiClient.OnConnectionFailedListener listener ;
    private GoogleSignListener googleSignListener ;
    public LoginUser user;

    public GoogleLogin(FragmentActivity activity , GoogleApiClient.OnConnectionFailedListener listener ){
        this.activity = activity ;
        this.listener = listener ;

        //初始化谷歌登录服务
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .requestIdToken( activity.getString(R.string.google_server_client_id))
                .requestProfile()
                .build();

        // Build a GoogleApiClient with access to GoogleSignIn.API and the options above.
        mGoogleApiClient = new GoogleApiClient.Builder( activity )
                .enableAutoManage( activity, listener )
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    /**
     * 登录
     */
    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        activity.startActivityForResult(signInIntent, requestCode);
    }

    /**
     * 退出登录
     */
    public void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if ( status.isSuccess() ){
                            if ( googleSignListener != null ){
                                googleSignListener.googleLogoutSuccess();
                            }
                        }else {
                            if ( googleSignListener!= null ){
                                googleSignListener.googleLogoutFail();
                            }
                        }
                    }
                });
    }

    public String handleSignInResult(GoogleSignInResult result) {
        String res = "" ;
        Log.e("res", "result:"+result);
        if (result.isSuccess()) {
            //登录成功
            GoogleSignInAccount acct = result.getSignInAccount();
            user = new LoginUser();
            res = "登录成功"
                    + "用户名为：" + acct.getDisplayName()
                    + "  邮箱为：" + acct.getEmail()
                    + " token为：" + acct.getIdToken()
                    + " 头像地址为：" + acct.getPhotoUrl()
                    + " Id为：" + acct.getId()
                    + " GrantedScopes为：" + acct.getGrantedScopes() ;
            Log.e("res", "res:"+res);
            user.setId(acct.getId());
            user.setName(acct.getDisplayName());
            user.setEmail(acct.getEmail());
            user.setAccess_token(acct.getIdToken());
            user.setImage(acct.getPhotoUrl().toString());
            Toast.makeText( activity, res, Toast.LENGTH_SHORT).show();
            if ( googleSignListener != null ){
                googleSignListener.googleLoginSuccess(user);
            }
        } else {
            // Signed out, show unauthenticated UI.
            res = "-1" ;  //-1代表用户退出登录了 ， 可以自定义
            Toast.makeText( activity , "退出登录", Toast.LENGTH_SHORT).show();
            if ( googleSignListener != null ){
                googleSignListener.googleLoginFail();
            }
        }
        return res ;
    }


    public void setGoogleSignListener( GoogleSignListener googleSignListener ){
        this.googleSignListener = googleSignListener ;
    }

    public interface GoogleSignListener {
        void googleLoginSuccess(LoginUser user);
        void googleLoginFail() ;
        void googleLogoutSuccess();
        void googleLogoutFail() ;
    }

}
