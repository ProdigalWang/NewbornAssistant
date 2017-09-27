package prodigalwang.newbornassistant.bean;

import java.io.Serializable;

/**
 * Created by ProdigalWang on 2016/12/7
 */

public class Entity implements Serializable{

    //内容类型字段,用于在tips中区别当前内容是否包含图片
    private String content_type;

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

}
