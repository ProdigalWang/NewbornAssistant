package prodigalwang.newbornassistant.location.view;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.baidu.lbsapi.model.BaiduPanoData;
import com.baidu.lbsapi.panoramaview.PanoramaRequest;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.app.MyApplication;
import prodigalwang.newbornassistant.bean.SearchInfo;
import prodigalwang.newbornassistant.utils.LogUtil;
import prodigalwang.newbornassistant.utils.ToastUtil;
import prodigalwang.newbornassistant.utils.Urls;

public class LocationActivity extends AppCompatActivity implements View.OnClickListener, BaiduMap.OnMapClickListener {

    private LocationService locationService;
    private MaterialDialog materialDialog;

    private Context context;
    private MapView myMapView;
    private BaiduMap myBaiduMap;
    private float current;
    private String[] types = {"普通地图", "卫星地图", "热力地图(已关闭)"};
    private ImageView mapRoad, selectMapType, mapPanorama;
    private ImageView addScale, lowScale;
    private ImageView myLoaction;
    private ImageView selectLocationMode;
    private TextView locationText;
    //定位相关
    private String[] LocationModeString = {"罗盘模式", "普通模式", "跟随模式", "3D俯视模式(已关闭)"};
    private boolean isFirstIn = true;
    private double myLatitude, myLongtitude;//"我所处的经纬度"
    private double goLatitude, goLongtitude;//搜索位置得到的目标经纬度
    private String locationTextString;//定义的位置的信息
    private BitmapDescriptor myBitmapLocation;
    private MyOrientationListener myOrientationListener;
    private float myCurrentX;

