package prodigalwang.newbornassistant.location.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.Marker;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.app.AppConfig;
import prodigalwang.newbornassistant.bean.SearchInfo;
import prodigalwang.newbornassistant.utils.LogUtil;
import prodigalwang.newbornassistant.utils.SDCardUtils;
import prodigalwang.newbornassistant.utils.ToastUtil;
import prodigalwang.newbornassistant.utils.Urls;

public class NaViPathActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private EditText etStart, etEnd;
    private AppCompatButton btn_search;
    private RadioGroup rgTripMode;
    private ImageView ivVoice;

    private String mSdcardPath=null;
    private static final String APP_FOLDER_NAME="NewBornNav";
    public static final String ROUTE_PLAN_NODE = "routePlanNode";

    private String mSDCardPath = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_na_vi_path);

        this.context = this;
        initSdcardPath();
        initView();
    }

    private void initView() {
        etStart = (EditText) findViewById(R.id.et_start);
        etEnd = (EditText) findViewById(R.id.et_end);
        btn_search = (AppCompatButton) findViewById(R.id.btn_search);
        ivVoice = (ImageView) findViewById(R.id.iv_voice);
        rgTripMode = (RadioGroup) findViewById(R.id.rg_trip_mode);

        btn_search.setOnClickListener(this);
        ivVoice.setOnClickListener(this);
        rgTripMode.setOnClickListener(this);
    }
    private String authinfo = null;

    private void initSdcardPath() {
        if (initDirs()) {
            initNavi();
        }
    }

    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }
    /**
     * 内部TTS播报状态回传handler
     */
    private Handler ttsHandler = new Handler() {
        public void handleMessage(Message msg) {
            int type = msg.what;
            switch (type) {
                case BaiduNaviManager.TTSPlayMsgType.PLAY_START_MSG: {
                    ToastUtil.showShort(context,"Handler : TTS play start");
                    break;
                }
                case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG: {
                    ToastUtil.showShort(context,"Handler : TTS play end");
                    break;
                }
                default :
                    break;
            }
        }
    };

    /**
     * 内部TTS播报状态回调接口
     */
    private BaiduNaviManager.TTSPlayStateListener ttsPlayStateListener = new BaiduNaviManager.TTSPlayStateListener() {

        @Override
        public void playEnd() {
            ToastUtil.showShort(context,"TTSPlayStateListener : TTS play end");
        }

        @Override
        public void playStart() {
            ToastUtil.showShort(context,"TTSPlayStateListener : TTS play start");
        }
    };

    private void initNavi() {

        BNOuterTTSPlayerCallback ttsCallback = null;

        BaiduNaviManager.getInstance().init(this, mSDCardPath, APP_FOLDER_NAME, new BaiduNaviManager.NaviInitListener() {
            @Override
            public void onAuthResult(int status, String msg) {
                if (0 == status) {
                    authinfo = "key校验成功!";
                } else {
                    authinfo = "key校验失败, " + msg;
                }
                NaViPathActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(NaViPathActivity.this, authinfo, Toast.LENGTH_LONG).show();
                    }
                });
            }

            public void initSuccess() {
                Toast.makeText(NaViPathActivity.this, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                initSetting();
            }

            public void initStart() {
                Toast.makeText(NaViPathActivity.this, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
            }

            public void initFailed() {
                Toast.makeText(NaViPathActivity.this, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
            }


        },  null, ttsHandler, ttsPlayStateListener);

    }

    private void initSetting(){
        // 设置是否双屏显示
        BNaviSettingManager.setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
        // 设置导航播报模式
        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
        // 是否开启路况
        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_search:
                getRouteFromNet();
                break;
            case R.id.iv_voice:
                break;
            case R.id.rg_trip_mode:
                break;
        }
    }

    private String inputStart, inputEnd;

    private List<SearchInfo> searchInfoLists;

    private SearchInfo startInfo;
    private SearchInfo endInfo;

    private static final int type_start = 0;
    private static final int type_end = 1;
    private MaterialDialog materialDialog;

    private void getRouteFromNet() {
        if (etStart.getText().toString().isEmpty()) {
            etStart.setError("请输入起点");
            return;
        }
        if (etEnd.getText().toString().isEmpty()) {
            etEnd.setError("请输入终点");
            return;
        }
        inputStart = etStart.getText().toString();
        inputEnd = etEnd.getText().toString();
        String url = Urls.SEARCHSITE_ONE + inputStart + Urls.SEARCHSITE_TWO;
        requestData(url, type_start);
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
     * 请求数据
     *
     * @param url
     * @param type 0代表起点，1代表终点
     */
    private void requestData(String url, final int type) {
        LogUtil.e(url);
        showProgress();
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
                        if (type == type_start) {
                            msg.what = 1001;
                        } else if (type == type_end) {
                            msg.what = 1002;
                        }

                        handler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }


    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            hideProgress();
            switch (msg.what) {
                case 1001:
                    //解析开始:然后把每一个地点信息封装到SearchInfo类中
                    JSONObject object = (JSONObject) msg.obj;
                    parseData(object, type_start);
                    break;
                case 1002:
                    JSONObject object2 = (JSONObject) msg.obj;
                    parseData(object2, type_end);
                    break;
                case 1000:

                    ToastUtil.showShort(context, "获取数据失败");
                    break;
            }

        }

    };

    private void parseData(JSONObject object, int type) {
        try {
            if ((Integer)object.get("status") != 0) {
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
            searchInfoLists=new ArrayList<>();
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

            displayInDialog(type);

        } catch (Exception e) {
            ToastUtil.showShort(context,"解析数据出错");
            e.printStackTrace();
        }
    }

    private void displayInDialog(final int type) {
        if (type == type_start) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setIcon(R.drawable.map_calibration_arrow)
                    .setTitle("请选择起点")
                    .setAdapter(new MyDialogListAdapter(searchInfoLists, context), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startInfo = searchInfoLists.get(which);

                            searchInfoLists.clear();
                            etStart.setText(startInfo.getDesname());
                            String url = Urls.SEARCHSITE_ONE + inputEnd + Urls.SEARCHSITE_TWO;
                            requestData(url, type_end);
                        }
                    }).show();
        } else if (type == type_end) {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setIcon(R.drawable.map_calibration_arrow)
                    .setTitle("请选择目的地")
                    .setAdapter(new MyDialogListAdapter(searchInfoLists, context), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            endInfo=searchInfoLists.get(which);

                            etEnd.setText(endInfo.getDesname());

                            initBNRoutePlan(startInfo,endInfo);
                        }
                    }).show();
        }
    }

    private void initBNRoutePlan(SearchInfo mySInfo,SearchInfo myEndInfo) {
        BNRoutePlanNode startNode=new BNRoutePlanNode(mySInfo.getLongtiude(), mySInfo.getLatitude(), mySInfo.getDesname(), null, BNRoutePlanNode.CoordinateType.BD09LL);//根据得到的起点的信息创建起点节点
        BNRoutePlanNode endNode=new BNRoutePlanNode(myEndInfo.getLongtiude(), myEndInfo.getLatitude(), myEndInfo.getDesname(),null, BNRoutePlanNode.CoordinateType.BD09LL);//根据得到的终点的信息创建终点节点
        if (startNode!=null&&endNode!=null) {
            ArrayList<BNRoutePlanNode>list=new ArrayList<>();
            list.add(startNode);//将起点和终点加入节点集合中
            list.add(endNode);
            BaiduNaviManager.getInstance().launchNavigator(NaViPathActivity.this, list, 1, true, new RoutePlanListener(list));
        }
    }

    public class RoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private ArrayList<BNRoutePlanNode>mList=null;

        public RoutePlanListener(ArrayList<BNRoutePlanNode> list) {
            mList = list;
        }

        @Override
        public void onJumpToNavigator() {
			/*
			 * 设置途径点以及resetEndNode会回调该接口
			 */
            Intent intent=new Intent(NaViPathActivity.this, PathGuideActivity.class);
            intent.putExtra(ROUTE_PLAN_NODE, mList);//将得到所有的节点集合传入到导航的Activity中去
            startActivity(intent);

        }

        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub
            Toast.makeText(context, "算路失败", Toast.LENGTH_SHORT).show();
        }
    }
}
