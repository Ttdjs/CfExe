package wjp.director.example.Task;

import wjp.director.annotation.HandlerMethod;
import wjp.director.domain.ExecuteTask;
import wjp.director.example.Manager.RpcManager;

import java.util.concurrent.CompletableFuture;

/**
 * @author lingse
 */
public class RpcTaskG extends ExecuteTask {
    private final RpcManager rpcManager = new RpcManager();
    @HandlerMethod
    public CompletableFuture<String> doHandler(String param, String param2, String param3) {
        return rpcManager.get("D").thenApply(x -> { return x + param + param2 + param3;
        });
    }
}
