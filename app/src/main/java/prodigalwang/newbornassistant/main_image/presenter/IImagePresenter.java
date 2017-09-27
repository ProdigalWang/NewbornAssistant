package prodigalwang.newbornassistant.main_image.presenter;

import android.content.Context;

import java.util.List;

import prodigalwang.newbornassistant.bean.SchoolImage;

/**
 * Created by ProdigalWang on 2016/12/12
 */

public interface IImagePresenter {

    /**
     * 请求图片
     * @param page
     */
    void loadImage(int page);

    /**
     * 浏览图片
     * @param context
     * @param data
     * @param position
     */
    void browseImage(Context context, List<SchoolImage> data,int position);

}
