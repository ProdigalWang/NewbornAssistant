package prodigalwang.newbornassistant.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.weavey.loading.lib.LoadingLayout;

import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.bean.Entity;
import prodigalwang.newbornassistant.utils.LogUtil;
import prodigalwang.newbornassistant.utils.StatusInfo;
import prodigalwang.newbornassistant.utils.ToastUtil;

/**
 * Created by ProdigalWang on 2016/12/7
 * 所有带下拉刷新，上拉加载更多的Fragment基类。包括列表类，瀑布流
 * 实现的接口包括列表的下拉刷新，item的点击和长按监听，view的点击事件，IBaseView
 */

public abstract class BaseListFragment<T extends Entity> extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener, BaseAdapter.OnItemClickLitener, IBaseView,View.OnClickListener {

    private static final String TAG = "BaseListFragment";

    //列表页数，默认第一页
    protected int page = 1;
    //当前是否正在加载更多
    protected boolean isLoading = false;

    //是不是瀑布流
    protected boolean isStaggeredGrid = false;
    //回馈信息展示(无网络，错误...)
    protected LoadingLayout loadingLayout;

    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected RecyclerView mRecyclerView;
    //线性列表Manager
    protected LinearLayoutManager mLayoutManager;
    //瀑布流
    protected StaggeredGridLayoutManager staggeredGridLayoutManager;

    //页面是否有浮动按钮
    protected boolean hasFloatingActionButton = false;

    //Adapter
    protected BaseAdapter<T> baseAdapter;

    //标志位，标志已经初始化完成
    protected boolean isPrepared = false;

    //是否已被加载过一次，第二次就不再去请求数据了
    protected boolean hasLoadedOnce = false;

    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    //可见
    protected void onVisible() {
        lazyLoad();
    }

    //不可见
    protected void onInvisible() {
    }

    protected int getLayout() {
        return R.layout.base_swiperefreshlayout;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(getLayout(), container, false);
        initView(view);

        isPrepared = true;
        lazyLoad();
        return view;

    }

