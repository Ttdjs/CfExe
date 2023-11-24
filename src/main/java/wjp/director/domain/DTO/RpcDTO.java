package wjp.director.domain.DTO;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RpcDTO<T> {
    private int code;
    private String message;
    Throwable exception;
    T data;
    boolean isSuccess() {
        return code == 0;
    }
}
