package com.itmg.imachika;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Bundle;

public class WelComeActivity extends Activity {
    Intent intent = new Intent();
    CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wel_come);
        countDownTimer = new CountDownTimer(3*1000,1000) {
            @Override
            public void onTick(long l) {
                //倒计时显示
            }
            @Override
            public void onFinish() {
                intent.setClass(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        countDownTimer.start();
    }
}
