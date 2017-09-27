package prodigalwang.newbornassistant.educational.model.et_login;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;

import okhttp3.Call;
import prodigalwang.newbornassistant.utils.Urls;

/**
 * Created by ProdigalWang on 2017/3/7.
 */

public class EtLoginModelImp implements IEtLoginModel {

    private static final String LOGIN_SUCCESS = "登录成功";
    private static final String LOGIN_FAIL = "登录失败";
    private static final String LOGIN_SUCCESS_INFO = "您好!欢迎您登录教务处网络平台";
    private static final String USER_OR_PWD_ERROR = "用户名或密码错误";
    private static final String REGNU_ERROR = "验证码错误";
    private static final String USER_OR_PWD_ERROR_INFO = "错误的用户名或者密码<br>";
    private static final String REGNU_ERROR_INFO = "请输入正确的附加码<br>";

    @Override
    public void loginEt(final EtLoginCallback etLoginCallback, HashMap<String, String> params) {

        OkHttpUtils.post().params(params).url(Urls.SCHOOL_LOGIN_ET)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

                etLoginCallback.fail(LOGIN_FAIL);
            }

            @Override
            public void onResponse(String response, int id) {

                String info = checkData(response);

                if (info.equals(LOGIN_SUCCESS)) {
                    etLoginCallback.success();
                } else {
                    etLoginCallback.fail(info);
                }
            }
        });
    }


    /**
     * JSoup解析服务器返回的信息
     *
     * @param data
     * @return 登录成功或失败的原因
     */
    private String checkData(String data) {
        try {
            //html转为一个Document对象
            Document document = Jsoup.parse(data);
            //获取table下第一个tr标签
            Element tr = document.select("table").select("tr").first();
            //获取tr标签下的第一个td标签
            Element td = tr.select("td").first();
            //信息填写正确登录成功
            if (td.text().equals(LOGIN_SUCCESS_INFO)) {

                return LOGIN_SUCCESS;
            } else {
                //获取script标签内的内容
                Elements js = document.getElementsByTag("script");
                //最后一组script内容，以 " 分割获取提示的错误信息内容，详细参考校园网址
                String[] errorInfo = js.last().data().split("\"");

                if (errorInfo[1].equals(USER_OR_PWD_ERROR_INFO)) {
                    return USER_OR_PWD_ERROR;
                } else if (errorInfo[1].equals(REGNU_ERROR_INFO)) {
                    return REGNU_ERROR;
                }
                return LOGIN_FAIL;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return LOGIN_FAIL;
    }
}
