package wjp.director.domain;

import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import wjp.director.domain.DTO.DataDTO;

import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

/**
 * @author lingse
 */

public class Script {
    @Getter
    private final String name;
    private final List<ExecuteTask> sortedExecuteTask = new ArrayList<>();
    private List<ExecuteTask> executeTasks = new ArrayList<>();

    private final Map<ExecuteTask, List<ExecuteTask>> dependencys;
    private final AggreTask aggreTask;
    private final List<ExecuteTask> aggreTaskDepedencys;
    private final Map<ExecuteTask, Object> defaultValueMap;

    public List<ExecuteTask> getDependency(ExecuteTask task) {
        return unmodifiableList(dependencys.getOrDefault(task, emptyList()));
    }
    public List<ExecuteTask> getAggreTaskDepedencys() {
        return unmodifiableList(aggreTaskDepedencys);
    }
    public Object getDefaultValue(ExecuteTask task) {
        return defaultValueMap.get(task);
    }
    public Script(Scene scene) {
        this.aggreTask = scene.getAggreTask();
        this.dependencys = new HashMap<>(scene.getDependencys());
        this.executeTasks = new ArrayList<>(scene.getExecuteTasks());
        this.aggreTaskDepedencys = new ArrayList<>(scene.getAggreTaskDepedencys());
        this.defaultValueMap = new HashMap<>(scene.getDefaultValueMap());
        this.name = scene.getSceneName();
        topoSort();
    }

    public  DataDTO<?> execute(Context context) {
        context.setScript(this);
        sortedExecuteTask.forEach(x -> x.doHandler(context));
        return aggreTask.doAggre(context);
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
        // 用优先队列是期望同一层下游任务多的任务先执行
        // 但是理论上如果同一层任务都是异步任务则本身就是同时执行
        PriorityQueue<ExecuteTask> queue = new PriorityQueue<>((x, y) -> {
            int xNext = nextNodeList.getOrDefault(x, emptyList()).size();
            int yNext = nextNodeList.getOrDefault(y, emptyList()).size();
            return Integer.compare(yNext, xNext);
        });
        prevNodeCount.forEach((key, value) -> {
            if (value == 0) {
                queue.add(key);
            }
        });

        while (!CollectionUtils.isEmpty(queue)) {
            ExecuteTask cur = queue.remove();
            sortedExecuteTask.add(cur);
            nextNodeList.getOrDefault(cur, emptyList()).forEach(
                    x -> {
                        if (prevNodeCount.compute(x, (k, v) -> v - 1) == 0) {
                            queue.add(x);
                        }
                    }
            );
        }
        Validate.isTrue(sortedExecuteTask.size() == executeTasks.size(), "初始化失败，有环");
    }
}
