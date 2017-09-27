package prodigalwang.newbornassistant.main_notice.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.List;

import prodigalwang.newbornassistant.base.BaseAdapter;
import prodigalwang.newbornassistant.base.BaseListFragment;
import prodigalwang.newbornassistant.bean.SchoolNotice;
import prodigalwang.newbornassistant.main_notice.presenter.INoticePresenter;
import prodigalwang.newbornassistant.main_notice.presenter.NoticePresenterImp;
import prodigalwang.newbornassistant.utils.LogUtil;

/**
 * Created by ProdigalWang on 2016/11/26
 */

public class NoticeFragment extends BaseListFragment<SchoolNotice> implements INoticeView{

    private INoticePresenter iNoticePresenter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iNoticePresenter=new NoticePresenterImp(this);

    }

    @Override
    protected BaseAdapter<SchoolNotice> getAdapter() {
        return new NoticeAdapter(getContext());
    }

    @Override
    protected void requestData() {

        iNoticePresenter.loadNotice(page);
    }

    @Override
    public void updateNotice(List data) {

        loadSuccess();
        if (data!=null){
            baseAdapter.addAllData(data);
        }

    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent=new Intent(getActivity(),NoticeDetailActivity.class);

        intent.putExtra("notice",baseAdapter.getItem(position));

        getActivity().startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(View view, int position) {
        return false;
    }

    @Override
    public void onClick(View v) {

    }
}
