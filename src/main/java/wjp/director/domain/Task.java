package wjp.director.domain;

import wjp.director.domain.DTO.RpcDTO;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author lingse
 */
public class Task {
    private Method doHandlermethod;
    private boolean needParamDTO;
    public void init() {

    }
    public void doHandler(ApiContext apiContext) throws InvocationTargetException, IllegalAccessException {
        CompletableFuture<?> res;
        try {
            List<Task> dependencyTask = apiContext.queryDependencyByTask(this);
            List<CompletableFuture<RpcDTO<?>>> argumentFuture = dependencyTask.stream().map(apiContext::queryResultByTask).collect(Collectors.toList());
            Object[] argument = new Objects[doHandlermethod.getParameterCount()];
            if (needParamDTO) {
                argument[0] = apiContext.getParamDTO();
            }
            CompletableFuture.allOf(argumentFuture.toArray(new CompletableFuture[0])).thenRun(
                    ()-> {
                        int beginIndex = needParamDTO ? 1 : 0;
                        for (int i = 0; i < argumentFuture.size(); i++) {
                            try {
                                argument[i + beginIndex] = argumentFuture.get(i).join().getData();
                            } catch (Exception e) {
                                throw new RuntimeException("参数获取错误" + i);
                            }
                        }
                    }
            );
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

}
