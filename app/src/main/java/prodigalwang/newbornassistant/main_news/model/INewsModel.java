package prodigalwang.newbornassistant.main_news.model;

import java.util.HashMap;

import prodigalwang.newbornassistant.main.model.IMainDataModel;
import prodigalwang.newbornassistant.main.model.LoadDataCallback;

/**
 * Created by ProdigalWang on 2016/12/9
 */

public interface INewsModel extends IMainDataModel{

    void loadImageNews(String url,LoadDataCallback LoadDataCallback);//加载轮播图数据
}
