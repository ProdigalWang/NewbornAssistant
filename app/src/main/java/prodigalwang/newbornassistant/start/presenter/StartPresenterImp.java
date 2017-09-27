package prodigalwang.newbornassistant.start.presenter;

import android.content.Context;

import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.start.model.IStartModel;
import prodigalwang.newbornassistant.start.model.LocalImageCallback;
import prodigalwang.newbornassistant.start.model.StartModelImp;
import prodigalwang.newbornassistant.start.view.IStartView;
import prodigalwang.newbornassistant.utils.NetUtil;

/**
 * Created by ProdigalWang on 2017/3/6.
 */

public class StartPresenterImp implements IStartPresenter {

    private IStartModel iStartModel;
    private IStartView iStartView;

    public StartPresenterImp(IStartView iStartView) {
        this.iStartView = iStartView;
        this.iStartModel = new StartModelImp();
    }

    @Override
    public void getData() {
        if (NetUtil.isConnected()) {

            countDown(3000);

            iStartModel.requsetData(new LocalImageCallback() {
                @Override
                public void success(String url) {
                    iStartView.showStartInfo(url);
                }

                @Override
                public void fail(String msg) {

                }
            });
        } else {

            //当前无网络，可以提示用户打开设置，或者直接进入主界面。
            countDown(1000);
        }

    }

    /**
     * 倒计时
     *
     * @param time 隔几秒进入主界面
     */
    private void countDown(int time) {
        Timer timer = new Timer(true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                iStartModel.cancelRequest();
                iStartView.goMainActivity();
            }
        };
        //time秒之后执行取消请求数据操作，跳转到主界面
        timer.schedule(timerTask, time);
    }
}
