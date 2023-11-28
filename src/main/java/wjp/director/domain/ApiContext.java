package wjp.director.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import wjp.director.domain.DTO.ParamDTO;
import wjp.director.domain.DTO.DataDTO;

import java.util.*;
import java.util.concurrent.CompletableFuture;



/**
 * @author lingse
 */
@Builder
public class ApiContext {
    PlayBook playBook;
    private Map<ExecuteTask, CompletableFuture<DataDTO<?>>> resultMap;
    @Getter
    private ParamDTO paramDTO;

    public List<ExecuteTask> queryDependencyByTask(ExecuteTask executeTask) {
        return playBook.queryDependencyByTask(executeTask);
    }

    public CompletableFuture<DataDTO<?>> queryResultByTask(Task task) {
        return resultMap.getOrDefault(task, CompletableFuture.completedFuture(DataDTO.builder().code(-1).exception(new NoSuchElementException("任务没有结果")).build()));
    }

    public void putResultForTask(ExecuteTask executeTask, CompletableFuture<DataDTO<?>> result) {
        resultMap.put(executeTask, result);
    }
    public List<ExecuteTask> queryAggreTaskDependency() {
        return playBook.getAggreTaskDepedency();
    }
}
