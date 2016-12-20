package cn.duanchao.homework.yoursoulismine.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;

import java.lang.ref.WeakReference;

import cn.duanchao.homework.yoursoulismine.bean.HttpResult;
import cn.duanchao.homework.yoursoulismine.utils.UiUtils;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * 请求结果的处理
 *
 * @param <T>
 */
public abstract class HttpResultSubscriber<T> extends Subscriber<HttpResult<T>> {
    //    是否能取消请求
    private boolean cancel;
    //    弱引用防止内存泄露
    private WeakReference<Context> mActivity;
    //    加载框可自己定义
    private ProgressDialog pd;


    public HttpResultSubscriber(Context context) {
        this.mActivity = new WeakReference<>(context);
        this.cancel = false;
        initProgressDialog();
    }

    public HttpResultSubscriber(Context context, boolean cancel) {
        this.mActivity = new WeakReference<>(context);
        this.cancel = cancel;
        initProgressDialog();
    }


    /**
     * 初始化加载框
     */
    private void initProgressDialog() {
        Context context = mActivity.get();
        if (pd == null && context != null) {
            pd = new ProgressDialog(context);
            pd.setCancelable(cancel);
            if (cancel) {
                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        onCancelProgress();
                    }
                });
            }
        }
    }

    /**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }


    /**
     * 显示加载框
     */
    private void showProgressDialog() {
        Context context = mActivity.get();
        if (pd == null || context == null) return;
        if (!pd.isShowing()) {
            pd.show();
        }
    }


    /**
     * 隐藏
     */
    private void dismissProgressDialog() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }


    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    public void onStart() {
        showProgressDialog();
    }

    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onCompleted() {
        dismissProgressDialog();
    }

    @Override
    public void onError(Throwable e) {
        Log.e("HttpResultSubscriber", e.getMessage());
        e.printStackTrace();
        //在这里做全局的错误处理
        if (e instanceof HttpException) {

        }
        UiUtils.showToast(e.getMessage());
    }

    @Override
    public void onNext(HttpResult<T> t) {
        if (!TextUtils.isEmpty(t.title))
            onSuccess(t);
        else
            UiUtils.showToast("title is empty!");
    }

    public abstract void onSuccess(HttpResult<T> t);
}