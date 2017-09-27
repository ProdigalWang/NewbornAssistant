package prodigalwang.newbornassistant.login_signup.view.signup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import de.hdodenhof.circleimageview.CircleImageView;
import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.base.BackHandledFragment;
import prodigalwang.newbornassistant.login_signup.presenter.signup.ISignUpPresenter;
import prodigalwang.newbornassistant.login_signup.presenter.signup.SignUpPresenterImp;
import prodigalwang.newbornassistant.utils.StatusInfo;
import prodigalwang.newbornassistant.utils.ToastUtil;

/**
 * Created by ProdigalWang on 2016/12/23.
 */

public class SignupThreeFragment extends BackHandledFragment implements ISignUpView {

    @BindView(R.id.cv_user_head)
    CircleImageView cvUserHead;
    @BindView(R.id.input_name)
    EditText inputName;
    @BindView(R.id.input_password)
    EditText inputPassword;
    @BindView(R.id.btn_creat)
    AppCompatButton btnCreat;

    private String phone;
    private String name;
    private String pwd;
    private String head_path = "@@";

    private ISignUpPresenter iSignUpPresenter;

    @Override
    protected boolean onBackPressed() {
        materialDialog = new MaterialDialog
                .Builder(getContext())
                .title(getString(R.string.points))
                .content(getString(R.string.give_up_sign_up_points))
                .positiveText(getString(R.string.confirm))
                .negativeText(getString(R.string.cancel))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        getActivity().finish();
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            phone = getArguments().getString("phone");
        }

        iSignUpPresenter = new SignUpPresenterImp(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_signup_three;
    }

    @Override
    protected String setToolBarTitle() {
        return getString(R.string.signUp);
    }

    public static SignupThreeFragment newInstance(String phone) {
        SignupThreeFragment signupThreeFragment = new SignupThreeFragment();
        Bundle args = new Bundle();
        args.putString("phone", phone);
        signupThreeFragment.setArguments(args);
        return signupThreeFragment;
    }

    @OnClick({R.id.btn_creat,R.id.cv_user_head})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_creat:
                creatAccount();
                break;
            case R.id.cv_user_head:
                choiceHeadImage();
            default:
                break;
        }
    }


    private void creatAccount() {
        name = inputName.getText().toString();
        pwd = inputPassword.getText().toString();

        if (name.isEmpty()) {
            inputName.setError(getString(R.string.input_name));
            return;
        } else if (name.length() < 4 || name.length() > 15) {
            inputName.setError(getString(R.string.input_name_length));
            return;
        }
        if (pwd.isEmpty()) {
            inputPassword.setError(getString(R.string.input_pwd));
            return;
        } else if (pwd.length() < 6 || pwd.length() > 15) {
            inputPassword.setError(getString(R.string.input_pwd_length));
            return;
        }

        iSignUpPresenter.postSignThreeInfo(phone, name, pwd, head_path);
    }

    protected final int REQUEST_CODE_CAMERA = 1000;
    protected final int REQUEST_CODE_GALLERY = 1001;
    protected final int REQUEST_CODE_CROP = 1002;
    protected final int REQUEST_CODE_EDIT = 1003;

    private void choiceHeadImage() {

        GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                for (PhotoInfo info : resultList) {
                    switch (reqeustCode) {
                        case REQUEST_CODE_GALLERY:

                            //保存选择的图片路径并展示
                            head_path = info.getPhotoPath();
                            Picasso.with(getContext()).load(new File(info.getPhotoPath()))
                                    .into(cvUserHead);
                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
                switch (requestCode) {
                    case REQUEST_CODE_GALLERY:
                        ToastUtil.showShort(getContext(), errorMsg);
                        break;
                    default:
                        break;
                }
            }
        });

    }

    @Override
    public void SignupOneInfo(String msg) {


    }

    @Override
    public void SignupThreeInfo(String msg) {
        if (msg.equals(StatusInfo.SIGNUP_SUCCESS)) {
            getActivity().finish();
        }
    }


}
