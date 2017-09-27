package prodigalwang.newbornassistant.bean;

/**
 * @author ProdigalWang
 * @Time 2016年12月14日 下午2:36:50
 * 类说明
 */
public class SchoolNotice extends Entity {

    private int id;
    private String title;
    private String time;
    private String unit;
    private String detail;
    private String attach_title;
    private String attachment;

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getAttach_title() {
        return attach_title;
    }

    public void setAttach_title(String attach_title) {
        this.attach_title = attach_title;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }


}
