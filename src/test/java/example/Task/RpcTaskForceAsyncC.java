package example.Task;

import example.Manager.RpcManager;
import org.executor.annotation.HandlerMethod;
import org.executor.domain.ExecuteTask;

/**
 * @author lingse
 */
public class RpcTaskForceAsyncC extends ExecuteTask {
    private final RpcManager rpcManager = new RpcManager();
    @HandlerMethod(forceAsync = true)
    public String doHandler(String param) {
        return rpcManager.get("C").thenApply(x -> { return x + param.substring(0, param.length());
        }).join();
    }
}
