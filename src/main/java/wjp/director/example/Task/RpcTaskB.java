package wjp.director.example.Task;

import wjp.director.annotation.HandlerMethod;
import wjp.director.domain.ExecuteTask;
import wjp.director.example.Manager.RpcManager;

import java.util.concurrent.CompletableFuture;

/**
 * @author lingse
 */
public class RpcTaskB extends ExecuteTask {
    private final RpcManager rpcManager = new RpcManager();
    @HandlerMethod
    CompletableFuture<String> doHandler(String param) {
        return rpcManager.get("B").thenApply(x -> x + param);
    }
}