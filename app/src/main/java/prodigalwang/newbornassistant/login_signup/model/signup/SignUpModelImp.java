package prodigalwang.newbornassistant.login_signup.model.signup;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.HashMap;

import okhttp3.Call;
import prodigalwang.newbornassistant.app.AppConfig;
import prodigalwang.newbornassistant.bean.User;
import prodigalwang.newbornassistant.bean.UserInfo;
import prodigalwang.newbornassistant.cache.CacheManager;
import prodigalwang.newbornassistant.utils.ImageUtils;
import prodigalwang.newbornassistant.utils.LogUtil;
import prodigalwang.newbornassistant.utils.StatusInfo;
import prodigalwang.newbornassistant.utils.ThreadPoolUtil;
import prodigalwang.newbornassistant.utils.Urls;

/**
 * Created by ProdigalWang on 2016/12/28.
 */

public class SignUpModelImp implements ISignUpModel {

    private User user;
    private UserInfo userInfo;

    @Override
    public void signUpOneRequest(HashMap<String, String> params, final SignUpCallback signUpCallback) {

        OkHttpUtils.post().url(Urls.SIGNUP).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                signUpCallback.signFail(StatusInfo.CLIENT_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                if (response != null) {
                    switch (response) {
                        case StatusInfo.SERVICE_ERROR:
                            signUpCallback.signFail(StatusInfo.SERVICE_ERROR);
                            break;
                        case StatusInfo.USERTEXIST:
                            signUpCallback.signUpResponse(StatusInfo.USERTEXIST);
                            break;
                        case StatusInfo.USERNOTEXIST:
                            signUpCallback.signUpResponse(StatusInfo.USERNOTEXIST);
                            break;
                        default:
                            signUpCallback.signFail(StatusInfo.SERVICE_ERROR);
                    }
                }
            }
        });
    }

    @Override
    public void signUpThreeRequest(HashMap<String, String> params, final SignUpCallback signUpCallback) {

        user = new User();
        userInfo = new UserInfo();
        String path = params.get("head");
        String imageName;
        //用户注册时上传头像
        if (!path.equals("@@")) {

            //获取选择的头像和其文件名参数
            File image = new File(path);

            //将图片已时间戳重命名以规范命名
            File reNameImage = ImageUtils.reNameImage(path);
            image.renameTo(reNameImage);

            imageName = reNameImage.getName();
            params.put("head", imageName);
            //保存到用户信息
            userInfo.setHead(imageName);

            OkHttpUtils.post().url(Urls.UPLOAD_HEDA_IMAGE)
                    .addFile("user_head", imageName, reNameImage)//表单名，文件名，文件
                    .build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {

                }

                @Override
                public void onResponse(String response, int id) {

                    // LogUtil.e(response);
                }
            });
        }

        OkHttpUtils.post().url(Urls.SIGNUP).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                signUpCallback.signFail(StatusInfo.CLIENT_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                if (response != null) {
                    switch (response) {
                        case StatusInfo.SERVICE_ERROR:
                            signUpCallback.signFail(StatusInfo.SERVICE_ERROR);
                            break;
                        case StatusInfo.SIGNUP_SUCCESS://注册成功
                            signUpCallback.signUpResponse(StatusInfo.SIGNUP_SUCCESS);

                            ThreadPoolUtil.getThreadpool().execute(new saveSignUpInfo());
                            break;
                        default:
                            signUpCallback.signFail(StatusInfo.SERVICE_ERROR);
                    }
                }
            }
        });

        user.setName(params.get("name"));
        user.setPhone(params.get("phone"));
        user.setPwd(params.get("pwd"));
        user.setUserInfo(userInfo);
    }

    private class saveSignUpInfo implements Runnable {

        @Override
        public void run() {

            CacheManager.saveData(user, AppConfig.getInstance().creatUserDataPath(), AppConfig.getInstance().userDataName);
        }
    }
}
