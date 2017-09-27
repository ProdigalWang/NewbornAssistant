package prodigalwang.newbornassistant.educational.model.et_login;

import java.util.HashMap;

/**
 * Created by ProdigalWang on 2017/3/7.
 */

public interface IEtLoginModel {
    /**
     * 登录
     * @param etLoginCallback 回掉接口
     * @param params 请求参数集合
     */
    void loginEt(EtLoginCallback etLoginCallback, HashMap<String,String> params);
}
