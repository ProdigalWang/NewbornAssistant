package prodigalwang.newbornassistant.bean;

/**
 * @author ProdigalWang
 * @Time 2016年12月12日 上午10:13:06
 * 图片轮播新闻
 */
public class ImageNews extends Entity{

    private int id;
    private String title;
    private String image;
    private String detail;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

}
