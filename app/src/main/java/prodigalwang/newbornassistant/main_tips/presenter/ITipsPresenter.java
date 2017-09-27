package prodigalwang.newbornassistant.main_tips.presenter;

import java.util.List;

import prodigalwang.newbornassistant.bean.TipsPost;

/**
 * Created by ProdigalWang on 2016/12/21.
 */

public interface ITipsPresenter {

    void loadPost(int page);

    void loadPostDetail();

    void PostTips(TipsPost data);

}
