package prodigalwang.newbornassistant.educational.model.et_classtable;

import java.util.List;

import prodigalwang.newbornassistant.bean.Course;

/**
 * Created by ProdigalWang on 2017/5/24.
 */

public interface EtClassTableCallback {
    /**
     * 获取课表数据成功
     * @param courses 课表数据
     */
    void success(List<Course> courses);

    void fail(int Err_Code, String msg);
}
