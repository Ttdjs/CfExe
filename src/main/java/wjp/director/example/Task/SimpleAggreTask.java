package wjp.director.example.Task;

import wjp.director.annotation.AggreMethod;
import wjp.director.domain.AggreTask;

/**
 * @author lingse
 */
public class SimpleAggreTask extends AggreTask {
    @AggreMethod
    public String get(String A, String B, String C) {
        return this.getClass().getSimpleName() + " " + A + " "+ B + " " + C;
    }
}
