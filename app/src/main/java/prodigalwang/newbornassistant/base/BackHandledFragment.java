package prodigalwang.newbornassistant.base;

import android.os.Bundle;

/**
 * Created by ProdigalWang on 2016/12/31.
 * 如果fragment需要监听返回的监听事件，则继承这个类，但是承载的Activity必须实现BackHandledInterface接口
 */

public abstract class BackHandledFragment extends BaseFragment {

    protected BackHandledInterface mBackHandledInterface;


    /**
     * 所有继承BackHandledFragment的子类都将在这个方法中实现物理Back键按下后的逻辑
     * Activity捕捉到物理返回键点击事件后会首先询问Fragment是否消费该事件
     * 如果没有Fragment消息时FragmentActivity自己才会消费该事件
     */
    protected abstract boolean onBackPressed();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!(getActivity() instanceof BackHandledInterface)) {
            throw new ClassCastException("Hosting Activity must implement BackHandledInterface");
        } else {
            this.mBackHandledInterface = (BackHandledInterface) getActivity();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        //告诉Activity，当前Fragment在栈顶
        mBackHandledInterface.setSelectedFragment(this);
    }


}

