package prodigalwang.newbornassistant.educational.presenter.et_login;

/**
 * Created by ProdigalWang on 2017/3/8.
 */

public interface IEtLoginPresenter {
    /**
     * 登录请求
     * @param stuId 学号
     * @param pwd 密码
     * @param regNum 验证码
     */
    void loginEt(String stuId,String pwd,String regNum);
}
