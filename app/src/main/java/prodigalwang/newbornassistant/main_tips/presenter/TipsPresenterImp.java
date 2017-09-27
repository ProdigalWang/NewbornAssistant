package prodigalwang.newbornassistant.main_tips.presenter;

import java.util.List;

import prodigalwang.newbornassistant.bean.SchoolNews;
import prodigalwang.newbornassistant.bean.TipsPost;
import prodigalwang.newbornassistant.bean.User;
import prodigalwang.newbornassistant.cache.CacheManager;
import prodigalwang.newbornassistant.main.model.LoadDataCallback;
import prodigalwang.newbornassistant.main_tips.model.ITipsModel;
import prodigalwang.newbornassistant.main_tips.model.TipsModelImp;
import prodigalwang.newbornassistant.main_tips.model.TipsPostCallback;
import prodigalwang.newbornassistant.main_tips.view.ITipsView;
import prodigalwang.newbornassistant.main_tips.view.IWriteTipsView;
import prodigalwang.newbornassistant.utils.LogUtil;
import prodigalwang.newbornassistant.utils.NetUtil;
import prodigalwang.newbornassistant.utils.StatusInfo;
import prodigalwang.newbornassistant.utils.Urls;

/**
 * Created by ProdigalWang on 2016/12/21.
 */

public class TipsPresenterImp implements ITipsPresenter {

    private ITipsView iTipsView;
    private ITipsModel iTipsModel;

    private IWriteTipsView iWriteTipsView;

    public TipsPresenterImp(ITipsView iTipsView) {
        this.iTipsView = iTipsView;
        iTipsModel = new TipsModelImp();
    }

    public TipsPresenterImp(IWriteTipsView iWriteTipsView) {
        this.iWriteTipsView = iWriteTipsView;
        iTipsModel = new TipsModelImp();
    }

    @Override
    public void loadPost(int page) {
        if (page <= 1) {
            iTipsView.showProgress("");
        }
        if (NetUtil.isConnected()) {

            String url = Urls.HOME_TIPS_GET_POST + "?currentPage=" + page + "&type=" + StatusInfo.TIPS_ALL;

            iTipsModel.loadData(url, new LoadDataCallback() {
                @Override
                public void success(List data) {
                    iTipsView.hideProgress();

                    iTipsView.updateTipsPost(checkData(data));
                }

                @Override
                public void fail(String msg) {
                    iTipsView.hideProgress();
                    iTipsView.showFailMsg(msg);
                }
            });
        } else {

            //有缓存无网络
            if (CacheManager.isExistDataCache(iTipsModel.getCachePath(page))) {
                List<TipsPost> data = iTipsModel.readCache(iTipsModel.getCachePath(page));
                iTipsView.showFailMsg(StatusInfo.HAS_CACHE_NONETWORK);
                iTipsView.hideProgress();
                iTipsView.updateTipsPost(data);

            } else {

                //无缓存无网络
                iTipsView.showFailMsg(StatusInfo.NONETWORK);
            }

        }
    }

    /**
     * 判断data的images字段是否为空，如果不为空则需要Adapter展示带有图片的item，
     * 通过对Content_type设置值进行区分,因为Adapter使用了泛型T代表数据，所以需要使用一个每个javabean都用到的
     * 也就是定义在Entity中的字段作为区分
     * @param data
     * @return
     */
    private List<TipsPost> checkData(List<TipsPost> data) {
        for (TipsPost tips : data) {
            if (tips.getImages() != null) {
                tips.setContent_type("tipsHasImage");
            }
        }
        return data;
    }

    @Override
    public void loadPostDetail() {

    }

    @Override
    public void PostTips(TipsPost data) {

        if (NetUtil.isConnected()) {
            iWriteTipsView.showProgress("正在发布...");

            iTipsModel.publishPost(data, new TipsPostCallback() {
                @Override
                public void Success() {
                    iWriteTipsView.hideProgress();
                    iWriteTipsView.postTipsSuccess();
                }

                @Override
                public void fail(String msg) {

                    iWriteTipsView.hideProgress();
                    iWriteTipsView.showFailMsg(msg);
                }
            });

        } else {

            iWriteTipsView.showFailMsg(StatusInfo.NONETWORK);
        }

    }

}
