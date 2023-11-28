package wjp.director.domain.DTO;


import lombok.Builder;
import lombok.Data;

/**
 * @author lingse
 */
@Data
@Builder
public class DataDTO<T> {
    private int code;
    private String message;
    Throwable exception;
    T data;
    boolean isSuccess() {
        return code == 0;
    }
}
