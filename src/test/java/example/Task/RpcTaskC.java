package example.Task;

import example.Manager.RpcManager;
import org.executor.annotation.HandlerMethod;
import org.executor.domain.ExecuteTask;

import java.util.concurrent.CompletableFuture;

/**
 * @author lingse
 */
public class RpcTaskC extends ExecuteTask {
    private final RpcManager rpcManager = new RpcManager();
    @HandlerMethod
    public CompletableFuture<String> doHandler(String param) {
        return rpcManager.get("C").thenApply(x -> x + param);
    }
}
