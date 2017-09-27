package prodigalwang.newbornassistant.educational.presenter.et_login;

import java.util.HashMap;

import prodigalwang.newbornassistant.educational.model.et_login.IEtLoginModel;
import prodigalwang.newbornassistant.educational.model.et_login.EtLoginCallback;
import prodigalwang.newbornassistant.educational.model.et_login.EtLoginModelImp;
import prodigalwang.newbornassistant.educational.view.et_login.IEtLoginView;
import prodigalwang.newbornassistant.utils.NetUtil;

/**
 * Created by ProdigalWang on 2017/3/8.
 */

public class EtLoginPresenterImp implements IEtLoginPresenter {
    private IEtLoginModel iLoginEtModel;
    private IEtLoginView iEtLoginView;
    private HashMap<String, String> params;

    private static final String FORM_DATA_STUID = "WebUserNO";
    private static final String FORM_DATA_PWD = "Password";
    private static final String FORM_DATA_Agnomen = "Agnomen";
    private static final String FORM_DATA_submit_x = "submit.x";
    private static final String FORM_DATA_submit_y = "submit.y";

    public EtLoginPresenterImp(IEtLoginView iEtLoginView) {
        iLoginEtModel = new EtLoginModelImp();
        this.iEtLoginView = iEtLoginView;

    }

    /**
     * 登录，拼装请求头
     * @param stuId 学号
     * @param pwd 密码
     * @param regNum 验证码
     */
    @Override
    public void loginEt(String stuId, String pwd, String regNum) {
        params = new HashMap<>();
        params.put(FORM_DATA_STUID, stuId);
        params.put(FORM_DATA_PWD, pwd);
        params.put(FORM_DATA_Agnomen, regNum);
        if (NetUtil.isConnected()) {

            iEtLoginView.showProgress("正在登录,请稍候...");
            iLoginEtModel.loginEt(new EtLoginCallback() {
                @Override
                public void success() {
                    iEtLoginView.hideProgress();
                    iEtLoginView.loginSuccess();
                }

                @Override
                public void fail(String msg) {
                    iEtLoginView.hideProgress();
                    iEtLoginView.showFailMsg(msg);
                }
            }, params);
        } else {

            iEtLoginView.showFailMsg("当前无网络!");
        }
    }
}
