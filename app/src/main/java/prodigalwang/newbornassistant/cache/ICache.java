package prodigalwang.newbornassistant.cache;

import java.util.List;

import prodigalwang.newbornassistant.bean.Entity;

/**
 * Created by ProdigalWang on 2016/12/15
 */

public interface ICache<T extends Entity> {

    /**
     * 读取缓存
     * @param path  存放路径
     * @return 实体类
     */
    List<T> readCache(String path);

    /**
     * 保存数据
     * @param data 数据
     * @param name 保存名
     */
    void saveCache(List<T> data,String name);

    String getCachePath(int page);
}
