package prodigalwang.newbornassistant.main_tips.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import prodigalwang.newbornassistant.bean.PageInfo;
import prodigalwang.newbornassistant.bean.TipsPost;
import prodigalwang.newbornassistant.main.model.MainDataModelImp;
import prodigalwang.newbornassistant.utils.LogUtil;
import prodigalwang.newbornassistant.utils.StatusInfo;
import prodigalwang.newbornassistant.utils.Urls;

/**
 * Created by ProdigalWang on 2016/12/21.
 */

public class TipsModelImp extends MainDataModelImp<TipsPost> implements ITipsModel {

    private static final String cahce = "home_tips_post";

    private HashMap<String, File> files = null;

    private StringBuilder sb;

    @Override
    protected String getCacheAlias() {
        return cahce;
    }

    @Override
    protected PageInfo parseData(String response) {
        Gson gson = new Gson();
        Type type = new TypeToken<PageInfo<TipsPost>>() {
        }.getType();

        PageInfo<TipsPost> pageInfo = gson.fromJson(response, type);
        return pageInfo;
    }


    @Override
    public void publishPost(TipsPost data, final TipsPostCallback tipsPostCallback) {

        String path = data.getImages();

        if (path != null && !path.isEmpty()) {

            sb = new StringBuilder();
            files = new HashMap<>();
            File file;
            String[] paths = path.split("@@");

            for (int i = 0; i < paths.length; i++) {
                //imageFile.add(new File(paths[i]));
                file = new File(paths[i]);
                files.put(file.getName(), file);//获取文件名和文件并打包
                sb.append(file.getName());
                sb.append("@@");
            }
            data.setImages(sb.toString());

        }


        OkHttpUtils.post()
                .url(Urls.UPLOADTIPS)
                .addParams("data", new Gson().toJson(data))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                tipsPostCallback.fail(StatusInfo.CLIENT_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {

                if (response.equals(StatusInfo.SUCCESS)) {

                    OkHttpUtils.post()
                            .url(Urls.UPLOADTIPS_IMAGES)
                            .files("images", files)
                            .build().execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {

                            //TODO: 逻辑验证
                            tipsPostCallback.Success();
                        }
                    });

                } else {
                    tipsPostCallback.fail(StatusInfo.SERVICE_ERROR);
                }
            }
        });


    }
}
