package com.example.chaoren.myapplication.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.chaoren.myapplication.BaseActivity;
import com.example.chaoren.myapplication.R;
import com.example.chaoren.myapplication.pullrefresh.recyclerview.LRecyclerView;
import com.example.chaoren.myapplication.util.StatusBarCompat;

/**
 * Author by chaoren
 * Date by 2017/1/6
 * Explanation ：
 */

public class EditTextActivity extends BaseActivity {
    private TextView textview_title;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edittext);
        StatusBarCompat.compat(this,this.getResources().getColor(R.color.colorAccent));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        textview_title = (TextView) findViewById(R.id.textview_title);
        textview_title.setText("测试edittext");
    }
}
