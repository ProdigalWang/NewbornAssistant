package prodigalwang.newbornassistant.login_signup.model.login;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;

import okhttp3.Call;
import prodigalwang.newbornassistant.app.AppConfig;
import prodigalwang.newbornassistant.bean.User;
import prodigalwang.newbornassistant.cache.CacheManager;
import prodigalwang.newbornassistant.login_signup.model.signup.SignUpCallback;
import prodigalwang.newbornassistant.utils.StatusInfo;
import prodigalwang.newbornassistant.utils.ThreadPoolUtil;
import prodigalwang.newbornassistant.utils.Urls;

/**
 * Created by ProdigalWang on 2016/12/22.
 */

public class LoginModelImp implements ILoginModel {

    private User data;

    @Override
    public void postLoginRequest(HashMap<String, String> params, final LoginCallback loginCallback) {

        OkHttpUtils.post().url(Urls.LOGIN).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                loginCallback.loginfail(StatusInfo.CLIENT_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {

                if (response != null) {
                    if (response.equals(StatusInfo.USERNOTEXIST)) {
                        loginCallback.loginfail(StatusInfo.USERNOTEXIST);
                    } else if (response.equals(StatusInfo.SERVICE_ERROR)) {
                        loginCallback.loginfail(StatusInfo.SERVICE_ERROR);
                    } else if (response.equals(StatusInfo.PWDERROR)) {
                        loginCallback.loginfail(StatusInfo.PWDERROR);
                    } else {
                        try {
                            Gson gson = new Gson();
                            Type type = new TypeToken<User>() {
                            }.getType();

                            data = gson.fromJson(response, type);
                            loginCallback.loginSuccess(data);

                            ThreadPoolUtil.getThreadpool().execute(new saveData());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        });
    }

    @Override
    public void saveUserInfo() {

        CacheManager.saveData(data, AppConfig.getInstance().creatUserDataPath(), AppConfig.getInstance().userDataName);
    }


    private class saveData implements Runnable {

        @Override
        public void run() {

            saveUserInfo();
        }
    }
}
