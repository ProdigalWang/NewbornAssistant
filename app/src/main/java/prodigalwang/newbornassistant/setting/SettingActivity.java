package prodigalwang.newbornassistant.setting;

import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.base.BaseActivity;
import prodigalwang.newbornassistant.setting.SettingFragment;

/**
 * 因为Preference自带滑动和NestedScrollView冲突，没有共用Activity
 */
public class SettingActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected boolean isSwipeBack() {
        return false;
    }

    @Override
    protected boolean hasLoadingLayout() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {

        toolbar.setTitle(getString(R.string.title_activity_setting));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    @Override
    protected void initData() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_setting, new SettingFragment()).commit();
    }

}
