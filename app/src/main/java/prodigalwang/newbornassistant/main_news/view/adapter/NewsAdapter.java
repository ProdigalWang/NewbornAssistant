package prodigalwang.newbornassistant.main_news.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.base.BaseAdapter;
import prodigalwang.newbornassistant.bean.SchoolNews;
import prodigalwang.newbornassistant.utils.Urls;

/**
 * Created by ProdigalWang on 2016/12/8
 */

public class NewsAdapter extends BaseAdapter <SchoolNews>{

    public NewsAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (headerView != null && viewType == TYPE_HEADER)
            return new NewsViewHolder(headerView);
        if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.footer, parent,false);
            return new FooterViewHolder(view);
        }
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == TYPE_HEADER)
            return;

        final int pos=getRealPosition(holder);

        if (holder instanceof NewsViewHolder) {

            if (pos==mDatas.size()){
                return;
            }
            ((NewsViewHolder) holder).tv_title.setText(mDatas.get(pos).getTitle());
            ((NewsViewHolder) holder).tv_resume.setText(mDatas.get(pos).getResume());
            ((NewsViewHolder) holder).tv_time.setText(mDatas.get(pos).getTime());
            Picasso.with(mContext).load(Urls.HOST_SCHOOL+mDatas.get(pos).getImage())
                    .placeholder(R.drawable.ic_nopic)
                    .error(R.drawable.ic_nopic)
                    .into(((NewsViewHolder) holder).iv_img);

        }
    }


    class NewsViewHolder extends BaseViewHolder {

        TextView tv_title;
        TextView tv_resume;
        TextView tv_time;
        ImageView iv_img;

        public NewsViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.item_news_tv_title);
            tv_resume=(TextView) itemView.findViewById(R.id.item_news_tv_resume);
            tv_time=(TextView) itemView.findViewById(R.id.item_news_tv_time);
            iv_img= (ImageView) itemView.findViewById(R.id.item_news_iv);

        }

    }
}
