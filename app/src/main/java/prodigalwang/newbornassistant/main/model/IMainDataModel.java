package prodigalwang.newbornassistant.main.model;

import prodigalwang.newbornassistant.cache.ICache;


/**
 * Created by ProdigalWang on 2016/12/16
 *
 */

public interface IMainDataModel extends ICache{
    /**
     * 请求数据
     * @param url 请求地址
     * @param loadDataCallback 回调接口
     */
    void loadData(String url,LoadDataCallback loadDataCallback);

    void loadDataDetail(String url,LoadDataDetailCallback loadDataDetailCallback);

}
