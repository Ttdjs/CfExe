package example.Access;

import example.Manager.TestDataManager;
import wjp.director.domain.Context;
import wjp.director.domain.DTO.DataDTO;
import wjp.director.domain.Scene;

import java.util.Map;

/**
 * @author lingse
 */
public class DirectrAccessService extends AccessService {

    @Override
    public Map<String, String> testSimple(String param) {
       return test("simple", param);
    }
    @Override
    public Map<String, String> testComplex(String param) {
       return test("complex", param);
    }

    public Map<String, String> test(String api, String param) {
        Context context = Context.builder().paramDTO(param).build();
        DataDTO<Map<String, String>> res = TestDataManager.executor.execute(api, context);
        return res.getData();
    }

}
