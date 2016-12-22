package cn.duanchao.homework.yoursoulismine.activity;

import android.app.ActionBar;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.duanchao.homework.yoursoulismine.R;
import cn.duanchao.homework.yoursoulismine.adapter.TestAdapter;
import cn.duanchao.homework.yoursoulismine.bean.HttpResult;
import cn.duanchao.homework.yoursoulismine.bean.ThoughtWorksBean;
import cn.duanchao.homework.yoursoulismine.http.HttpResultSubscriber;

public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private ArrayList<ThoughtWorksBean.RowsBean> rows = new ArrayList();
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.refreshlayout)
    SwipeRefreshLayout refreshlayout;
    private ActionBar actionBar;
    private TestAdapter testAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        actionBar = getActionBar();
        //手动调用,通知系统去测量,实现初次进入refreshlayout效果
        refreshlayout.measure(0, 0);
        refreshlayout.setRefreshing(true);
        initListener();
        initView();
    }

    /**
     * 结果处理
     * @return
     */
    @Override
    public HttpResultSubscriber creatSubscriber() {
        return new HttpResultSubscriber(MainActivity.this, false) {//第二个参数配置是否显示progressbar

            @Override
            public void onSuccess(HttpResult t) {
                ThoughtWorksBean thoughtworksbean = (ThoughtWorksBean) t;
                showTheResult(thoughtworksbean);
                rows.removeAll(rows);
                MainActivity.this.rows.addAll(thoughtworksbean.getRows());
                testAdapter.notifyDataSetChanged();
            }

            @Override
            public void requestFinish() {
                //无论结果如何关闭刷新控件显示
                if (refreshlayout.isRefreshing()) {
                    refreshlayout.setRefreshing(false);
                }
            }
        };
    }

    /**
     * 初始化View
     */
    public void initView() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        testAdapter = new TestAdapter(rows);
        recyclerview.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = 8;
                outRect.top = 8;
            }
        });
        recyclerview.setAdapter(testAdapter);
    }

    /**
     * 设置监听器(刷新监听和列表滑动监听)
     */
    public void initListener() {
        refreshlayout.setOnRefreshListener(this);
        //图片懒加载，可以写成自定义recyclerview继承recyclerview去做设置（省略了~~~）
        recyclerview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                //根据newState状态做处理
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE://滑动结束时恢复加载
                        Fresco.getImagePipeline().resume();
                        break;

                    case RecyclerView.SCROLL_STATE_DRAGGING://scroll时暂停加载
                        Fresco.getImagePipeline().pause();
                        break;

                    case RecyclerView.SCROLL_STATE_SETTLING://fling时暂停加载
                        Fresco.getImagePipeline().pause();
                        break;
                }
            }
        });
    }

    /**
     * 在UI上显示正确的数据
     */
    public void showTheResult(ThoughtWorksBean thoughtworksbean) {
        actionBar.setTitle(thoughtworksbean.getTitle());
    }

    /**
     * 调刷新的时候直接用父类方法请求初次进入时的数据
     */
    @Override
    public void onRefresh() {
        getData();
    }
}
