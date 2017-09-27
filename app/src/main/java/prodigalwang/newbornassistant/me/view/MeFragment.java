package prodigalwang.newbornassistant.me.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.base.BackHandledFragment;
import prodigalwang.newbornassistant.bean.User;
import prodigalwang.newbornassistant.login_signup.presenter.login.ILoginPresenter;
import prodigalwang.newbornassistant.login_signup.presenter.login.LoginPresenterImp;
import prodigalwang.newbornassistant.login_signup.view.login.LoginFragment;
import prodigalwang.newbornassistant.me.presenter.IMePresenter;
import prodigalwang.newbornassistant.me.presenter.MePresenterImp;
import prodigalwang.newbornassistant.utils.ToastUtil;
import prodigalwang.newbornassistant.utils.Urls;


/**
 * Created by ProdigalWang on 2016/11/26
 */

public class MeFragment extends BackHandledFragment implements IMeView {

    @BindView(R.id.cv_user_head)
    CircleImageView cvUserHead;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.ll_my_info)
    LinearLayout llMyInfo;
    @BindView(R.id.ll_my_message)
    LinearLayout llMyMessage;
    @BindView(R.id.ll_my_post)
    LinearLayout llMyPost;
    @BindView(R.id.ll_my_collection)
    LinearLayout llMyCollection;
    @BindView(R.id.ll_my_star)
    LinearLayout llMyStar;
    @BindView(R.id.ll_my_fans)
    LinearLayout llMyFans;
    @BindView(R.id.ll_quit)
    LinearLayout llQuit;

    private boolean hasLogin;

    private IMePresenter iMePresenter;
    private ILoginPresenter iLoginPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected String setToolBarTitle() {
        return getString(R.string.title_me);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iMePresenter = new MePresenterImp(this);
        iMePresenter.initUserData();

        iLoginPresenter = new LoginPresenterImp();
    }

    @OnClick({R.id.ll_my_info,R.id.ll_my_message,R.id.ll_my_post,
            R.id.ll_my_collection,R.id.ll_my_star,
            R.id.ll_my_fans,R.id.cv_user_head,R.id.ll_quit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_my_info:

//                addFragment(new EditMeInfoFragment(),getString(R.string.fragment_edit_me_info));
//                hideFragment(getString(R.string.fragment_me));
                replaceFragment(R.id.fl_base,new EditMeInfoFragment(), getString(R.string.fragment_edit_me_info));
                break;
            case R.id.ll_my_message:
                break;
            case R.id.ll_my_post:
                break;
            case R.id.ll_my_collection:
                break;
            case R.id.ll_my_star:
                break;
            case R.id.ll_my_fans:
                break;
            case R.id.cv_user_head:
                goLoginOrSignUp();
                break;
            case R.id.ll_quit:

                iLoginPresenter.quitLogin();
                getActivity().finish();

                break;

        }
    }

    @Override
    protected boolean onBackPressed() {
        return false;
    }


    @Override
    public void showUserHasLogin(User user) {
        hasLogin = true;
        tvUserName.setText(user.getName());
        if (user.getUserInfo().getHead() != null) {
            Picasso.with(getContext())
                    .load(Urls.GET_USER_HEAD_IMAGE + user.getUserInfo().getHead())
                    .error(R.drawable.ic_user_avatar_default)
                    .placeholder(R.drawable.ic_user_avatar_default)
                    .into(cvUserHead);
        }
    }

    @Override
    public void showUserNoLogin() {
        hasLogin = false;
        tvUserName.setText("点击头像登录");
        cvUserHead.setImageResource(R.drawable.ic_user_avatar_default);
    }

    private void goLoginOrSignUp() {
        if (!hasLogin) {
            replaceFragment(R.id.fl_base, new LoginFragment(), getString(R.string.fragment_login));
        } else {
            ToastUtil.showShort(getContext(), "已登录");
        }

    }
}
