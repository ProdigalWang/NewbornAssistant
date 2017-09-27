package prodigalwang.newbornassistant.main_tips.view;

import prodigalwang.newbornassistant.base.IBaseView;
import prodigalwang.newbornassistant.bean.TipsPost;

/**
 * Created by ProdigalWang on 2017/1/13.
 */

public interface IWriteTipsView extends IBaseView {

    void postTips();

    void postTipsSuccess();
}
