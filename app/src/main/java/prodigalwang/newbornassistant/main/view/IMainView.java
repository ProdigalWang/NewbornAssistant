package prodigalwang.newbornassistant.main.view;

import prodigalwang.newbornassistant.bean.User;

/**
 * Created by ProdigalWang on 2016/11/26
 */

public interface IMainView {

    void switchMain();//侧滑菜单选项
    void switchBook();
    void switchEducational();
    void switchMe();

    /**
     * 去登录
     */
    void goLoginFragment();

    /**
     * 已登录，去个人中心
     */
    void goMeFragment();

    void goSettingFragment();

    void goAboutFragment();

    void quit();

    /**
     * 展示用户未登录的布局
     */
    void showUserNoLogin();

    /**
     * 展示用户已登录的数据
     * @param data
     */
    void showUserHasLogin(User data);
}
