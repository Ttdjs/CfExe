package wjp.director.domain;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import wjp.director.domain.DTO.RpcDTO;

import java.util.function.BiFunction;

@Builder
@Slf4j
public class DefaultHandler implements BiFunction<Object, Throwable, RpcDTO<?>> {
    private Class<?> defaultValue;
    private boolean logResult;
    private ApiContext apiContext;
    private String name;
    @Override
    public RpcDTO<?> apply(Object result, Throwable r) {
        // todo 日志打点
        if (logResult) {
            if (r != null) {
                log.error("任务" + name + "执行出错，上下文是{}", apiContext, r);
            }
            log.info("任务" + name + "执行结果是 {}", result);
        }
        if (r != null) {
            return RpcDTO.builder()
                    .exception(r)
                    .message(r.getMessage())
                    .data(defaultValue)
                    .build();
        } else {
            return RpcDTO.builder()
                    .data(result)
                    .build();
        }
    }

//    @Override
//    public U apply(Object o, Throwable throwable) {
//        return null;
//    }
}
