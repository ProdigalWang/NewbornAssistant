package prodigalwang.newbornassistant.main_tips.model;

import java.util.List;

import prodigalwang.newbornassistant.bean.TipsPost;
import prodigalwang.newbornassistant.main.model.IMainDataModel;

/**
 * Created by ProdigalWang on 2016/12/21.
 */
public interface ITipsModel  extends IMainDataModel{

   void publishPost(TipsPost data,TipsPostCallback tipsPostCallback);


}
