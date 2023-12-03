package wjp.director.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import wjp.director.domain.DTO.DataDTO;

import java.util.*;
import java.util.concurrent.CompletableFuture;



/**
 * @author lingse
 */
@Builder
public class Context {
    @Setter
    @Getter
    Script script;
    final private Map<ExecuteTask, CompletableFuture<DataDTO<?>>> resultMap = new HashMap<>();
    @Getter
    private Object paramDTO;

    public List<ExecuteTask> queryDependency(ExecuteTask executeTask) {
        return script.getDependency(executeTask);
    }

    public CompletableFuture<DataDTO<?>> queryResultByTask(Task task) {
        return resultMap.getOrDefault(task, CompletableFuture.completedFuture(DataDTO.builder().code(-1).exception(new NoSuchElementException("任务没有结果")).build()));
    }

    public void putResultForTask(ExecuteTask executeTask, CompletableFuture<DataDTO<?>> result) {
        resultMap.put(executeTask, result);
    }
    public List<ExecuteTask> queryAggreTaskDependency() {
        return script.getAggreTaskDepedencys();
    }
    public Object queryDefaultValue(ExecuteTask task) {
        return  script.getDefaultValue(task);
    }
   // todo 完善逻辑
    public int queryRetryTime(ExecuteTask task) {
        return 1;
    }
}
