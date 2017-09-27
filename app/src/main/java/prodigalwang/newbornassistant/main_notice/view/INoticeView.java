package prodigalwang.newbornassistant.main_notice.view;

import java.util.List;

import prodigalwang.newbornassistant.base.IBaseView;

/**
 * Created by ProdigalWang on 2016/12/14
 */

public interface INoticeView<T> extends IBaseView{

    void updateNotice(List<T> data);
}
