package wjp.director.domain;

import lombok.extern.slf4j.Slf4j;
import wjp.director.annotation.HandlerMethod;
import wjp.director.domain.DTO.RpcDTO;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author lingse
 */
@Slf4j
public class ExecuteTask extends Task{
    private Method doHandlermethod;
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
        
        doHandlermethod = methods.get(0);
    }
    public void doHandler(ApiContext apiContext) {
        CompletableFuture<?> res;
        try {
            Object[] argument = this.getParas(apiContext, doHandlermethod);
            // todo 同步转异步
            Object invokeResult = doHandlermethod.invoke(this, argument);
            if (invokeResult instanceof CompletableFuture) {
                res = (CompletableFuture<?>) invokeResult;
            } else {
                CompletableFuture<Object> objectCompletableFuture = new CompletableFuture<>();
                objectCompletableFuture.complete(invokeResult);
                res = objectCompletableFuture;
            }
        } catch (Exception e) {
            res = new CompletableFuture<>();
            res.completeExceptionally(e);
        }
        apiContext.putResultForTask(this, res.handle(DefaultHandler.builder().defaultValue(null).build()));
    }
    @Override
    public List<? extends Task> getDependency(ApiContext apiContext) {
        return apiContext.queryDependencyByTask(this);
    }
}
