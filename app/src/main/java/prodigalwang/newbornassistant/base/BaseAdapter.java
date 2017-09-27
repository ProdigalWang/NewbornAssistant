package prodigalwang.newbornassistant.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import prodigalwang.newbornassistant.bean.Entity;
import prodigalwang.newbornassistant.bean.TipsPost;
import prodigalwang.newbornassistant.utils.LogUtil;

/**
 * Created by ProdigalWang on 2016/12/7
 * RecyclerView Adapter基类
 */

public abstract class BaseAdapter<T extends Entity>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "BaseAdapter";

    /**
     * item点击事件回调接口
     */
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        boolean onItemLongClick(View view, int position);
    }

    protected OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    protected List<T> mDatas = new ArrayList<>();

    protected Context mContext;

    public BaseAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {

        return headerView == null ? mDatas.size() + 1 : mDatas.size() + 2;

    }

    protected View headerView;

    public void setHeaderView(View view) {
        headerView = view;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return headerView;
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View view) {
            super(view);
        }

    }

    protected static final int TYPE_HEADER = 0;
    protected static final int TYPE_ITEM = 1;
    protected static final int TYPE_FOOTER = 2;
    protected static final int TYPE_ITEM_HASIMAGE = 3;

    private static final String itemHasImage = "tipsHasImage";

    @Override
    public int getItemViewType(int position) {
        if (headerView != null && position == 0)//在0位置并且头布局不为空
            return TYPE_HEADER;
        if ((headerView != null && position + 1 == getItemCount())
                || (headerView == null && position + 1 == getItemCount()))//最后位置包含两种情况
            return TYPE_FOOTER;

        //展示带有image的item,详情参考TipsPresenterImp中的checkData()方法。
        if (getItem(position).getContent_type() != null
                && getItem(position).getContent_type().equals(itemHasImage))
            return TYPE_ITEM_HASIMAGE;

        return TYPE_ITEM;

    }

    public void addAllData(List<T> data) {
        if (mDatas != null) {
            mDatas.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void clearData() {
        if (mDatas != null) {
            mDatas.clear();
        }
        notifyDataSetChanged();
    }

    public List<T> getAllData() {
        if (mDatas != null) {
            return mDatas;
        }
        return null;
    }

    /**
     * 获取点击位置的item数据
     *
     * @param position
     * @return
     */
    public T getItem(int position) {
        if (mDatas != null && mDatas.size() > 0) {

//            if (headerView == null) {
//                LogUtil.e("headerView is null");
//            }
            return headerView == null ? mDatas.get(position) : mDatas.get(position - 1);
        }
        return null;
    }

    /**
     * 获取点击的位置
     *
     * @param holder
     * @return
     */
    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return headerView == null ? position : position - 1;
    }


    public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public BaseViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickLitener != null) {
                mOnItemClickLitener.onItemClick(view, getLayoutPosition());
            }

        }

        @Override
        public boolean onLongClick(View v) {
            if (mOnItemClickLitener != null) {
                mOnItemClickLitener.onItemLongClick(v, getLayoutPosition());
                return true;
            }
            return false;
        }
    }
}
