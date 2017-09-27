package prodigalwang.newbornassistant.login_signup.model.login;

import prodigalwang.newbornassistant.bean.User;

/**
 * Created by ProdigalWang on 2016/12/22.
 */

public interface LoginCallback {

    void loginSuccess(User user);
    void loginfail(String msg);
}

