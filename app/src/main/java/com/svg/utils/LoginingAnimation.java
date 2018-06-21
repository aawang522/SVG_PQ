package com.svg.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.svg.R;


/**
 * 登录、加载动画
 * Created by aawang
 */
public class LoginingAnimation {
    /**
     * 动画布局
     */
    private RelativeLayout linearLoading;
    /**
     * loading   ImageView
     */
    private ImageView animationImg;

    /**
     * 加载动画对话框
     */
    private Dialog dialog;


    private Animation operatingAnim;

    public LoginingAnimation(Context mContext) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.logininganimation, null);
        dialog = new Dialog(mContext, R.style.MyDialog);
        // 设置弹窗外围不可点击取消
        dialog.setCancelable(false);
        // 设置布局
        dialog.setContentView(view);

        // 加载界面
        linearLoading = (RelativeLayout) view
                .findViewById(R.id.loginingRelativeLayout);
        animationImg = (ImageView) view.findViewById(R.id.loginingImage);
//        textLoad = (TextView) view.findViewById(R.id.loginingtext);
        operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.logininganimation);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);

    }

    public void showLoading() {
        dialog.show();
        linearLoading.setVisibility(View.VISIBLE);
//        textLoad.setText(text);
        if (null != operatingAnim) {
            animationImg.startAnimation(operatingAnim);
        }

    }

    public boolean isShowed(){
        return dialog.isShowing();
    }

    /**
     * 隐藏加载对话框
     */
    public void dismissLoading() {
        if (dialog.isShowing()) {
            dialog.dismiss();
            linearLoading.setVisibility(View.GONE);

            // 为了防止动画下次出现时乱掉，所以最好清除一下动画
            animationImg.clearAnimation();
        }
    }
}
