package prodigalwang.newbornassistant.main.model;

/**
 * Created by ProdigalWang on 2016/12/16
 */

public interface LoadDataDetailCallback {

    void success(String html);
    void fail(String msg);
}
