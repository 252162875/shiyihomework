package cn.duanchao.homework.yoursoulismine.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.duanchao.homework.yoursoulismine.R;
import cn.duanchao.homework.yoursoulismine.bean.ThoughtWorksBean;
import cn.duanchao.homework.yoursoulismine.utils.UiUtils;


public class TestAdapter extends RecyclerView.Adapter {
    private ArrayList<ThoughtWorksBean.RowsBean> rows;

    public TestAdapter(ArrayList<ThoughtWorksBean.RowsBean> rows) {
        this.rows = rows;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(UiUtils.getContext());
        View view = inflater.inflate(R.layout.view_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        /**
         * 需求中没说集合中全部参数为空要怎么处理，这里就没做处理，所以UI上有一个空Item，如果需药这个不存在的话那么处理此处的rows数据集合就OK了，这样的话getItemCount就会把空数据去掉
         */
        ThoughtWorksBean.RowsBean rowsBean = rows.get(position);
        String title1 = rowsBean.getTitle();
        ((MyViewHolder) holder).tvTitle.setText(title1);
        ((MyViewHolder) holder).tvDescription.setText(rows.get(position).getDescription());
        String imageHref = rows.get(position).getImageHref();
        if (imageHref == null) {
            //没图片地址时不显示imageview
            ((MyViewHolder) holder).myImageView.setVisibility(View.GONE);
        } else {
            ((MyViewHolder) holder).myImageView.setVisibility(View.VISIBLE);
            Uri uri = Uri.parse(imageHref);
            ImageRequest request = ImageRequestBuilder
                    .newBuilderWithSource(uri)
                    .setProgressiveRenderingEnabled(true)//设置jpeg渐进加载
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setTapToRetryEnabled(true)//设置失败重新加载
                    .setOldController(((MyViewHolder) holder).myImageView.getController())
                    .build();
            ((MyViewHolder) holder).myImageView.setController(controller);
        }
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rl_root)
        RelativeLayout rlRoot;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_description)
        TextView tvDescription;
        @BindView(R.id.my_image_view)
        SimpleDraweeView myImageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}