package prodigalwang.newbornassistant.main_tips.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.base.BackHandledFragment;
import prodigalwang.newbornassistant.bean.TipsPost;
import prodigalwang.newbornassistant.bean.User;
import prodigalwang.newbornassistant.login_signup.presenter.login.ILoginPresenter;
import prodigalwang.newbornassistant.login_signup.presenter.login.LoginPresenterImp;
import prodigalwang.newbornassistant.main_tips.presenter.ITipsPresenter;
import prodigalwang.newbornassistant.main_tips.presenter.TipsPresenterImp;
import prodigalwang.newbornassistant.utils.ImageUtils;
import prodigalwang.newbornassistant.utils.StatusInfo;
import prodigalwang.newbornassistant.utils.ToastUtil;

/**
 * Created by ProdigalWang on 2017/1/13.
 */

public class WriteTipsFragment extends BackHandledFragment implements IWriteTipsView {
    @BindView(R.id.et_input_type)
    EditText etInputType;
    @BindView(R.id.et_input_title)
    EditText etInputTitle;
    @BindView(R.id.et_input_tips)
    EditText etInputTips;
    @BindView(R.id.tv_input_count)
    TextView tvInputCount;
    @BindView(R.id.btn_add_image)
    Button btnAddImage;
    @BindView(R.id.iv_select_one)
    ImageView ivSelectOne;
    @BindView(R.id.iv_select_two)
    ImageView ivSelectTwo;
    @BindView(R.id.iv_select_three)
    ImageView ivSelectThree;

    @Override
    protected boolean onBackPressed() {
        return false;
    }

    private ITipsPresenter iTipsPresenter;
    private ILoginPresenter iLoginPresenter;

