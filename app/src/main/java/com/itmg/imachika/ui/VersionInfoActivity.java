package com.itmg.imachika.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.itmg.imachika.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VersionInfoActivity extends Activity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    Intent intent = new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version_info);
        ButterKnife.bind(this);
        tvTitle.setText(getResources().getString(R.string.me_version));
    }

    @OnClick({R.id.img_back, R.id.tv1, R.id.tv2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
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
        }
    }
}
