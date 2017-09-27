package prodigalwang.newbornassistant.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.base.BaseFragment;

/**
 * Created by ProdigalWang on 2016/12/29.
 */

public class AboutFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_about;
    }

    @Override
    protected String setToolBarTitle() {
        return getString(R.string.title_about);
    }
}
