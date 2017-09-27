package prodigalwang.newbornassistant.me.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.base.BackHandledFragment;
import prodigalwang.newbornassistant.base.BaseFragment;
import prodigalwang.newbornassistant.utils.ToastUtil;

/**
 * Created by ProdigalWang on 2016/12/30.
 */

public class EditMeInfoFragment extends BackHandledFragment {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);//Fragment 设置menu要先调用这个方法
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_edit_me_info;
    }

    @Override
    protected String setToolBarTitle() {
        return "编辑个人信息";
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_submit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                ToastUtil.showShort(getContext(), "提交被点击");
                break;
            default:

        }
        return true;
    }

    @Override
    protected boolean onBackPressed() {
        materialDialog = new MaterialDialog
                .Builder(getContext())
                .title(getString(R.string.points))
                .content("是否放弃修改个人信息?")
                .positiveText(getString(R.string.confirm))
                .negativeText(getString(R.string.cancel))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        replaceFragment(R.id.fl_base,new MeFragment(), getString(R.string.fragment_me));
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();

        return true;
    }


}
