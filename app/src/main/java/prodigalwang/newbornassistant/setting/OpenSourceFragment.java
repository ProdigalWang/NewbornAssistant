package prodigalwang.newbornassistant.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import butterknife.BindView;
import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.base.BaseFragment;

/**
 * Created by ProdigalWang on 2016/12/30.
 */

public class OpenSourceFragment extends BaseFragment {

    @BindView(R.id.htmlContent)
    HtmlTextView htmlTextView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        htmlTextView.setHtml(R.raw.open_source_license);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_open_source;
    }

    @Override
    protected String setToolBarTitle() {
        return getString(R.string.open_source_license);
    }

}
