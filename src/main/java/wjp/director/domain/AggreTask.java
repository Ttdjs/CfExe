package wjp.director.domain;

import wjp.director.annotation.AggreMethod;
import wjp.director.domain.DTO.RpcDTO;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lingse
 */
public class AggreTask extends Task{
    private Method doAggreMethod;
    public AggreTask() {init();}
    private void init() {
        Method[] declaredMethods = this.getClass().getDeclaredMethods();
        List<Method> methods = Arrays.stream(declaredMethods).filter(x -> x.isAnnotationPresent(AggreMethod.class)).collect(Collectors.toList());
        if (methods.size() > 1) {
            throw new RuntimeException(this.getClass().getSimpleName() +  "任务上定义的聚合方法有多个");
        }
        if (methods.isEmpty()) {
            throw new RuntimeException(this.getClass().getSimpleName() +  "任务上没有指定的聚合方法");
        }
        doAggreMethod = methods.get(0);
    }
    public void doAggre(ApiContext apiContext) {
        try {
            Object[] arguments = this.getParas(apiContext, doAggreMethod);
            Object result = doAggreMethod.invoke(this, arguments);
            apiContext.setResult(RpcDTO.builder().data(result).build());
        } catch (Exception e) {
            apiContext.setResult(RpcDTO.builder().message( this.getClass().getSimpleName() + "聚合函数执行错误" + e.getMessage()).exception(e).data(null).build());
        }
    }
    @Override
    public List<? extends Task> getDependency(ApiContext apiContext) {
        return apiContext.queryAggreTaskDependency();
    }
}
