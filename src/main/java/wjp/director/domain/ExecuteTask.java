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
        CompletableFuture<?> res;
        try {
            res = this.getParas(context).thenCompose(argument -> {
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
            });
        } catch (Exception e) {
            res = new CompletableFuture<>();
            res.completeExceptionally(e);
        }
        context.putResultForTask(this, res.handle(DefaultHandler.builder().defaultValue(context.queryDefaultValue(this)).build()));
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
