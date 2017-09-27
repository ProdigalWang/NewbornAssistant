package prodigalwang.newbornassistant.main_image.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.app.AppConfig;
import prodigalwang.newbornassistant.base.BaseActivity;
import prodigalwang.newbornassistant.bean.SchoolImage;
import prodigalwang.newbornassistant.main_image.view.adapter.ImageBrowseAdapter;
import prodigalwang.newbornassistant.utils.ToastUtil;
import prodigalwang.newbornassistant.utils.Urls;

/**
 * 图片浏览
 */
public class ImageBrowseActivity extends BaseActivity {

    private List<SchoolImage> data;
    private int position;

    @BindView(R.id.image_browse_vp)
    ViewPager vp;
    @BindView(R.id.image_browse_vp_hint)
    TextView hint;

    private ImageBrowseAdapter imageBrowseAdapter;

    @Override
    protected boolean isSwipeBack() {
        return false;
    }

    @Override
    protected boolean hasLoadingLayout() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_image_browse;
    }


    @Override
    protected void initView() {


        data = (List<SchoolImage>) getIntent().getSerializableExtra("image_data");
        position = getIntent().getIntExtra("position", 0);

        if (imageBrowseAdapter == null) {
            imageBrowseAdapter = new ImageBrowseAdapter(this, data);
            vp.setAdapter(imageBrowseAdapter);
            vp.setCurrentItem(position);
        }
        vp.addOnPageChangeListener(mOnPageChangeListener);

        hint.setText(position + 1 + "/" + data.size());
    }

    @Override
    protected void initData() {
        hideProgress();
    }

    @OnClick({R.id.image_browse_save, R.id.iv_arrow_back})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_browse_save:
                saveImage();
                break;
            case R.id.iv_arrow_back:
                this.finish();
                break;
        }
    }

    private void saveImage() {
        final int nowPoit = vp.getCurrentItem();
        Picasso.with(this).load(Urls.HOST_SCHOOL + data.get(nowPoit).getImage()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                String fileName = data.get(nowPoit).getImage();
                int start = fileName.lastIndexOf("/");

                File file = new File(AppConfig.getInstance().creatImagePath(), fileName.substring(start + 1));
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();
                    ToastUtil.showShort(getApplicationContext(), getString(R.string.save_success));
                } catch (Exception e) {

                    ToastUtil.showShort(getApplicationContext(), getString(R.string.save_fail));
                    e.printStackTrace();
                }

                //添加到图库
                try {
                    MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), file.getAbsolutePath(), fileName, null);
                    // 最后通知图库更新
                    getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getPath())));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            hint.setText(position + 1 + "/" + data.size());
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


}
