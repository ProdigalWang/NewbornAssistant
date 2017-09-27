package prodigalwang.newbornassistant.main_image.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.List;

import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.base.BaseAdapter;
import prodigalwang.newbornassistant.base.BaseListFragment;
import prodigalwang.newbornassistant.bean.SchoolImage;
import prodigalwang.newbornassistant.login_signup.presenter.login.ILoginPresenter;
import prodigalwang.newbornassistant.login_signup.presenter.login.LoginPresenterImp;
import prodigalwang.newbornassistant.main_image.presenter.IImagePresenter;
import prodigalwang.newbornassistant.main_image.presenter.ImagePresenterImpl;
import prodigalwang.newbornassistant.main_image.view.adapter.ImageAdapter;
import prodigalwang.newbornassistant.utils.LogUtil;
import prodigalwang.newbornassistant.utils.ToastUtil;

/**
 * Created by ProdigalWang on 2016/11/26
 */

public class ImageFragment extends BaseListFragment<SchoolImage> implements IImageView{

    private static final String TAG = "ImageFragment";
    private IImagePresenter iImagePresenter;

    private ILoginPresenter iLoginPresenter;
    @Override
    protected int getLayout() {
        return R.layout.fragment_home_image;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isStaggeredGrid = true;
        hasFloatingActionButton = true;

        iImagePresenter = new ImagePresenterImpl(this);

        iLoginPresenter=new LoginPresenterImp();
    }


    @Override
    protected BaseAdapter getAdapter() {
        return new ImageAdapter(getContext());
    }

    @Override
    protected void requestData() {

        iImagePresenter.loadImage(page);
    }

    @Override
    public void updateImage(List data) {

        loadSuccess();
        if (data != null) {
            baseAdapter.addAllData(data);

        }

    }

    @Override
    public void onItemClick(View view, int position) {

        List<SchoolImage> data = baseAdapter.getAllData();
        iImagePresenter.browseImage(getContext(), data, position);

    }

    @Override
    public boolean onItemLongClick(View view, int position) {
       return false;
    }

    @Override
    protected void initFloatAction(View view) {
        FloatingActionButton takePhoto = (FloatingActionButton) view.findViewById(R.id.action_takePhoto);
        takePhoto.setOnClickListener(this);

        FloatingActionButton sharePhoto = (FloatingActionButton) view.findViewById(R.id.action_share);
        sharePhoto.setOnClickListener(this);
    }

    private void showInfo(){
        if (iLoginPresenter.userHasLogin()){
            ToastUtil.showShort(getContext(),"开发中...");
        }else {
            ToastUtil.showShort(getContext(),"该功能需登录后体验");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.action_takePhoto:
                showInfo();
                break;
            case R.id.action_share:
                showInfo();
                break;
        }
    }
}