    /**
     * 初始化基本布局
     *
     * @param view
     */
    protected void initView(View view) {
        loadingLayout = (LoadingLayout) view.findViewById(R.id.loading);
        if (!hasLoadedOnce) {
            loadingLayout.setStatus(LoadingLayout.Loading);
        } else {
            loadingLayout.setStatus(LoadingLayout.Success);
        }

        loadingLayout.setOnReloadListener(onReloadListener);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.main_SwipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.primary,
                R.color.primary_dark, R.color.primary_light,
                R.color.accent);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.main_RecyclerView);
        mRecyclerView.setHasFixedSize(true);

        if (!isStaggeredGrid) {
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
        } else {
            //设置为瀑布流布局
            staggeredGridLayoutManager = new StaggeredGridLayoutManager
                    (2, StaggeredGridLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(staggeredGridLayoutManager);

        }

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        //是否包含悬浮按钮
        if (hasFloatingActionButton) {

            initFloatAction(view);
        }

        //设置适配器
        if (baseAdapter != null) {
            mRecyclerView.setAdapter(baseAdapter);
        } else {

            baseAdapter = getAdapter();
            mRecyclerView.setAdapter(baseAdapter);
        }

        //TODO 未解决懒加载后会再次初始化 动态添加的头布局导致第一次请求的状态不能保存
        initHeaderView();

        baseAdapter.setOnItemClickLitener(this);
    }

    /**
     * 执行懒加载
     */
    protected void lazyLoad() {
        LogUtil.d("lazyLoad executing:isPrepared==" + isPrepared + "==hasLoadedOnce==" + hasLoadedOnce);
        if (!isPrepared || !isVisible || hasLoadedOnce) {
            return;
        }

        if (requestDataIfViewCreated()) {

            onRefresh();//加载第一次数据
        }
    }

    /**
     * 加载失败时 重新加载 的点击监听
     */
    protected LoadingLayout.OnReloadListener onReloadListener = new LoadingLayout.OnReloadListener() {
        @Override
        public void onReload(View v) {

            onRefresh();
        }
    };
    /**
     * RecyclerView的滑动事件
     */
    protected RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

        private int lastVisibleItem;

        //当且仅当滑动到最后一项并且手指上拉抛出时才执行上拉加载更多效果
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                    lastVisibleItem + 1 == baseAdapter.getItemCount()) {

                //这两个判断是为了解决 同时下拉刷新和上拉加载更多产生的冲突。
                boolean isRefreshing = mSwipeRefreshLayout.isRefreshing();
                if (isRefreshing) {
                    //如果正在下拉刷新则不显示最后的下拉加载
                    baseAdapter.notifyItemRemoved(baseAdapter.getItemCount());
                    return;
                }
                if (!isLoading) {
                    //请求更多数据，展示加载更多view
                    isLoading = true;
                    requestData();

                }
            }
        }

        private int[] lastPositions;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            //判断当前布局类型
            if (mRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            } else if (mRecyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {

                if (lastPositions == null) {
                    lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                }
                staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                lastVisibleItem = findMax(lastPositions);

            }


        }
    };

    protected int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    /**
     * SwipeRefreshLayout的下拉刷新事件
     */
    @Override
    public void onRefresh() {

        page = 1;
        baseAdapter.clearData();

        requestData();
        requestHeaderData();

        LogUtil.d("refresh is executing...");
    }

    /**
     * 初始化头布局，子类实现
     */
    protected void initHeaderView() {

    }

    /**
     * 请求头布局数据，子类需求去实现
     */
    protected void requestHeaderData() {

    }

    /**
     * @return 由子类返回具体的Adapter
     */
    protected abstract BaseAdapter<T> getAdapter();

    /**
     * 在布局加载完成后是否请求数据，扩展使用
     *
     * @return
     */
    protected boolean requestDataIfViewCreated() {
        return true;
    }

    /**
     * 请求数据，刷新数据或者加载更多由page决定
     */
    protected abstract void requestData();

    /**
     * 初始化FloatActionButton
     *
     * @param view
     */
    protected void initFloatAction(View view) {

    }

    @Override
    public void showProgress(String progressText) {

        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgress() {

        loadingLayout.setStatus(LoadingLayout.Success);
        isLoading = false;
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showFailMsg(String msg) {
        switch (msg) {
            case StatusInfo.ALLDATA://全部数据
                //删除List最后的 加载更多 item
                baseAdapter.notifyItemRemoved(baseAdapter.getItemCount());
                ToastUtil.showShort(getContext(), getString(R.string.alldata));
                break;
            case StatusInfo.NODATA://无数据
                loadingLayout.setStatus(LoadingLayout.Empty);
                break;
            case StatusInfo.SERVICE_ERROR://服务器出错
                loadingLayout.setStatus(LoadingLayout.Error);
                break;
            case StatusInfo.CLIENT_ERROR://客户端出错
                loadingLayout.setStatus(LoadingLayout.Error);
                break;
            case StatusInfo.NONETWORK://无网络连接
                loadingLayout.setStatus(LoadingLayout.No_Network);
                break;
            case StatusInfo.HAS_CACHE_NONETWORK://无网络有缓存
                ToastUtil.showShort(getContext(), getString(R.string.NoNetworkText));
                break;
            default:
                loadingLayout.setStatus(LoadingLayout.Error);
                break;
        }

    }

    protected void loadSuccess() {
        hasLoadedOnce = true;
        ++page;//下次的请求时page+1
        loadingLayout.setStatus(LoadingLayout.Success);
    }


    protected void confirmLogin() {
        new MaterialDialog.Builder(getActivity())
                .iconRes(R.drawable.ic_importent_hint)
                .limitIconToDefaultSize()
                .title("您尚未登录")
                .content("是否前去登录?")
                .positiveText("确定").onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                Intent intent = new Intent(getActivity(), BaseFragmentActivity.class);
                intent.putExtra(getString(R.string.fragment_tag), getString(R.string.fragment_login));
                startActivity(intent);
            }
        })
                .negativeText("取消")
                .show();
    }
}
