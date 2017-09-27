package prodigalwang.newbornassistant.main.model;

import java.util.List;

import prodigalwang.newbornassistant.bean.Entity;

/**
 * Created by ProdigalWang on 2016/12/16
 */

public interface LoadDataCallback<T extends Entity> {
    void success(List<T> data);
    void fail(String msg);
}
