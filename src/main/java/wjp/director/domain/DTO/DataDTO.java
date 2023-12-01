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
    // todo 获取data如果code ！= 0，应该抛异常
    T data;
    boolean isSuccess() {
        return code == 0;
    }

//    public T getData()  {
//        if (exception != null) {
//            throw new RuntimeException(exception);
//        }
//        return data;
//    }
}
