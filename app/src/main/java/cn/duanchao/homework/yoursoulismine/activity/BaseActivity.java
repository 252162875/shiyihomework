package cn.duanchao.homework.yoursoulismine.activity;

import android.app.Activity;
import android.os.Bundle;

import cn.duanchao.homework.yoursoulismine.http.HttpManager;
import cn.duanchao.homework.yoursoulismine.http.HttpResultSubscriber;
import cn.duanchao.homework.yoursoulismine.intefaces.retrofit.ThoughtWorksService;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public abstract class BaseActivity extends Activity {
    private HttpResultSubscriber httpResultSubscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
    }

    /**
     * 联网拿数据
     */
    public void getData() {
        this.httpResultSubscriber = creatSubscriber();
        HttpManager.getInstance()
                .createService(ThoughtWorksService.class)
                .getThoutWorksData()//请求地址
                .observeOn(AndroidSchedulers.mainThread())//result线程
                .subscribeOn(Schedulers.io())//网络线程
                .subscribe(httpResultSubscriber);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭时取消订阅
        httpResultSubscriber.unsubscribe();
    }

    /**
     * 创建回调，子类实现
     */
    public abstract HttpResultSubscriber creatSubscriber();

}