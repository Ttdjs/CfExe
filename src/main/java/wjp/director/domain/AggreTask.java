package wjp.director.domain;

import wjp.director.annotation.AggreMethod;
import wjp.director.domain.DTO.DataDTO;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author lingse
 */
public class AggreTask extends Task{
    private Method invokeMethod;
    public AggreTask() {init();}
    private void init() {
        Method[] declaredMethods = this.getClass().getDeclaredMethods();
        List<Method> methods = Arrays.stream(declaredMethods).filter(x -> x.isAnnotationPresent(AggreMethod.class)).collect(Collectors.toList());
        if (methods.size() > 1) {
            throw new RuntimeException(this.getClass().getSimpleName() +  "任务上定义的聚合方法有多个");
        }
        if (methods.isEmpty()) {
            throw new RuntimeException(this.getClass().getSimpleName() +  "任务上没有指定的聚合方法");
        }
        invokeMethod = methods.get(0);
        invokeMethod.setAccessible(true);
    }
    @SuppressWarnings("unchecked")
    public DataDTO<?> doAggre(Context context) {
        DataDTO<?> rpcResult;
        try {
            CompletableFuture<Object> resultFuture = this.getParas(context).thenApply(arguments -> {
                Object result = null;
                try {
                    result = invokeMethod.invoke(this, arguments);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
                return result;
            });
            rpcResult = DataDTO.builder().data(resultFuture.get(10, TimeUnit.SECONDS)).build();
        } catch (Exception e) {
            rpcResult =  DataDTO.builder().message( this.getClass().getSimpleName() + "聚合函数执行错误" + e.getMessage()).exception(e).data(null).build();
        }
        return rpcResult;
    }
    @Override
    public List<? extends Task> queryDependency(Context context) {
        return context.queryAggreTaskDependency();
    }

    @Override
    public Method queryInvokeMethod() {
        return invokeMethod;
    }
}
