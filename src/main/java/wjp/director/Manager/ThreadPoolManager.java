package wjp.director.Manager;

import java.util.concurrent.*;

/**
 * @author lingse
 */
public class ThreadPoolManager {
    public static String preFix = "sync2async";
    // 使用ConcurrentHashMap存储线程池
    private static final ConcurrentHashMap<String, ExecutorService> THREAD_POOLS = new ConcurrentHashMap<>();

    // 创建线程池并存入ConcurrentHashMap
    private static ExecutorService createThreadPool(String name, int corePoolSize, int maximumPoolSize, long keepAliveTime) {
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                keepAliveTime, TimeUnit.MILLISECONDS,
                new SynchronousQueue<>());
    }

    // 根据线程池名字获取线程池，如果不存在则创建
    public static ExecutorService getThreadPool(String name) {
        name = name + preFix;
        return THREAD_POOLS.computeIfAbsent(name, key -> createThreadPool(key, 5, 50, 5000));
    }

    // 获取线程池的信息
    public static String getThreadPoolInfo(String name) {
        name = name + preFix;
        ExecutorService threadPool = THREAD_POOLS.get(name);
        if (threadPool instanceof ThreadPoolExecutor) {
            ThreadPoolExecutor pool = (ThreadPoolExecutor) threadPool;
            return "Core threads: " + pool.getCorePoolSize() + " Active threads: " + pool.getActiveCount() + " Maximum threads: " + pool.getMaximumPoolSize();
        } else {
            return "Error: Invalid thread pool name.";
        }
    }
}

