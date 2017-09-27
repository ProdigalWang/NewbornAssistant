package prodigalwang.newbornassistant.main.presenter;

import android.content.Context;

import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.app.AppConfig;
import prodigalwang.newbornassistant.bean.User;
import prodigalwang.newbornassistant.cache.CacheManager;
import prodigalwang.newbornassistant.login_signup.presenter.login.ILoginPresenter;
import prodigalwang.newbornassistant.login_signup.presenter.login.LoginPresenterImp;
import prodigalwang.newbornassistant.main.view.IMainView;
import prodigalwang.newbornassistant.utils.LogUtil;
import prodigalwang.newbornassistant.utils.ToastUtil;

/**
 * Created by ProdigalWang on 2016/11/26
 */

public class IMainPresenterImpl implements IMainPresenter {

    private IMainView mIMainView;
    private ILoginPresenter iLoginPresenter;

    public IMainPresenterImpl(IMainView IMainView) {
        this.mIMainView = IMainView;

    }

    @Override
    public void switchNavigation(int id) {
        switch (id) {
            case R.id.main_user_image:
                if (CacheManager.isExistFile(AppConfig.getInstance().DEFAULT_USER_DATA, "user.txt")) {
                    mIMainView.goMeFragment();
                } else {
                    mIMainView.goLoginFragment();
                }
                break;
            case R.id.navigation_item_news:
                mIMainView.switchMain();
                break;
            case R.id.navigation_item_book:
                mIMainView.switchBook();
                break;
            case R.id.navigation_item_educational:
                mIMainView.switchEducational();
                break;
            case R.id.navigation_item_me:
                mIMainView.switchMe();
                break;
            case R.id.navigation_item_set:
                mIMainView.goSettingFragment();
                break;
            case R.id.navigation_item_about:
                mIMainView.goAboutFragment();
                break;
            case R.id.navigation_item_quit:
                mIMainView.quit();
                break;
            default:
                mIMainView.switchMain();
                break;
        }
    }

    @Override
    public void initUserData() {

        if (iLoginPresenter==null){
            iLoginPresenter=new LoginPresenterImp();
        }
        if (iLoginPresenter.userHasLogin()){
            mIMainView.showUserHasLogin(iLoginPresenter.readUserData());
        }else {
            mIMainView.showUserNoLogin();
        }

    }

}
