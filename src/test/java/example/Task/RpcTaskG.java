package example.Task;

import example.Manager.RpcManager;
import wjp.director.annotation.HandlerMethod;
import wjp.director.domain.ExecuteTask;

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
