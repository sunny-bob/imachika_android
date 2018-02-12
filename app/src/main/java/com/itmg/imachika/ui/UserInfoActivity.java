package com.itmg.imachika.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itmg.imachika.MainActivity;
import com.itmg.imachika.R;
import com.itmg.imachika.application.APP;
import com.itmg.imachika.model.User;
import com.itmg.imachika.util.Constant;
import com.itmg.imachika.util.GlideUtil;
import com.itmg.imachika.util.GsonUtil;
import com.itmg.imachika.util.ImgUtil;
import com.itmg.imachika.util.Mytoast;
import com.itmg.imachika.util.OkHttpUtils;
import com.itmg.imachika.util.PreferencesUtils;
import com.itmg.imachika.util.RequestCallBack;
import com.itmg.imachika.util.URLInfo;
import com.itmg.imachika.util.Utils;
import com.itmg.imachika.view.ImageLoader;
import com.itmg.imachika.view.SelectPicPopupWindow;
import com.itmg.imachika.view.SelectSexPopupWindow;
import com.itmg.imachika.view.WaitDialog;
import com.yuyh.library.imgsel.ImgSelActivity;
import com.yuyh.library.imgsel.ImgSelConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserInfoActivity extends Activity {

    @BindView(R.id.tv_exit)
    TextView tvExit;
    @BindView(R.id.head_img)
    ImageView headImg;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_gender)
    TextView tvGender;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private static final String TAG = "UserInfoActivity";
    private WaitDialog mWaitDialog;
    APP app = APP.getMyApplication();
    private SelectSexPopupWindow menuWindow;
    private SelectPicPopupWindow menuPicWindow;
    String uid;
    String userInfoStr;
    User.UserInfo userInfo;
    private ArrayList<String> mPathList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        app.addActivity(this);
        tvTitle.setText(getResources().getString(R.string.userInfo));
    }

    @Override
    protected void onResume() {
        super.onResume();
        uid = PreferencesUtils.getString(this,"id","");
        userInfoStr = PreferencesUtils.getString(this,"userInfo","");
        if (!userInfoStr.equals("")) {
            userInfo = (User.UserInfo) GsonUtil.praseJsonToModel(userInfoStr, User.UserInfo.class);
            tvName.setText(userInfo.getUser_name());
            if(null != userInfo.getGender()){
                if("male".equals(userInfo.getGender())){
                    tvGender.setText(getResources().getString(R.string.userInfo_male));
                }else if("female".equals(userInfo.getGender())){
                    tvGender.setText(getResources().getString(R.string.userInfo_female));
                }
            }
            tvPhone.setText(userInfo.getTel());
            tvAddress.setText(userInfo.getAddress());
            tvDesc.setText(userInfo.getSignature());
            GlideUtil.setImg(UserInfoActivity.this,headImg,userInfo.getImage());
        }
    }

    @OnClick({R.id.img_back, R.id.tv_exit,R.id.userInfo_img_view,R.id.userInfo_name_view,
            R.id.userInfo_sex_view, R.id.userInfo_tel_view,R.id.userInfo_address_view,
            R.id.userInfo_signature_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_exit:
                PreferencesUtils.putBoolean(this,Constant.SP_IS_LOGIN, false);
                PreferencesUtils.putString(this,Constant.SP_USER_INFO, "");
                PreferencesUtils.putString(this,Constant.SP_USER_NAME,"");
                PreferencesUtils.putString(this,Constant.SP_USER_ID,"");
                Utils.gotoNewAct(this,MainActivity.class);
//                MainActivity.table = 0;
//                Intent intent = new Intent(this, MainActivity.class);
//                intent.putExtra("type","home");
//                startActivity(intent);
                finish();
                break;
            case R.id.userInfo_img_view://修改个人头像
                picSelector();
                break;
            case R.id.userInfo_name_view://修改昵称
                Utils.gotoNewAct(this,EditUserInfoActivity.class,Constant.INFO_NAME);
                break;
            case R.id.userInfo_sex_view://修改性别
                Utils.gotoNewAct(this,EditUserInfoActivity.class,Constant.INFO_SEX);
                break;
            case R.id.userInfo_tel_view://修改手机号
                Utils.gotoNewAct(this,EditUserInfoActivity.class,Constant.INFO_TEL);
                break;
            case R.id.userInfo_address_view://修改地址
                Utils.gotoNewAct(this,EditUserInfoActivity.class,Constant.INFO_ADDRESS);
                break;
            case R.id.userInfo_signature_view://修改个性签名
                Utils.gotoNewAct(this,EditUserInfoActivity.class,Constant.INFO_SIGN);
                break;
            default:
                break;
        }
    }

    //选择图片
    public void goPictureSelector() {
        ImageLoader loader = new ImageLoader();
        ImgSelConfig config = new ImgSelConfig.Builder(this, loader)
                .multiSelect(false)
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
                .needCamera(false)
                .maxNum(1)
                .build();
        ImgSelActivity.startActivity(this, config, Constant.SELECT_PHOTO_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:
//                Bundle b = data.getExtras(); //data为B中回传的Intent
                if (requestCode == Constant.SELECT_PHOTO_CODE) {//读取照片
                    if (data != null) {
                        List<String> tempList = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT);
                        for (String path : tempList) {
                            mPathList.add(path);
                        }
                        FileInputStream fis = null;
                        try {
                            Log.e("sdPath2",mPathList.get(0));
                            //把图片转化为字节流
                            fis = new FileInputStream(mPathList.get(0));
                            //把流转化图片
                            Bitmap bitmap = BitmapFactory.decodeStream(fis);
                            uploadImgRequest(mPathList.get(0), ImgUtil.zoomImg(bitmap,375,375));
//                            mImageView.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }finally{
                            try {
                                fis.close();//关闭流
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.i(TAG,"mPathList --- "+mPathList.size());
                    }
                }else if(requestCode == Constant.SELECT_CAMERA_CODE){//拍照返回
                    Uri originalUri ;
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
                    Log.i(TAG,"bitmap === "+bitmap);
                    if (data.getData() != null) {
                        originalUri = data.getData();
                    }else{
                        originalUri  = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null,null));
                    }
                    //获取图片的路径：
                    String[] proj = {MediaStore.Images.Media.DATA};
                    //好像是android多媒体数据库的封装接口，具体的看Android文档
                    Cursor cursor = managedQuery(originalUri, proj, null, null, null);
                    //按我个人理解 这个是获得用户选择的图片的索引值
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    //将光标移至开头 ，这个很重要，不小心很容易引起越界
                    cursor.moveToFirst();
                    //最后根据索引值获取图片路径
                    String path = cursor.getString(column_index);
                    Log.i(TAG,"path === "+path);
                    uploadImgRequest(path, ImgUtil.zoomImg(bitmap,375,375));
//                    headImg.setImageBitmap(bitmap);
                }
                break;
            default:
                break;
        }
    }

    private void uploadImgRequest(String path ,Bitmap bitmap){//上传图像
        String url = URLInfo.editInfoUrl;
        mWaitDialog = new WaitDialog(this,R.style.loading_dialog,1);
        mWaitDialog.show();
        String file = ImgUtil.imgToBase64(path, bitmap,".jpg");
        Map<String, String> params = new HashMap<>();
        params.put("image", file);
        Log.i(TAG,"uploadImgRequest url === " + url+"  params == "+params.toString());
        String userId = PreferencesUtils.getString(this,Constant.SP_USER_ID,"");
        Log.i(TAG,"uploadImgRequest userId === " + userId );
        OkHttpUtils.getInstance().okHttpPost(url,userId , params, new RequestCallBack() {
            @Override
            public void onSuccess(String result) {
                mWaitDialog.dismiss();
                if(null != result){
                    Log.i(TAG,"uploadImgRequest  result = "+result);
                    User user = (User) GsonUtil.praseJsonToModel(result,User.class);
                    if (user.getStatus().equals("ok")){
                        String userInfo = GsonUtil.objectToJson(user.getData());
                        Log.i(TAG,"upDataInfo  userInfo === "+userInfo);
                        PreferencesUtils.putString(UserInfoActivity.this,"userInfo",userInfo);
                        UserInfoActivity.this.finish();
                    }else{
                        Mytoast.show(getApplicationContext(),user.getMsg());
                    }
                }
            }

            @Override
            public void onFail() {
                mWaitDialog.dismiss();
                Toast.makeText(UserInfoActivity.this,getResources().getString(R.string.net_bad_retry), Toast.LENGTH_LONG).show();
            }

        });

    }

    //图片打开选择
    private void picSelector() {
//		hideKeyBoard();
        menuPicWindow = new SelectPicPopupWindow(this, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuPicWindow.backgroundAlpha(UserInfoActivity.this, 1f);
                menuPicWindow.dismiss();
                switch (view.getId()) {
                    case R.id.setting_takePhoto://拍照 MediaStore.ACTION_IMAGE_CAPTURE
//
//                        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        String f = System.currentTimeMillis()+".jpg";
//                        Uri fileUri = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory("").getPath()+f));
//                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); //指定图片存放位置，指定后，在onActivityResult里得到的Data将为null
//                        startActivityForResult(openCameraIntent, Constant.SELECT_CAMERA_CODE);

                        Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // 启动相机
                        startActivityForResult(intent1,Constant.SELECT_CAMERA_CODE);
                        break;
                    case R.id.setting_selectPhoto://选择图片
                        goPictureSelector();
                        break;
                    default:
                        break;
                }
            }
        });
        //显示窗口
        menuPicWindow.showAtLocation(this.findViewById(R.id.tv_gender), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
    }

}
