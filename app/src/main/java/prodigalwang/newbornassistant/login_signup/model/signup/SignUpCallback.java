package prodigalwang.newbornassistant.login_signup.model.signup;

/**
 * Created by ProdigalWang on 2016/12/23.
 */

public interface SignUpCallback {

    void signUpResponse(String response);

    void signFail(String msg);
}
