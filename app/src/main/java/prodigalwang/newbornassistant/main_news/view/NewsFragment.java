package prodigalwang.newbornassistant.main_news.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.LayoutInflater;
import android.view.View;

import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.base.BaseAdapter;
import prodigalwang.newbornassistant.base.BaseListFragment;
import prodigalwang.newbornassistant.bean.SchoolNews;
import prodigalwang.newbornassistant.main_news.presenter.INewsPresenter;
import prodigalwang.newbornassistant.main_news.presenter.NewsPresenterImpl;
import prodigalwang.newbornassistant.main_news.view.adapter.NewsAdapter;
import prodigalwang.newbornassistant.main_news.view.adapter.NewsHeaderAdapter;
import prodigalwang.newbornassistant.utils.LogUtil;
import prodigalwang.newbornassistant.utils.StatusInfo;
import prodigalwang.newbornassistant.utils.Urls;

/**
 * Created by ProdigalWang on 2016/11/26
 */

public class NewsFragment extends BaseListFragment<SchoolNews> implements INewsView {

    private INewsPresenter iNewsPresenter;

    private RollPagerView mRRollPagerView;
    private NewsHeaderAdapter newsHeaderAdapter;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iNewsPresenter = new NewsPresenterImpl(this);//presenter和view结合
    }


    @Override
    protected BaseAdapter<SchoolNews> getAdapter() {
        return new NewsAdapter(getContext());
    }

    @Override
    protected void requestData() {

        iNewsPresenter.loadNews(page);

    }

    @Override
    public void updateNews(List data) {
        loadSuccess();

        if (data != null) {
            baseAdapter.addAllData(data);
        }
        LogUtil.d("loading completed and net page is:" + page);

    }

    @Override
    protected void initHeaderView() {

        View header = LayoutInflater.from(getContext()).inflate(R.layout.item_news_header, mRecyclerView, false);

        mRRollPagerView = (RollPagerView) header.findViewById(R.id.roll_view_pager);

        mRRollPagerView.setHintView(new ColorPointHintView(getContext(), Color.WHITE, Color.GRAY));

        // mRRollPagerView.setHintView(new TextHintView(getContext()));
        baseAdapter.setHeaderView(header);
        LogUtil.d("header view has created");
    }

    @Override
    protected void requestHeaderData() {
        iNewsPresenter.loadImageNews();
    }


    @Override
    public void updateHeaderImageNews(List data) {
        LogUtil.d("load imagenews has completed and size is:" + data.size());

        if (data != null) {
            newsHeaderAdapter = new NewsHeaderAdapter(mRRollPagerView);
            mRRollPagerView.setAdapter(newsHeaderAdapter);
            newsHeaderAdapter.addAllData(data);
            mRRollPagerView.getViewPager().getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(View view, int position) {

        SchoolNews news = baseAdapter.getItem(position);
        Intent intent = new Intent(getActivity(), NewsDetailActivity.class);

        intent.putExtra("type", StatusInfo.HOMENES);
        intent.putExtra("homenews", news);

        View transitionView = view.findViewById(R.id.item_news_iv);
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                        transitionView, getString(R.string.transition_news_img));

        ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
    }

    /**
     * 长按分享新闻
     * @param view
     * @param position
     */
    @Override
    public boolean onItemLongClick(View view, int position) {
        SchoolNews schoolNews = baseAdapter.getItem(position);

        ShareSDK.initSDK(getContext());
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        oks.setTitle(schoolNews.getTitle());
        String url = Urls.SHOOL_NEWS_DETAIL + schoolNews.getDetail().split("\\.")[0];
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(schoolNews.getResume());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        //oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);

        // 启动分享GUI
        oks.show(getContext());
        return true;
    }

    @Override
    public void onClick(View v) {

    }
}
