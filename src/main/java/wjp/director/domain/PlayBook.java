package wjp.director.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import org.apache.commons.collections.CollectionUtils;
import wjp.director.domain.DTO.DataDTO;

import java.util.*;

/**
 * @author lingse
 */
@Builder
public class PlayBook {
    private String apiName;
    // 依赖关系
    private Map<ExecuteTask, List<ExecuteTask>> dependencys = new HashMap<>();
    // todo 不可变
    @Getter
    private List<ExecuteTask> aggreTaskDepedency = new ArrayList<>();
    // 执行顺序
    private List<ExecuteTask> sortExecuteTask = new ArrayList<>();
    @Getter
    private AggreTask aggreTask;
    @Getter
    private  boolean ready;
    public PlayBook(String name) {
        this.apiName = name;
    }
    public List<ExecuteTask> queryDependencyByTask(ExecuteTask executeTask) {
        return dependencys.getOrDefault(executeTask, Collections.emptyList());
    }
    public PlayBook setDependencysByTask(ExecuteTask task, ExecuteTask... dependency) {
        if (this.dependencys == null) {
            dependencys = new HashMap<>();
        }
        if (dependencys.containsKey(task)) {
            // todo 自定义异常
            throw new RuntimeException("重复添加" + task.getClass().getSimpleName());
        }
        dependencys.put(task, Arrays.asList(dependency));
        return this;
    }

    public PlayBook setAggreTaskDepedency(AggreTask aggreTask ,List<ExecuteTask> aggreTaskDepedency) {
        if (this.aggreTask != null || !aggreTaskDepedency.isEmpty()) {
            throw new RuntimeException("重复添加聚合任务依赖" + apiName);
        }
        this.aggreTask = aggreTask;
        this.aggreTaskDepedency = aggreTaskDepedency;
        return this;
    }

    synchronized public  PlayBook init() {
        if (ready) {
            throw new RuntimeException(apiName + "已经被初始化");
        }
        if (dependencys.isEmpty() || aggreTask == null) {
            throw new RuntimeException("初始化错误，聚合任务或者执行任务为空");
        }
        topoSort();
        ready = true;
        return this;
    }
    private void topoSort() {
        final Map<ExecuteTask, Integer> prevNodeCount = new HashMap<>();
        dependencys.forEach((key, value) -> {
                    prevNodeCount.computeIfAbsent(key, k -> 0);
                    value.forEach(ele -> prevNodeCount.compute(
                            ele, (k, v) -> v == null ? 1 : v + 1));
                    }
        );
        // 用优先队列是期望下游任务多的任务先执行
        PriorityQueue <ExecuteTask> queue = new PriorityQueue<>((x, y) -> {
            int xNext = dependencys.getOrDefault(x, Collections.emptyList()).size();
            int yNext = dependencys.getOrDefault(x, Collections.emptyList()).size();
            return Integer.compare(yNext, xNext);
        });
        prevNodeCount.forEach((key, value) -> {
            if (value == 0) {
                queue.add(key);
            }
        });

        while (!CollectionUtils.isEmpty(queue)) {
            ExecuteTask cur = queue.remove();
            sortExecuteTask.add(cur);
            dependencys.getOrDefault(cur, Collections.emptyList()).forEach(
                    x -> {
                        if (prevNodeCount.compute(x, (k, v) -> v - 1) == 0) {
                            queue.add(x);
                        }
                    }
            );
        }
        if (sortExecuteTask.size() != dependencys.size()) {
            throw new RuntimeException(apiName + "有环，初始化失败");
        }
    }

    @SuppressWarnings("unchecked")
    public <T> DataDTO<T> execute(ApiContext apiContext) {
        if (!ready) {
            throw new RuntimeException("剧本还没有初始化，不允许执行");
        }
        sortExecuteTask.forEach(x -> x.doHandler(apiContext));
        return (DataDTO<T>) aggreTask.doAggre(apiContext);
    }
}
