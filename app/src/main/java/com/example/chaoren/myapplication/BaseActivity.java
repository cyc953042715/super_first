package com.example.chaoren.myapplication;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.TextView;

import com.example.chaoren.myapplication.util.AnimationLoaderUtils;
import com.example.chaoren.myapplication.util.DensityUtil;
import com.example.chaoren.myapplication.widgest.TitleBar;

/**
 * Created by chaoren on 2016/12/27.
 */

public class BaseActivity extends AppCompatActivity implements TitleBar.OnTitleBarClickCallBack {
    LayoutInflater mInflater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInflater = LayoutInflater.from(this);
    }

    protected void setTitleText(TitleBar titleBar, String text) {
        titleBar.setOnTitleBarClickCallBack(this);
        titleBar.setNomal();
        titleBar.setTitleText(text);
    }

    /**
     * 设置中间title
     * @param textView
     * @param text
     */
    public void setMessageTitleText(TextView textView,String text) {
        textView.setVisibility(View.VISIBLE);
        textView.setText(text);
    }

    /**
     * 设置右边view
     * @param textView
     * @param rightText
     * @param resouceId
     */
    public void setRightText(TextView textView, String rightText, int resouceId) {
        textView.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(rightText)) {
            textView.setText(rightText);
            if (resouceId > 0) {
                Drawable drawable = getDrawableFromId(resouceId);
                textView.setCompoundDrawables(null, null, drawable, null);//画在右边
                textView.setCompoundDrawablePadding(DensityUtil.dip2px(this, 5));
            }
        } else {
            if (resouceId > 0) {
                Drawable drawable = getDrawableFromId(resouceId);
                textView.setCompoundDrawables(null, null, drawable, null);//画在右边
            }
        }
    }

    @NonNull
    private Drawable getDrawableFromId(int resouceId) {
        Drawable drawable = getResources().getDrawable(resouceId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
        return drawable;
    }

    /**
     * 点击动画
     * @param view
     */
    protected void startAnimationView(View view) {
        AnimationSet animationSet = (AnimationSet) AnimationLoaderUtils
                .loadAnimation(this, R.anim.my_image_scale_in);
        view.startAnimation(animationSet);
    }

    /**
     * 返回键关闭
     * @param whichButton
     * @param view
     */
    @Override
    public void onViewClick(int whichButton, View view) {
        if(whichButton == TitleBar.CODE_LEFT_IMG){
            this.finish();
        }
    }
}
