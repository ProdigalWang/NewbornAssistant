package prodigalwang.newbornassistant.educational.presenter.et_classtable;

/**
 * Created by ProdigalWang on 2017/5/24.
 */

public interface IEtClassTablePresenter {

    /**
     * 第一次加载默认课表数据
     */
    void loadClassTable();

    /**
     * 获取课表数据
     *
     * @param termNo 学期代号
     */
    void getClassTable(String termNo);
}
