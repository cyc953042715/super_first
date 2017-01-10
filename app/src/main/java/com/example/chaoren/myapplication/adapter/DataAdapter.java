package com.example.chaoren.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chaoren.myapplication.R;
import com.example.chaoren.myapplication.base.ListBaseAdapter;
import com.example.chaoren.myapplication.bean.ItemModel;

/**
 * Created by chaoren on 2016/12/27.
 */

public class DataAdapter extends ListBaseAdapter<ItemModel> {
    private LayoutInflater mLayoutInflater;
    public DataAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DataAdapter.ViewHolder(mLayoutInflater.inflate(R.layout.list_item_text, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemModel item = mDataList.get(position);
        DataAdapter.ViewHolder viewHolder = (DataAdapter.ViewHolder) holder;
        viewHolder.textView.setText(item.title);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.info_text);
        }
    }
}
