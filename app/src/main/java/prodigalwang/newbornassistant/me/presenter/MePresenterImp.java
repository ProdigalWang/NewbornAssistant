package prodigalwang.newbornassistant.me.presenter;

import prodigalwang.newbornassistant.app.AppConfig;
import prodigalwang.newbornassistant.bean.User;
import prodigalwang.newbornassistant.cache.CacheManager;
import prodigalwang.newbornassistant.me.view.IMeView;

/**
 * Created by ProdigalWang on 2016/12/31.
 */

public class MePresenterImp implements IMePresenter {

    private IMeView iMeView;

    public MePresenterImp(IMeView iMeView) {
        this.iMeView = iMeView;

    }

    @Override
    public void initUserData() {
        if (CacheManager.isExistFile(AppConfig.getInstance().DEFAULT_USER_DATA, "user.txt")) {

            User data = (User) CacheManager.readData(AppConfig.getInstance().DEFAULT_USER_DATA, "user.txt");
            if (data != null) {
                iMeView.showUserHasLogin(data);
            }
        } else {
            iMeView.showUserNoLogin();
        }
    }
}
