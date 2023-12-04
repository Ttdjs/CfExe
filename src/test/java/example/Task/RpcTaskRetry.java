package example.Task;

import example.Manager.RpcManager;
import org.executor.annotation.HandlerMethod;
import org.executor.domain.ExecuteTask;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lingse
 */
public class RpcTaskRetry extends ExecuteTask {
    private final RpcManager rpcManager = new RpcManager();
    private AtomicInteger atomicInteger = new AtomicInteger();
    @HandlerMethod(forceAsync = false)
    public CompletableFuture<String> doHandler(String param) {
        return rpcManager.get("A").thenApply(x -> {
            int cur = atomicInteger.getAndIncrement();
            if (cur == 0 || cur % 3 != 0) {
                System.out.println("重试次数" + cur);
                throw new RuntimeException("失败");
            }
            return x + param.substring(0, param.length()) + cur;
        });
    }
}
