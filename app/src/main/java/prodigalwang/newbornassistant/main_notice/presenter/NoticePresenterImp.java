package prodigalwang.newbornassistant.main_notice.presenter;

import java.util.List;

import prodigalwang.newbornassistant.bean.SchoolNotice;
import prodigalwang.newbornassistant.cache.CacheManager;
import prodigalwang.newbornassistant.main.model.IMainDataModel;
import prodigalwang.newbornassistant.main.model.LoadDataCallback;
import prodigalwang.newbornassistant.main.model.LoadDataDetailCallback;
import prodigalwang.newbornassistant.main_notice.model.NoticeModelImp;
import prodigalwang.newbornassistant.main_notice.view.INoticeDetailView;
import prodigalwang.newbornassistant.main_notice.view.INoticeView;
import prodigalwang.newbornassistant.utils.LogUtil;
import prodigalwang.newbornassistant.utils.NetUtil;
import prodigalwang.newbornassistant.utils.StatusInfo;
import prodigalwang.newbornassistant.utils.Urls;

/**
 * Created by ProdigalWang on 2016/12/14
 */

public class NoticePresenterImp implements INoticePresenter {

    private IMainDataModel iMainDataModel;
    private INoticeView iNoticeView;
    private INoticeDetailView iNoticeDetailView;


    public NoticePresenterImp(INoticeView iNoticeView) {
        this.iNoticeView = iNoticeView;

        this.iMainDataModel = new NoticeModelImp();
    }

    public NoticePresenterImp(INoticeDetailView iNoticeDetailView) {


        this.iNoticeDetailView = iNoticeDetailView;
        this.iMainDataModel = new NoticeModelImp();
    }

    @Override
    public void loadNotice(int page) {
        if (page <= 1) {
            iNoticeView.showProgress("");
        }
        if (NetUtil.isConnected()) {

            String url = Urls.HOME_NEWS + "?currentPage=" + page + "&type=" + StatusInfo.HOMENOTICE;

            iMainDataModel.loadData(url, new LoadDataCallback() {
                @Override
                public void success(List data) {

                    iNoticeView.hideProgress();
                    iNoticeView.updateNotice(data);

                }

                @Override
                public void fail(String msg) {
                    iNoticeView.hideProgress();
                    iNoticeView.showFailMsg(msg);
                }
            });


        } else {

            if (CacheManager.isExistDataCache(iMainDataModel.getCachePath(page))) {

                List<SchoolNotice> data = iMainDataModel.readCache(iMainDataModel.getCachePath(page));

                iNoticeView.showFailMsg(StatusInfo.HAS_CACHE_NONETWORK);
                iNoticeView.hideProgress();
                iNoticeView.updateNotice(data);
            } else {
                iNoticeView.showFailMsg(StatusInfo.NONETWORK);
            }

        }

    }

    @Override
    public void loadNotcieDetail(String htmlID) {
        if (NetUtil.isConnected()) {

            String url = Urls.HOME_NOTICE_DETAIL + htmlID;

            LogUtil.e("请求notice详情==url:" + url);

            iMainDataModel.loadDataDetail(url, new LoadDataDetailCallback() {
                @Override
                public void success(String html) {

                    iNoticeDetailView.showNoticeDetail(html);
                }

                @Override
                public void fail(String msg) {

                    iNoticeDetailView.showFailMsg(msg);
                }
            });
        } else {
            iNoticeDetailView.showFailMsg(StatusInfo.NONETWORK);
        }
    }
}