    private TipsPost data;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_write_tips;
    }

    @Override
    protected String setToolBarTitle() {
        return "发表帖子";
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);//Fragment 设置menu要先调用这个方法

        //初始化编辑框字数监听器
        etInputTips.addTextChangedListener(myTextWatcher);
        etInputTips.setSelection(etInputTips.length());

        iTipsPresenter = new TipsPresenterImp(this);
        iLoginPresenter = new LoginPresenterImp();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_submit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                regInput();
                break;
            default:
                break;
        }
        return true;
    }

    @OnClick({R.id.et_input_type, R.id.btn_add_image})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.et_input_type:

                showChoiceType();
                break;
            case R.id.btn_add_image:
                showChoiceImage();
                break;


        }
    }

    private String choiceType;
    private String inputTitle;
    private String inputDetail;

    /**
     * 弹出类型选择框
     */
    private void showChoiceType() {
        new MaterialDialog.Builder(getContext())
                .title("请选择类型")
                .items(R.array.type_values)
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                        choiceType = text.toString();
                        etInputType.setText(choiceType);

                        return true; // allow selection
                    }
                })
                .positiveText(getString(R.string.confirm))
                .show();
    }

    private void regInput() {
        choiceType = etInputType.getText().toString();
        inputTitle = etInputTitle.getText().toString();
        inputDetail = etInputTips.getText().toString();
        if (choiceType.isEmpty()) {
            ToastUtil.showShort(getContext(), "请选择类别");
            return;
        }

        if (inputTitle.isEmpty()) {
            ToastUtil.showShort(getContext(), "请输入标题");
            return;
        }
        if (inputTitle.length() > 20) {
            ToastUtil.showShort(getContext(), "标题不能超过20个字符");
            return;
        }
        if (inputDetail.isEmpty()) {
            ToastUtil.showShort(getContext(), "请输入详细内容");
            return;
        }


        packageData();
    }

    /**
     * 包装输入的数据
     */
    private void packageData() {


        data = new TipsPost();
        data.setType(choiceType);
        data.setTitle(inputTitle);
        data.setDetail(inputDetail);

        User user = iLoginPresenter.readUserData();
        data.setUid(user.getId());

        packgeImageData();

        postTips();
    }

    private void packgeImageData() {

        if (choiceImagePath != null && choiceImagePath.size() > 0) {
            StringBuilder sb = new StringBuilder();
            File oldFile;
            File newFile;

            for (String path : choiceImagePath
                    ) {
                oldFile = new File(path);
                newFile = ImageUtils.reNameImage(path);
                oldFile.renameTo(newFile);

                sb.append(newFile.getAbsolutePath());
                sb.append("@@");
            }

            data.setImages(sb.toString());
        }
    }

    @Override
    public void postTips() {

        iTipsPresenter.PostTips(data);

    }

    /**
     * 成功返回上一个界面并通知刷新列表
     */
    @Override
    public void postTipsSuccess() {

        Intent intent = new Intent();
        intent.putExtra(StatusInfo.CODE, StatusInfo.SUCCESS);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    private int MAX_COUNT = 300;

    private TextWatcher myTextWatcher = new TextWatcher() {
        private int editStart;

        private int editEnd;

        public void afterTextChanged(Editable s) {
            editStart = etInputTips.getSelectionStart();
            editEnd = etInputTips.getSelectionEnd();

			/* 先去掉监听器，否则会出现栈溢出 */
            tvInputCount.removeTextChangedListener(myTextWatcher);
            /*
             * 注意这里只能每次都对整个EditText的内容求长度，不能对删除的单个字符求长度
			 * 因为是中英文混合，单个字符而言，calculateLength函数都会返回1 当输入字符个数超过限制的大小时，进行截断操作
			 */
            while (calculateLength(s.toString()) > MAX_COUNT) {
                s.delete(editStart - 1, editEnd);
                editStart--;
                editEnd--;
            }

            etInputTips.setSelection(editStart);

			/* 恢复监听器 */
            etInputTips.addTextChangedListener(myTextWatcher);

            setLeftCount();
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }
    };

    private long calculateLength(CharSequence c) {
        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            int tmp = (int) c.charAt(i);
            if (tmp > 0 && tmp < 127) {
                len += 0.5;
            } else {
                len++;
            }
        }
        return Math.round(len);
    }

    private void setLeftCount() {
        tvInputCount.setText("还可以输入"
                + String.valueOf((MAX_COUNT - getInputCount())) + "个字符！");
    }

    private long getInputCount() {
        return calculateLength(etInputTips.getText().toString());
    }

    private final int REQUEST_CODE_GALLERY = 1001;
    private final int MAX_IMAGE_CHOICE = 3;

    private List<String> choiceImagePath;

    /**
     * 弹出选择图片
     */
    private void showChoiceImage() {
        GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, MAX_IMAGE_CHOICE, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {

                choiceImagePath = new ArrayList<String>();

                if (reqeustCode == REQUEST_CODE_GALLERY) {
                    //保存选择的图片的路径
                    for (PhotoInfo info : resultList) {
                        choiceImagePath.add(info.getPhotoPath());
                    }

                    switch (resultList.size()) {
                        case 1:
                            Picasso.with(getContext())
                                    .load(new File(resultList.get(0).getPhotoPath()))
                                    .into(ivSelectOne);
                            break;
                        case 2:
                            Picasso.with(getContext())
                                    .load(new File(resultList.get(0).getPhotoPath()))
                                    .into(ivSelectOne);
                            Picasso.with(getContext())
                                    .load(new File(resultList.get(1).getPhotoPath()))
                                    .into(ivSelectTwo);
                            break;
                        case 3:
                            Picasso.with(getContext())
                                    .load(new File(resultList.get(0).getPhotoPath()))
                                    .into(ivSelectOne);
                            Picasso.with(getContext())
                                    .load(new File(resultList.get(1).getPhotoPath()))
                                    .into(ivSelectTwo);
                            Picasso.with(getContext())
                                    .load(new File(resultList.get(2).getPhotoPath()))
                                    .into(ivSelectThree);
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
}
