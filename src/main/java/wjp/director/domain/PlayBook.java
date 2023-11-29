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
public class PlayBook {
    @Getter
    private String apiName;
    // 依赖关系
    private Map<ExecuteTask, List<ExecuteTask>> dependencys = new HashMap<>();
    // todo 是否可以一个task多个实例
    private List<ExecuteTask> executeTasks = new ArrayList<>();
    // todo 不可变
    @Getter
    private List<ExecuteTask> aggreTaskDepedencys = new ArrayList<>();
    // 执行顺序
    // todo 运行时数据和静态数据最好别放一起
    private List<ExecuteTask> sortExecuteTask = new ArrayList<>();
    @Getter
    private AggreTask aggreTask;
    @Getter
    private boolean ready;
//    public PlayBook(String name) {
//        this.apiName = name;
//    }
    public PlayBook api( String name) {
        this.apiName = name;
        return this;
    }
    public PlayBook executeTask(ExecuteTask... tasks) {
        this.executeTasks = Arrays.asList(tasks);
        return this;
    }
    public PlayBook aggreTask(AggreTask aggreTask) {
        this.aggreTask = aggreTask;
        return this;
    }
    public static PlayBook getInstance() {
        return new PlayBook();
    }



    public List<ExecuteTask> queryDependencyByTask(ExecuteTask executeTask) {
        return dependencys.getOrDefault(executeTask, Collections.emptyList());
    }
    public PlayBook dependencys(ExecuteTask task, ExecuteTask... dependency) {
        if (dependencys.containsKey(task)) {
            // todo 自定义异常
            throw new RuntimeException("重复添加" + task.getClass().getSimpleName());
        }
        dependencys.put(task, Arrays.asList(dependency));
        return this;
    }

    public PlayBook aggreTaskDepedency(ExecuteTask... aggreTaskDepedency) {
        if (!aggreTaskDepedencys.isEmpty()) {
            throw new RuntimeException("重复添加聚合任务依赖" + apiName);
        }
//        this.aggreTask = aggreTask;
        this.aggreTaskDepedencys = Arrays.asList(aggreTaskDepedency);
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
        // todo 校验是否重复和相等
//        executeTasks.forEach( x -> {
//            if ()
//        });
        ready = true;
        return this;
    }

    private void topoSort() {
        final Map<ExecuteTask, Integer> prevNodeCount = new HashMap<>();
        final Map<ExecuteTask, List<ExecuteTask>> nextNodeList = new HashMap<>();
        executeTasks.forEach(x -> prevNodeCount.computeIfAbsent(x, k -> 0));
        dependencys.forEach((key, value) -> prevNodeCount.compute(key, (k, v) -> value.size()
        ));
        dependencys.forEach((key, value) -> {
            value.forEach(node -> nextNodeList.compute(node, (k, v) -> {
                if (v == null) v =  new ArrayList<>();
                v.add(key);
                return v;
            }));
        });
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
            nextNodeList.getOrDefault(cur, Collections.emptyList()).forEach(
                    x -> {
                        if (prevNodeCount.compute(x, (k, v) -> v - 1) == 0) {
                            queue.add(x);
                        }
                    }
            );
        }
        if (sortExecuteTask.size() != executeTasks.size()) {
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
