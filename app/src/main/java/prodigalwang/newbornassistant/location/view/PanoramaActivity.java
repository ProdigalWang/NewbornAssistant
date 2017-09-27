package prodigalwang.newbornassistant.location.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.lbsapi.BMapManager;
import com.baidu.lbsapi.MKGeneralListener;
import com.baidu.lbsapi.model.BaiduPanoData;
import com.baidu.lbsapi.panoramaview.PanoramaRequest;
import com.baidu.lbsapi.panoramaview.PanoramaView;

import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.utils.LogUtil;
import prodigalwang.newbornassistant.utils.ToastUtil;

public class PanoramaActivity extends AppCompatActivity {

    private PanoramaView panoramaView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panorama);

        panoramaView = (PanoramaView) findViewById(R.id.panorama);
        setPanoramaView();
    }


    private void setPanoramaView() {
        double[] lats = getIntent().getDoubleArrayExtra("panoramaLatLng");

        panoramaView.setPanorama(lats[0], lats[1]);//经度，纬度
        // 根据枚举类ImageDefinition来设置清晰级别
        //较低清晰度 ImageDefinationLow
        //中等清晰度 ImageDefinationMiddle
        //较高清晰度 ImageDefinationHigh
        panoramaView.setPanoramaImageLevel(PanoramaView.ImageDefinition.ImageDefinitionMiddle);


    }

    @Override
    protected void onPause() {
        super.onPause();
        panoramaView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        panoramaView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        panoramaView.destroy();
    }
}
