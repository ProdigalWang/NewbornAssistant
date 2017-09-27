package prodigalwang.newbornassistant.main_image.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.base.BaseAdapter;
import prodigalwang.newbornassistant.bean.SchoolImage;
import prodigalwang.newbornassistant.utils.Urls;

/**
 * Created by ProdigalWang on 2016/12/12
 */

public class ImageAdapter extends BaseAdapter<SchoolImage> {

    private List<Integer> mHeights = new ArrayList<>();

    public ImageAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.footer, parent, false);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            return new FooterViewHolder(view);
        }
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ImageViewHolder) {
            if (position == mDatas.size())
                return;

            // 随机高度, 模拟瀑布效果.
            if (mHeights.size() <= position) {
                mHeights.add((int) (150 + Math.random() * 300));
            }

            ViewGroup.LayoutParams lp = ((ImageViewHolder) holder).getImageHigh().getLayoutParams();
            lp.height = mHeights.get(position);

            ((ImageViewHolder) holder).getImageHigh().setLayoutParams(lp);


            ((ImageViewHolder) holder).textView.setText(mDatas.get(position).getTitle());

            Picasso.with(mContext).load(Urls.HOST_SCHOOL + mDatas.get(position).getImage())
                    .placeholder(R.drawable.ic_nopic)
                    .error(R.drawable.ic_nopic)
                    .into(((ImageViewHolder) holder).imageView);
        }
    }

    class ImageViewHolder extends BaseAdapter.BaseViewHolder {

        TextView textView;
        ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_image_title);
            imageView = (ImageView) itemView.findViewById(R.id.iv_image);
            itemView.setOnClickListener(this);
        }

        public ImageView getImageHigh() {
            return imageView;
        }

    }
}
