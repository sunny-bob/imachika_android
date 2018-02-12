package com.itmg.imachika.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.itmg.imachika.R;


public class WaitDialog extends Dialog {

    private Context mContext;

//    private TextView mWaitTv = null;
    private int mType = 0;

    private String mWaitingTxt = null;

    public WaitDialog(Context context, int theme , int type) {//1 圆形加载圈
        super(context, theme);
        mContext = context;
        this.mType = type;
    }

    public WaitDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(1 == mType){
            setContentView(R.layout.wait_dialog);
        }else{
            setContentView(R.layout.wait_dialog_bottom);
        }
//

//        mWaitTv = (TextView) findViewById(R.id.wait_tv);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
//        if (mWaitingTxt != null && !mWaitingTxt.isEmpty()) {
//            mWaitTv.setVisibility(View.VISIBLE);
//            mWaitTv.setText(mWaitingTxt);
//        } else {
//            mWaitTv.setVisibility(View.GONE);
//        }
    }

//    public void setWaitText(String text) {
//        mWaitingTxt = text;
//        if (mWaitTv == null) {
//            return;
//        }
//        if (mWaitingTxt != null && !mWaitingTxt.isEmpty()) {
//            mWaitTv.setVisibility(View.VISIBLE);
//            mWaitTv.setText(text);
//        } else {
//            mWaitTv.setVisibility(View.GONE);
//        }
//    }

    @Override
    public void show() {
        if (mContext != null && !((Activity) mContext).isFinishing()) {
            try {
                super.show();
            } catch (Exception e) {
            }
        }

    }

    @Override
    public void dismiss() {
        if (mContext != null && !((Activity) mContext).isFinishing()) {
            try {
                super.dismiss();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void cancel() {
        if (mContext != null && !((Activity) mContext).isFinishing()) {
            try {
                super.cancel();
            } catch (Exception e) {
            }
        }
    }
}
