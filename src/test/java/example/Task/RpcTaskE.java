package example.Task;

import example.Manager.RpcManager;
import wjp.director.annotation.HandlerMethod;
import wjp.director.domain.ExecuteTask;

import java.util.concurrent.CompletableFuture;

/**
 * @author lingse
 */
public class RpcTaskE extends ExecuteTask {
    private final RpcManager rpcManager = new RpcManager();
    @HandlerMethod
    public CompletableFuture<String> doHandler(String param, String param2) {
        return rpcManager.get("E").thenApply(x -> { return x + param + param2;
        });
    }
}