package prodigalwang.newbornassistant.login_signup.view.signup;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.base.BackHandledFragment;
import prodigalwang.newbornassistant.utils.LogUtil;
import prodigalwang.newbornassistant.utils.ThreadPoolUtil;
import prodigalwang.newbornassistant.utils.ToastUtil;

/**
 * Created by ProdigalWang on 2016/12/21.
 * 注意handler导致的内存泄漏
 */

public class SignupTwoFragment extends BackHandledFragment {

    @BindView(R.id.tv_sign_hint)
    TextView tvSignHint;
    @BindView(R.id.tv_countdown)
    TextView tvCountdown;
    @BindView(R.id.tv_sign_repost)
    TextView tvSignRepost;
    @BindView(R.id.ll_sign_count)
    LinearLayout llSignCount;
    @BindView(R.id.input_phone)
    EditText inputPhone;
    @BindView(R.id.input_reg_num)
    EditText inputRegNum;
    @BindView(R.id.btn_sign_next)
    AppCompatButton btnSignNext;


    private String input_phone;
    private static int i = 60;

    private final MyHandler handler = new MyHandler(this);

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_signup_two;
    }

    @Override
    protected String setToolBarTitle() {
        return getString(R.string.signUp);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            input_phone = getArguments().getString("phone");
            inputPhone.setText(input_phone);
            initSmsSdk();
            showProgress("正在获取验证码...");
        }
    }

    public static SignupTwoFragment newInstance(String phone) {
        SignupTwoFragment signupTwoFragment = new SignupTwoFragment();
        Bundle args = new Bundle();
        args.putString("phone", phone);
        signupTwoFragment.setArguments(args);
        return signupTwoFragment;
    }

    /**
     * 初始化sdk,请求验证码
     */
    private void initSmsSdk() {
        SMSSDK.initSDK(getContext(), "1a222250d48b0", "8de83abd2b632f4693a4ed100bb74033");
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;

                //验证验证码
                handler.sendMessage(msg);

                LogUtil.d("send reg_num is " + msg.what);
            }
        };
        //注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);

        requetCode();
    }

    /**
     * 去请求验证码
     */
    private void requetCode() {
        SMSSDK.getVerificationCode("86", input_phone);
    }

    /**
     * fragment处理物理返回事件
     *
     * @return
     */
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

    private static final int MSG_COUNT = 100;
    private static final int MSG_RECOUNT = 101;
    /**
     * 倒计时
     */
    private final Runnable countDown = new Runnable() {
        @Override
        public void run() {
            for (; i > 0; i--) {
                handler.sendEmptyMessage(MSG_COUNT);
                if (i <= 0) {
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            handler.sendEmptyMessage(MSG_RECOUNT);
        }
    };

    /**
     * 防止handler引起的内存泄漏
     * 填完验证码当前活动结束，但是handler和线程并没有结束
     */
    private static class MyHandler extends Handler {
        private final WeakReference<SignupTwoFragment> signupTwoFragmentWeakReference;

        public MyHandler(SignupTwoFragment signupTwoFragment) {
            signupTwoFragmentWeakReference = new WeakReference<>(signupTwoFragment);
        }

        @Override
        public void dispatchMessage(Message msg) {

            if (signupTwoFragmentWeakReference.get() == null || signupTwoFragmentWeakReference.get().isRemoving()) {
                return;
            }

            LogUtil.d("receive msg_num is: " + msg.what);

            switch (msg.what) {
                case MSG_COUNT:
                    if (signupTwoFragmentWeakReference.get().tvCountdown != null) {
                        signupTwoFragmentWeakReference.get().tvCountdown.setText(i + "s");
                    }
                    break;
                case MSG_RECOUNT:
                    if (signupTwoFragmentWeakReference.get().tvCountdown != null &&
                            signupTwoFragmentWeakReference.get().tvSignRepost != null) {
                        signupTwoFragmentWeakReference.get().tvCountdown.setText("");
                        i = 60;
                        signupTwoFragmentWeakReference.get().tvSignRepost.setEnabled(true);
                        signupTwoFragmentWeakReference.get().tvSignRepost.setText("点我重新发送");
                    }
                    break;
                default:
                    signupTwoFragmentWeakReference.get().confirmCode(msg);
                    break;
            }
        }
    }

    @OnClick({R.id.btn_sign_next, R.id.tv_sign_repost})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_sign_next://点击验证
                submitRegNum();
                break;
            case R.id.tv_sign_repost://点击重新发送
                requetCode();
                tvSignRepost.setEnabled(false);
                tvSignRepost.setText(getString(R.string.can_repost_code));
                break;
            default:
                break;
        }
    }

    /**
     * 点击验证发送用户输入的验证码
     */
    private void submitRegNum() {
        String num = inputRegNum.getText().toString();
        if (num.isEmpty()) {
            inputRegNum.setError(getString(R.string.please_input_code));
        } else {
            SMSSDK.submitVerificationCode("86", input_phone, num);
        }

    }

    /**
     * 验证用户填的验证码正确与否
     *
     * @param msg
     */
    private void confirmCode(Message msg) {
        int event = msg.arg1;
        int result = msg.arg2;
        Object data = msg.obj;
        if (result == SMSSDK.RESULT_COMPLETE) {
            //隐藏进度条
            hideProgress();

            if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {

                // handler.removeCallbacksAndMessages(null);
                //验证码正确，跳到下一页
                replaceFragment(R.id.fl_base, SignupThreeFragment.newInstance(input_phone), getString(R.string.fragment_signUp_three));


            } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {

                //发出验证码,开始倒计时
                tvSignHint.setVisibility(View.VISIBLE);
                llSignCount.setVisibility(View.VISIBLE);

                ThreadPoolUtil.getThreadpool().execute(countDown);

            } else {
                ((Throwable) data).printStackTrace();
            }
        }
        if (result == SMSSDK.RESULT_ERROR) {

            //隐藏进度条
            hideProgress();

            try {
                Throwable throwable = (Throwable) data;
                throwable.printStackTrace();
                JSONObject object = new JSONObject(throwable.getMessage());
                String des = object.optString("detail");//错误描述
                int status = object.optInt("status");//错误代码
                if (status > 0 && !TextUtils.isEmpty(des)) {

                    ToastUtil.showShort(getContext(), des);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        SMSSDK.unregisterAllEventHandler();
        //TODO fragment这里调用不生效？
        handler.removeCallbacksAndMessages(null);//fragment销毁时结束分发消息
//        handler.removeMessages(MSG_COUNT);
//        handler.removeMessages(MSG_RECOUNT);
//        handler.removeCallbacks(countDown);
    }
}
