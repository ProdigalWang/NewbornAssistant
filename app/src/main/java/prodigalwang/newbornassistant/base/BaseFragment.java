package prodigalwang.newbornassistant.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.utils.StatusInfo;
import prodigalwang.newbornassistant.utils.ToastUtil;


/**
 * Created by ProdigalWang on 2016/12/22.
 * 所有Fragment的父类，共有部分:加载动画及设置加载信息，提示信息，设置ToolBar标题，onCreateView()方法
 * 子类fragment如果需要处理物理返回事件则继承BackHandledFragment,初始化数据的步骤在onViewCreated()中完成
 */

public abstract class BaseFragment extends Fragment implements IBaseView {

    //进度条
    protected MaterialDialog materialDialog;
    //ButterKnife
    private Unbinder unbinder;
    protected Context mContext;
    //toolbar文本
    protected String toolBarText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);

        mContext = getContext();

        toolBarText = setToolBarTitle();
        if (toolBarText != null)
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(toolBarText);

        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    /**
     * @return 获取布局资源id
     */
    protected abstract int getLayoutId();

    /**
     * @return toolbar文本
     */
    protected abstract String setToolBarTitle();

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        unbinder.unbind();
        mContext = null;
    }

    @Override
    public void showProgress(String progressText) {
        if (materialDialog == null) {
            materialDialog = new MaterialDialog.Builder(mContext)
                    .content(progressText)
                    .progress(true, 0)
                    .progressIndeterminateStyle(false)
                    .cancelable(false)
                    .show();
        } else {
            materialDialog.show();
        }
    }

    @Override
    public void hideProgress() {
        if (materialDialog != null) {
            materialDialog.dismiss();
        }
    }

    @Override
    public void showFailMsg(String msg) {
        switch (msg) {
            case StatusInfo.SERVICE_ERROR:
                ToastUtil.showShort(mContext, getString(R.string.service_error));
                break;
            case StatusInfo.CLIENT_ERROR:
                ToastUtil.showShort(mContext, getString(R.string.request_error));
                break;
            case StatusInfo.NONETWORK:
                ToastUtil.showShort(mContext, getString(R.string.NoNetworkText));
                break;
            case StatusInfo.USERNOTEXIST:
                ToastUtil.showShort(mContext, getString(R.string.user_notexist));
                break;
            case StatusInfo.USERTEXIST:
                ToastUtil.showShort(mContext, getString(R.string.user_exist));
                break;
            case StatusInfo.PWDERROR:
                ToastUtil.showShort(mContext, getString(R.string.pwd_error));
                break;
            default:
                ToastUtil.showShort(mContext, msg);
                break;
        }
    }

    /**
     * 替换fragment
     *
     * @param id       FrameLayout资源id
     * @param fragment 新的fragment对象
     * @param tag      新的fragment标签
     */
    protected void replaceFragment(int id, Fragment fragment, String tag) {

        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(id, fragment, tag);
        transaction.commit();
    }


//    protected void addFragment(int id, Fragment fragment, String tag) {
//        FragmentManager manager = getActivity().getSupportFragmentManager();
//        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//                .add(id, fragment, tag);
//        transaction.addToBackStack(tag);
//        transaction.commit();
//    }
//
//
//    protected void hideFragment(String tag) {
//        FragmentManager manager = getActivity().getSupportFragmentManager();
//        Fragment fragment = manager.findFragmentByTag(tag);
//        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.hide(fragment);
//        //transaction.addToBackStack("hide fragment3");
//        transaction.commit();
//    }
//
//    protected void showFragment(String tag) {
//        FragmentManager manager = getActivity().getSupportFragmentManager();
//        Fragment fragment = manager.findFragmentByTag(tag);
//        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.show(fragment);
//        //transaction.addToBackStack("show fragment3");
//        transaction.commit();
//    }
//
//    public void switchContent(Fragment from, Fragment to) {
//        FragmentTransaction transaction = getActivity()
//                .getSupportFragmentManager().beginTransaction();
//        if (!to.isAdded()) {    // 先判断是否被add过
//            transaction.hide(from).add(R.id.fl_base, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
//        } else {
//            transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
//        }
//    }


}
