package prodigalwang.newbornassistant.main.model;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;
import prodigalwang.newbornassistant.bean.Entity;
import prodigalwang.newbornassistant.bean.PageInfo;
import prodigalwang.newbornassistant.cache.CacheManager;
import prodigalwang.newbornassistant.utils.LogUtil;
import prodigalwang.newbornassistant.utils.StatusInfo;
import prodigalwang.newbornassistant.utils.ThreadPoolUtil;

/**
 * Created by ProdigalWang on 2016/12/16
 */

public abstract class MainDataModelImp<T extends Entity> implements IMainDataModel {

    protected int page;
    protected List<T> data;

    protected abstract String getCacheAlias();

    protected abstract PageInfo parseData(String response);

    @Override
    public void loadData(String url, final LoadDataCallback loadDataCallback) {
        OkHttpUtils.get().url(url)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                call.cancel();
                loadDataCallback.fail(StatusInfo.CLIENT_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {

                if (response.equals(StatusInfo.ALLDATA)) {

                    loadDataCallback.fail(StatusInfo.ALLDATA);

                } else if (response.equals(StatusInfo.NODATA)) {

                    loadDataCallback.fail(StatusInfo.NODATA);
                } else if (response.equals(StatusInfo.SERVICE_ERROR)) {

                    loadDataCallback.fail(StatusInfo.SERVICE_ERROR);
                } else {
//                    Gson gson = new Gson();
//                    Type type = new TypeToken<PageInfo<T >>() {
//                    }.getType();
//
//                    PageInfo<T> pageInfo = gson.fromJson(response, type);


                    data = parseData(response).getPageData();
                    page = parseData(response).getCurrentPage();
                    loadDataCallback.success(data);


                    //保存数据到本地为缓存

                    ThreadPoolUtil.getThreadpool().execute(new saveData());
                }

            }
        });

    }

    @Override
    public void loadDataDetail(String url, final LoadDataDetailCallback loadDataDetailCallback) {

        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                call.cancel();
                loadDataDetailCallback.fail(StatusInfo.CLIENT_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {

                if (response != null) {

                    loadDataDetailCallback.success(response);
                }

            }
        });
    }

    @Override
    public List readCache(String path) {
        data = (List<T>) CacheManager.readInternalCache(path);

        LogUtil.d("read cache success,size is:" + data.size());

        return data;
    }

    @Override
    public void saveCache(List data, String name) {
        CacheManager.saveInternalCache(data, name);
        LogUtil.d("save cache success!name is:" + name);

    }

    @Override
    public String getCachePath(int page) {
        return new StringBuilder(getCacheAlias()).append("_").append(page).toString();
    }

    protected class saveData implements Runnable {
        @Override
        public void run() {

            saveCache(data, getCachePath(page));
        }
    }
}
