package prodigalwang.newbornassistant.main_image.view;

import java.util.List;

import prodigalwang.newbornassistant.base.IBaseView;

/**
 * Created by ProdigalWang on 2016/12/12
 */

public interface IImageView <T> extends IBaseView{
    void updateImage(List<T> data);
}
