package prodigalwang.newbornassistant.main_notice.view;

import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weavey.loading.lib.LoadingLayout;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import butterknife.BindView;
import prodigalwang.newbornassistant.utils.LogUtil;
import prodigalwang.newbornassistant.utils.Urls;
import prodigalwang.swipebacklayout.lib.app.SwipeBackActivity;
import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.bean.SchoolNotice;
import prodigalwang.newbornassistant.main_notice.presenter.INoticePresenter;
import prodigalwang.newbornassistant.main_notice.presenter.NoticePresenterImp;

public class NoticeDetailActivity extends SwipeBackActivity implements INoticeDetailView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.htmlContent)
    HtmlTextView htmlTextView;
    @BindView(R.id.ll_textview)
    LinearLayout linearLayout;
    @BindView(R.id.toolbar_title)
    TextView tv_title;

    private INoticePresenter iNoticePresenter;
    private SchoolNotice notice;

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
        return R.layout.activity_notice_detail;
    }

    @Override
    protected void initView() {
        notice = (SchoolNotice) getIntent().getSerializableExtra("notice");

        tv_title.setText(notice.getTitle());

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null); //给toolbar的文字设置跑马灯效果
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void initData() {
        iNoticePresenter = new NoticePresenterImp(this);
        iNoticePresenter.loadNotcieDetail(notice.getDetail());
    }

    @Override
    public void showNoticeDetail(String html) {
        htmlTextView.setHtml(html);
        initDownView();
        loadingLayout.setStatus(LoadingLayout.Success);
    }


    void initDownView() {
        if (notice.getAttach_title() != null) {

            String[] title = notice.getAttach_title().split("@@");
            String[] url = notice.getAttachment().split("@@");

            for (int i = 0; i < title.length; i++) {
                TextView textView = new TextView(this);
                //给TextView设置超链接
                SpannableString ss = new SpannableString(title[i]);
                ss.setSpan(new URLSpan(Urls.HOST_SCHOOL + url[i]), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                textView.setText(ss);
                textView.setTextSize(20);
                textView.setMovementMethod(LinkMovementMethod.getInstance());

                linearLayout.addView(textView);
            }
        }

    }
}
