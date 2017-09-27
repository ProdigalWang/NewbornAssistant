package prodigalwang.newbornassistant.login_signup.presenter.login;

import java.util.HashMap;

import prodigalwang.newbornassistant.app.AppConfig;
import prodigalwang.newbornassistant.bean.User;
import prodigalwang.newbornassistant.cache.CacheManager;
import prodigalwang.newbornassistant.login_signup.model.login.ILoginModel;
import prodigalwang.newbornassistant.login_signup.model.login.LoginCallback;
import prodigalwang.newbornassistant.login_signup.model.login.LoginModelImp;
import prodigalwang.newbornassistant.login_signup.model.signup.SignUpCallback;
import prodigalwang.newbornassistant.login_signup.presenter.login.ILoginPresenter;
import prodigalwang.newbornassistant.login_signup.view.login.ILoginView;
import prodigalwang.newbornassistant.login_signup.view.signup.ISignUpView;
import prodigalwang.newbornassistant.utils.NetUtil;
import prodigalwang.newbornassistant.utils.StatusInfo;

/**
 * Created by ProdigalWang on 2016/12/22.
 */

public class LoginPresenterImp implements ILoginPresenter {

    private ILoginView iLoginView;
    private ILoginModel iLoginModel;

    public LoginPresenterImp(ILoginView iLoginView) {
        this.iLoginView = iLoginView;
        iLoginModel = new LoginModelImp();
    }

    public LoginPresenterImp() {
        iLoginModel = new LoginModelImp();
    }

    @Override
    public void postUserInfo(String phone, String pwd) {
        iLoginView.showProgress("正在登录,请稍后...");

        if (NetUtil.isConnected()) {
            HashMap<String, String> info = new HashMap<>();
            info.put("phone", phone);
            info.put("pwd", pwd);

            iLoginModel.postLoginRequest(info, new LoginCallback() {
                @Override
                public void loginSuccess(User user) {
                    iLoginView.hideProgress();
                    iLoginView.loginSucess();
                }

                @Override
                public void loginfail(String msg) {
                    iLoginView.hideProgress();
                    iLoginView.showFailMsg(msg);
                }
            });

        } else {
            iLoginView.hideProgress();
            iLoginView.showFailMsg(StatusInfo.NONETWORK);
        }

    }

    @Override
    public boolean userHasLogin() {
        if (CacheManager.isExistFile(AppConfig.getInstance().DEFAULT_USER_DATA, AppConfig.getInstance().userDataName)) {
            return true;
        }
        return false;
    }

    @Override
    public User readUserData() {

        User data = (User) CacheManager.readData(AppConfig.getInstance().DEFAULT_USER_DATA, AppConfig.getInstance().userDataName);
        return data;
    }

    @Override
    public void quitLogin() {

        CacheManager.deleteFile(AppConfig.getInstance().DEFAULT_USER_DATA, AppConfig.getInstance().userDataName);
    }


}
