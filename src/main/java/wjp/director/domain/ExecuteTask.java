package wjp.director.domain;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import wjp.director.Manager.ThreadPoolManager;
import wjp.director.annotation.HandlerMethod;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author lingse
 */
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class ExecuteTask extends Task{
    private Method invokeMethod;
    private boolean forceAsync;
    private final Object defaultValue = null;
    public ExecuteTask() {
        init();
    }
    private void init() {
        Method[] declaredMethods = this.getClass().getDeclaredMethods();
        List<Method> methods = Arrays.stream(declaredMethods).filter(x -> x.isAnnotationPresent(HandlerMethod.class)).collect(Collectors.toList());
        if (methods.size() > 1) {
            throw new RuntimeException(this.getClass().getSimpleName() +  "任务上定义的处理方法有多个");
        } 
        if (methods.isEmpty()) {
            throw new RuntimeException(this.getClass().getSimpleName() +  "任务上没有指定的处理方法");
        }
        
        invokeMethod = methods.get(0);
        invokeMethod.setAccessible(true);
        HandlerMethod annotation = invokeMethod.getAnnotation(HandlerMethod.class);
        forceAsync = annotation.forceAsync();
    }
    public void doHandler(Context context) {
        CompletableFuture<Object> res = new CompletableFuture<>();
        try {
            this.getParas(context).thenAccept(argument -> doInvoke(context, argument, res, context.queryRetryTime(this)));
        } catch (Exception e) {
            res.completeExceptionally(e);
        }
        context.putResultForTask(this, res.handle(DefaultHandler.builder().defaultValue(context.queryDefaultValue(this)).build()));
    }

    private CompletableFuture<?> doInvoke(Context context, Object[] argument) {
        CompletableFuture<?> midRes;
        Object invokeResult;
        if (forceAsync && !invokeMethod.getReturnType().equals(CompletableFuture.class)) {
            invokeResult = CompletableFuture.supplyAsync(() -> this.doInvoke(invokeMethod, argument), ThreadPoolManager.getThreadPool(context.getScript().getName()));
        } else {
            invokeResult = this.doInvoke(invokeMethod, argument);
        }
        if (invokeResult instanceof CompletableFuture) {
            midRes = (CompletableFuture<?>) invokeResult;
        } else {
            CompletableFuture<Object> objectCompletableFuture = new CompletableFuture<>();
            objectCompletableFuture.complete(invokeResult);
            midRes = objectCompletableFuture;
        }
        return midRes;
    }
    // todo 单测抛异常场景
    private void doInvoke(Context context, Object[] argument, CompletableFuture<Object> result, int retryTime) {
        CompletableFuture<?> completableFuture = doInvoke(context, argument);
        completableFuture.whenComplete((r, t) -> {
            if (t == null) {
                result.complete(r);
            } else if (retryTime > 0) {
                log.info("第{}次重试" + this.getClass().getSimpleName(), retryTime, t);
                doInvoke(context, argument, result, retryTime - 1);
            } else {
                result.completeExceptionally(t);
            }
        });

    }

    @Override
    public List<? extends Task> queryDependency(Context context) {
        return context.queryDependency(this);
    }

    @Override
    public Method queryInvokeMethod() {
        return invokeMethod;
    }
}
