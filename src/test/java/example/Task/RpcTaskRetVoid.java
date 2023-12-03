package example.Task;

import example.Manager.RpcManager;
import wjp.director.annotation.HandlerMethod;
import wjp.director.domain.ExecuteTask;

import java.util.concurrent.CompletableFuture;

/**
 * @author lingse
 */
public class RpcTaskRetVoid extends ExecuteTask {
    private final RpcManager rpcManager = new RpcManager();
    @HandlerMethod(forceAsync = true)
    public Void doHandler(String param) {
        return rpcManager.get("RetVoid").thenRun(() -> {}).join();
    }
}
