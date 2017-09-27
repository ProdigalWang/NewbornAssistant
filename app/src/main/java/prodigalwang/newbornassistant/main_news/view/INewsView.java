package prodigalwang.newbornassistant.main_news.view;

import java.util.List;

import prodigalwang.newbornassistant.base.IBaseView;
import prodigalwang.newbornassistant.bean.ImageNews;

/**
 * Created by ProdigalWang on 2016/12/8
 */

public interface INewsView<T> extends IBaseView {

    void updateNews(List<T> data);//更新列表数据

    void updateHeaderImageNews(List<ImageNews> data);//更新头部轮播器数据

}
