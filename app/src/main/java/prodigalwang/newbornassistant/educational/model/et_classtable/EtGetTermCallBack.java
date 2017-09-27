package prodigalwang.newbornassistant.educational.model.et_classtable;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by ProdigalWang on 2017/5/30.
 */

public interface EtGetTermCallBack {

    /**
     * 获取学期
     *
     * @param terms 保存学期数的集合
     */
    void success(LinkedHashMap<String, String> terms);

    void fail(int Err_Code, String msg);
}
