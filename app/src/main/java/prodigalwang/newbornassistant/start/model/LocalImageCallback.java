package prodigalwang.newbornassistant.start.model;

/**
 * Created by ProdigalWang on 2017/3/6.
 */

public interface LocalImageCallback {

    void success(String url);

    void fail(String msg);
}
