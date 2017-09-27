package prodigalwang.newbornassistant.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ProdigalWang on 2016/12/15
 * 自定义线程池，配置模仿AsyncTask 源码配置
 */

public class ThreadPoolUtil {

    //单例模式
    private ThreadPoolUtil() {
    }

    // 线程池ThreadPoolExecutor java自带的线程池
    private volatile static ThreadPoolExecutor threadpool;

    public static ThreadPoolExecutor getThreadpool() {
        if (threadpool == null) {
            synchronized (ThreadPoolUtil.class) {
                if (threadpool == null)
                    threadpool = new ThreadPoolExecutor(
                            CORE_POOL_SIZE, MAXMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, workQueue, threadFactory);
                LogUtil.e("ThreadPoolExecutor has created success");
            }
        }
        return threadpool;
    }

    //CPU核心数
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    // 定义核心线程数，并行线程数
    private static int CORE_POOL_SIZE = CPU_COUNT + 1;

    // 线程池最大线程数：除了正在运行的线程额外保存多少个线程
    private static int MAXMUM_POOL_SIZE = CPU_COUNT * 2 + 1;

    // 额外线程空闲状态生存时间
    private static int KEEP_ALIVE = 1;

    // 阻塞队列。当核心线程队列满了放入的
    // 初始化一个大小为128的泛型为Runnable的队列
    private static BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(128);
    // 线程工厂,把传递进来的runnable对象生成一个Thread
    private static ThreadFactory threadFactory = new ThreadFactory() {

        // 原子型的integer变量生成的integer值不会重复
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "Thread Current ID = " + mCount.getAndIncrement());
        }
    };

    // 当线程池发生异常的时候回调进入
    private static RejectedExecutionHandler handler = new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            // 进行重启操作
        }

    };
}
