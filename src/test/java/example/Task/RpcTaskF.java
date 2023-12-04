package example.Task;

import example.Manager.RpcManager;
import org.executor.annotation.HandlerMethod;
import org.executor.domain.ExecuteTask;

import java.util.concurrent.CompletableFuture;

/**
 * @author lingse
 */
public class RpcTaskF extends ExecuteTask {
    private final RpcManager rpcManager = new RpcManager();
    @HandlerMethod
    public CompletableFuture<String> doHandler(String param, String param2) {
        return rpcManager.get("F").thenApply(x -> { return x + param + param2;
        });
    }
}
