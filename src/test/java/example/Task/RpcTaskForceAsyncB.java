package example.Task;

import example.Manager.RpcManager;
import wjp.director.annotation.HandlerMethod;
import wjp.director.domain.ExecuteTask;

/**
 * @author lingse
 */
public class RpcTaskForceAsyncB extends ExecuteTask {
    private final RpcManager rpcManager = new RpcManager();
    @HandlerMethod(forceAsync = true)
    public String doHandler(String param) {
        return rpcManager.get("B").thenApply(x -> { return x + param.substring(0, param.length());
        }).join();
    }
}
