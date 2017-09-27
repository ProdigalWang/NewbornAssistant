package prodigalwang.newbornassistant.main.presenter;

/**
 * Created by ProdigalWang on 2016/11/26
 */

public interface IMainPresenter {
    /**
     * 侧滑item的点击监听事件
     * @param id  控件id
     */
    void switchNavigation(int id);

    /**
     * 初始化用户数据
     */
    void initUserData();
}
