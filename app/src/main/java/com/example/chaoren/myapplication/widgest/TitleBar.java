package com.example.chaoren.myapplication.widgest;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.chaoren.myapplication.R;
import com.example.chaoren.myapplication.util.AnimationLoaderUtils;
import com.example.chaoren.myapplication.util.DensityUtil;

/**
 * Created by chaoren on 2016/12/27.
 */

public class TitleBar extends LinearLayout implements View.OnClickListener, SwitchButton.SwitchButtonClickCallback {
    private SwitchButton mSwitchbutton;
    private TextView mTitleTextView;
    private ImageView mRightImgv;
    private TextView mRightTextView;
    private Toolbar toolbar;

    public final static int CODE_RIGHT_TEXT = 0X0;
    public final static int CODE_RIGHT_IMG = 0X1;
    public final static int CODE_LEFT_IMG = 0X2;
    public final static int CODE_LEFT_SWITCH = 0X3;
    public final static int CODE_RIGHT_SWITCH = 0X4;
    private OnTitleBarClickCallBack mOnTitleBarClickCallBack;

    public TitleBar(Activity context) {
        this(context, null);
    }

    public TitleBar(Activity context, AttributeSet attrs) {
        super(context, attrs);
        initTitleBar(context);
    }

    private void initTitleBar(Activity context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.title_bar, null);
        mSwitchbutton = (SwitchButton) findViewById(R.id.view_switchbutton);
        mTitleTextView = (TextView) findViewById(R.id.textview_title);
        mRightTextView = (TextView) findViewById(R.id.right_textview);
        mRightImgv = (ImageView) findViewById(R.id.iamge_right);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mSwitchbutton.setVisibility(GONE);

        mRightTextView.setOnClickListener(this);
        mSwitchbutton.setSwitchButtonClickCallback(this);
        ((AppCompatActivity)context).setSupportActionBar(toolbar);
        ((AppCompatActivity)context).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(this);
    }

    /**
     * 设置正常模式
     */
    public void setNomal() {
        setRightText("",0);
        mTitleTextView.setTextColor(this.getResources().getColor(R.color.colorPrimary));
        toolbar.setNavigationIcon(this.getResources().getDrawable(R.mipmap.back));
    }

    /**
     * 设置正常模式
     */
    public void setTypeSwith(String text1,String text2) {
        mTitleTextView.setVisibility(GONE);
        mSwitchbutton.setVisibility(VISIBLE);
        mSwitchbutton.setText(text1,text2);
    }

    private void setToolbar(AppCompatActivity context,boolean isHideLeft,String leftTitle) {
        context.getSupportActionBar().setDisplayHomeAsUpEnabled(isHideLeft);
        toolbar.setTitle(leftTitle);
    }

    /**
     * 隐藏返回键
     * @param context
     */
    private void setHideLeft(AppCompatActivity context) {
        context.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    private void setLeftImage(AppCompatActivity context) {
        toolbar.setNavigationIcon(this.getResources().getDrawable(R.mipmap.my_message));
        context.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * 设置titlebar的点击事件
     * @param mOnTitleBarClickCallBack
     */
    public void setOnTitleBarClickCallBack(OnTitleBarClickCallBack mOnTitleBarClickCallBack) {
        this.mOnTitleBarClickCallBack = mOnTitleBarClickCallBack;
    }

    public TextView getmTitleTextView() {
        return mTitleTextView;
    }

    /**
     * 设置title
     * @param mTitleTextView
     */
    public void setmTitleTextView(TextView mTitleTextView) {
        this.mTitleTextView = mTitleTextView;
    }

    public void setTitleText(String text) {
        mTitleTextView.setText(text);
    }

    /**
     * 设置右边文本
     * @param rightText 文本,只需要图片设置文本为""
     * @param resouceId 右边的图片,只需要文本设置为0
     */
    public void setRightText(String rightText, int resouceId) {
        if (!TextUtils.isEmpty(rightText)) {
            mRightTextView.setText(rightText);
            if (resouceId > 0) {
                Drawable drawable = getDrawableFromId(resouceId);
                mRightTextView.setCompoundDrawables(null, null, drawable, null);//画在右边
                mRightTextView.setCompoundDrawablePadding(DensityUtil.dip2px(getContext(), 5));
            }
        } else {
            if (resouceId > 0) {
                Drawable drawable = getDrawableFromId(resouceId);
                mRightTextView.setCompoundDrawables(null, null, drawable, null);//画在右边
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
     * 动画效果
     * @param view
     */
    private void startAnimationView(View view) {
        AnimationSet animationSet = (AnimationSet) AnimationLoaderUtils
                .loadAnimation(getContext(), R.anim.my_image_scale_in);
        view.startAnimation(animationSet);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.right_textview:
                startAnimationView(view);
                mOnTitleBarClickCallBack.onViewClick(CODE_RIGHT_TEXT,view);
                break;
            case R.id.toolbar:
                startAnimationView(view);
                mOnTitleBarClickCallBack.onViewClick(CODE_LEFT_IMG,view);
                break;
        }
    }

    @Override
    public void onButtonClick(int whichButton, View button) {
        if (mOnTitleBarClickCallBack == null) return;
        switch (whichButton) {
            case SwitchButton.CODE_LEFT_BUTTON:
                mOnTitleBarClickCallBack.onViewClick(CODE_LEFT_SWITCH, button);
                break;
            case SwitchButton.CODE_RIGHT_BUTTON:
                mOnTitleBarClickCallBack.onViewClick(CODE_RIGHT_SWITCH, button);
                break;
        }
    }

    /**
     * 标题栏所有点击的回调
     */
    public interface OnTitleBarClickCallBack {
        void onViewClick(int whichButton, View view);
    }
}
