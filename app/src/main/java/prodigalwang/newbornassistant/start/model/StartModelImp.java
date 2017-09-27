package prodigalwang.newbornassistant.start.model;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import prodigalwang.newbornassistant.utils.StatusInfo;
import prodigalwang.newbornassistant.utils.Urls;

/**
 * Created by ProdigalWang on 2017/3/6.
 */

public class StartModelImp implements IStartModel {

    private RequestCall call;

    @Override
    public void requsetData(final LocalImageCallback localImageCallback) {
        call = OkHttpUtils.get().url(Urls.GET_BING_IMAGE).build();
        call.execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                localImageCallback.fail(StatusInfo.CLIENT_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {

                if (!response.isEmpty()) {

                    String url = resolveData(response);
                    if (!url.isEmpty()) {
                        localImageCallback.success(Urls.BING_URL + url);
                    } else {
                        localImageCallback.fail("get url fail");
                    }
                }

            }
        });
    }

    @Override
    public void cancelRequest() {

        if (call != null)
            call.cancel();
    }

    /**
     * 解析json数据
     * @param data
     * @return
     */
    private String resolveData(String data) {

        String url = "";
        try {
            JSONArray jsonArray = new JSONObject(data).getJSONArray("images");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                if (jsonObject.has("url")) {

                    url = jsonObject.getString("url");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return url;
    }
}
