package wjp.director.domain;

import wjp.director.domain.DTO.RpcDTO;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ApiContext {
    private Map<Task, List<Task>> dependency;
    private Map<Task, CompletableFuture<RpcDTO<?>>> resultMap;

    public void setDependency(Map<Task, List<Task>> dependency) {
        this.dependency = dependency == null ? new HashMap<>() : dependency;
    }

    public void setResultMap(Map<Task, CompletableFuture<RpcDTO<?>>> resultMap) {
        this.resultMap = resultMap == null ? new HashMap<>() : resultMap;
    }

    public List<Task> queryDependencyByTask(Task task) {
        return dependency.getOrDefault(task, Collections.emptyList());
    }

    public CompletableFuture<RpcDTO<?>> queryResultByTask(Task task) {
        // todo 返回一个异常的CF
        return resultMap.getOrDefault(task, null);
    }
}
