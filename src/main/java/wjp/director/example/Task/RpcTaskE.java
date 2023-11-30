package wjp.director.example.Task;

import wjp.director.annotation.HandlerMethod;
import wjp.director.domain.ExecuteTask;
import wjp.director.example.Manager.RpcManager;

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
