package prodigalwang.newbornassistant.educational.model.et_classtable;

import java.util.HashMap;

/**
 * Created by ProdigalWang on 2017/5/24.
 */

public interface IEtClassTableModel {

    /**
     * 从html解析出学期数
     * @param getTermCallBack
     * @param params null
     */
    void getTermFromHtml(EtGetTermCallBack getTermCallBack,HashMap<String,String> params);

    /**
     * 从html解析课表
     * @param etClassTableCallback 回掉接口
     * @param params 学期代号
     */
    void getClassTableFromHtml(EtClassTableCallback etClassTableCallback, HashMap<String,String> params);
}
