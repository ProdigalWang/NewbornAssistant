package prodigalwang.newbornassistant.educational.view.et_login;

import android.content.Intent;
import android.view.View;

import butterknife.OnClick;
import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.base.BaseFragment;
import prodigalwang.newbornassistant.educational.view.et_classtable.ClassTableActivity;
import prodigalwang.newbornassistant.educational.view.et_sore.SoreActivity;

/**
 * Created by ProdigalWang on 2017/3/14.
 */

public class EtLoginSuccessFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_et_login_success;
    }

    @Override
    protected String setToolBarTitle() {
        return null;
    }

    @OnClick({R.id.btn_my_class_table,R.id.btn_my_score})
    public void onClickView(View v) {

        switch (v.getId()){
            case R.id.btn_my_class_table:
                getActivity().startActivity(new Intent(getActivity(), ClassTableActivity.class));
                break;
            case R.id.btn_my_score:
                getActivity().startActivity(new Intent(getActivity(), SoreActivity.class));
                break;
        }
    }
}
