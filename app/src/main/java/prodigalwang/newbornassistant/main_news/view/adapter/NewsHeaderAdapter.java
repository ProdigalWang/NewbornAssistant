package prodigalwang.newbornassistant.main_news.view.adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.bean.ImageNews;
import prodigalwang.newbornassistant.main_news.view.NewsDetailActivity;
import prodigalwang.newbornassistant.utils.StatusInfo;
import prodigalwang.newbornassistant.utils.Urls;

/**
 * Created by ProdigalWang on 2016/12/12
 *
 * 首页新闻头部图片轮播的Viewpager适配器
 */

public class NewsHeaderAdapter extends LoopPagerAdapter{

    protected List<ImageNews> imageNewsList;

    public NewsHeaderAdapter(RollPagerView viewPager) {
        super(viewPager);
    }

    public void addAllData(List<ImageNews> data){
        imageNewsList=data;
    }


    @Override
    public View getView(final ViewGroup container, final int position) {

        ImageView view = new ImageView(container.getContext());

//        LogUtil.e("请求图片地址："+Urls.HOST_SCHOOL+imageNewsList.get(position).getImage());

        Picasso.with(container.getContext()).load(Urls.HOST_SCHOOL+imageNewsList.get(position).getImage())
                .placeholder(R.drawable.ic_nopic).error(R.drawable.ic_nopic)
                .into(view);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        view.setOnClickListener(new View.OnClickListener()      // 点击事件
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(container.getContext(), NewsDetailActivity.class);
                intent.putExtra("type", StatusInfo.HOME_IMAGE_NEWS);
                intent.putExtra("imagenews", imageNewsList.get(position));

                 container.getContext().startActivity(intent);
            }

        });

        return view;
    }

    @Override
    public int getRealCount() {
        if (imageNewsList!=null){
            return imageNewsList.size();
        }
        return 0;
    }
}
