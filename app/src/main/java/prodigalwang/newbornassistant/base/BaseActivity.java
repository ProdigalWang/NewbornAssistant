package prodigalwang.newbornassistant.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.weavey.loading.lib.LoadingLayout;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.utils.LogUtil;
import prodigalwang.newbornassistant.utils.StatusInfo;
import prodigalwang.newbornassistant.utils.ToastUtil;

/**
 * Created by ProdigalWang on 2016/12/7
 * 共用的部分：子类是不是支持滑动返回(如果子类继承SwipeBackActivity则返回ture)
 * 子类布局是不是包含LoadingLayout，主要用于有网络请求，界面单一的Activity，因为这个父类实现了IBaseView，
 * 可以共用 展示错误信息，加载进度 这部分代码
 */

public abstract class BaseActivity extends AppCompatActivity implements IBaseView {

    protected LoadingLayout loadingLayout;
    protected MaterialDialog materialDialog;

    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        //绑定ButterKnife
        unbinder = ButterKnife.bind(this);

        if (isSwipeBack()) {
            initSwipeBack();
        }
        if (hasLoadingLayout()) {
            loadingLayout = (LoadingLayout) findViewById(R.id.loading);
            loadingLayout.setStatus(LoadingLayout.Loading);
        }
        initView();
        initData();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //  AppManager.getAppManager().removeActivity(this);

        unbinder.unbind();
    }

    /**
     * 是否继承SwipeBackActivity
     *
     * @return
     */
    protected abstract boolean isSwipeBack();

    /**
     * 是否有LoadingLayout
     *
     * @return
     */
    protected abstract boolean hasLoadingLayout();

    /**
     * 获取布局资源id  子类实现
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 由SwipeBackActivity实现
     */
    protected void initSwipeBack() {

    }

    /**
     * 初始化布局，子类实现
     */
    protected void initView() {

    }

    /**
     * 初始化数据，子类实现
     */
    protected void initData() {

    }


    @Override
    public void showProgress(String progressText) {
        if (hasLoadingLayout()) {
            loadingLayout.setStatus(LoadingLayout.Loading);
        } else {
            if (materialDialog == null) {
                materialDialog = new MaterialDialog.Builder(this)
                        .content(progressText)
                        .progress(true, 0)
                        .progressIndeterminateStyle(false)
                        .cancelable(false)
                        .show();
            } else {
                materialDialog.show();
            }
        }

    }

    @Override
    public void hideProgress() {
        if (hasLoadingLayout()) {
            loadingLayout.setStatus(LoadingLayout.Success);
        } else {
            if (materialDialog != null) {
                materialDialog.dismiss();
            }
        }

    }

    @Override
    public void showFailMsg(String msg) {
        switch (msg) {
            case StatusInfo.ALLDATA://全部数据
                ToastUtil.showShort(this, getString(R.string.alldata));
                break;
            case StatusInfo.NODATA://无数据
                loadingLayout.setStatus(LoadingLayout.Empty);
                break;
            case StatusInfo.SERVICE_ERROR://服务器出错
                loadingLayout.setStatus(LoadingLayout.Error);
                break;
            case StatusInfo.CLIENT_ERROR://客户端出错
                loadingLayout.setStatus(LoadingLayout.Error);
                break;
            case StatusInfo.NONETWORK://无网络连接
                loadingLayout.setStatus(LoadingLayout.No_Network);
                break;
            case StatusInfo.HAS_CACHE_NONETWORK://无网络有缓存
                ToastUtil.showShort(this, getString(R.string.NoNetworkText));
                break;
            default:
                ToastUtil.showShort(this, msg);
                break;
//                loadingLayout.setStatus(LoadingLayout.Error);
        }

    }
}
