package prodigalwang.newbornassistant.base;

/**
 * Created by ProdigalWang on 2016/12/11
 * 所有Activity，Fragment所需要实现的接口
 */

public interface IBaseView {
    /**
     * 展示加载进度
     */
    void showProgress(String progressText);
    void hideProgress();

    /**
     * 展示错误信息
     * @param msg
     */
    void showFailMsg(String msg);
}
