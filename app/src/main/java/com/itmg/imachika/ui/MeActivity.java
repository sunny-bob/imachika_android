package com.itmg.imachika.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.itmg.imachika.R;
import com.itmg.imachika.application.APP;
import com.itmg.imachika.base.BaseActivity;
import com.itmg.imachika.model.User;
import com.itmg.imachika.util.GlideUtil;
import com.itmg.imachika.util.GsonUtil;
import com.itmg.imachika.util.PreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/11/9 0009.
 */

public class MeActivity extends BaseActivity {
    View view;
    Unbinder unbinder;
    @BindView(R.id.head_img)
    ImageView headImg;
    @BindView(R.id.tv_name)
    TextView tvName;
    Intent intent = new Intent();
    String userInfoStr;
    User.UserInfo userInfo;
    private APP app = APP.getMyApplication();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me);
        ButterKnife.bind(this);
        app.addActivity(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        userInfoStr = PreferencesUtils.getString(this,"userInfo","");
        userInfo = (User.UserInfo) GsonUtil.praseJsonToModel(userInfoStr,User.UserInfo.class);
        if (!userInfoStr.equals("")){
            tvName.setText(userInfo.getUser_name());
            GlideUtil.setImg(this,headImg,userInfo.getImage());
        }
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        unbinder.unbind();
//    }

    @OnClick({R.id.img_back,R.id.layout_info, R.id.tv_mark1, R.id.tv_mark2, R.id.tv_mark3, R.id.layout_one, R.id.layout_two, R.id.layout_three})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                this.finish();
                break;
            case R.id.layout_info:
                intent.setClass(this, UserInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_mark1:
                break;
            case R.id.tv_mark2:
                break;
            case R.id.tv_mark3:
                intent.setClass(this, ConcernActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_one:
                intent.setClass(this, CollectionActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_two:
                intent.setClass(this, NewNoticeActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_three:
                intent.setClass(this, VersionInfoActivity.class);
                startActivity(intent);
                break;
        }
    }
}
