package prodigalwang.newbornassistant.main.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.main_image.view.ImageFragment;
import prodigalwang.newbornassistant.main_news.view.NewsFragment;
import prodigalwang.newbornassistant.main_notice.view.NoticeFragment;
import prodigalwang.newbornassistant.main_tips.view.TipsFragment;

/**
 * Created by ProdigalWang on 2016/11/26
 */

public class MainFragment extends Fragment {

    private TabLayout mTablayout;
    private ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container,false);
        mTablayout = (TabLayout) view.findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) view.findViewById(R.id.main_viewpager);

        //mViewPager.setOffscreenPageLimit(3);//viewpager预加载的数量
        setupViewPager(mViewPager);//初始化ViewPager

        mTablayout.addTab(mTablayout.newTab().setText(R.string.news));
        mTablayout.addTab(mTablayout.newTab().setText(R.string.tips));
        mTablayout.addTab(mTablayout.newTab().setText(R.string.image));
        mTablayout.addTab(mTablayout.newTab().setText(R.string.notice));
        mTablayout.setupWithViewPager(mViewPager);//Tablayout和ViewPager相结合
        return view;
    }

    private void setupViewPager(ViewPager mViewPager) {
        //Fragment中嵌套使用Fragment一定要使用getChildFragmentManager(),否则会有问题
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new NewsFragment(), getString(R.string.news));
        adapter.addFragment(new TipsFragment(), getString(R.string.tips));
        adapter.addFragment(new ImageFragment(), getString(R.string.image));

        adapter.addFragment(new NoticeFragment(), getString(R.string.notice));
        mViewPager.setAdapter(adapter);
    }


}
