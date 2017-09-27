package prodigalwang.newbornassistant.main_news.presenter;

import java.util.HashMap;
import java.util.List;

import prodigalwang.newbornassistant.bean.ImageNews;
import prodigalwang.newbornassistant.bean.SchoolNews;
import prodigalwang.newbornassistant.cache.CacheManager;
import prodigalwang.newbornassistant.main.model.LoadDataCallback;
import prodigalwang.newbornassistant.main.model.LoadDataDetailCallback;
import prodigalwang.newbornassistant.main_news.model.INewsModel;
import prodigalwang.newbornassistant.main_news.model.NewsModelImpl;
import prodigalwang.newbornassistant.main_news.view.INewsDetailView;
import prodigalwang.newbornassistant.main_news.view.INewsView;
import prodigalwang.newbornassistant.utils.NetUtil;
import prodigalwang.newbornassistant.utils.StatusInfo;
import prodigalwang.newbornassistant.utils.Urls;

/**
 * Created by ProdigalWang on 2016/12/9
 */

public class NewsPresenterImpl implements INewsPresenter, LoadDataDetailCallback {

    private INewsModel iNewsModel;
    private INewsView iNewsView;

    private INewsDetailView iNewsDetailView;

    public NewsPresenterImpl(INewsDetailView iNewsDetailView) {
        this.iNewsDetailView = iNewsDetailView;
        iNewsModel = new NewsModelImpl();
    }

    public NewsPresenterImpl(INewsView iNewsView) {
        this.iNewsView = iNewsView;
        iNewsModel = new NewsModelImpl();
    }

    @Override
    public void loadNews(int page) {
        if (page <= 1) {
            iNewsView.showProgress("");
        }
        if (NetUtil.isConnected()) {

            String url = Urls.HOME_NEWS + "?currentPage=" + page + "&type=" + StatusInfo.HOMENES;

            iNewsModel.loadData(url, new LoadDataCallback() {
                @Override
                public void success(List data) {
                    iNewsView.hideProgress();
                    iNewsView.updateNews(data);
                }

                @Override
                public void fail(String msg) {
                    iNewsView.hideProgress();
                    iNewsView.showFailMsg(msg);
                }
            });
        } else {

            //有缓存无网络
            if (CacheManager.isExistDataCache(iNewsModel.getCachePath(page))) {
                List<SchoolNews> data = iNewsModel.readCache(iNewsModel.getCachePath(page));
                iNewsView.showFailMsg(StatusInfo.HAS_CACHE_NONETWORK);
                iNewsView.hideProgress();
                iNewsView.updateNews(data);

            } else {

                //无缓存无网络
                iNewsView.showFailMsg(StatusInfo.NONETWORK);
            }

        }
    }

    @Override
    public void loadImageNews() {
        if (NetUtil.isConnected()) {
            iNewsModel.loadImageNews(Urls.HOME_NEWS_IMAGE_HEADER, new LoadDataCallback() {
                @Override
                public void success(List data) {
                    iNewsView.updateHeaderImageNews(data);
                }

                @Override
                public void fail(String msg) {
                    iNewsView.showFailMsg(msg);
                }
            });
        } else {

            //只有新闻页有轮播图,向下转型,使用实现类特有方法
            NewsModelImpl newsModel = (NewsModelImpl) iNewsModel;

            if (CacheManager.isExistDataCache(newsModel.getHeaderPath())) {
                List<ImageNews> data = newsModel.readHeaderCache(newsModel.getHeaderPath());

                iNewsView.updateHeaderImageNews(data);

            } else {

                iNewsView.showFailMsg(StatusInfo.NONETWORK);
            }
        }

    }

    @Override
    public void loadNewsDatil(int type, String htmlId) {
        if (NetUtil.isConnected()) {
            iNewsDetailView.showProgress("");

            if (type == StatusInfo.HOMENES) {
                iNewsModel.loadDataDetail(Urls.HOME_NES_DETAIL + htmlId, this);
            } else if (type == StatusInfo.HOME_IMAGE_NEWS) {
                iNewsModel.loadDataDetail(Urls.HOME_NEWS_HEADER_DETAIL + htmlId, this);
            }

        } else {

            iNewsDetailView.showFailMsg(StatusInfo.NONETWORK);
        }

    }

    @Override
    public void success(String html) {
        iNewsDetailView.hideProgress();
        iNewsDetailView.showHtmlString(html);
    }

    @Override
    public void fail(String msg) {
        iNewsDetailView.hideProgress();
        iNewsDetailView.showFailMsg(msg);
    }
}
