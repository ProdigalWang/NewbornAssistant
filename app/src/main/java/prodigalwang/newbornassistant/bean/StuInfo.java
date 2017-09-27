package prodigalwang.newbornassistant.bean;

import java.io.Serializable;

/**
 * Created by ProdigalWang on 2017/3/13.
 */

public class StuInfo implements Serializable {

    private String stuid;
    private String stupwd;

    public String getStuid() {
        return stuid;
    }

    public void setStuid(String stuid) {
        this.stuid = stuid;
    }

    public String getStupwd() {
        return stupwd;
    }

    public void setStupwd(String stupwd) {
        this.stupwd = stupwd;
    }


}
