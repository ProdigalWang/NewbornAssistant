package prodigalwang.newbornassistant.main_news.presenter;

/**
 * Created by ProdigalWang on 2016/12/9
 */

public interface INewsPresenter {
    /**
     * 加载新闻
     * @param page 当前页数
     */
    void loadNews(int page);

    /**
     * 加载轮播图
     */
    void loadImageNews();

    /**
     * 加载新闻详情
      * @param type  是item还是轮播图
      * @param htmlId 请求文件的id
     */
    void loadNewsDatil(int type, String htmlId);
}
