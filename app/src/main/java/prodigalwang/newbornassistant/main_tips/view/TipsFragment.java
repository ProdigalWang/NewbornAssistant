package prodigalwang.newbornassistant.main_tips.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.List;

import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.base.BaseAdapter;
import prodigalwang.newbornassistant.base.BaseFragmentActivity;
import prodigalwang.newbornassistant.base.BaseListFragment;
import prodigalwang.newbornassistant.bean.TipsPost;
import prodigalwang.newbornassistant.location.view.LocationActivity;
import prodigalwang.newbornassistant.login_signup.presenter.login.ILoginPresenter;
import prodigalwang.newbornassistant.login_signup.presenter.login.LoginPresenterImp;
import prodigalwang.newbornassistant.main_tips.presenter.ITipsPresenter;
import prodigalwang.newbornassistant.main_tips.presenter.TipsPresenterImp;
import prodigalwang.newbornassistant.utils.LogUtil;
import prodigalwang.newbornassistant.utils.StatusInfo;
import prodigalwang.newbornassistant.utils.ToastUtil;


/**
 * Created by ProdigalWang on 2016/11/26
 */

public class TipsFragment extends BaseListFragment<TipsPost> implements ITipsView{

    private ITipsPresenter iTipsPresenter;
    private ILoginPresenter iLoginPresenter;
    private View tipsHeaderView;
    private LinearLayout item_tips_head_route, item_tips_head_newlook,
            item_tips_head_procedure, item_tips_head_hotissues;

    @Override
    protected int getLayout() {
        return R.layout.fragment_home_tips;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hasFloatingActionButton = true;

        iTipsPresenter = new TipsPresenterImp(this);
        iLoginPresenter = new LoginPresenterImp();//调用有关登录的操作

    }

    @Override
    protected void initFloatAction(View view) {

        FloatingActionButton a = (FloatingActionButton) view.findViewById(R.id.action_write_tips);

        a.setOnClickListener(this);
    }

    @Override
    protected BaseAdapter<TipsPost> getAdapter() {
        return new TipsPostAdapter(getContext());
    }

    @Override
    protected void requestData() {

        iTipsPresenter.loadPost(page);
    }

    @Override
    public void updateTipsPost(List<TipsPost> data) {

        loadSuccess();

        if (data != null) {
            baseAdapter.addAllData(data);
        }
    }

    @Override
    protected void initHeaderView() {
        tipsHeaderView = LayoutInflater.from(getContext())
                .inflate(R.layout.item_tips_header, mRecyclerView, false);
        item_tips_head_route = (LinearLayout) tipsHeaderView.findViewById(R.id.item_tips_head_route);
        item_tips_head_newlook = (LinearLayout) tipsHeaderView.findViewById(R.id.item_tips_head_newlook);
        item_tips_head_procedure = (LinearLayout) tipsHeaderView.findViewById(R.id.item_tips_head_procedure);
        item_tips_head_hotissues = (LinearLayout) tipsHeaderView.findViewById(R.id.item_tips_head_hotissues);

        item_tips_head_hotissues.setOnClickListener(this);
        item_tips_head_newlook.setOnClickListener(this);
        item_tips_head_procedure.setOnClickListener(this);
        item_tips_head_route.setOnClickListener(this);

        baseAdapter.setHeaderView(tipsHeaderView);
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public boolean onItemLongClick(View view, int position) {
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_tips_head_route:

                getActivity().startActivity(new Intent(getActivity(), LocationActivity.class));
                break;
            case R.id.item_tips_head_newlook:
                ToastUtil.showShort(getContext(), "正在开发中...");
                break;
            case R.id.item_tips_head_procedure:
                ToastUtil.showShort(getContext(), "正在开发中...");
                break;
            case R.id.item_tips_head_hotissues:
                ToastUtil.showShort(getContext(), "正在开发中...");
                break;
            case R.id.action_write_tips:
                if (iLoginPresenter.userHasLogin()) {

                    //启动编辑界面,发布成功返回根据结果刷新列表
                    Intent intent = new Intent(getActivity(), BaseFragmentActivity.class);
                    intent.putExtra(getString(R.string.fragment_tag), getString(R.string.fragment_write_tips));
                    //注意在Fargment中要用this.启动才能获取到另一个Activity的返回值,不能使用getActivity
                    this.startActivityForResult(intent, 1);
                } else {
                    confirmLogin();
                }

                break;
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {

                    //发布成功返回当前fragment时刷新列表
                    if (data.getStringExtra(StatusInfo.CODE).equals(StatusInfo.SUCCESS))

                        onRefresh();
                }
                break;
            default:
                break;
        }
    }
}
