package org.executor.domain;

import org.executor.domain.DTO.DataDTO;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author lingse
 */
public abstract class Task {
    public CompletableFuture<Object[]> getParas(Context context) {
        Method doHandlermethod = queryInvokeMethod();
        List<? extends Task> dependencyExecuteTask = queryDependency(context);
        List<CompletableFuture<DataDTO<?>>> argumentFuture = dependencyExecuteTask.stream().map(context::queryResultByTask).collect(Collectors.toList());
        int parameterCount = doHandlermethod.getParameterCount();
        if (dependencyExecuteTask.size() != parameterCount && dependencyExecuteTask.size() + 1 != parameterCount) {
            throw new RuntimeException(this.getClass().getSimpleName() +  "任务参数列表和依赖列表数量不匹配");
        }
        Class<?>[] parameterTypes = doHandlermethod.getParameterTypes();
        final boolean needParamDTO = dependencyExecuteTask.size() != parameterCount;
        return  CompletableFuture.allOf(argumentFuture.toArray(new CompletableFuture[0])).thenApply(
                x -> {
                    Object[] argument = new Object[parameterCount];
                    if (needParamDTO) {
                        argument[0] = context.getParamDTO();
                    }
                    int beginIndex = needParamDTO ? 1 : 0;
                    for (int i = 0; i < argumentFuture.size(); i++) {
                        try {
                            if (parameterTypes[i].equals(DataDTO.class)) {
                                argument[i + beginIndex] = argumentFuture.get(i).join();
                            } else {
                                argument[i + beginIndex] = argumentFuture.get(i).join().getData();
                            }
                        } catch (Exception e) {
                            throw new RuntimeException("参数获取错误" + i);
                        }
                    }
                    return argument;
                }

        );
    }
    public abstract List<? extends Task> queryDependency(Context context);
    public abstract Method queryInvokeMethod();
    public Object doInvoke(Method method, Object[] param) {
        try {
            return method.invoke(this, param);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
