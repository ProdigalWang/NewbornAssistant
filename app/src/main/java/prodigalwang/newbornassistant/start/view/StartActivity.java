package prodigalwang.newbornassistant.start.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.main.view.MainActivity;
import prodigalwang.newbornassistant.start.presenter.IStartPresenter;
import prodigalwang.newbornassistant.start.presenter.StartPresenterImp;
import prodigalwang.newbornassistant.utils.LogUtil;

public class StartActivity extends AppCompatActivity implements IStartView {

    private IStartPresenter iStartPresenter;

    private ImageView startImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        startImage = (ImageView) findViewById(R.id.iv_start);

        iStartPresenter = new StartPresenterImp(StartActivity.this);
        iStartPresenter.getData();
    }


    @Override
    public void showStartInfo(String url) {

        LogUtil.e("start image url:" + url);

        Picasso.with(this).load(url)
                .placeholder(R.drawable.iv_start).error(R.drawable.iv_start)
                .into(startImage);
    }

    @Override
    public void goMainActivity() {
        this.startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }
}
