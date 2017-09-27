package prodigalwang.newbornassistant.main.view;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.base.BaseActivity;
import prodigalwang.newbornassistant.base.BaseFragmentActivity;
import prodigalwang.newbornassistant.bean.User;
import prodigalwang.newbornassistant.book.view.BookFragment;
import prodigalwang.newbornassistant.educational.view.et_login.EtLoginFragment;
import prodigalwang.newbornassistant.main.presenter.IMainPresenter;
import prodigalwang.newbornassistant.main.presenter.IMainPresenterImpl;
import prodigalwang.newbornassistant.setting.SettingActivity;
import prodigalwang.newbornassistant.utils.ToastUtil;
import prodigalwang.newbornassistant.utils.Urls;

public class MainActivity extends BaseActivity implements IMainView,
        NavigationView.OnNavigationItemSelectedListener {

    private IMainPresenter mIMainPresenter;
    private ActionBarDrawerToggle mDrawerToggle;

    @BindView(R.id.main_drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.main_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.main_navigation_view)
    NavigationView mNavigationView;

    //@BindView(R.id.main_user_image)
    CircleImageView cv_head;
   // @BindView(R.id.main_user_name)
    TextView tv_user_name;

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
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

        setSupportActionBar(mToolbar);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mNavigationView.setNavigationItemSelectedListener(this);

        View head = mNavigationView.inflateHeaderView(R.layout.navigation_header);
        cv_head = (CircleImageView) head.findViewById(R.id.main_user_image);
        cv_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIMainPresenter.switchNavigation(v.getId());
            }
        });
        tv_user_name = (TextView) head.findViewById(R.id.main_user_name);


        mIMainPresenter = new IMainPresenterImpl(this);//presenter持有view
        switchMain();
    }


    @Override
    protected void onResume() {
        super.onResume();
        //初始化用户是否登录数据
        mIMainPresenter.initUserData();
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        mIMainPresenter.switchNavigation(item.getItemId());
        item.setChecked(true);
        mDrawerLayout.closeDrawers();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:
                ToastUtil.showShort(this, "搜索被点击");
                break;
            default:

        }
        return true;
    }


    @Override
    public void switchMain() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frame_content, new MainFragment())
                .commit();
        mToolbar.setTitle(R.string.navigation_news);
    }

    @Override
    public void switchBook() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frame_content, new BookFragment())
                .commit();
        mToolbar.setTitle(R.string.navigation_book);
    }

    /**
     * 去教务系统，判断是否缓存了数据
     */
    @Override
    public void switchEducational() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frame_content, new EtLoginFragment())
                .commit();
        mToolbar.setTitle(R.string.navigation_educational);
    }

    @Override
    public void switchMe() {
        goBaseActivity(getString(R.string.fragment_me));
    }

    @Override
    public void goLoginFragment() {
        goBaseActivity(getString(R.string.fragment_login));
    }

    @Override
    public void goMeFragment() {
        goBaseActivity(getString(R.string.fragment_me));
    }

    @Override
    public void goSettingFragment() {
        startActivity(new Intent(this, SettingActivity.class));
    }

    @Override
    public void goAboutFragment() {
        goBaseActivity(getString(R.string.fragment_about));
    }

    private void goBaseActivity(String tag) {
        Intent intent = new Intent(this, BaseFragmentActivity.class);
        intent.putExtra(getString(R.string.fragment_tag), tag);
        startActivity(intent);
    }

    @Override
    public void quit() {
        this.finish();
    }

    @Override
    public void showUserNoLogin() {
        tv_user_name.setText("点击头像登录");
        cv_head.setImageResource(R.drawable.ic_user_avatar_default);
    }

    @Override
    public void showUserHasLogin(User data) {
        tv_user_name.setText(data.getName());
        if (data.getUserInfo().getHead() != null) {
            Picasso.with(this)
                    .load(Urls.GET_USER_HEAD_IMAGE + data.getUserInfo().getHead())
                    .error(R.drawable.ic_user_avatar_default)
                    .placeholder(R.drawable.ic_user_avatar_default)
                    .into(cv_head);
        }
    }

    private long clickTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - clickTime) > 2000) {
                ToastUtil.showShort(this, "再按一次退出");
                clickTime = System.currentTimeMillis();
            } else {
                //AppManager.getAppManager().exitApp();
                this.finish();

            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
