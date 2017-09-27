package prodigalwang.newbornassistant.login_signup.view.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.base.BaseFragment;
import prodigalwang.newbornassistant.login_signup.presenter.login.ILoginPresenter;
import prodigalwang.newbornassistant.login_signup.presenter.login.LoginPresenterImp;
import prodigalwang.newbornassistant.login_signup.view.signup.SignupOneFragment;
import prodigalwang.newbornassistant.login_signup.view.signup.SignupThreeFragment;
import prodigalwang.newbornassistant.utils.RegUtil;
import prodigalwang.newbornassistant.utils.ToastUtil;

/**
 * Created by ProdigalWang on 2016/12/21.
 */

public class LoginFragment extends BaseFragment implements ILoginView {


    @BindView(R.id.input_phone)
    EditText et_phone;
    @BindView(R.id.input_password)
    EditText et_pwd;

    private String input_phone, input_pwd;

    private ILoginPresenter iLoginPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iLoginPresenter = new LoginPresenterImp(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected String setToolBarTitle() {
        return getString(R.string.login);
    }

    @Override
    public void loginSucess() {

        getActivity().finish();
    }

    @OnClick(R.id.btn_login)
    protected void login() {
        if (!regInput()) {
            return;
        }

        iLoginPresenter.postUserInfo(input_phone, input_pwd);
    }

    //TODO  这里的逻辑是不是该抽取到P层
    private boolean regInput() {
        input_phone = et_phone.getText().toString();
        input_pwd = et_pwd.getText().toString();
        if (input_phone.isEmpty()) {
            et_phone.setError(getString(R.string.input_phone));
            return false;
        } else if (!RegUtil.isMobileNumber(input_phone)) {
            et_phone.setError(getString(R.string.input_right_phone));
            return false;
        }
        if (input_pwd.isEmpty()) {
            et_pwd.setError(getString(R.string.input_pwd));
            return false;
        }
        return true;
    }


    @OnClick(R.id.link_signup)
    protected void link_Sign() {
        replaceFragment(R.id.fl_base, new SignupOneFragment(), getString(R.string.fragment_signUp_one));
    }
}
