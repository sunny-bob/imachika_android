package com.itmg.imachika.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.itmg.imachika.MainActivity;
import com.itmg.imachika.R;
import com.itmg.imachika.application.APP;
import com.itmg.imachika.contans.Contans;
import com.itmg.imachika.model.User;
import com.itmg.imachika.util.CheckUtil;
import com.itmg.imachika.util.Constant;
import com.itmg.imachika.util.GsonUtil;
import com.itmg.imachika.util.ImgUtil;
import com.itmg.imachika.util.Mytoast;
import com.itmg.imachika.util.OkHttpUtil;
import com.itmg.imachika.util.PreferencesUtils;
import com.itmg.imachika.util.URLInfo;
import com.itmg.imachika.view.ImageLoader;
import com.itmg.imachika.view.SelectPicPopupWindow;
import com.yuyh.library.imgsel.ImgSelActivity;
import com.yuyh.library.imgsel.ImgSelConfig;

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
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class EmailRegActivity extends Activity implements OkHttpUtil.Result {
    @BindView(R.id.et_nick_name)
    EditText etNickName;
    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.et_mail)
    EditText etMail;

    @BindView(R.id.img)
    ImageView headImg;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    String name = "", gender = "", phone ="",mail="", pwd="",location = "",file = "";
    @BindView(R.id.women_btn)
    RadioButton womenBtn;
    @BindView(R.id.man_btn)
    RadioButton manBtn;
    @BindView(R.id.login_btn)
    Button loginBtn;

    private static final String TAG = "EmailRegActivity";
    SharedPreferences sp;
    Intent intent = new Intent();
    APP app = APP.getMyApplication();
    private ArrayList<String> mPathList = new ArrayList<>();
    private SelectPicPopupWindow menuPicWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_reg);
        ButterKnife.bind(this);
        app.addActivity(this);
    }

    @OnClick({R.id.img_back, R.id.img, R.id.login_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img:
                picSelector();
                break;
            case R.id.login_btn:
                name = etNickName.getText().toString();
                if (womenBtn.isChecked()){
                    gender = "female";
                }
                if (manBtn.isChecked()){
                    gender = "male";
                }
                phone = etNumber.getText().toString();
                mail = etMail.getText().toString();
                pwd = etPwd.getText().toString();
                if (!mail.isEmpty() && !pwd.isEmpty()){
                    if (CheckUtil.checkEmail(mail)){
                        register();
                    }else{
                        Mytoast.show(getApplicationContext(),getResources().getString(R.string.toast_bad_email));
                    }
                }else{
                    Mytoast.show(getApplicationContext(),getResources().getString(R.string.toast_null_email));
                }
                break;
        }
    }

    //图片打开选择
    private void picSelector() {
		hideKeyBoard();
        menuPicWindow = new SelectPicPopupWindow(this, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuPicWindow.backgroundAlpha(EmailRegActivity.this, 1f);
                menuPicWindow.dismiss();
                switch (view.getId()) {
                    case R.id.setting_takePhoto://拍照
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
        menuPicWindow.showAtLocation(this.findViewById(R.id.img), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
    }

    private void hideKeyBoard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.RESULT_HIDDEN);
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
                .title("图片")
                .titleColor(Color.WHITE)
                .titleBgColor(Color.parseColor("#64b4ff"))
                .cropSize(1, 1, 200, 200)
                .needCrop(false)
                .needCamera(true)
                .maxNum(1)
                .build();
        ImgSelActivity.startActivity(this, config, Constant.SELECT_PHOTO_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:
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
                            file = ImgUtil.imgToBase64(mPathList.get(0), bitmap,".jpg");
                            headImg.setImageBitmap(bitmap);
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
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");

                    Uri originalUri = data.getData();        //获得图片的uri

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

                    file = ImgUtil.imgToBase64(path, bitmap,".jpg");

                }
                break;
            default:
                break;
        }
    }

    private void register() {
        Map<String,String> map = new HashMap<>();
        map.put("name",name);
        map.put("email",mail);
        map.put("pwd",pwd);
        map.put("tel",phone);
        map.put("gender",gender);
        if(!"".equals(PreferencesUtils.getString(this,"location",""))){
            location = PreferencesUtils.getString(this,"location","");
            map.put("location",location);
        }
        map.put("file",file);
        String data = GsonUtil.mapToJson(map);
        RequestBody body = new FormBody.Builder().add("data",data).build();
        OkHttpUtil.post(URLInfo.register, Contans.register,body,this,this);
    }

    @Override
    public void result(final String s, final int code) {
        runOnUiThread(new Runnable() {
            @SuppressLint("ApplySharedPref")
            @Override
            public void run() {
                switch (code){
                    case Contans.error:
                        break;
                    case Contans.register:
                        Log.i(Contans.TAG,s);
                        User user = (User) GsonUtil.praseJsonToModel(s,User.class);
                        if (user.getStatus().equals("ok")){
                            String userInfo = GsonUtil.objectToJson(user.getUser());
                            PreferencesUtils.putBoolean(EmailRegActivity.this,"isLogin",true);
                            PreferencesUtils.putString(EmailRegActivity.this,"userInfo",userInfo);
                            PreferencesUtils.putString(EmailRegActivity.this,"id",user.getUser().get_id());
                            intent.setClass(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Mytoast.show(getApplicationContext(),user.getMsg());
                        }
                        break;
                }
            }
        });
    }
}
