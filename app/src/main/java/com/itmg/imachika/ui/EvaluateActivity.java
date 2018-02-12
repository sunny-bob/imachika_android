package com.itmg.imachika.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.itmg.imachika.R;
import com.itmg.imachika.adapter.PicGridViewAdapter;
import com.itmg.imachika.application.APP;
import com.itmg.imachika.base.BaseActivity;
import com.itmg.imachika.model.Shop;
import com.itmg.imachika.util.Constant;
import com.itmg.imachika.util.GlideUtil;
import com.itmg.imachika.util.Mytoast;
import com.itmg.imachika.util.OkHttpUtils;
import com.itmg.imachika.util.PreferencesUtils;
import com.itmg.imachika.util.RequestCallBack;
import com.itmg.imachika.util.URLInfo;
import com.itmg.imachika.view.ImageLoader;
import com.itmg.imachika.view.MyGridView;
import com.itmg.imachika.view.WaitDialog;
import com.yuyh.library.imgsel.ImgSelActivity;
import com.yuyh.library.imgsel.ImgSelConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;


public class EvaluateActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView mTitleTv;
    @BindView(R.id.evaluate_iv)
    ImageView mImg;
    @BindView(R.id.evaluate_name_tv)
    TextView mNameTv;
    @BindView(R.id.evaluate_ratingBar)
    RatingBar mRb;
    @BindView(R.id.evaluate_gv)
    MyGridView mGridView;
    @BindView(R.id.evaluate_et)
    EditText mContentEt;

    private APP app = APP.getMyApplication();
    private static String TAG = "EvaluateActivity";
    private WaitDialog mWaitDialog;
    private List<String> mPathList;
    private PicGridViewAdapter mGridViewAdapter;
    private Shop.Data.Info mInfo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
        ButterKnife.bind(this);
        app.addActivity(this);
        initView();
        initData();
    }

    protected void initView() {
        mTitleTv.setText(getResources().getString(R.string.review));
        mPathList = new ArrayList<>();
        mGridViewAdapter = new PicGridViewAdapter(this, mPathList, 1);
        mGridView.setAdapter(mGridViewAdapter);

    }

    protected void initData() {
        if(null != getIntent() && null != getIntent().getExtras()){
            mInfo = (Shop.Data.Info)getIntent().getSerializableExtra("data");
            mNameTv.setText(mInfo.getContent() != null ? mInfo.getContent() : "");
            if(null != mInfo.getBig_img() || null != mInfo.getSmall_img()){
                GlideUtil.setImg(this,mImg,mInfo.getBig_img() != null ? mInfo.getBig_img() : "");
            }else{
                mImg.setVisibility(View.GONE);
            }
        }
    }

    @OnClick({R.id.img_back,R.id.tv_conform,//R.id.refund_tel_tv
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back://返回
                EvaluateActivity.this.finish();
                break;
            case R.id.tv_conform://完成
                conformUpload();
                break;
            default:
                break;
        }
    }

    @OnItemClick(R.id.evaluate_gv)
    public void onItemClick(int position) {
        if (position == mPathList.size()) {
            goPictureSelector();
        }
    }

    private void conformUpload(){

        if(mContentEt.getText().toString().trim().equals("")){
            Mytoast.show(this,getResources().getString(R.string.toast_review));
            return;
        }

        uploadRequest();

    }

    //选择图片
    public void goPictureSelector() {
        ImageLoader loader = new ImageLoader();
        ImgSelConfig config = new ImgSelConfig.Builder(this, loader)
                .multiSelect(true)
                .rememberSelected(false)
                .btnBgColor(Color.GRAY)
                .btnTextColor(Color.BLACK)
                .statusBarColor(Color.parseColor("#64b4ff"))
                .backResId(R.drawable.go_back)
                .title(getResources().getString(R.string.photo))
                .titleColor(Color.WHITE)
                .titleBgColor(Color.parseColor("#64b4ff"))
                .cropSize(1, 1, 200, 200)
                .needCrop(false)
                .needCamera(true)
                .maxNum(10)
                .build();
        ImgSelActivity.startActivity(this, config, Constant.SELECT_PHOTO_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:
//                Bundle b = data.getExtras(); //data为B中回传的Intent
                if (requestCode == Constant.SELECT_PHOTO_CODE) {
                    if (data != null) {
                        List<String> tempList = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT);
                        for (String path : tempList) {
                            if (mPathList.size() >= 10) {
                                break;
                            } else {
                                mPathList.add(path);
                            }
                        }
                        mGridViewAdapter.notifyDataSetChanged();
                    }
                }
                break;
            default:
                break;
        }
    }

    private void uploadRequest(){
        mWaitDialog = new WaitDialog(EvaluateActivity.this,R.style.loading_dialog);
        mWaitDialog.show();
        Map<String, String> params = new HashMap<>();
        params.put("rating", (int)mRb.getRating()+"");
        params.put("item_id", mInfo.get_id());
        params.put("content", mContentEt.getText().toString().trim());
        params.put("t", "review");

        String uId = PreferencesUtils.getString(EvaluateActivity.this, Constant.SP_USER_ID,"");
        Log.i(TAG, "postReportRequest url === " + URLInfo.addReview + params.toString());
        OkHttpUtils.getInstance().okHttpPost(URLInfo.addReview,uId , params,
            new RequestCallBack() {
                    @Override
                    public void onSuccess(String result) {
                        if(null != result){
                            mWaitDialog.dismiss();
                            Log.i(TAG,result);
                            if(result.contains("\"status\": \"ok\"")){
                                Mytoast.show(EvaluateActivity.this,getResources().getString(R.string.toast_publish));
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFail() {
                        mWaitDialog.dismiss();
                        Mytoast.show(EvaluateActivity.this, getResources().getString(R.string.net_bad_retry));
                    }
                });
    }

}
