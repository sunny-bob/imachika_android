package com.itmg.imachika.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itmg.imachika.R;
import com.itmg.imachika.adapter.ExpandAdapter;
import com.itmg.imachika.adapter.PicGridViewAdapter;
import com.itmg.imachika.model.All;
import com.itmg.imachika.util.Constant;
import com.itmg.imachika.view.ImageLoader;
import com.itmg.imachika.view.MyGridView;
import com.yuyh.library.imgsel.ImgSelActivity;
import com.yuyh.library.imgsel.ImgSelConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2017/11/9 0009.
 */

public class AddFragment extends Fragment {
    @BindView(R.id.add_img_gv)
    MyGridView imgGridView;
    @BindView(R.id.add_img_con)
    EditText imgConEt;
    @BindView(R.id.add_et_one)
    EditText oneEt;
    @BindView(R.id.add_et_two)
    EditText twoEt;
    @BindView(R.id.add_category_tv)
    TextView catTv;
    @BindView(R.id.add_tel_tv)
    TextView telTv;
    @BindView(R.id.add_phone_tv)
    TextView phoneTv;
    @BindView(R.id.add_address_tv)
    TextView addressTv;
    @BindView(R.id.add_show)
    TextView showTv;
    @BindView(R.id.ex_listView)
    ExpandableListView exListView;
    @BindView(R.id.add_show_view)
    LinearLayout addShowView;
    @BindView(R.id.add_time_start_view)
    LinearLayout addStartView;
    @BindView(R.id.add_time_end_view)
    LinearLayout addEndView;
    @BindView(R.id.add_price_view)
    LinearLayout addPriceView;
    @BindView(R.id.add_time_start_tv)
    TextView addStartTv;
    @BindView(R.id.add_time_end_tv)
    TextView addEndTv;
    @BindView(R.id.add_price_tv)
    TextView addPriceTv;
    @BindView(R.id.add_phone_view)
    LinearLayout addPhoneView;

    private static final String TAG = "AddFragment";
    View view;
    List<All> groups = new ArrayList<>();
    List<List<All.Cat2s>> chlids = new ArrayList<>();
    ExpandAdapter expandAdapter;
    PicGridViewAdapter imgAdapter;
    Unbinder unbinder;
    private ArrayList<String> mPathList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add, null);
        unbinder = ButterKnife.bind(this, view);
//        for (int i = 0; i < 3; i++) {
//            groups.add(i);
//        }
//        chlids.add(groups);
//        chlids.add(groups);
//        chlids.add(groups);

        intitView();

        return view;
    }

    private void intitView(){
        imgAdapter = new PicGridViewAdapter(getActivity(),mPathList,1);
        imgGridView.setAdapter(imgAdapter);
        imgGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == adapterView.getCount()){
                    goPictureSelector();
                }
            }
        });
        expandAdapter = new ExpandAdapter(getActivity(), groups, chlids);
        exListView.setAdapter(expandAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.add_title_one,R.id.add_title_two,R.id.add_title_three,R.id.add_cat_view,
            R.id.add_show,R.id.add_upload})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.add_title_one:
                addPhoneView.setVisibility(View.VISIBLE);
                addStartView.setVisibility(View.GONE);
                addEndView.setVisibility(View.GONE);
                addPriceView.setVisibility(View.GONE);
                Log.i(TAG,"add_title_one is clicked");
                break;
            case R.id.add_title_two:
                addPhoneView.setVisibility(View.GONE);
                addStartView.setVisibility(View.GONE);
                addEndView.setVisibility(View.GONE);
                addPriceView.setVisibility(View.VISIBLE);
                Log.i(TAG,"add_title_two is clicked");
                break;
            case R.id.add_title_three:
                addPhoneView.setVisibility(View.GONE);
                addStartView.setVisibility(View.VISIBLE);
                addEndView.setVisibility(View.VISIBLE);
                addPriceView.setVisibility(View.GONE);
                Log.i(TAG,"add_title_three is clicked");
                break;
            case R.id.add_cat_view:
                Log.i(TAG,"add_cat_view is clicked");
                break;
            case R.id.add_show:
                if(addShowView.isShown()){
                    addShowView.setVisibility(View.GONE);
                    showTv.setText(getResources().getString(R.string.submit_show));
                }else{
                    addShowView.setVisibility(View.VISIBLE);
                    showTv.setText(getResources().getString(R.string.submit_hint));
                }
                Log.i(TAG,"add_show is clicked");
                break;
            case R.id.add_upload:
                Log.i(TAG,"add_upload is clicked");
                break;
        }
    }

    //选择图片
    public void goPictureSelector() {
        ImageLoader loader = new ImageLoader();
        ImgSelConfig config = new ImgSelConfig.Builder(getActivity(), loader)
                .multiSelect(true)
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
                .maxNum(10)
                .build();
        ImgSelActivity.startActivity(getActivity(), config, Constant.SELECT_PHOTO_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:
//                Bundle b = data.getExtras(); //data为B中回传的Intent
                if (requestCode == Constant.SELECT_PHOTO_CODE) {//读取照片
                    if (data != null) {
                        List<String> tempList = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT);
                        for (String path : tempList) {
                            if (mPathList.size() >= 10) {
                                break;
                            } else {
                                mPathList.add(path);
                            }
                        }
                        imgAdapter.notifyDataSetChanged();

//                        FileInputStream fis = null;
//                        try {
//                            Log.e("sdPath2",mPathList.get(0));
//                            //把图片转化为字节流
//                            fis = new FileInputStream(mPathList.get(0));
//                            //把流转化图片
//                            Bitmap bitmap = BitmapFactory.decodeStream(fis);
//                            uploadImgRequest(mPathList.get(0), ImgUtil.zoomImg(bitmap,375,375));
////                            mImageView.setImageBitmap(bitmap);
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        }finally{
//                            try {
//                                fis.close();//关闭流
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }

                        Log.i(TAG,"mPathList --- "+mPathList.size());
                    }
                }else if(requestCode == Constant.SELECT_CAMERA_CODE){//拍照返回
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");

                }
                break;
            default:
                break;
        }
    }

}
