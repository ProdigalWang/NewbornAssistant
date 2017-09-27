package prodigalwang.newbornassistant.me.view;

import prodigalwang.newbornassistant.base.IBaseView;
import prodigalwang.newbornassistant.bean.User;

/**
 * Created by ProdigalWang on 2016/12/30.
 */

public interface IMeView extends IBaseView{

    void showUserHasLogin(User user);

    void showUserNoLogin();

}
