package example.Task;

import example.Manager.RpcManager;
import org.executor.annotation.HandlerMethod;
import org.executor.domain.ExecuteTask;

/**
 * @author lingse
 */
public class RpcTaskParamDot extends ExecuteTask {
    private final RpcManager rpcManager = new RpcManager();
    @HandlerMethod(forceAsync = true)
    public Void doHandler(String... param) {
        return rpcManager.get("RetVoid").thenRun(() -> {}).join();
    }
}
