package prodigalwang.newbornassistant.main_tips.view;

import java.util.List;

import prodigalwang.newbornassistant.base.IBaseView;
import prodigalwang.newbornassistant.bean.TipsPost;

/**
 * Created by ProdigalWang on 2016/12/21.
 */

public interface ITipsView extends IBaseView {

    void updateTipsPost(List<TipsPost> data);

}
