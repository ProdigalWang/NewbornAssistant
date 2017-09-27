package prodigalwang.newbornassistant.main_notice.presenter;

/**
 * Created by ProdigalWang on 2016/12/14
 */

public interface INoticePresenter {
    /**
     *
     * @param page
     */
    void loadNotice(int page);
    void loadNotcieDetail(String htmlID);
}
