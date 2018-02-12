package com.itmg.imachika.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.itmg.imachika.R;
import com.itmg.imachika.contans.Contans;
import com.itmg.imachika.model.NearPerson;
import com.itmg.imachika.model.UserInfo;
import com.itmg.imachika.util.GlideUtil;
import com.itmg.imachika.util.GsonUtil;
import com.itmg.imachika.util.Mytoast;
import com.itmg.imachika.util.OkHttpUtil;
import com.itmg.imachika.util.URLInfo;
import com.suke.widget.SwitchButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class UserHomePageActivity extends Activity implements OkHttpUtil.Result {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.head_img)
    ImageView headImg;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_range)
    TextView tvRange;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    NearPerson.Data data;
    Intent intent;
    String address, uid;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    SharedPreferences sp;
    UserInfo userInfo;
    com.suke.widget.SwitchButton switchButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_page);
        ButterKnife.bind(this);
        switchButton = findViewById(R.id.switch_button);
        tvTitle.setText(getResources().getString(R.string.near));
        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    Mytoast.show(getApplicationContext(),"已经加入黑名单");
                    pullBlack(0);
                    }else{
                    Mytoast.show(getApplicationContext(),"取消黑名单");
                    pullBlack(1);
                }
            }
        });
        intent = getIntent();
        data = (NearPerson.Data) intent.getSerializableExtra("person");
        Log.i("person", data.toString());
    }

    private void pullBlack(int i) {
        String url = URLInfo.pullblack+"?user_id="+userInfo.getData().get_id()+"&cancel="+i;
        OkHttpUtil.get(url,uid,Contans.pullblack,this,this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sp = getSharedPreferences("login", MODE_PRIVATE);
        uid = sp.getString("id", "");
        String url = URLInfo.userInfo + data.get_id() + "/";
        OkHttpUtil.get(url, uid, Contans.userInfo, this, this);
        Log.i("person", data.toString());
    }
    @OnCheckedChanged(R.id.checkbox)
    void onCheck(boolean isFocus) {
        if (isFocus) {
            checkbox.setText("已关注");
            concern(1);
        } else {
            checkbox.setText("关注");
            concern(0);
        }
    }

    private void concern(int i) {
        String url = URLInfo.concern + "?user_id=" + userInfo.getData().get_id() + "&status=" + i;
        OkHttpUtil.get(url, uid, Contans.concern, this, this);
    }

    @OnClick({R.id.img_back, R.id.layout1, R.id.layout2, R.id.layout3, R.id.btn_chat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.layout1:
                intent = new Intent();
                intent.setClass(getApplicationContext(), CoverActivity.class);
                startActivity(intent);
                break;
            case R.id.layout2:
                intent = new Intent();
                intent.setClass(getApplicationContext(), ConcernActivity.class);
                startActivity(intent);
                break;
            case R.id.layout3:
                intent = new Intent();
                intent.setClass(getApplicationContext(), ReportActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_chat:
                intent = new Intent();
                intent.setClass(getApplicationContext(), ChatActivity.class);
                intent.putExtra("data", data);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void result(final String s, final int code) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (code) {
                    case Contans.error:
                        Mytoast.show(getApplicationContext(), s);
                        break;
                    case Contans.userInfo:
                        Log.i(Contans.TAG, s);
                        userInfo = (UserInfo) GsonUtil.praseJsonToModel(s, UserInfo.class);
                        if (userInfo.getData().isIs_follow()) {
                            checkbox.setText("已关注");
                        } else {
                            checkbox.setText("关注");
                        }
                        if (userInfo.getData().isIsblack()){
                            switchButton.setChecked(true);
                        }else{
                            switchButton.setChecked(false);
                        }
                        GlideUtil.setImg(UserHomePageActivity.this, headImg, userInfo.getData().getImage());
                        tvName.setText(userInfo.getData().getUser_name());
                        tvRange.setText(data.getDistance());
                        address = "";
                        if (data.getAddress_pub() != null) {
                            for (String s : userInfo.getData().getAddress_pub()) {
                                address += s;
                            }
                        }
                        tvAddress.setText(address);
                        break;
                    case Contans.concern:
                        Log.i(Contans.TAG, s);
                        break;
                    case Contans.pullblack:
                        Log.i(Contans.TAG, s);
                        break;
                }
            }
        });
    }
}
