package prodigalwang.newbornassistant.main_news.view;

import android.graphics.Typeface;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.weavey.loading.lib.LoadingLayout;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import prodigalwang.swipebacklayout.lib.app.SwipeBackActivity;
import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.bean.SchoolNews;
import prodigalwang.newbornassistant.bean.ImageNews;
import prodigalwang.newbornassistant.main_news.presenter.INewsPresenter;
import prodigalwang.newbornassistant.main_news.presenter.NewsPresenterImpl;
import prodigalwang.newbornassistant.utils.StatusInfo;
import prodigalwang.newbornassistant.utils.Urls;

public class NewsDetailActivity extends SwipeBackActivity implements INewsDetailView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.htmlContent)
    HtmlTextView mTVNewsContent;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.ivImage)
    ImageView imageView;


    private SchoolNews schoolNews;
    private ImageNews imageNews;
    private int type;

    private INewsPresenter iNewsPresenter;
    private TextView tv_title;

    @Override
    protected boolean isSwipeBack() {
        return true;
    }

    @Override
    protected boolean hasLoadingLayout() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_detail;
    }

    @Override
    protected void initView() {
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle(null); //给toolbar的文字设置跑马灯效果
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        iNewsPresenter = new NewsPresenterImpl(this);
    }

    @Override
    protected void initData() {
        type = getIntent().getIntExtra("type", 0);

        if (type != 0) {

            //从item点过来的
            if (type == StatusInfo.HOMENES) {
                schoolNews = (SchoolNews) getIntent().getSerializableExtra("homenews");

                collapsingToolbar.setTitle(schoolNews.getTitle());
                // collapsingToolbar.setCollapsedTitleTypeface(Typeface.SANS_SERIF);
                //  tv_title.setText(schoolNews.getTitle());

                Picasso.with(this).load(Urls.HOST_SCHOOL + schoolNews.getImage())
                        .placeholder(R.drawable.ic_nopic)
                        .error(R.drawable.ic_nopic)
                        .into(imageView);

                iNewsPresenter.loadNewsDatil(type, schoolNews.getDetail());
            } //从轮播图过来的
            else if (type == StatusInfo.HOME_IMAGE_NEWS) {

                imageNews = (ImageNews) getIntent().getSerializableExtra("imagenews");

                collapsingToolbar.setTitle(imageNews.getTitle());

                //tv_title.setText(imageNews.getTitle());
                Picasso.with(this).load(Urls.HOST_SCHOOL + imageNews.getImage())
                        .placeholder(R.drawable.ic_nopic)
                        .error(R.drawable.ic_nopic)
                        .into(imageView);
                iNewsPresenter.loadNewsDatil(type, imageNews.getDetail());
            }
        }
    }

    @Override
    public void showHtmlString(String html) {
        mTVNewsContent.setHtml(html);
        loadingLayout.setStatus(LoadingLayout.Success);

    }

    @OnClick(R.id.floatingActionButton)
    void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        if (type == StatusInfo.HOMENES) {
            // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
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
        } else if (type == StatusInfo.HOME_IMAGE_NEWS) {

            // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
            oks.setTitle(imageNews.getTitle());
            String url = Urls.SCHOOL_IMAGENEWS_DETAIL + imageNews.getDetail().split("\\.")[0];
            // titleUrl是标题的网络链接，QQ和QQ空间等使用
            oks.setTitleUrl(url);
            // text是分享文本，所有平台都需要这个字段
            oks.setText(imageNews.getTitle());
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


        }

        // 启动分享GUI
        oks.show(this);
    }
}
