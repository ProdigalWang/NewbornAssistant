package prodigalwang.newbornassistant.login_signup.view.signup;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.OnClick;
import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.base.BaseFragment;
import prodigalwang.newbornassistant.login_signup.presenter.signup.ISignUpPresenter;
import prodigalwang.newbornassistant.login_signup.presenter.signup.SignUpPresenterImp;
import prodigalwang.newbornassistant.login_signup.view.login.LoginFragment;
import prodigalwang.newbornassistant.utils.RegUtil;
import prodigalwang.newbornassistant.utils.StatusInfo;
import prodigalwang.newbornassistant.utils.ToastUtil;

/**
 * Created by ProdigalWang on 2016/12/21.
 */

public class SignupOneFragment extends BaseFragment implements ISignUpView {


    @BindView(R.id.input_phone)
    EditText et_phone;

    private String input_phone;
    private ISignUpPresenter iSignUpPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iSignUpPresenter = new SignUpPresenterImp(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_signup_one;
    }

    @Override
    protected String setToolBarTitle() {
        return getString(R.string.signUp);
    }


    @OnClick({R.id.btn_sign_next, R.id.link_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sign_next:
                nextClick();
                break;
            case R.id.link_login:
                replaceFragment(R.id.fl_base, new LoginFragment(), getString(R.string.fragment_login));

                break;
            default:
                break;
        }
    }

    private void nextClick() {
        if (!regInput())
            return;

        iSignUpPresenter.postSignOneInfo(input_phone);

    }


    private boolean regInput() {
        input_phone = et_phone.getText().toString();
        if (input_phone.isEmpty()) {
            et_phone.setError(getString(R.string.input_phone));
            return false;
        }
        if (!RegUtil.isMobileNumber(input_phone)) {
            et_phone.setError("请输入正确的手机号");
            return false;
        }
        et_phone.setError(null);
        return true;

    }

    @Override
    public void SignupOneInfo(String msg) {
        if (msg.equals(StatusInfo.USERNOTEXIST)) {

            SignupTwoFragment signupTwoFragment = SignupTwoFragment.newInstance(input_phone);
            replaceFragment(R.id.fl_base, signupTwoFragment, getString(R.string.fragment_signUp_two));

        } else if (msg.equals(StatusInfo.USERTEXIST)) {
            ToastUtil.showShort(getContext(), getString(R.string.phone_has_register));
        }
    }

    @Override
    public void SignupThreeInfo(String msg) {

    }
}
