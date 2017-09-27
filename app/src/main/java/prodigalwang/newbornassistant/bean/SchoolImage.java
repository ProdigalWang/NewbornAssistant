package prodigalwang.newbornassistant.bean;

/**
 * @author ProdigalWang
 * @Time 2016年12月12日 下午8:55:18
 * 类说明
 */
public class SchoolImage extends Entity {

    private int id;
    private String image;
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
