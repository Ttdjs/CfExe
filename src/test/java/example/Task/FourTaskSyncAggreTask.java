package example.Task;

import org.executor.annotation.AggreMethod;
import org.executor.domain.AggreTask;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lingse
 */
public class FourTaskSyncAggreTask extends AggreTask {
    @AggreMethod
    public Map<String, String> get(String A, String B, String C, String D) {
        Map<String , String> res = new HashMap<>();
        res.put("1", A);
        res.put("2", B);
        res.put("3", C);
        res.put("4", D);
        return res;
     }
}
