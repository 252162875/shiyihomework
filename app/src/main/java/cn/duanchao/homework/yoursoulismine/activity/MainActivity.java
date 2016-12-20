package cn.duanchao.homework.yoursoulismine.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.duanchao.homework.yoursoulismine.R;
import cn.duanchao.homework.yoursoulismine.adapter.TestAdapter;
import cn.duanchao.homework.yoursoulismine.bean.HttpResult;
import cn.duanchao.homework.yoursoulismine.bean.ThoughtWorksBean;
import cn.duanchao.homework.yoursoulismine.http.HttpResultSubscriber;

public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.refreshlayout)
    SwipeRefreshLayout refreshlayout;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initListener();
        initView();
        actionBar = getActionBar();
    }


    @Override
    public HttpResultSubscriber creatSubscriber() {
        return new HttpResultSubscriber<ThoughtWorksBean>(MainActivity.this, true) {

            @Override
            public void onSuccess(HttpResult<ThoughtWorksBean> t) {
                refreshlayout.setRefreshing(false);
                ThoughtWorksBean thoughtworksbean = (ThoughtWorksBean) t;
                showTheResult(thoughtworksbean);
            }
        };
    }

    //    @Override
    public void initView() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerview.setAdapter(new TestAdapter());
    }

    //    @Override
    public void initListener() {
        refreshlayout.setOnRefreshListener(this);
    }

    /**
     * 在UI上显示正确的数据
     */
    public void showTheResult(ThoughtWorksBean thoughtworksbean) {
        actionBar.setTitle(thoughtworksbean.getTitle());
    }

    @Override
    public void onRefresh() {
        getData();
    }
}
