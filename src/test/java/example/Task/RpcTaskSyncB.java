package example.Task;

import example.Manager.RpcManager;
import org.executor.annotation.HandlerMethod;
import org.executor.domain.ExecuteTask;

/**
 * @author lingse
 */
public class RpcTaskSyncB extends ExecuteTask {
    private final RpcManager rpcManager = new RpcManager();
    @HandlerMethod(forceAsync = false)
    public String doHandler(String param) throws InterruptedException {
        return rpcManager.get("B").thenApply(x -> { return x + param.substring(0, param.length());
        }).join();
//        System.out.println(Thread.currentThread() + "sync " + System.currentTimeMillis() / 1000 );
//        Thread.sleep(1000l);
    }
}
