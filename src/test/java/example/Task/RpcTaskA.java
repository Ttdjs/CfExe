package example.Task;

import example.Manager.RpcManager;
import org.executor.annotation.HandlerMethod;
import org.executor.domain.ExecuteTask;

import java.util.concurrent.CompletableFuture;

/**
 * @author lingse
 */
public class RpcTaskA extends ExecuteTask {
    private final RpcManager rpcManager = new RpcManager();
    @HandlerMethod(forceAsync = false)
    public CompletableFuture<String> doHandler(String param) {
        return rpcManager.get("A").thenApply(x -> {
            return x + param.substring(0, param.length());
        });
    }
}
