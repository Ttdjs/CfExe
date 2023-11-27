package wjp.director.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author lingse
 */
@Builder
public class PlayBook {
    private String apiName;
    // 依赖关系
    private Map<ExecuteTask, List<ExecuteTask>> dependencys;
    // todo 不可变
    @Getter
    private List<ExecuteTask> aggreTaskDepedency;
    // 执行顺序
    private List<ExecuteTask> orders;
    @Getter
    private AggreTask aggreTask;
    public List<ExecuteTask> queryDependencyByTask(ExecuteTask executeTask) {
        return dependencys.getOrDefault(executeTask, Collections.emptyList());
    }

}
