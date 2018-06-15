package com.svg.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;

/**
 * 通用操作类
 * Created by aawang on 2017/3/29.
 */

public class CommUtil {
    private static Toast mToast;
    /**
     * 设置状态栏的颜色
     * @param activity
     * @param colorResId
     */
    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(colorResId));

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context)
    {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context)
    {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 判断网络是否连接  by aawang
     *
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (null != context) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
            Toast.makeText(context, SysCode.GETINNET, Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     *            （DisplayMetrics类中属性density）
     * @return
     */
    public static int px2dip(Context mContext, float pxValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     *
     * @Title: dip2px @Description: TODO(dp转px) @param @param
     *         context @param @param dpValue @param @return 设定文件 @return int
     *         返回类型 @throws
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 显示toast
     * @param content
     *            显示的内容
     */

    public static void showToast(Context mContext, String content) {
//        if (null != mContext) {
//            Toast.makeText(mContext, content, Toast.LENGTH_LONG).show();
//        }
        if(null == mToast) {
            mToast = Toast.makeText(mContext, content, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(content);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    /**
     * 非空判断
     * @param str
     * @return
     */
    public static boolean isNotBlank(String str){
        if (null != str && !"".equals(str)){
            return true;
        }
        return false;
    }

    /**
     * 空判断
     * @param str
     * @return
     */
    public static boolean isBlank(String str){
        if (null == str || "".equals(str)){
            return true;
        }
        return false;
    }

    /**
     * 非空判断
     * @param str
     * @return
     */
    public static String setNull(String str){
        if (null != str && !"".equals(str)){
            return str;
        }
        return "----";
    }

    /**
     * 获取时间分;秒 mm:ss
     *
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String formatLongToTimeStr(Long ms) {
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        String time = formatter.format(ms);
        return time;
    }



    /**
     * 隐藏键盘 by aawang
     *
     * @param activity
     * @return
     */
    public static void hideSystemKeyBoard(EditText editText, Activity activity) {
        if (activity.getCurrentFocus() != null
                && activity.getCurrentFocus().getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            boolean result = imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context, String packageName) {
        int versionCode = 0;
        try {
            versionCode = context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionCode;
    }

    /**
     * 添加一个设置ip界面后，手动拼接ip地址；如果不是手动填写ip，则直接拼接地址头和接口
     * @param isTogetherIP true表示手填，false表示使用写死的url
     * @param ip ip地址
     * @param port 端口
     * @param interfaces 接口
     * @param urlHead 如果不手填，则直接用写死的url
     * @return
     */
    public static String togetherIPFromUser(boolean isTogetherIP, String urlHead, String ip, String port, String interfaces){
        StringBuilder stringBuilder = new StringBuilder();
        if (isTogetherIP) {
            stringBuilder.append(SysCode.HTTP);
            stringBuilder.append(ip);
            stringBuilder.append(":");
            stringBuilder.append(port);
            stringBuilder.append(SysCode.WEBSERVICENAME);
            stringBuilder.append(interfaces);
        } else {
            stringBuilder.append(urlHead);
            stringBuilder.append(interfaces);
        }
        return stringBuilder.toString();
    }

    /**
     * sd卡中创建一个目标文件
     *
     * @param name
     * @return
     */
    public static String createSDCardDir(String name) {
        File sdcardDir = Environment.getExternalStorageDirectory();
        String path = sdcardDir.getPath() + "/ZNPD_UPDATE";
        File file = null;
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState())) {

                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                file = new File(dir + File.separator + name);
                if (file.exists()) {
                    file.delete();
                }
                file.createNewFile();
            }
        } catch (Exception e) {
        }
        if (null != file) {
            return file.getPath();
        } else {
            return "";
        }
    }

    public static void installApk(String urlPath, Context context) {
        Intent apkIntent = new Intent();
        apkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        apkIntent.setAction(Intent.ACTION_VIEW);
        File apkFile = new File(urlPath);
        Log.i("gjq", "apk length " + apkFile.length() + "");
        Uri uri = Uri.fromFile(apkFile);
        apkIntent
                .setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(apkIntent);
    }

    /**
     * fragment切换时隐藏输入法
     * @param context
     */
    public static void hideIputKeyboard(final Context context) {
        final Activity activity = (Activity) context;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                InputMethodManager mInputKeyBoard = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (null != activity && null != activity.getCurrentFocus()) {
                    mInputKeyBoard.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                }
            }
        });
    }
}
