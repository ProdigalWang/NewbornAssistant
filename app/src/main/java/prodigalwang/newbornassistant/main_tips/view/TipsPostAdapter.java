package prodigalwang.newbornassistant.main_tips.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.base.BaseAdapter;
import prodigalwang.newbornassistant.bean.TipsPost;
import prodigalwang.newbornassistant.utils.LogUtil;
import prodigalwang.newbornassistant.utils.Urls;

/**
 * Created by ProdigalWang on 2016/12/21.
 */

public class TipsPostAdapter extends BaseAdapter<TipsPost> {

    public TipsPostAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (headerView != null && viewType == TYPE_HEADER)
            return new TipsPosNoImageHolder(headerView);
        if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.footer, parent, false);
            return new FooterViewHolder(view);
        }
        if (viewType == TYPE_ITEM_HASIMAGE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_tips_post_hasimage, parent, false);
            return new TipsPostHasImageHolder(view);
        }

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tips_post_noimage, parent, false);
        return new TipsPosNoImageHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER)
            return;

        final int pos = getRealPosition(holder);
        String images;
        if (holder instanceof TipsPostHasImageHolder) {
            if (pos == mDatas.size())
                return;
            ((TipsPostHasImageHolder) holder).tv_nickname.setText(mDatas.get(pos).getName());
            ((TipsPostHasImageHolder) holder).tv_time.setText(mDatas.get(pos).getTime());
            ((TipsPostHasImageHolder) holder).tv_type.setText(mDatas.get(pos).getType());
            ((TipsPostHasImageHolder) holder).tv_title.setText(mDatas.get(pos).getTitle());
            ((TipsPostHasImageHolder) holder).tv_detail.setText(mDatas.get(pos).getDetail());

            Picasso.with(mContext).load(Urls.GET_USER_HEAD_IMAGE + mDatas.get(pos).getHead())
                    .placeholder(R.drawable.ic_nopic).error(R.drawable.ic_nopic)
                    .into(((TipsPostHasImageHolder) holder).circleImageView);

            images = mDatas.get(pos).getImages();
            if (images != null && !images.isEmpty()) {
                String[] image = images.split("@@");
                int size = image.length;
                switch (size) {
                    case 1:
                        Picasso.with(mContext).load(Urls.HOME_TIPS_POST_IMAGES + image[0])
                                .placeholder(R.drawable.ic_nopic).error(R.drawable.ic_nopic)
                                .into(((TipsPostHasImageHolder) holder).iv_one);
                        break;
                    case 2:
                        Picasso.with(mContext).load(Urls.HOME_TIPS_POST_IMAGES + image[0])
                                .placeholder(R.drawable.ic_nopic).error(R.drawable.ic_nopic)
                                .into(((TipsPostHasImageHolder) holder).iv_one);
                        Picasso.with(mContext).load(Urls.HOME_TIPS_POST_IMAGES + image[1])
                                .placeholder(R.drawable.ic_nopic).error(R.drawable.ic_nopic)
                                .into(((TipsPostHasImageHolder) holder).iv_two);
                        break;
                    case 3:
                        Picasso.with(mContext).load(Urls.HOME_TIPS_POST_IMAGES + image[0])
                                .placeholder(R.drawable.ic_nopic).error(R.drawable.ic_nopic)
                                .into(((TipsPostHasImageHolder) holder).iv_one);
                        Picasso.with(mContext).load(Urls.HOME_TIPS_POST_IMAGES + image[1])
                                .placeholder(R.drawable.ic_nopic).error(R.drawable.ic_nopic)
                                .into(((TipsPostHasImageHolder) holder).iv_two);
                        Picasso.with(mContext).load(Urls.HOME_TIPS_POST_IMAGES + image[2])
                                .placeholder(R.drawable.ic_nopic).error(R.drawable.ic_nopic)
                                .into(((TipsPostHasImageHolder) holder).iv_three);
                    break;

                }
            }
        }else if (holder instanceof TipsPosNoImageHolder){
            if (pos == mDatas.size())
                return;
            ((TipsPosNoImageHolder) holder).tv_nickname.setText(mDatas.get(pos).getName());
            ((TipsPosNoImageHolder) holder).tv_time.setText(mDatas.get(pos).getTime());
            ((TipsPosNoImageHolder) holder).tv_type.setText(mDatas.get(pos).getType());
            ((TipsPosNoImageHolder) holder).tv_title.setText(mDatas.get(pos).getTitle());
            ((TipsPosNoImageHolder) holder).tv_detail.setText(mDatas.get(pos).getDetail());

            Picasso.with(mContext).load(Urls.GET_USER_HEAD_IMAGE + mDatas.get(pos).getHead())
                    .placeholder(R.drawable.ic_nopic).error(R.drawable.ic_nopic)
                    .into(((TipsPosNoImageHolder) holder).circleImageView);
        }
    }

    private class TipsPostHasImageHolder extends BaseViewHolder {

        CircleImageView circleImageView;
        TextView tv_nickname;
        TextView tv_time;
        TextView tv_type;
        TextView tv_title;
        TextView tv_detail;
        ImageView iv_one;
        ImageView iv_two;
        ImageView iv_three;

        public TipsPostHasImageHolder(View itemView) {
            super(itemView);

            circleImageView = (CircleImageView) itemView.findViewById(R.id.item_tips_user_image);
            tv_nickname = (TextView) itemView.findViewById(R.id.item_tips_nickname);
            tv_time = (TextView) itemView.findViewById(R.id.item_tips_time);
            tv_type = (TextView) itemView.findViewById(R.id.item_tips_type);
            tv_title = (TextView) itemView.findViewById(R.id.item_tips_title);
            tv_detail = (TextView) itemView.findViewById(R.id.item_tips_detail);
            iv_one = (ImageView) itemView.findViewById(R.id.item_tips_ll_image_one);
            iv_two = (ImageView) itemView.findViewById(R.id.item_tips_ll_image_two);
            iv_three = (ImageView) itemView.findViewById(R.id.item_tips_ll_image_three);
        }
    }

    private class TipsPosNoImageHolder extends BaseViewHolder {

        CircleImageView circleImageView;
        TextView tv_nickname;
        TextView tv_time;
        TextView tv_type;
        TextView tv_title;
        TextView tv_detail;

        public TipsPosNoImageHolder(View itemView) {
            super(itemView);

            circleImageView = (CircleImageView) itemView.findViewById(R.id.item_tips_user_image);
            tv_nickname = (TextView) itemView.findViewById(R.id.item_tips_nickname);
            tv_time = (TextView) itemView.findViewById(R.id.item_tips_time);
            tv_type = (TextView) itemView.findViewById(R.id.item_tips_type);
            tv_title = (TextView) itemView.findViewById(R.id.item_tips_title);
            tv_detail = (TextView) itemView.findViewById(R.id.item_tips_detail);
        }

    }
}
