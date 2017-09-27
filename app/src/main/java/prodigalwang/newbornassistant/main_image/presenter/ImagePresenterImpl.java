package prodigalwang.newbornassistant.main_image.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;
import java.util.List;

import prodigalwang.newbornassistant.bean.SchoolImage;
import prodigalwang.newbornassistant.bean.SchoolNotice;
import prodigalwang.newbornassistant.bean.User;
import prodigalwang.newbornassistant.cache.CacheManager;
import prodigalwang.newbornassistant.main.model.IMainDataModel;
import prodigalwang.newbornassistant.main.model.LoadDataCallback;
import prodigalwang.newbornassistant.main_image.model.ImageModelImpl;
import prodigalwang.newbornassistant.main_image.view.IImageView;
import prodigalwang.newbornassistant.main_image.view.ImageBrowseActivity;
import prodigalwang.newbornassistant.utils.NetUtil;
import prodigalwang.newbornassistant.utils.StatusInfo;
import prodigalwang.newbornassistant.utils.ToastUtil;
import prodigalwang.newbornassistant.utils.Urls;

/**
 * Created by ProdigalWang on 2016/12/12
 */

public class ImagePresenterImpl implements IImagePresenter {

    protected IMainDataModel iMainDataModel;
    protected IImageView iImageView;

    public ImagePresenterImpl(IImageView iImageView) {
        this.iImageView = iImageView;
        iMainDataModel = new ImageModelImpl();
    }

    @Override
    public void loadImage(int page) {
        if (NetUtil.isConnected()) {
            String url = Urls.HOME_SCHOOL_IMAGE + "?currentPage=" + page;

            iMainDataModel.loadData(url, new LoadDataCallback() {
                @Override
                public void success(List data) {

                    iImageView.hideProgress();
                    iImageView.updateImage(data);
                }

                @Override
                public void fail(String msg) {

                    iImageView.hideProgress();
                    iImageView.showFailMsg(msg);
                }
            });
        } else {

            if (CacheManager.isExistDataCache(iMainDataModel.getCachePath(page))) {
                List<SchoolNotice> data = iMainDataModel.readCache(iMainDataModel.getCachePath(page));

                iImageView.showFailMsg(StatusInfo.HAS_CACHE_NONETWORK);
                iImageView.hideProgress();
                iImageView.updateImage(data);
            } else {
                iImageView.showFailMsg(StatusInfo.NONETWORK);
            }

        }

    }

    @Override
    public void browseImage(Context context, List<SchoolImage> data, int position) {

        if (data!=null&&data.size()>0){
            Intent intent=new Intent(context, ImageBrowseActivity.class);
            intent.putExtra("image_data",(Serializable) data);
            intent.putExtra("position",position);

            context.startActivity(intent);
        }else {
            ToastUtil.showShort(context,"获取图片详情失败");
        }


    }

}
