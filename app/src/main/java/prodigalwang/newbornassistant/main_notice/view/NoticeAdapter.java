package prodigalwang.newbornassistant.main_notice.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.base.BaseAdapter;
import prodigalwang.newbornassistant.bean.SchoolNotice;

/**
 * Created by ProdigalWang on 2016/12/14
 */

public class NoticeAdapter extends BaseAdapter<SchoolNotice> {

    public NoticeAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.footer, parent, false);
            return new FooterViewHolder(view);
        }
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notice, parent, false);
        return new NoticeAdapter.NoticeHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NoticeHolder) {
            if (position == mDatas.size())
                return;
            ((NoticeHolder) holder).tv_title.setText(mDatas.get(position).getTitle());
            ((NoticeHolder) holder).tv_unit.setText(mDatas.get(position).getUnit());
            ((NoticeHolder) holder).tv_time.setText(mDatas.get(position).getTime());

        }


    }

    class NoticeHolder extends BaseAdapter.BaseViewHolder {

        TextView tv_title;
        TextView tv_unit;
        TextView tv_time;

        public NoticeHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.item_notice_tv_title);
            tv_unit = (TextView) itemView.findViewById(R.id.item_notice_tv_unit);
            tv_time = (TextView) itemView.findViewById(R.id.item_notice_tv_time);

        }
    }

}
