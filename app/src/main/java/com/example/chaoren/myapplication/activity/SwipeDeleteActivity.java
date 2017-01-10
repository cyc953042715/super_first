package com.example.chaoren.myapplication.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chaoren.myapplication.BaseActivity;
import com.example.chaoren.myapplication.R;
import com.example.chaoren.myapplication.adapter.SwipeMenuAdapter;
import com.example.chaoren.myapplication.bean.ItemModel;
import com.example.chaoren.myapplication.pullrefresh.interfaces.OnRefreshListener;
import com.example.chaoren.myapplication.pullrefresh.recyclerview.LRecyclerView;
import com.example.chaoren.myapplication.pullrefresh.recyclerview.LRecyclerViewAdapter;
import com.example.chaoren.myapplication.pullrefresh.recyclerview.ProgressStyle;
import com.example.chaoren.myapplication.pullrefresh.util.RecyclerViewStateUtils;
import com.example.chaoren.myapplication.pullrefresh.view.LoadingFooter;
import com.example.chaoren.myapplication.util.NetworkUtils;
import com.example.chaoren.myapplication.util.StatusBarCompat;
import com.example.chaoren.myapplication.util.TLog;
import com.example.chaoren.myapplication.view.SampleHeader;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Author by chaoren
 * Date by 2017/1/5
 * Explanation ：
 */

public class SwipeDeleteActivity extends BaseActivity {
    private TextView textview_title;
    private Toolbar toolbar;
    private LRecyclerView mRecyclerView;

    private static final String TAG = "lzx";

    /**服务器端一共多少条数据*/
    private static final int TOTAL_COUNTER = 64;

    /**每一页展示多少条数据*/
    private static final int REQUEST_COUNT = 10;

    /**已经获取到多少条数据了*/
    private static int mCurrentCounter = 0;

    private SwipeMenuAdapter mDataAdapter = null;

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    private boolean isRefresh = false;
    private PreviewHandler mHandler = new PreviewHandler(this);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        StatusBarCompat.compat(this,this.getResources().getColor(R.color.colorAccent));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (LRecyclerView) findViewById(R.id.refresh_listview);
        textview_title = (TextView) findViewById(R.id.textview_title);


        ArrayList<ItemModel> dataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ItemModel itemModel = new ItemModel();
            itemModel.title = "item" + i;
            dataList.add(itemModel);
            mCurrentCounter += dataList.size();
        }

        mDataAdapter = new SwipeMenuAdapter(this);
        mDataAdapter.setDataList(dataList);
        mDataAdapter.setOnDelListener(new SwipeMenuAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                Toast.makeText(SwipeDeleteActivity.this, "删除:" + pos, Toast.LENGTH_SHORT).show();
                //RecyclerView关于notifyItemRemoved的那点小事 参考：http://blog.csdn.net/jdsjlzx/article/details/52131528
                mDataAdapter.getDataList().remove(pos);
                mDataAdapter.notifyItemRemoved(pos);//推荐用这个

                if(pos != (mDataAdapter.getDataList().size())){ // 如果移除的是最后一个，忽略
                    mDataAdapter.notifyItemRangeChanged(pos, mDataAdapter.getDataList().size() - pos);
                }
//                且如果想让侧滑菜单同时关闭，需要同时调用
//                        ((CstSwipeDelMenu) holder.itemView).quickClose();
            }

            @Override
            public void onTop(int pos) {  //置顶功能有bug，后续解决
//                ItemModel itemModel = mDataAdapter.getDataList().get(pos);
//                mDataAdapter.getDataList().remove(pos);
//                mDataAdapter.notifyItemRemoved(pos);
//                mDataAdapter.getDataList().add(0, itemModel);
//                mDataAdapter.notifyItemInserted(0);
//                if(pos != (mDataAdapter.getDataList().size())){ // 如果移除的是最后一个，忽略
//                    mDataAdapter.notifyItemRangeChanged(0, mDataAdapter.getDataList().size() - 1,"jdsjlzx");
//                }
//                mRecyclerView.scrollToPosition(0);
            }
        });
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

        mLRecyclerViewAdapter.addHeaderView(new SampleHeader(this));

        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.Normal);
                mDataAdapter.clear();
                mLRecyclerViewAdapter.notifyDataSetChanged();//fix bug:crapped or attached views may not be recycled. isScrap:false isAttached:true
                mCurrentCounter = 0;
                isRefresh = true;
                requestData();
            }
        });

        mRecyclerView.setRefreshing(true);

    }

    private View.OnClickListener mFooterClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerViewStateUtils.setFooterViewState(SwipeDeleteActivity.this, mRecyclerView, REQUEST_COUNT, LoadingFooter.State.Loading, null);
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
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //模拟一下网络请求失败的情况
                if(NetworkUtils.isNetAvailable(SwipeDeleteActivity.this)) {
                    mHandler.sendEmptyMessage(-1);
                } else {
                    mHandler.sendEmptyMessage(-3);
                }
            }
        }.start();
    }

    private void notifyDataSetChanged() {
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<ItemModel> list) {

        mDataAdapter.addAll(list);
        mCurrentCounter += list.size();

    }

    private static class PreviewHandler extends Handler {

        private WeakReference<SwipeDeleteActivity> ref;

        PreviewHandler(SwipeDeleteActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final SwipeDeleteActivity activity = ref.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            switch (msg.what) {

                case -1:
                    if(activity.isRefresh){
                        activity.mDataAdapter.clear();
                        mCurrentCounter = 0;
                    }

                    int currentSize = activity.mDataAdapter.getItemCount();

                    //模拟组装10个数据
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

                    if(activity.isRefresh){
                        activity.isRefresh = false;
                        activity.mRecyclerView.refreshComplete();
                    }

                    RecyclerViewStateUtils.setFooterViewState(activity.mRecyclerView, LoadingFooter.State.Normal);
                    activity.notifyDataSetChanged();
                    break;
                case -2:
                    activity.notifyDataSetChanged();
                    break;
                case -3:
                    if(activity.isRefresh){
                        activity.isRefresh = false;
                        activity.mRecyclerView.refreshComplete();
                    }
                    activity.notifyDataSetChanged();
                    RecyclerViewStateUtils.setFooterViewState(activity, activity.mRecyclerView, REQUEST_COUNT, LoadingFooter.State.NetWorkError, activity.mFooterClick);
                    break;
                default:
                    break;
            }
        }
    }
}
