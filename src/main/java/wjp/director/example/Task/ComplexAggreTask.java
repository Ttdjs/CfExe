package wjp.director.example.Task;

import wjp.director.annotation.AggreMethod;
import wjp.director.domain.AggreTask;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lingse
 */
public class ComplexAggreTask extends AggreTask {
    @AggreMethod
    public Map<String, String> get(String A, String B, String C, String D, String E, String F, String G, String H) {
        Map<String , String> res = new HashMap<>();
        res.put("A", A);
        res.put("B", B);
        res.put("C", C);
        res.put("D", D);
        res.put("E", E);
        res.put("F", F);
        res.put("G", G);
        res.put("H", H);
        return res;
     }
}
