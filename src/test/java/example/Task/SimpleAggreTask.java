package example.Task;

import org.executor.annotation.AggreMethod;
import org.executor.domain.AggreTask;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lingse
 */
public class SimpleAggreTask extends AggreTask {
    @AggreMethod
    public Map<String, String> get(String A, String B, String C) {
        Map<String , String> res = new HashMap<>();
        res.put("A", A);
        res.put("B", B);
        res.put("C", C);
        return res;
     }
}
