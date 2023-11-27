package wjp.director.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import wjp.director.domain.DTO.ParamDTO;
import wjp.director.domain.DTO.RpcDTO;

import java.util.*;
import java.util.concurrent.CompletableFuture;



/**
 * @author lingse
 */
@Builder
public class ApiContext {
    PlayBook playBook;
    private Map<Task, CompletableFuture<RpcDTO<?>>> resultMap;
//    private
    @Getter
    private ParamDTO paramDTO;

    @Setter
    RpcDTO<?> result;

    public List<ExecuteTask> queryDependencyByTask(ExecuteTask executeTask) {
        return playBook.queryDependencyByTask(executeTask);
    }

    public CompletableFuture<RpcDTO<?>> queryResultByTask(Task task) {
        return resultMap.getOrDefault(task, CompletableFuture.completedFuture(RpcDTO.builder().code(-1).exception(new NoSuchElementException("任务没有结果")).build()));
    }

    public void putResultForTask(Task executeTask, CompletableFuture<RpcDTO<?>> result) {
        resultMap.put(executeTask, result);
    }
    public List<ExecuteTask> queryAggreTaskDependency() {
        return playBook.getAggreTaskDepedency();
    }

    public <T> RpcDTO<T> getResult() {
        return (RpcDTO<T>) result;
    }
}
