package prodigalwang.newbornassistant.main_image.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.bean.SchoolImage;
import prodigalwang.newbornassistant.utils.Urls;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageBrowseAdapter extends PagerAdapter {
    private Context context;
    private List<SchoolImage> data;
    private SparseArray<View> cacheView;//缓冲


    public ImageBrowseAdapter(Context context, List<SchoolImage> data) {
        this.context = context;
        this.data = data;
        cacheView = new SparseArray<>(data.size());
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {

        View view = cacheView.get(position);

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_browse_image, container, false);
            view.setTag(position);
            final ImageView image = (ImageView) view.findViewById(R.id.image_browse_iv);
            final PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(image);

            Picasso.with(context).load(Urls.HOST_SCHOOL + data.get(position).getImage()).into(image, new Callback() {
                @Override
                public void onSuccess() {
                    photoViewAttacher.update();
                }

                @Override
                public void onError() {

                }
            });

            photoViewAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    Activity activity = (Activity) context;
                    activity.finish();
                }
            });
            cacheView.put(position, view);
        }
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }


}
