package com.example.chaoren.myapplication.activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chaoren.myapplication.BaseActivity;
import com.example.chaoren.myapplication.R;
import com.example.chaoren.myapplication.adapter.DataAdapter;
import com.example.chaoren.myapplication.bean.ItemModel;
import com.example.chaoren.myapplication.pullrefresh.interfaces.OnLoadMoreListener;
import com.example.chaoren.myapplication.pullrefresh.recyclerview.LuRecyclerView;
import com.example.chaoren.myapplication.pullrefresh.recyclerview.LuRecyclerViewAdapter;
import com.example.chaoren.myapplication.pullrefresh.util.AppUtil;
import com.example.chaoren.myapplication.pullrefresh.util.LuRecyclerViewStateUtils;
import com.example.chaoren.myapplication.pullrefresh.view.LoadingFooter;
import com.example.chaoren.myapplication.util.NetUtils;
import com.example.chaoren.myapplication.util.StatusBarCompat;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by chaoren on 2016/12/26.
 */

public class SwipeRefreshActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {
    private static final int TOTAL_COUNTER = 34;//服务器端一共多少条数据
    private static final int REQUEST_COUNT = 10;//每一页展示多少条数据
    private static int mCurrentCounter = 0;//已经获取到多少条数据了
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView = null;

    private PreviewHandler mHandler = new PreviewHandler(this);
    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;

    private DataAdapter mDataAdapter = null;
    private boolean isRefresh = false;

    private TextView textview_title;
    private Toolbar toolbar;
    private View viewHead;
    private String titleFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);
        StatusBarCompat.compat(this,this.getResources().getColor(R.color.colorAccent));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        textview_title = (TextView) findViewById(R.id.textview_title);
        mRecyclerView = (LuRecyclerView) findViewById(R.id.list);
        if(getIntent()!=null){
            titleFlag = getIntent().getStringExtra("titleFlag");
        }
        doToolBarInit();
        doMyInit();
        doInit();
    }

    private void doToolBarInit() {
        toolbar.setTitle("");
        if(!TextUtils.isEmpty(titleFlag)){
            textview_title.setText(titleFlag);
        }else{
            textview_title.setText("SwipeRefreshLayout");
        }

        toolbar.setNavigationIcon(this.getResources().getDrawable(R.mipmap.back));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void doMyInit() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(this, 48));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               SwipeRefreshActivity.this.finish();
            }
        });
    }

    private void doInit() {
        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(this, 48));
            mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                    android.R.color.holo_orange_light, android.R.color.holo_green_light);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }

        mDataAdapter = new DataAdapter(this);
        mLuRecyclerViewAdapter = new LuRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLuRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//      mLuRecyclerViewAdapter.addHeaderView(new SampleHeader(this));
        mLuRecyclerViewAdapter.addHeaderView(createHeadview());
        //mLuRecyclerViewAdapter.addFooterView(new SampleFooter(this));//foot
        mRecyclerView.setOnLoadMoreListener(this);
        onRefresh();
    }

    private View createHeadview() {
         viewHead = LayoutInflater.from(this).inflate(R.layout.sample_header,null);
        TextView textView = (TextView) viewHead.findViewById(R.id.person_video_list_honey_count);
        textView.setText("陈有超");
        return viewHead;
    }

    @Override
    public void onRefresh() {
        mCurrentCounter = 0;
        isRefresh = true;
        mSwipeRefreshLayout.setRefreshing(true);
        requestData();
    }

    @Override
    public void onLoadMore() {
        LoadingFooter.State state = LuRecyclerViewStateUtils.getFooterViewState(mRecyclerView);
        if (state == LoadingFooter.State.Loading) return;
        if (mCurrentCounter < TOTAL_COUNTER) {
            LuRecyclerViewStateUtils.setFooterViewState(SwipeRefreshActivity.this, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
          requestData();
        } else {
            LuRecyclerViewStateUtils.setFooterViewState(SwipeRefreshActivity.this, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
        }
    }

    private void notifyDataSetChanged() {
        mLuRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<ItemModel> list) {
        mDataAdapter.addAll(list);
        mCurrentCounter += list.size();
    }

    private static class PreviewHandler extends Handler {
        private WeakReference<SwipeRefreshActivity> ref;
        PreviewHandler(SwipeRefreshActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final SwipeRefreshActivity activity = ref.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            switch (msg.what) {

                case -1:
                    if (activity.isRefresh) {
                        activity.mDataAdapter.clear();
                        mCurrentCounter = 0;
                    }
                    int currentSize = activity.mDataAdapter.getItemCount();
                    ArrayList<ItemModel> newList = new ArrayList<>();
                    for (int i = 0; i < 10; i++) {
                        if (newList.size() + currentSize >= TOTAL_COUNTER) {
                            break;
                        }

                        ItemModel item = new ItemModel();
                        item.id = currentSize + i;
                        item.title = "item" + (item.id);
                        newList.add(item);
                    }

                    activity.addItems(newList);
                    if (activity.isRefresh) {
                        activity.isRefresh = false;
                        activity.mSwipeRefreshLayout.setRefreshing(false);
                    }
                    LuRecyclerViewStateUtils.setFooterViewState(activity.mRecyclerView, LoadingFooter.State.Normal);
                    activity.notifyDataSetChanged();
                    break;
                case -2:
                    activity.mSwipeRefreshLayout.setRefreshing(false);
                    activity.notifyDataSetChanged();
                    break;
                case -3:
                    if (activity.isRefresh) {
                        activity.isRefresh = false;
                        activity.mSwipeRefreshLayout.setRefreshing(false);
                    }
                    Toast.makeText(activity.getApplicationContext(),"网络请求失败",Toast.LENGTH_SHORT).show();
                    LuRecyclerViewStateUtils.setFooterViewState(activity, activity.mRecyclerView, REQUEST_COUNT, LoadingFooter.State.NetWorkError, activity.mFooterClick);
                    activity.notifyDataSetChanged();
                    break;
            }
        }
    }

    private View.OnClickListener mFooterClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LuRecyclerViewStateUtils.setFooterViewState(SwipeRefreshActivity.this, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
            requestData();
        }
    };

    /**
     * 模拟请求网络
     */
    private void requestData() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(NetUtils.isNetworkConnected(getApplicationContext())){
                    mHandler.sendEmptyMessage(-1);
                } else {
                    mHandler.sendEmptyMessage(-3);
                }
            }
        }.start();
    }

}
