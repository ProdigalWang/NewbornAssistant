package prodigalwang.newbornassistant.login_signup.model.signup;

import java.util.HashMap;

/**
 * Created by ProdigalWang on 2016/12/28.
 */

public interface ISignUpModel {
    /**
     * @param params 封装的请求数据
     * @param signUpCallback  回掉接口
     */
    void signUpOneRequest(HashMap<String, String> params, SignUpCallback signUpCallback);

    void signUpThreeRequest(HashMap<String, String> params, SignUpCallback signUpCallback);

}
