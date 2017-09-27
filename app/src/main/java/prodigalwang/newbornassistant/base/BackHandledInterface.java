package prodigalwang.newbornassistant.base;

/**
 * Created by ProdigalWang on 2016/12/31.
 */

public interface BackHandledInterface {

    /**
     * 回调方法，确定当前显示在前台的fragment
     * @param selectedFragment
     */
    void setSelectedFragment(BackHandledFragment selectedFragment);
}
