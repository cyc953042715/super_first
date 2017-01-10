package com.example.chaoren.myapplication.widgest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.example.chaoren.myapplication.R;

public class SwitchButton extends RelativeLayout {
    Button mLeft;
    Button mRight;
    RadioGroup mRadiogroup;
    boolean mFlag = true;
    public final static int CODE_LEFT_BUTTON = 0;
    public final static int CODE_RIGHT_BUTTON = 1;

    private SwitchButtonClickCallback mSwitchButtonClickCallback;

    public SwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.button_textview, this);
        mLeft = (Button) findViewById(R.id.button_left);
        mRight = (Button) findViewById(R.id.button_right);
        mRadiogroup = (RadioGroup) findViewById(R.id.radiogroup_switch);
        mLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                setButtonStyle(CODE_LEFT_BUTTON);
            }
        });
        mRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                setButtonStyle(CODE_RIGHT_BUTTON);
            }
        });
    }

    public void setButtonStyle(int buttonCode) {

        if (buttonCode == CODE_LEFT_BUTTON) {
            if (mFlag) {
                if (null != mSwitchButtonClickCallback)
                    mSwitchButtonClickCallback.onButtonClick(CODE_LEFT_BUTTON, mLeft);
                setLeftButtonStyle();
                mFlag = false;
            }
        } else if (buttonCode == CODE_RIGHT_BUTTON) {
            if (!mFlag) {
                if (null != mSwitchButtonClickCallback)
                    mSwitchButtonClickCallback.onButtonClick(CODE_RIGHT_BUTTON, mRight);
                setRightBottonStyle();
                mFlag = true;
            }
        }
    }

    private void setLeftButtonStyle() {
        mLeft.setTextColor(getResources().getColor(R.color.whole_color));
        mRight.setTextColor(getResources().getColor(R.color.white));
    }

    private void setRightBottonStyle() {
        mLeft.setTextColor(getResources().getColor(R.color.white));
        mRight.setTextColor(getResources().getColor(R.color.whole_color));
    }

    public void setWhichButtonSelected(int id) {
        setButtonStyle(id);
    }


    public void resetState() {
        setLeftButtonStyle();
        mFlag = false;
    }

    public SwitchButton(Context context) {
        super(context);
    }

    public void setSwitchButtonClickCallback(SwitchButtonClickCallback switchButtonClickCallback) {
        mSwitchButtonClickCallback = switchButtonClickCallback;
    }

    public void setText(String str1, String str2) {
        mLeft.setText(str1);
        mRight.setText(str2);
        mLeft.performClick();
    }

    public int getSelectButtonCode() {
        if (mFlag)
            return CODE_RIGHT_BUTTON;
        else return CODE_LEFT_BUTTON;
    }

    public interface SwitchButtonClickCallback {
        void onButtonClick(int whichButton, View button);
    }
}
