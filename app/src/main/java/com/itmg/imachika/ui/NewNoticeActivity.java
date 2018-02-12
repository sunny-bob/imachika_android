package com.itmg.imachika.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;

import com.itmg.imachika.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewNoticeActivity extends Activity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.msg_switch)
    Switch msgSwitch;
    @BindView(R.id.concern_switch)
    Switch concernSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_notice);
        ButterKnife.bind(this);
        tvTitle.setText(getResources().getString(R.string.new_notice));
    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        finish();
    }
}
