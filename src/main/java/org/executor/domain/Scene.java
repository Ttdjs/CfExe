package org.executor.domain;

import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.executor.utils.CommonUtils;

import java.util.*;

/**
 * @author lingse
 */
@Getter
public class Scene {
    private String sceneName;
    // 依赖关系
    private Map<ExecuteTask, List<ExecuteTask>> dependencys = new HashMap<>();
    private HashSet<ExecuteTask> executeTasks = new HashSet<>();
    private List<ExecuteTask> aggreTaskDepedencys = new ArrayList<>();
    // 执行顺序
    private AggreTask aggreTask;
    private Map<ExecuteTask, Object> defaultValueMap = new HashMap<>();
    private final Map<ExecuteTask, Integer> retryTimeMap = new HashMap<>();

    public  Scene sceneName(String name) {
        Validate.notBlank(name, "api名称不允许为空");
        this.sceneName = name;
        return this;
    }
    public  Scene executeTask(ExecuteTask... tasks) {
        Validate.isTrue(CollectionUtils.isEmpty(this.executeTasks), "不允许重复设置执行任务");
        Validate.isTrue(CommonUtils.noDuplicates(Arrays.asList(tasks)), "不允许有重复元素");
        this.executeTasks.addAll(Arrays.asList(tasks));
        return this;
    }
    public  Scene aggreTask(AggreTask aggreTask) {
        Validate.isTrue(Objects.isNull(this.aggreTask), "不允许重复设置执行任务");
        this.aggreTask = aggreTask;
        return this;
    }
    public  Scene defaultValue(ExecuteTask task, Object defaultValue) {
        Validate.isTrue(!defaultValueMap.containsKey(task), "不能重复为同一个任务添加默认值");
        defaultValueMap.put(task, defaultValue);
        return this;
    }

    public static Scene getInstance() {
        return new Scene();
    }

    public  Scene dependency(ExecuteTask task, ExecuteTask... dependency) {
        Validate.isTrue(!dependencys.containsKey(task), "不能重复为同一个任务添加依赖任务");
        Validate.isTrue(CommonUtils.noDuplicates(Arrays.asList(dependency)), "不允许有重复元素");
        dependencys.put(task, Arrays.asList(dependency));
        return this;
    }
    public Scene retryTimes(ExecuteTask task, int retryTime) {
        Validate.isTrue(retryTime > 0, "重复次数需要大于0");
        retryTimeMap.put(task, retryTime);
        return this;
    }

    public  Scene aggreTaskDepedency(ExecuteTask... aggreTaskDepedency) {
        Validate.isTrue(CollectionUtils.isEmpty(this.aggreTaskDepedencys), "不能为聚合任务重复添加依赖任务");
        Validate.isTrue(CommonUtils.noDuplicates(Arrays.asList(aggreTaskDepedency)), "不允许有重复元素");
        this.aggreTaskDepedencys = Arrays.asList(aggreTaskDepedency);
        return this;
    }

    synchronized public Scene init(Executor executor) {
        Validate.notNull(aggreTask, "初始化错误，聚合任务为空");
        validTaskHasRegister();
        executor.register(this);
        return this;
    }
    private void validTaskHasRegister() {
        dependencys.forEach((key, value) -> {
            Validate.isTrue(executeTasks.contains(key) && executeTasks.containsAll(value), "所有执行任务都需要注册");
        });
        Validate.isTrue(executeTasks.containsAll(retryTimeMap.keySet()));
        Validate.isTrue(executeTasks.containsAll(defaultValueMap.keySet()), "所有有默认值的执行任务都需要注册");
        Validate.isTrue(executeTasks.containsAll(aggreTaskDepedencys), "聚合任务依赖的所有任务都需要注册");
    }


}
