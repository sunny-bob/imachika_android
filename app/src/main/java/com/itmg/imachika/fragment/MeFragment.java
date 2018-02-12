package com.itmg.imachika.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.itmg.imachika.R;
import com.itmg.imachika.model.User;
import com.itmg.imachika.ui.ConcernActivity;
import com.itmg.imachika.ui.NewNoticeActivity;
import com.itmg.imachika.ui.UserInfoActivity;
import com.itmg.imachika.ui.VersionInfoActivity;
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

public class MeFragment extends Fragment {
    View view;
    Unbinder unbinder;
    @BindView(R.id.head_img)
    ImageView headImg;
    @BindView(R.id.tv_name)
    TextView tvName;
    Intent intent = new Intent();
    String userInfoStr;
    User.UserInfo userInfo;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.me, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        userInfoStr = PreferencesUtils.getString(getActivity(),"userInfo","");
        userInfo = (User.UserInfo) GsonUtil.praseJsonToModel(userInfoStr,User.UserInfo.class);
        if (!userInfoStr.equals("")){
            tvName.setText(userInfo.getUser_name());
            GlideUtil.setImg(getActivity(),headImg,userInfo.getImage());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.layout_info, R.id.tv_mark1, R.id.tv_mark2, R.id.tv_mark3, R.id.layout_one, R.id.layout_two, R.id.layout_three})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_info:
                intent.setClass(getActivity(), UserInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_mark1:
                break;
            case R.id.tv_mark2:
                break;
            case R.id.tv_mark3:
                intent.setClass(getActivity(), ConcernActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_one:
                intent.setClass(getActivity(), ConcernActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_two:
                intent.setClass(getActivity(), NewNoticeActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_three:
                intent.setClass(getActivity(), VersionInfoActivity.class);
                startActivity(intent);
                break;
        }
    }
}
