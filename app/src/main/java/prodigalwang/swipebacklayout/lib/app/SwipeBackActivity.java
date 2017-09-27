
package prodigalwang.swipebacklayout.lib.app;

import android.os.Bundle;
import android.support.annotation.Nullable;

import android.view.View;

import prodigalwang.swipebacklayout.lib.SwipeBackLayout;
import prodigalwang.swipebacklayout.lib.Utils;
import prodigalwang.newbornassistant.base.BaseActivity;

/**
 * 修改自开源项目：https://github.com/ikew0ng/SwipeBackLayout
 */
public abstract class SwipeBackActivity extends BaseActivity implements SwipeBackActivityBase {
    private SwipeBackActivityHelper mHelper;

    private SwipeBackLayout mSwipeBackLayout;
    @Override
    protected void initSwipeBack() {
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();

        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeSize(getResources().getDisplayMetrics().widthPixels);
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        mHelper = new SwipeBackActivityHelper(this);
////        mHelper.onActivityCreate();
//    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }
}
