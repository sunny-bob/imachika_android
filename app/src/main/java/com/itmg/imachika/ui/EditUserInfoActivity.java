package com.itmg.imachika.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itmg.imachika.R;
import com.itmg.imachika.application.APP;
import com.itmg.imachika.base.BaseActivity;
import com.itmg.imachika.model.User;
import com.itmg.imachika.util.Constant;
import com.itmg.imachika.util.GsonUtil;
import com.itmg.imachika.util.Mytoast;
import com.itmg.imachika.util.OkHttpUtils;
import com.itmg.imachika.util.PreferencesUtils;
import com.itmg.imachika.util.RequestCallBack;
import com.itmg.imachika.util.URLInfo;
import com.itmg.imachika.view.WaitDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/27 0027.
 */

public class EditUserInfoActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.edit_save)
    TextView tvSave;
    @BindView(R.id.edit_cb_male)
    CheckBox maleCb;
    @BindView(R.id.edit_cb_female)
    CheckBox femaleCb;
    @BindView(R.id.edit_view)
    LinearLayout editView;
    @BindView(R.id.edit_tel_view)
    LinearLayout telView;
    @BindView(R.id.edit_sex_view)
    LinearLayout sexView;
    @BindView(R.id.edit_info_et)
    EditText editInput;
    @BindView(R.id.edit_tel_et)
    EditText telEditInput;
    @BindView(R.id.edit_location_iv)
    ImageView locationIv;

    private static final String TAG = "EditUserInfoActivity";
    private WaitDialog mWaitDialog;
    APP app = APP.getMyApplication();
    String sexStr;
    String uid;
    String userInfoStr;
    User.UserInfo userInfo;
    private int type = -1;
    private String titleStr = "";
    private String hintStr = "";
    private boolean canSave;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        ButterKnife.bind(this);
        app.addActivity(this);
        initView();
        initData();
    }

    @Override
    protected void initView() {
        super.initView();
        maleCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    sexStr = "male";
                    femaleCb.setChecked(false);
                }
                if(sexStr.equals(userInfo.getGender())){
                    canSave = false;
                }else{
                    canSave = true;
                }
                Log.i(TAG,"maleCb setOnCheckedChangeListener    sexStr === " + sexStr);
            }
        });

        femaleCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    sexStr = "female";
                    maleCb.setChecked(false);
                }
                if(sexStr.equals(userInfo.getGender())){
                    canSave = false;
                }else{
                    canSave = true;
                }
                Log.i(TAG,"femaleCb setOnCheckedChangeListener    sexStr === " + sexStr);
            }
        });

        editInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                Log.i(TAG,"editInput afterTextChanged  "+editable.length());
                switch (type){
                    case Constant.INFO_NAME:
                        if(null !=  userInfo.getUser_name()){
                            if(userInfo.getUser_name().equals(editInput.getText().toString().trim())
                                    ){
                                canSave = false;
                            }else {
                                canSave = true;
                            }
                        }else{
                            if(!"".equals(editInput.getText().toString().trim())){
                                canSave = true;
                            }
                        }
                        break;
                    case Constant.INFO_ADDRESS:
                        if(null !=  userInfo.getAddress()){
                            if(userInfo.getAddress().equals(editInput.getText().toString().trim())
                                    ){
                                canSave = false;
                            }else {
                                canSave = true;
                            }
                        }else{
                            if(!"".equals(editInput.getText().toString().trim())){
                                canSave = true;
                            }
                        }
                        break;
                    case Constant.INFO_SIGN:
                        if(null !=  userInfo.getSignature()){
                            if(userInfo.getSignature().equals(editInput.getText().toString().trim())){
                                canSave = false;
                            }else {
                                canSave = true;
                            }
                        }else{
                            if(!"".equals(editInput.getText().toString().trim())){
                                canSave = true;
                            }
                        }
                        break;
                }
            }
        });

        telEditInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.i(TAG,"telEditInput afterTextChanged  "+editable.length());
                if(null !=  userInfo.getTel()){
                    if(userInfo.getTel().equals(telEditInput.getText().toString().trim())
                            ){
                        canSave = false;
                    }else {
                        canSave = true;
                    }
                }else{
                    if(!"".equals(telEditInput.getText().toString().trim())){
                        canSave = true;
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        if(null != this.getIntent()){
            type = this.getIntent().getIntExtra("data",0);
            Log.i(TAG,"type == "+type);
        }

        uid = PreferencesUtils.getString(this,Constant.SP_USER_ID,"");
        userInfoStr = PreferencesUtils.getString(this,Constant.SP_USER_INFO,"");
        if (!userInfoStr.equals("")) {
            userInfo = (User.UserInfo) GsonUtil.praseJsonToModel(userInfoStr, User.UserInfo.class);
            if(null != userInfo.getGender()){
                if("male".equals(userInfo.getGender())){
                    maleCb.setChecked(true);
                }else if("female".equals(userInfo.getGender())){
                    femaleCb.setChecked(true);
                }
            }
        }

        switch (type){
            case Constant.INFO_NAME:
                titleStr = getResources().getString(R.string.userInfo_name);
                hintStr = userInfo.getUser_name();
                break;
            case Constant.INFO_SEX:
                editView.setVisibility(View.GONE);
                sexView.setVisibility(View.VISIBLE);
                titleStr = getResources().getString(R.string.userInfo_sex);
                break;
            case Constant.INFO_TEL:
                editView.setVisibility(View.GONE);
                telView.setVisibility(View.VISIBLE);
                titleStr = getResources().getString(R.string.userInfo_tel);
                telEditInput.setText(userInfo.getTel() != null ? userInfo.getTel() : "");
                break;
            case Constant.INFO_ADDRESS:
                locationIv.setVisibility(View.VISIBLE);
                titleStr = getResources().getString(R.string.userInfo_address);
                hintStr = userInfo.getAddress();
                break;
            case Constant.INFO_SIGN:
                titleStr = getResources().getString(R.string.userInfo_sign);
                hintStr = userInfo.getSignature();
                break;
        }
        tvTitle.setText(titleStr);
        editInput.setText(hintStr);
        editInput.setSelection(editInput.getText().length());

    }

    @OnClick({R.id.img_back, R.id.edit_save,R.id.edit_male_view,R.id.edit_female_view
            ,R.id.edit_cb_male,R.id.edit_cb_female,R.id.edit_location_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.edit_save:
                if(canSave){
                    upDataInfo();
                }
                break;
            case R.id.edit_male_view:
                if(maleCb.isChecked()){
                    maleCb.setChecked(false);
                }else{
                    maleCb.setChecked(true);
                }
                break;
            case R.id.edit_female_view:
                if(femaleCb.isChecked()){
                    femaleCb.setChecked(false);
                }else{
                    femaleCb.setChecked(true);
                }
                break;
            case R.id.edit_location_iv:
                if(!"".equals(PreferencesUtils.getString(EditUserInfoActivity.this,"city", ""))){
                    editInput.setText(PreferencesUtils.getString(EditUserInfoActivity.this,"city", ""));
                }
                Log.i(TAG,"获取定位位置");
//                editInput.setText("");
                break;
            default:
                break;
        }
    }

//    private boolean checkChange(){
//        switch (type){
//            case Constant.INFO_NAME:
//                if(userInfo.getUser_name().equals(editInput.getText().toString().trim())
//                        || "".equals(editInput.getText().toString().trim())){
//                    return false;
//                }
//                break;
//            case Constant.INFO_SEX:
//                if(userInfo.getGender().equals(sexStr)
//                        || "".equals(editInput.getText().toString().trim())){
//                    return false;
//                }
//                break;
//            case Constant.INFO_TEL:
//                if(userInfo.getTel().equals(telEditInput.getText().toString().trim())
//                        || "".equals(editInput.getText().toString().trim())){
//                    return false;
//                }
//                break;
//            case Constant.INFO_ADDRESS:
//                if(userInfo.getAddress().equals(editInput.getText().toString().trim())
//                        || "".equals(editInput.getText().toString().trim())){
//                    return false;
//                }
//                break;
//            case Constant.INFO_SIGN:
//                if(userInfo.getSignature().equals(editInput.getText().toString().trim())
//                        || "".equals(editInput.getText().toString().trim())){
//                    return false;
//                }
//                break;
//        }
//        return true;
//    }

    private void upDataInfo(){
        String url = URLInfo.editInfoUrl;
        mWaitDialog = new WaitDialog(this,R.style.loading_dialog,1);
        mWaitDialog.show();
        Map<String, String> params = new HashMap<>();
        switch (type){
            case Constant.INFO_NAME:
                params.put("user_name",editInput.getText().toString().trim());
                break;
            case Constant.INFO_SEX:
                if(!maleCb.isChecked() && !femaleCb.isChecked()){
                    return;
                }
                params.put("gender",sexStr);
                break;
            case Constant.INFO_TEL:
                params.put("tel",telEditInput.getText().toString().trim());
                break;
            case Constant.INFO_ADDRESS:
                params.put("address",editInput.getText().toString().trim());
                break;
            case Constant.INFO_SIGN:
                params.put("signature",editInput.getText().toString().trim());
                break;
            default:
                break;
        }
        Log.i(TAG,"upDataInfo url === " + url+"  params == "+params.toString());

        Log.i(TAG,"upDataInfo userId === " + uid );
        OkHttpUtils.getInstance().okHttpPost(url,uid , params, new RequestCallBack() {
            @Override
            public void onSuccess(String result) {
                mWaitDialog.dismiss();
                if(null != result){
                    Log.i(TAG,"upDataInfo  result = "+result);
                    User user = (User) GsonUtil.praseJsonToModel(result,User.class);
                    if (user.getStatus().equals("ok")){
                        String userInfo = GsonUtil.objectToJson(user.getData());
                        Log.i(TAG,"upDataInfo  userInfo === "+userInfo);
                        PreferencesUtils.putString(EditUserInfoActivity.this,"userInfo",userInfo);
                        PreferencesUtils.putString(EditUserInfoActivity.this,Constant.SP_USER_NAME,user.getData().getUser_name());
                        EditUserInfoActivity.this.finish();
                        Mytoast.show(getApplicationContext(),getResources().getString(R.string.save_success));
                    }else{
                        Mytoast.show(getApplicationContext(),user.getMsg());
                    }
                }
            }

            @Override
            public void onFail() {
                mWaitDialog.dismiss();
                Toast.makeText(EditUserInfoActivity.this,getResources().getString(R.string.net_bad_retry), Toast.LENGTH_LONG).show();
            }

        });

    }
}
