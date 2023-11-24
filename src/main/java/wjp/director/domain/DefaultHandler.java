package wjp.director.domain;

import lombok.Builder;
import wjp.director.domain.DTO.RpcDTO;

import java.util.function.BiFunction;

@Builder
public class DefaultHandler implements BiFunction<Object, Throwable, RpcDTO<?>> {
    private Class<?> defaultValue;
    private boolean logResult;
    @Override
    public RpcDTO<?> apply(Object result, Throwable r) {
        // todo 日志打点
//        if (logResult)
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
