package wjp.director.domain;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import wjp.director.domain.DTO.DataDTO;

import java.util.function.BiFunction;

@Builder
@Slf4j
public class DefaultHandler implements BiFunction<Object, Throwable, DataDTO<?>> {
    private Class<?> defaultValue;
    private boolean logResult;
    private ApiContext apiContext;
    private String name;
    @Override
    public DataDTO<?> apply(Object result, Throwable r) {
        // todo 日志打点
        if (logResult) {
            if (r != null) {
                log.error("任务" + name + "执行出错，上下文是{}", apiContext, r);
            }
            log.info("任务" + name + "执行结果是 {}", result);
        }
        if (r != null) {
            return DataDTO.builder()
                    .exception(r)
                    .message(r.getMessage())
                    .data(defaultValue)
                    .build();
        } else {
            return DataDTO.builder()
                    .data(result)
                    .build();
        }
    }

//    @Override
//    public U apply(Object o, Throwable throwable) {
//        return null;
//    }
}
