package example.Task;

import example.Manager.RpcManager;
import wjp.director.annotation.HandlerMethod;
import wjp.director.domain.ExecuteTask;

import java.util.concurrent.CompletableFuture;

/**
 * @author lingse
 */
public class RpcTaskA extends ExecuteTask {
    private final RpcManager rpcManager = new RpcManager();
    @HandlerMethod
    public CompletableFuture<String> doHandler(String param) {
        return rpcManager.get("A").thenApply(x -> { return x + param.substring(0, param.length());
        });
    }
}
