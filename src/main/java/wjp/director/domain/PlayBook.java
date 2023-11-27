package wjp.director.domain;

import lombok.Builder;
import lombok.Getter;

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
    private List<ExecuteTask> orders = new ArrayList<>();
    @Getter
    private AggreTask aggreTask;
    public List<ExecuteTask> queryDependencyByTask(ExecuteTask executeTask) {
        return dependencys.getOrDefault(executeTask, Collections.emptyList());
    }
    public void setDependencysByTask(ExecuteTask task, ExecuteTask... dependency) {
        if (this.dependencys == null) {
            dependencys = new HashMap<>();
        }
        if (dependencys.containsKey(task)) {
            // todo 自定义异常
            throw new RuntimeException("重复添加" + task.getClass().getSimpleName());
        }
        dependencys.put(task, Arrays.asList(dependency));
    }

    public void setAggreTaskDepedency(List<ExecuteTask> aggreTaskDepedency) {
        if (!aggreTaskDepedency.isEmpty()) {
            throw new RuntimeException("重复添加聚合任务依赖" + apiName);
        }
        this.aggreTaskDepedency = aggreTaskDepedency;
    }
}
