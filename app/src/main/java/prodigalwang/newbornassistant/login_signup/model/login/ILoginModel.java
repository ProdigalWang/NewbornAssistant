package prodigalwang.newbornassistant.login_signup.model.login;

import java.util.HashMap;

import prodigalwang.newbornassistant.login_signup.model.signup.SignUpCallback;

/**
 * Created by ProdigalWang on 2016/12/22.
 */

public interface ILoginModel {

    /**
     * 登录
     * @param params  登录信息
     * @param loginCallback 登录回掉接口
     */
    void postLoginRequest(HashMap<String,String> params, LoginCallback loginCallback);

    /**
     * 保存用户信息
     */
    void saveUserInfo();

}
