package prodigalwang.newbornassistant.educational.view.et_login;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.base.BaseFragment;
import prodigalwang.newbornassistant.bean.StuInfo;
import prodigalwang.newbornassistant.db.etLoginDbHelper;
import prodigalwang.newbornassistant.educational.presenter.et_login.IEtLoginPresenter;
import prodigalwang.newbornassistant.educational.presenter.et_login.EtLoginPresenterImp;
import prodigalwang.newbornassistant.utils.NetUtil;
import prodigalwang.newbornassistant.utils.Urls;

/**
 * Created by ProdigalWang on 2016/11/26
 */

public class EtLoginFragment extends BaseFragment implements IEtLoginView {

    @BindView(R.id.input_stu_id)
    EditText etStuId;
    @BindView(R.id.input_password)
    EditText etPwd;
    @BindView(R.id.input_reg_num)
    EditText etRegNum;

    @BindView(R.id.iv_reg_pic)
    ImageView ivReg;
    @BindView(R.id.cb_save_pwd)
    AppCompatCheckBox remPwd;

    private IEtLoginPresenter iEtLoginPresenter;

    private etLoginDbHelper dbHelper;
    private boolean isSavePwd = true;

    private String id, pwd, reg;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //初始化数据库
        dbHelper = new etLoginDbHelper(mContext);
        iEtLoginPresenter = new EtLoginPresenterImp(this);

        requestRexPic();
        //如果之前不是第一次登录教务系统，并且之前选择了保存用户名和密码，则填充控件数据
        StuInfo info = dbHelper.queryStuInfo();
        if (info != null) {
            etStuId.setText(info.getStuid());
            etPwd.setText(info.getStupwd());
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_loginet;
    }

    @Override
    protected String setToolBarTitle() {
        return "登录教务系统";
    }

    @Override
    public void loginSuccess() {
        showProgress("正在跳转,请稍后...");
        if (isSavePwd) {
            StuInfo info = new StuInfo();
            info.setStuid(id);
            info.setStupwd(pwd);
            //不存在，就保存当前学号密码
            if (!dbHelper.hasUser(id)) {
                dbHelper.saveStuInfo(info);
            }
        }
        hideProgress();

        replaceFragment(R.id.main_frame_content, new EtLoginSuccessFragment(), "fragment_et_login_success");
    }

    /**
     * 这里用okhttp请求图片验证码是为了保存下cookie
     */
    @Override
    @OnClick(R.id.iv_reg_pic)
    public void requestRexPic() {
        if (NetUtil.isConnected()) {

            this.showProgress("正在获取验证码...");
            OkHttpUtils.get().url(Urls.SCHOOL_GET_REGPIC).build().execute(new BitmapCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    hideProgress();
                    showFailMsg("获取验证码失败,请点击图片重试！");
                }

                @Override
                public void onResponse(Bitmap response, int id) {

                    hideProgress();
                    ivReg.setImageBitmap(response);
                }
            });
        } else {
            this.showFailMsg("当前无网络");
        }

    }

    @OnClick(R.id.btn_login)
    void login() {
        id = etStuId.getText().toString();
        pwd = etPwd.getText().toString();
        reg = etRegNum.getText().toString();

        if (id.isEmpty()) {
            etStuId.setError("请输入学号");
            return;
        }
        if (pwd.isEmpty()) {
            etPwd.setError("请输入密码");
            return;
        }
        if (reg.isEmpty()) {
            etRegNum.setError("请输入验证码");
            return;
        }

        iEtLoginPresenter.loginEt(id, pwd, reg);
    }

    @OnClick(R.id.cb_save_pwd)
    void savePwd() {
        isSavePwd = remPwd.isChecked();
    }


}
