package com.itmg.imachika.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.itmg.imachika.R;
import com.itmg.imachika.adapter.NearAdapter;
import com.itmg.imachika.application.APP;
import com.itmg.imachika.contans.Contans;
import com.itmg.imachika.model.NearPerson;
import com.itmg.imachika.util.GsonUtil;
import com.itmg.imachika.util.OkHttpUtil;
import com.itmg.imachika.util.URLInfo;
import com.itmg.imachika.view.MyDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NearActivity extends Activity implements OkHttpUtil.Result {
    List<NearPerson.Data> list = new ArrayList<>();
    NearAdapter adapter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    APP app = APP.getMyApplication();
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.img_more)
    ImageView imgMore;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    String location ,uid;//测试值
    SharedPreferences sp;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near);
        ButterKnife.bind(this);
        app.addActivity(this);
        tvTitle.setText(getResources().getString(R.string.near));
        //设置Item增加、移除动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        recyclerView.addItemDecoration(new MyDecoration(this, MyDecoration.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        sp = getSharedPreferences("login",MODE_PRIVATE);
        location = sp.getString("location","");
        uid  = sp.getString("id","");
        loadData();
    }

    private void loadData() {
        location = "35.702069,139.775327";
        String url = URLInfo.infoList + "?catid=people_nearby" + "&location=" + location;
        Log.i("url",url);
        OkHttpUtil.get(url,uid, Contans.near,this,this);
    }

    @OnClick({R.id.img_back, R.id.img_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_more:
                break;
        }
    }

    @Override
    public void result(final String s, final int code) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (code){
                    case Contans.error:
                        break;
                    case Contans.near:
                        Log.i(Contans.TAG,s);
                        NearPerson nearPerson = (NearPerson) GsonUtil.praseJsonToModel(s,NearPerson.class);
                        if (nearPerson.getStatus().equals("ok")){
                            list = nearPerson.getData();
                            adapter = new NearAdapter(NearActivity.this, list);
                            recyclerView.setAdapter(adapter);
                            adapter.setmOnItemClickListener(new NearAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    intent = new Intent();
                                    intent.setClass(getApplicationContext(),UserHomePageActivity.class);
                                    intent.putExtra("person",list.get(position));
                                    startActivity(intent);
                                }
                            });
                        }
                        break;
                }
            }
        });
    }
}
