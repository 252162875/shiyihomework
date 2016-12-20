package cn.duanchao.homework.yoursoulismine.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import cn.duanchao.homework.yoursoulismine.application.MyApplication;


/**
 * 提供ui操作的帮助类
 */
public class UiUtils {
    // 获取context对象
    public static Context getContext() {
        return MyApplication.getContext();
    }

    // 获取主线程的handler
    public static Handler getMainThreadHandler() {
        return MyApplication.getHandler();
    }

    // 获取主线程的线程id
    public static int getMainThreadId() {
        return MyApplication.getMainThreadId();
    }

    // 获取字符串
    public static String getString(int id) {
        return getContext().getResources().getString(id);
    }

    // 获取字符串数组
    public static String[] getStringArray(int id) {
        return getContext().getResources().getStringArray(id);
    }

    // 获取drawable
    public static Drawable getDrawable(int id) {
        return getContext().getResources().getDrawable(id);
    }

    // 获取color
    public static int getColor(int id) {
        return getContext().getResources().getColor(id);
    }

    // 获取color的状态选择器
    public static ColorStateList getColorStateList(int id) {
        return getContext().getResources().getColorStateList(id);
    }

    // dimen下对应的像素值
    public static int getDimen(int id) {
        return getContext().getResources().getDimensionPixelSize(id);
    }

    // dp-->px
    public static int dip2px(int dip) {
        float density = getContext().getResources().getDisplayMetrics().density;// 320dpi/160=2
        return (int) (dip * density + 0.5);// 加上0.5 四舍五入
    }

    // 判断是否在主线程中运行
    public static boolean isRunOnUiThread() {
        int currentThreadId = android.os.Process.myTid();// 获取当前线程所在的id
        return currentThreadId == getMainThreadId();
    }

    // 让r一定在主线程当中执行
    public static void runOnUiThread(Runnable r) {
        if (isRunOnUiThread()) {
            r.run();// r.start--启动新的线程
        } else {
            getMainThreadHandler().post(r);// 把r放到主线程的消息队列执行
        }
    }

    // 加载布局文件
    public static View inflateView(int id) {
        return View.inflate(getContext(), id, null);
    }

    public static void showToast(String str) {
        Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
    }

}
