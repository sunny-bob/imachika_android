package com.itmg.imachika.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itmg.imachika.R;
import com.itmg.imachika.application.APP;
import com.itmg.imachika.util.Constant;
import com.itmg.imachika.util.Mytoast;
import com.itmg.imachika.util.OkHttpUtils;
import com.itmg.imachika.util.PreferencesUtils;
import com.itmg.imachika.util.RequestCallBack;
import com.itmg.imachika.util.URLInfo;
import com.itmg.imachika.util.Utils;
import com.itmg.imachika.view.WaitDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 举报界面
 */
public class ReportActivity extends Activity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.et_msg)
    EditText etMsg;
    @BindView(R.id.radio_btn1)
    CheckBox radioBtn1;
    @BindView(R.id.radio_btn2)
    CheckBox radioBtn2;
    @BindView(R.id.radio_btn3)
    CheckBox radioBtn3;
    @BindView(R.id.radio_btn4)
    CheckBox radioBtn4;
    @BindView(R.id.radio_btn5)
    CheckBox radioBtn5;
    @BindView(R.id.radio_btn6)
    CheckBox radioBtn6;

    private final static String TAG = "ReportActivity";
    private WaitDialog mWaitDialog;
    private APP app = APP.getMyApplication();
    private String detailId ;
    private ArrayList<String> puts = new ArrayList<>();
    private StringBuffer putsStr ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);
        app.addActivity(this);
        tvTitle.setText(getResources().getString(R.string.jubao));
        Intent intent = this.getIntent();
        if(null != intent && intent.hasExtra("data")){
            detailId = this.getIntent().getStringExtra("data");
        }
    }

    @OnClick({R.id.img_back, R.id.radio_btn1, R.id.radio_btn2, R.id.radio_btn3, R.id.radio_btn4,
            R.id.radio_btn5, R.id.radio_btn6, R.id.img, R.id.report_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.radio_btn1:
                if(radioBtn1.isChecked()){
                    puts.add("1");
                }else{
                    puts.remove("1");
                }
                Log.i(TAG,"   onViewClicked  radio_btn1  isclicked === ");
                break;
            case R.id.radio_btn2:
                if(radioBtn2.isChecked()){
                    puts.add("2");
                }else{
                    puts.remove("2");
                }
                Log.i(TAG,"   onViewClicked  radio_btn2  isclicked === ");
                break;
            case R.id.radio_btn3:
                if(radioBtn3.isChecked()){
                    puts.add("3");
                }else{
                    puts.remove("3");
                }
                Log.i(TAG,"   onViewClicked  radio_btn3  isclicked === ");
                break;
            case R.id.radio_btn4:
                if(radioBtn4.isChecked()){
                    puts.add("4");
                }else{
                    puts.remove("4");
                }
                Log.i(TAG,"   onViewClicked  radio_btn4  isclicked === ");
                break;
            case R.id.radio_btn5:
                if(radioBtn5.isChecked()){
                    puts.add("5");
                }else{
                    puts.remove("5");
                }
                Log.i(TAG,"   onViewClicked  radio_btn5  isclicked === ");
                break;
            case R.id.radio_btn6:
                if(radioBtn6.isChecked()){
                    puts.add("6");
                }else{
                    puts.remove("6");
                }
                Log.i(TAG,"   onViewClicked  radio_btn6  isclicked === ");
                break;
            case R.id.img://选择图片
                Log.i(TAG,"   onViewClicked  img  isclicked === ");
                break;
            case R.id.report_btn://通报
                Log.i(TAG,"   onViewClicked  report_btn  isclicked === ");
//          id，cases，file，content，type 1 用户  2 店铺 3 评论  4 评论回复
                if(puts.size() > 0){
                    putsStr = new StringBuffer();
                    for(int i = 0 ; i < puts.size() ; i++){
                        if(i == 0){
                            putsStr.append(puts.get(i));
                        }else{
                            putsStr.append(","+puts.get(i));
                        }
                    }
                    postReportRequest();
                }
                break;
        }
    }

    private void postReportRequest(){
        String url = URLInfo.reportUrl;
        mWaitDialog = new WaitDialog(this,R.style.loading_dialog);
        mWaitDialog.show();
        Map<String, String> params = new HashMap<>();
        params.put("id", detailId);
        Log.i(TAG,"putsStr === "+putsStr);
        params.put("cases", putsStr.toString());
        params.put("file", "");
        params.put("content", etMsg.getText().toString().trim());
        params.put("type", Constant.REPORT_BUSS);
        Log.i(TAG,"postReportRequest url === " + url+"  params == "+params.toString());
        String userId = PreferencesUtils.getString(this,Constant.SP_USER_ID,"");
        if("".equals(userId)){//用户未登录，跳转登录页面
            Utils.gotoNewAct(this,LoginActivity.class);
        }
        Log.i(TAG,"postReportRequest userId === " + userId );
        Log.i(TAG, "postReportRequest url === " + url + params.toString());
        OkHttpUtils.getInstance().okHttpPost(url,userId , params, new RequestCallBack() {
            @Override
            public void onSuccess(String result) {
                mWaitDialog.dismiss();
                if(null != result){
                    Log.i(TAG,"postReportRequest  result = "+result);
                    if(null != result && result.contains("\"status\": \"ok\"")){
                        Mytoast.show(ReportActivity.this,getResources().getString(R.string.toast_report));
                        ReportActivity.this.finish();
                    }
                }
            }

            @Override
            public void onFail() {
                mWaitDialog.dismiss();
                Toast.makeText(ReportActivity.this,getResources().getString(R.string.net_bad_retry), Toast.LENGTH_LONG).show();
            }

        });
    }

}
