package prodigalwang.newbornassistant.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;

import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.educational.view.et_login.EtLoginFragment;
import prodigalwang.newbornassistant.login_signup.view.login.LoginFragment;
import prodigalwang.newbornassistant.main_tips.view.WriteTipsFragment;
import prodigalwang.newbornassistant.me.view.MeFragment;
import prodigalwang.newbornassistant.setting.AboutFragment;
import prodigalwang.newbornassistant.setting.OpenSourceFragment;

/**
 * Created by ProdigalWang on 2016/12/29.
 * 普通Fragment的承载Activity
 */

public class BaseFragmentActivity extends BaseActivity implements BackHandledInterface{

    private Toolbar toolbar;
    private String fragment_tag;

    private static final String fragment_about="fragment_about";
    private static final String fragment_login="fragment_login";
    private static final String fragment_me="fragment_me";
    private static final String fragment_open_source="fragment_open_source";
    private static final String fragment_write_tips="fragment_write_tips";
    private static final String fragment_setting="fragment_setting";
    private static final String fragment_et_login="fragment_et_login";
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
        return R.layout.fragment_base;
    }

    @Override
    protected void initView() {
        fragment_tag=getIntent().getStringExtra(getString(R.string.fragment_tag));

        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        switch (fragment_tag){
            case fragment_about:
                replaceFragment(new AboutFragment(),fragment_about);
                break;
            case fragment_me:
                replaceFragment(new MeFragment(),fragment_me);
                break;
            case fragment_login:
                replaceFragment(new LoginFragment(),fragment_login);
                break;
            case fragment_open_source:
                replaceFragment(new OpenSourceFragment(),fragment_open_source);
                break;
            case fragment_write_tips:
                replaceFragment(new WriteTipsFragment(),fragment_write_tips);
                break;
            case fragment_et_login:
                replaceFragment(new EtLoginFragment(),fragment_et_login);
                break;
        }
    }

    protected void addFragment(Fragment fragment, String tag) {
        FragmentManager manager =getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fl_base, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }
    protected void replaceFragment(Fragment fragment, String tag){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fl_base,fragment, tag);
        transaction.commit();
    }

    /**
     * 处于栈顶也就是Activity正在显示的Fragment
     */
    private BackHandledFragment backHandledFragment;

    @Override
    public void setSelectedFragment(BackHandledFragment selectedFragment) {
        backHandledFragment=selectedFragment;
    }

    @Override
    public void onBackPressed() {
        //如果没有Fragment或者Fragment onBackPressed()返回false
        //则交给Activity处理返回点击事件
        if(backHandledFragment == null || !backHandledFragment.onBackPressed()){

            //如果当前Fragment返回栈中只有当前Fragment
            if(getSupportFragmentManager().getBackStackEntryCount() == 0){
                super.onBackPressed();
            }else{
                //弹出最上层的Fragment
                getSupportFragmentManager().popBackStack();
            }

        }
    }
}