    //搜索
    private EditText searchEdit;
    private ImageView okToSearch;
    private List<SearchInfo> searchInfoLists;
    //全景
    private String uid;
    //线路导航
    private ImageButton startGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        this.context = this;
        showProgress();
        initMapView();
        changeDefaultBaiduMapView();
        initSearchDestination();
    }

    private void showProgress() {
        if (materialDialog == null) {
            materialDialog = new MaterialDialog.Builder(context)
                    .content("请稍后...")
                    .progress(true, 0)
                    .progressIndeterminateStyle(false)
                    .cancelable(false)
                    .show();
        } else {
            materialDialog.show();
        }
    }

    private void hideProgress() {
        if (materialDialog != null) {
            materialDialog.dismiss();
        }
    }

    /**
     * 初始化地图
     */
    private void initMapView() {
        myMapView = (MapView) findViewById(R.id.mymap_view);
        myBaiduMap = myMapView.getMap();
        myBaiduMap.setMyLocationEnabled(true);

        mapRoad = (ImageView) findViewById(R.id.road_condition);
        selectMapType = (ImageView) findViewById(R.id.map_type);
        addScale = (ImageView) findViewById(R.id.add_scale);
        lowScale = (ImageView) findViewById(R.id.low_scale);
        myLoaction = (ImageView) findViewById(R.id.my_location);
        locationText = (TextView) findViewById(R.id.mylocation_text);
        selectLocationMode = (ImageView) findViewById(R.id.map_location);
        mapPanorama = (ImageView) findViewById(R.id.map_panorama);
        startGo = (ImageButton) findViewById(R.id.start_go);

        mapRoad.setOnClickListener(this);
        selectMapType.setOnClickListener(this);
        addScale.setOnClickListener(this);
        lowScale.setOnClickListener(this);
        myLoaction.setOnClickListener(this);
        selectLocationMode.setOnClickListener(this);
        mapPanorama.setOnClickListener(this);
        startGo.setOnClickListener(this);
    }

    /**
     * 修改默认百度地图的View
     */
    private void changeDefaultBaiduMapView() {

        // 不显示地图上比例尺
        myMapView.showScaleControl(false);

        // 不显示地图缩放控件（按钮控制栏）
        myMapView.showZoomControls(false);
    }

    //当Activity调用onStart方法，开启定位以及开启方向传感器，即将定位的服务、方向传感器和Activity生命周期绑定在一起
    @Override
    protected void onStart() {
        super.onStart();

        locationService = ((MyApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例
        locationService.registerListener(myLocationListener);

        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();
        useLocationOrientationListener();//调用方向传感器
    }

    /**
     * 定位结合方向传感器，从而可以实时监测到X轴坐标的变化，从而就可以检测到
     * 定位图标方向变化，只需要将这个动态变化的X轴的坐标更新myCurrentX值，
     * 最后在MyLocationData data.driection(myCurrentX);
     */
    private void useLocationOrientationListener() {
        myOrientationListener = new MyOrientationListener(context);
        myOrientationListener.setMyOrientationListener(new MyOrientationListener.onOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {//监听方向的改变，方向改变时，需要得到地图上方向图标的位置
                myCurrentX = x;

                LogUtil.e("x====" + x);
            }
        });
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //在Activity执行onResume是执行MapView(地图)生命周期管理
        myMapView.onResume();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        myMapView.onPause();
    }

    @Override
    protected void onStop() {//当Activity调用onStop方法，关闭定位以及关闭方向传感器
        super.onStop();
        myBaiduMap.setMyLocationEnabled(false);
        locationService.unregisterListener(myLocationListener); //注销掉监听
        myOrientationListener.stop();//关闭方向传感器
        locationService.stop(); //停止定位服务

    }

    protected void onDestroy() {
        super.onDestroy();
        //在Activity执行onDestory时执行mapView(地图)生命周期管理
        myMapView.onDestroy();
    }


    /**
     * 获得最新定位的位置,并且地图的中心点设置为我的位置
     */
    private void getMyLatestLocation(double lat, double lng) {
        //创建一个经纬度对象，需要传入当前的经度和纬度两个整型值参数
        LatLng latLng = new LatLng(lat, lng);
        //创建一个地图最新更新的状态对象，需要传入一个最新经纬度对象
        MapStatus mapStatus = new MapStatus.Builder()
                .zoom(16f)
                .target(latLng)
                .build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);

        //表示使用动画的效果传入，通过传入一个地图更新状态对象，然后利用百度地图对象来展现和还原那个地图更新状态，即此时的地图显示就为你现在的位置
        myBaiduMap.animateMapStatus(mapStatusUpdate);


    }

    private BDLocationListener myLocationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {

            myLatitude = bdLocation.getLatitude();//得到当前的纬度
            myLongtitude = bdLocation.getLongitude();//得到当前的经度
            //得到一个MyLocationData对象，需要将BDLocation对象转换成MyLocationData对象
            MyLocationData data = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())//精度半径
                    .direction(myCurrentX)//方向
                    .latitude(bdLocation.getLatitude())//经度
                    .longitude(bdLocation.getLongitude())//纬度
                    .build();
            myBaiduMap.setMyLocationData(data);
            //配置自定义的定位图标,需要在紧接着setMyLocationData后面设置
            if (isFirstIn) {//表示用户第一次打开，就定位到用户当前位置，即此时只要将地图的中心点设置为用户此时的位置即可

                changeLocationIcon();

                getMyLatestLocation(myLatitude, myLongtitude);//获得最新定位的位置,并且地图的中心点设置为我的位置
                locationTextString = bdLocation.getAddrStr();

                hideProgress();

                locationText.setText("您现在的位置：" + locationTextString);
                isFirstIn = false;//表示第一次才会去定位到中心点
                goLatitude = myLatitude;
                goLongtitude = myLongtitude;//先设置为自己的位置
            }
        }

    };

    /**
     * 把定位图标更换为自己的
     */
    private void changeLocationIcon() {
        myBitmapLocation = BitmapDescriptorFactory
                .fromResource(R.drawable.map_location_marker);//引入自己的图标

        MyLocationConfiguration config = new
                MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, myBitmapLocation);
        myBaiduMap.setMyLocationConfigeration(config);

    }

    /**
     * 搜索相关
     */
    private void initSearchDestination() {
        searchEdit = (EditText) findViewById(R.id.search_panorama);
        okToSearch = (ImageView) findViewById(R.id.ok_to_search);
        okToSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                search();
                // ThreadPoolUtil.getThreadpool().execute(new searchThread());
            }
        });
    }

    private String inputSearchSite;

    private void search() {
        inputSearchSite = searchEdit.getText().toString();
        if (inputSearchSite.isEmpty()) {
            ToastUtil.showShort(context, "请输入搜索信息");
            return;
        }
        String url = Urls.SEARCHSITE_ONE + inputSearchSite + Urls.SEARCHSITE_TWO;
        showProgress();
        requestData(url);
    }

    private void requestData(String url) {
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

                handler.sendEmptyMessage(1000);
            }

            @Override
            public void onResponse(String response, int id) {
                if (response != null) {

                    try {
                        Message msg = new Message();
                        msg.obj = new JSONObject(response);
                        msg.what = 1001;
                        handler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    /**
     * 搜索网络请求API得到的JSON数据
     * 利用子线程请求中得到的网络数据，利用Handler来更新
     */
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            hideProgress();
            switch (msg.what) {
                case 1001:
                    try {
                        //解析开始:然后把每一个地点信息封装到SearchInfo类中
                        JSONObject object = (JSONObject) msg.obj;
                        if ((Integer) object.get("status") != 0) {
                            ToastUtil.showShort(context, "请求数据错误");
                            return;
                        }
                        if (!object.has("results")) {
                            return;
                        }
                        JSONArray array = object.getJSONArray("results");
                        if (array.length() < 1) {
                            ToastUtil.showShort(context, "未查询到相关数据");
                            return;
                        }

                        double lat = 0;
                        double lng = 0;
                        String name = "";
                        String uids = "";
                        String address = "";
                        String streetIds = "";
                        searchInfoLists = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject joObject = array.getJSONObject(i);
                            if (joObject.has("name"))
                                name = joObject.getString("name");
                            if (joObject.has("location")) {
                                JSONObject object2 = joObject.getJSONObject("location");
                                lat = object2.getDouble("lat");
                                lng = object2.getDouble("lng");
                            }
                            if (joObject.has("uid"))
                                uids = joObject.getString("uid");

                            if (joObject.has("address"))
                                address = joObject.getString("address");
                            if (joObject.has("street_id"))
                                streetIds = joObject.getString("street_id");

                            SearchInfo mInfo = new SearchInfo(name, lat, lng, address, streetIds, uids);
                            searchInfoLists.add(mInfo);

                        }

                        displayInDialog();

                    } catch (Exception e) {
                        ToastUtil.showShort(context, "解析数据出错");
                        e.printStackTrace();
                    }
                    break;
                case 1000:

                    ToastUtil.showShort(context, "获取数据失败");
                    break;
            }

        }

    };

    /**
     * 显示搜索后信息的自定义列表项对话框，以及对话框点击事件的处理
     */
    private void displayInDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.map_calibration_arrow)
                .setTitle("请选择你查询到的地点")
                .setAdapter(new MyDialogListAdapter(searchInfoLists, context), new DialogInterface.OnClickListener() {

                    //点击地图上搜索的图标进入全景
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SearchInfo mInfos = searchInfoLists.get(which);
                      //把用户选择的搜索到的位置的经纬度保存
                        goLongtitude = mInfos.getLongtiude();
                        goLatitude = mInfos.getLatitude();
                        // TODO: uid = mInfos.getUid();内景id,未实现
                        myBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker PnoramaMark) {

                                goPanoramaActivity(goLongtitude, goLatitude);
                                return true;
                            }
                        });
                        addPnoramaLayout(mInfos);//
                    }
                }).show();

    }

    /**
     * @author mikyou
     * 添加全景覆盖物，即全景的图标，迅速定位到该地点在地图上的位置
     */
    public void addPnoramaLayout(SearchInfo mInfos) {
        myBaiduMap.clear();
        LatLng latLng = new LatLng(mInfos.getLatitude(), mInfos.getLongtiude());
        OverlayOptions options;
        BitmapDescriptor mPnoramaIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_mapmark_red);
        options = new MarkerOptions().position(latLng).icon(mPnoramaIcon).zIndex(6);
        Marker pnoramaMarker = (Marker) myBaiduMap.addOverlay(options);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        myBaiduMap.animateMapStatus(msu);
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.road_condition:
                if (myBaiduMap.isTrafficEnabled()) {
                    myBaiduMap.setTrafficEnabled(false);
                    mapRoad.setImageResource(R.drawable.map_roadcondition_off);
                    ToastUtil.showShort(context, "已关闭路况显示");
                } else {
                    myBaiduMap.setTrafficEnabled(true);
                    mapRoad.setImageResource(R.drawable.map_roadcondition_on);
                    ToastUtil.showShort(context, "已开启路况显示");
                }
                break;
            case R.id.map_type:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("请选择地图的类型")
                        .setItems(types, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String select = types[which];
                                if (select.equals("普通地图")) {
                                    myBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                                } else if (select.equals("卫星地图")) {
                                    myBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                                } else if (select.equals("热力地图(已关闭)") || select.equals("热力地图(已打开)")) {
                                    if (myBaiduMap.isBaiduHeatMapEnabled()) {
                                        myBaiduMap.setBaiduHeatMapEnabled(false);
                                        ToastUtil.showShort(LocationActivity.this, "热力地图已关闭");
                                        types[which] = "热力地图(已关闭)";
                                    } else {
                                        myBaiduMap.setBaiduHeatMapEnabled(true);
                                        ToastUtil.showShort(LocationActivity.this, "热力地图已打开");

                                        types[which] = "热力地图(已打开)";
                                    }
                                }
                            }
                        }).show();
                break;
            case R.id.add_scale:
                current += 0.5f;
                MapStatusUpdate
                        factory = MapStatusUpdateFactory.zoomTo(16f + current);
                myBaiduMap.animateMapStatus(factory);//设置百度地图的缩放效果动画animateMapStatus
                break;
            case R.id.low_scale:
                current -= 0.5f;
                MapStatusUpdate
                        factory2 = MapStatusUpdateFactory.zoomTo(16f + current);
                myBaiduMap.animateMapStatus(factory2);
                break;
            case R.id.my_location://定位功能，需要用到LocationClient进行定位
                //BDLocationListener
                getMyLatestLocation(myLatitude, myLongtitude);//获取最新的位置
                break;
            case R.id.map_location:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setIcon(R.drawable.track_collect_running)
                        .setTitle("请选择定位的模式")
                        .setItems(LocationModeString, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String mode = LocationModeString[which];
                                if (mode.equals("罗盘模式")) {
                                    MyLocationConfiguration config = new
                                            MyLocationConfiguration(MyLocationConfiguration.LocationMode.COMPASS, true, myBitmapLocation);
                                    myBaiduMap.setMyLocationConfigeration(config);
                                } else if (mode.equals("跟随模式")) {
                                    MyLocationConfiguration config = new
                                            MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, myBitmapLocation);
                                    myBaiduMap.setMyLocationConfigeration(config);
                                } else if (mode.equals("普通模式")) {
                                    MyLocationConfiguration config = new
                                            MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, myBitmapLocation);
                                    myBaiduMap.setMyLocationConfigeration(config);
                                } else if (mode.equals("3D俯视模式(已关闭)") || mode.equals("3D俯视模式(已打开)")) {
                                    if (mode.equals("3D俯视模式(已打开)")) {
                                        UiSettings mUiSettings = myBaiduMap.getUiSettings();
                                        mUiSettings.setCompassEnabled(true);
                                        LocationModeString[which] = "3D俯视模式(已关闭)";
                                        ToastUtil.showShort(LocationActivity.this, "3D模式已关闭");

                                    } else {
                                        MyLocationConfiguration config = new
                                                MyLocationConfiguration(MyLocationConfiguration.LocationMode.COMPASS, true, myBitmapLocation);
                                        myBaiduMap.setMyLocationConfigeration(config);
                                        MyLocationConfiguration config2 = new
                                                MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, myBitmapLocation);
                                        myBaiduMap.setMyLocationConfigeration(config2);
                                        LocationModeString[which] = "3D俯视模式(已打开)";
                                        ToastUtil.showShort(LocationActivity.this, "3D模式已打开");

                                    }
                                }
                            }
                        }).show();
                break;
            case R.id.map_panorama:
                goPanoramaActivity(goLongtitude, goLatitude);
                break;
            case R.id.start_go:
                Intent intent2 = new Intent(LocationActivity.this, NaViPathActivity.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }


    /**
     * 查看全景图，先判断是否有全景图
     *
     * @param longtitude 经度
     * @param latitude   纬度
     */
    private void goPanoramaActivity(double longtitude, double latitude) {

        PanoramaRequest panoramaRequest = PanoramaRequest.getInstance(context);
        BaiduPanoData locationPanoData = panoramaRequest
                .getPanoramaInfoByLatLon(longtitude, latitude);
        //判断是否有外景(街景)
        boolean hasStreetPano = locationPanoData.hasStreetPano();

        if (hasStreetPano) {
            Intent intent = new Intent(context, PanoramaActivity.class);
            intent.putExtra("panoramaLatLng", new double[]{longtitude, latitude});
            startActivity(intent);
        } else {
            ToastUtil.showShort(context, "当前地点无全景图");
        }

    }

    /**
     * @author zhongqihong
     * 给整个地图添加的点击事件
     */
    @Override
    public void onMapClick(LatLng arg0) {//表示点击地图其他的地方使得覆盖物的详情介绍的布局隐藏，但是点击已显示的覆盖物详情布局上，则不会消失，因为在详情布局上添加了Clickable=true
        //由于事件的传播机制，因为点击事件首先会在覆盖物布局的父布局(map)中,由于map是可以点击的，map则会把点击事件给消费掉，如果加上Clickable=true表示点击事件由详情布局自己处理，不由map来消费
        //markLayout.setVisibility(View.GONE);
        myBaiduMap.hideInfoWindow();//隐藏InfoWindow
    }

    @Override
    public boolean onMapPoiClick(MapPoi arg0) {
        // TODO Auto-generated method stub
        return false;
    }

}
