package com.example.chaoren.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.chaoren.myapplication.activity.EditTextActivity;
import com.example.chaoren.myapplication.activity.MyInfoActivity;
import com.example.chaoren.myapplication.activity.SwipeDeleteActivity;
import com.example.chaoren.myapplication.activity.SwipeRefreshActivity;
import com.example.chaoren.myapplication.adapter.DataAdapter;
import com.example.chaoren.myapplication.adapter.MenuAdapter;
import com.example.chaoren.myapplication.bean.ItemModel;
import com.example.chaoren.myapplication.bean.MenuEntity;
import com.example.chaoren.myapplication.pullrefresh.interfaces.OnItemClickListener;
import com.example.chaoren.myapplication.pullrefresh.interfaces.OnLoadMoreListener;
import com.example.chaoren.myapplication.pullrefresh.recyclerview.LuRecyclerView;
import com.example.chaoren.myapplication.pullrefresh.recyclerview.LuRecyclerViewAdapter;
import com.example.chaoren.myapplication.pullrefresh.util.AppUtil;
import com.example.chaoren.myapplication.pullrefresh.util.LuRecyclerViewStateUtils;
import com.example.chaoren.myapplication.pullrefresh.view.LoadingFooter;
import com.example.chaoren.myapplication.util.StatusBarCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chaoren on 2016/12/27.
 */

public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, AdapterView.OnItemClickListener, OnLoadMoreListener, OnItemClickListener {
    SwipeRefreshLayout swipe_refresh_layout;
    Toolbar toolbar;
    ActionBarDrawerToggle mDrawerToggle;
    private String[] lvs = {"SwipeDeleteActivity","EditTextActivity","消息中心","我的收藏","我的文件"};
    private MenuAdapter menuAdapter;
    List<MenuEntity> menuList = new ArrayList<>();
    private ListView lvLeftMenu;
    private DrawerLayout mDrawerLayout;
    private TextView mRightTextView,mTitleTextView;
    private LuRecyclerView mRecyclerView = null;

    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;
    private DataAdapter mDataAdapter = null;
    private boolean isRefresh = false;
    private static final int TOTAL_COUNTER = 34;//服务器端一共多少条数据
    private static final int REQUEST_COUNT = 10;//每一页展示多少条数据
    private static int mCurrentCounter = 0;//已经获取到多少条数据了

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarCompat.compat(this,this.getResources().getColor(R.color.colorAccent));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        lvLeftMenu = (ListView) findViewById(R.id.lv_left_menu);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
        mRightTextView = (TextView) findViewById(R.id.textview_right);
        mTitleTextView = (TextView) findViewById(R.id.textview_title);
        mRecyclerView = (LuRecyclerView) findViewById(R.id.mainlistview);

        setRightText(mRightTextView,"个人信息",R.mipmap.arrow_down);
        mRightTextView.setOnClickListener(this);

        doToolBarInit();
        doInit();
    }

    private void doToolBarInit() {
        toolbar.setBackgroundColor(this.getResources().getColor(R.color.colorAccent));
        toolbar.setTitle("陈有超");
        toolbar.setTitleTextColor(this.getResources().getColor(R.color.white));

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,R.string.open,R.string.close){
            @Override
            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView){
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        for (int i = 0; i < lvs.length; i++) {
            MenuEntity menuEntity = new MenuEntity();
            menuEntity.setTitle(lvs[i]);
            menuList.add(menuEntity);
        }
        menuAdapter = new MenuAdapter(this,menuList);
        lvLeftMenu.setAdapter(menuAdapter);
        lvLeftMenu.setOnItemClickListener(this);
    }

    private void doInit() {
        swipe_refresh_layout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        if (swipe_refresh_layout != null) {
            swipe_refresh_layout.setProgressViewOffset(false, 0, AppUtil.dip2px(this, 48));
            swipe_refresh_layout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                    android.R.color.holo_orange_light, android.R.color.holo_green_light);
            swipe_refresh_layout.setOnRefreshListener(this);
        }

        mRecyclerView.setOnLoadMoreListener(this);
        mDataAdapter = new DataAdapter(this);
        mLuRecyclerViewAdapter = new LuRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLuRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mLuRecyclerViewAdapter.addHeaderView(createHeadview());
        mLuRecyclerViewAdapter.setOnItemClickListener(this);
        onRefresh();
    }

    private View createHeadview() {
       View viewHead = LayoutInflater.from(this).inflate(R.layout.view_head_home,null);
        return viewHead;
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        fillData();
//        swipe_refresh_layout.setRefreshing(false);
    }

    @Override
    public void onLoadMore() {
        LoadingFooter.State state = LuRecyclerViewStateUtils.getFooterViewState(mRecyclerView);
        if (state == LoadingFooter.State.Loading) return;
        if (mCurrentCounter < TOTAL_COUNTER) {
            LuRecyclerViewStateUtils.setFooterViewState(this, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
            fillData();
        } else {
            LuRecyclerViewStateUtils.setFooterViewState(this, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
        }
    }

    private void fillData() {
        if (isRefresh) {
            mDataAdapter.clear();
            mCurrentCounter = 0;
        }
        int currentSize = mLuRecyclerViewAdapter.getItemCount();
        ArrayList<ItemModel> newList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            if (newList.size() + currentSize >= TOTAL_COUNTER) {
                break;
            }
            ItemModel item = new ItemModel();
            item.id = currentSize + i;
            item.title = "跳转到SwipeRefreshActivity" + (item.id);
            newList.add(item);
        }

        addItems(newList);
        if (isRefresh) {
            isRefresh = false;
            swipe_refresh_layout.setRefreshing(false);
        }
        LuRecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.Normal);
        notifyDataSetChanged();
    }

    private void addItems(ArrayList<ItemModel> list) {
        mDataAdapter.addAll(list);
        mCurrentCounter += list.size();
    }

    private void notifyDataSetChanged() {
        mDataAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.textview_right:
                startAnimationView(view);
              startActivity(new Intent(this, MyInfoActivity.class));
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        switch (position){
            case 0:
                Intent DeleteIntent = new Intent(this, SwipeDeleteActivity.class);
                DeleteIntent.putExtra("titleFlag","SwipeDeleteActivity");
                startActivity(DeleteIntent);
                break;
            case 1:
                Intent editIntent = new Intent(this, EditTextActivity.class);
                editIntent.putExtra("titleFlag","EditTextActivity");
                startActivity(editIntent);
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        ArrayList<ItemModel> list = (ArrayList<ItemModel>) mDataAdapter.getDataList();
        Intent intent = new Intent(this, SwipeRefreshActivity.class);
        intent.putExtra("titleFlag",list.get(position).getTitle());
        startActivity(intent);
    }

}
