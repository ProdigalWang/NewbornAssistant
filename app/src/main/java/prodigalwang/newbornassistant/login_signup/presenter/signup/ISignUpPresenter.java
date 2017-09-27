package prodigalwang.newbornassistant.login_signup.presenter.signup;

/**
 * Created by ProdigalWang on 2016/12/28.
 */

public interface ISignUpPresenter {
    /**
     * 传递第一步注册用户填写的数据
     * @param phone 手机号
     */
    void postSignOneInfo(String phone);

    /**
     * 传递第三步注册用户填写的的信息
     * @param phone 手机号
     * @param name 昵称
     * @param pwd 密码
     * @param head 头像地址
     */
    void postSignThreeInfo(String phone,String name,String pwd,String head);
}
