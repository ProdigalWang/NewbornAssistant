package prodigalwang.newbornassistant.bean;

import java.util.List;

/**
 * @param <T>
 * @author ProdigalWang
 * @Time 2016年6月29日 下午9:51:38
 * 封装分页的参数
 */
public class PageInfo<T> {

    // 当前页, 默认显示第一页
    private int currentPage = 1;
    // 每页显示的行数(查询返回的行数)
    private int pageCount;
    // 总记录数
    private int totalCount;
    // 总页数 = 总记录数 / 每页显示的行数  (+ 1)
    private int totalPage;
    // 分页查询到的数据
    private List<T> pageData;

    //传入每页显示的页数
    public PageInfo(int pageCount) {
        this.pageCount = pageCount;
    }

    // 返回总页数
    public int getTotalPage() {
        if (totalCount % pageCount == 0) {
            totalPage = totalCount / pageCount;
        } else {
            totalPage = totalCount / pageCount + 1;
        }
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getPageData() {
        return pageData;
    }

    public void setPageData(List<T> pageData) {
        this.pageData = pageData;
    }
}
