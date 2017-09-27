package prodigalwang.newbornassistant.main_news.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import prodigalwang.newbornassistant.bean.SchoolNews;
import prodigalwang.newbornassistant.bean.ImageNews;
import prodigalwang.newbornassistant.bean.PageInfo;
import prodigalwang.newbornassistant.cache.CacheManager;
import prodigalwang.newbornassistant.main.model.MainDataModelImp;
import prodigalwang.newbornassistant.main.model.LoadDataCallback;
import prodigalwang.newbornassistant.utils.LogUtil;
import prodigalwang.newbornassistant.utils.StatusInfo;
import prodigalwang.newbornassistant.utils.ThreadPoolUtil;

/**
 * Created by ProdigalWang on 2016/12/9
 */

public class NewsModelImpl extends MainDataModelImp<SchoolNews> implements INewsModel {

    private List<ImageNews> imageNewses;//轮播图

    private static final String cache = "home_news";
    private static final String header = "header";
    @Override
    protected String getCacheAlias() {
        return cache;
    }

    @Override
    protected PageInfo parseData(String response) {
        Gson gson = new Gson();
        Type type = new TypeToken<PageInfo<SchoolNews>>() {
        }.getType();

        PageInfo<SchoolNews> pageInfo = gson.fromJson(response, type);
        return pageInfo;
    }
    @Override
    public void loadImageNews(String url, final LoadDataCallback loadDataCallback) {

        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

                loadDataCallback.fail(StatusInfo.CLIENT_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                if (response.equals(StatusInfo.NODATA)) {

                    loadDataCallback.fail(StatusInfo.NODATA);
                } else if (response.equals(StatusInfo.SERVICE_ERROR)) {

                    loadDataCallback.fail(StatusInfo.SERVICE_ERROR);
                } else {
                    imageNewses = new Gson().fromJson(response,
                            new TypeToken<List<ImageNews>>() {
                            }.getType());

                    loadDataCallback.success(imageNewses);

                    //保存轮播图
                    ThreadPoolUtil.getThreadpool().execute(new saveNewsHeader());
                }

            }
        });

    }


    public String getHeaderPath() {
        return new StringBuilder(cache).append("_").append(header).toString();
    }

    class saveNewsHeader implements Runnable {
        @Override
        public void run() {
            saveCache(imageNewses, getHeaderPath());
        }
    }

    public List<ImageNews> readHeaderCache(String path) {
        List<ImageNews> data = (List<ImageNews>) CacheManager.readInternalCache(path);

        LogUtil.e("读取新闻头部缓存成功--大小:" + data.size());
        return data;
    }
}
