package prodigalwang.newbornassistant.login_signup.presenter.signup;

import java.util.HashMap;

import prodigalwang.newbornassistant.login_signup.model.login.LoginModelImp;
import prodigalwang.newbornassistant.login_signup.model.signup.ISignUpModel;
import prodigalwang.newbornassistant.login_signup.model.signup.SignUpCallback;
import prodigalwang.newbornassistant.login_signup.model.signup.SignUpModelImp;
import prodigalwang.newbornassistant.login_signup.view.signup.ISignUpView;
import prodigalwang.newbornassistant.utils.NetUtil;
import prodigalwang.newbornassistant.utils.StatusInfo;

/**
 * Created by ProdigalWang on 2016/12/28.
 */

public class SignUpPresenterImp implements ISignUpPresenter {

    private ISignUpModel iSignUpModel;
    private ISignUpView iSignUpView;

    public SignUpPresenterImp(ISignUpView iSignUpView){
        this.iSignUpView=iSignUpView;
        iSignUpModel =new SignUpModelImp();
    }
    @Override
    public void postSignOneInfo(String phone) {
        iSignUpView.showProgress("正在验证手机号...");

        if (NetUtil.isConnected()){
            HashMap<String,String> info=new HashMap<>();
            info.put("phone",phone);
            info.put("type","reg_one");

            iSignUpModel.signUpOneRequest(info, new SignUpCallback() {
                @Override
                public void signUpResponse(String response) {
                    iSignUpView.hideProgress();
                    iSignUpView.SignupOneInfo(response);
                }

                @Override
                public void signFail(String msg) {
                    iSignUpView.hideProgress();
                    iSignUpView.showFailMsg(msg);
                }
            });

        }else {
            iSignUpView.hideProgress();
            iSignUpView.showFailMsg(StatusInfo.NONETWORK);
        }
    }

    @Override
    public void postSignThreeInfo(String phone, String name, String pwd, String head) {
        iSignUpView.showProgress("正在注册,请稍后...");
        if (NetUtil.isConnected()){

            HashMap<String,String> info=new HashMap<>();
            info.put("type","reg_two");
            info.put("phone",phone);
            info.put("name",name);
            info.put("pwd",pwd);
            info.put("head",head);

            iSignUpModel.signUpThreeRequest(info, new SignUpCallback() {
                @Override
                public void signUpResponse(String response) {
                    iSignUpView.hideProgress();
                    iSignUpView.SignupThreeInfo(response);

                }

                @Override
                public void signFail(String msg) {
                    iSignUpView.hideProgress();
                    iSignUpView.showFailMsg(msg);
                }
            });


        }else {
            iSignUpView.hideProgress();
            iSignUpView.showFailMsg(StatusInfo.NONETWORK);
        }
    }
}
