package com.example.chaoren.myapplication.activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chaoren.myapplication.BaseActivity;
import com.example.chaoren.myapplication.R;
import com.example.chaoren.myapplication.pullrefresh.util.AppToast;
import com.example.chaoren.myapplication.pullrefresh.util.AppUtil;
import com.example.chaoren.myapplication.util.StatusBarCompat;

/**
 * toolbar 测试实例
 */
public class MyInfoActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, Toolbar.OnMenuItemClickListener, View.OnClickListener {
    SwipeRefreshLayout swipe_refresh_layout;
    private TextView textview_title;
    private ImageView iamge_right;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        StatusBarCompat.compat(this,this.getResources().getColor(R.color.colorAccent));
        textview_title = (TextView) findViewById(R.id.textview_title);
        iamge_right = (ImageView) findViewById(R.id.iamge_right);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        doToolBarInit();
        doInit();
    }

    private void doToolBarInit() {
        toolbar.setBackgroundColor(this.getResources().getColor(R.color.colorAccent));
        toolbar.setTitle("");
        textview_title.setText("个人信息");
        textview_title.setTextColor(this.getResources().getColor(R.color.colorPrimary));
        toolbar.setNavigationIcon(this.getResources().getDrawable(R.mipmap.back));

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void doInit() {
        swipe_refresh_layout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipe_refresh_layout.setProgressViewOffset(false, 0, AppUtil.dip2px(this, 48));
        swipe_refresh_layout.setOnRefreshListener(this);
        iamge_right.setOnClickListener(this);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              MyInfoActivity.this.finish();
            }
        });
    }

    @Override
    public void onRefresh() {
        swipe_refresh_layout.setRefreshing(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        String msg = "";
        switch(item.getItemId()){
            case  R.id.linnear_layout:
                msg = "线性布局";
                break;
            case  R.id.grid_layout:
                msg = "网格布局";
                break;
            case  R.id.staggered_grid_layout:
                msg = "瀑布流布局";
                break;
        }
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.iamge_right:
                break;
        }
    }
}