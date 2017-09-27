package prodigalwang.newbornassistant.educational.view.et_classtable;

import java.util.HashMap;
import java.util.List;

import prodigalwang.newbornassistant.base.IBaseView;
import prodigalwang.newbornassistant.bean.Course;

/**
 * Created by ProdigalWang on 2017/5/24.
 */

public interface IEtClassTableView extends IBaseView {

    /**
     * 展示学期数据
     *
     * @param term   学期
     * @param termNo 学期对应的学期号
     */
    void showTerm(List<String> term, List<String> termNo);

    /**
     * 展示课表数据
     *
     * @param courses    课表完整数据
     * @param courseText 要展示的相关数据
     */
    void showClassTable(List<Course> courses, String[][] courseText);
}
