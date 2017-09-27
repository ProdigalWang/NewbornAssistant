package prodigalwang.newbornassistant.login_signup.presenter.login;

import prodigalwang.newbornassistant.bean.User;

/**
 * Created by ProdigalWang on 2016/12/22.
 */

public interface ILoginPresenter {
    /**
     * 将用户输入的信息发送至服务端
     * @param phone 手机号
     * @param pwd 密码
     */
    void postUserInfo(String phone,String pwd);

    /**
     * 用户是否已经登录
     * @return 已登录 true
     */
    boolean userHasLogin();

    /**
     * 读取用户的数据
     * @return
     */
    User readUserData();

    /**
     * 注销登录
     */
    void quitLogin();
}
