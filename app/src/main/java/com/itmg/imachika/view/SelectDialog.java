package com.itmg.imachika.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.ListView;

import com.itmg.imachika.R;
import com.itmg.imachika.adapter.SortAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/2/1 0001.
 */

public class SelectDialog extends AlertDialog {
    private Context context;
    private ArrayList<String> datas;
    SortAdapter mAdapter;
    public SelectDialog(Context context, int theme,SortAdapter adapter) {
        super(context, theme);
        this.mAdapter = adapter;
    }

    public SelectDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_detail_more);
        ListView list = this.findViewById(R.id.shop_detail_more);
        list.setAdapter(mAdapter);
    }
}
