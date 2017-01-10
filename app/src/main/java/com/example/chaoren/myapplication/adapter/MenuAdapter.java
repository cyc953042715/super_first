package com.example.chaoren.myapplication.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.chaoren.myapplication.R;
import com.example.chaoren.myapplication.bean.MenuEntity;

import java.util.List;

/**
 * Created by chaoren on 2016/12/27.
 */

public class MenuAdapter extends BaseAdapter {
    private Context context;
    private List<MenuEntity> menuList;

    public MenuAdapter(Context context, List<MenuEntity> menuList) {
        this.context = context;
        this.menuList = menuList;
    }

    @Override
    public int getCount() {
        return menuList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        MenuHolder holder = null;
        if (null == convertView) {
            holder = new MenuHolder();
            convertView = View.inflate(context, R.layout.item_menu, null);
            holder.textview_menu = (TextView) convertView.findViewById(R.id.textview_menu);
            convertView.setTag(holder);
        } else {
            holder = (MenuHolder) convertView.getTag();
        }
        holder.textview_menu.setText(menuList.get(position).getTitle());
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return menuList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class MenuHolder {
        TextView textview_menu;
    }

}
