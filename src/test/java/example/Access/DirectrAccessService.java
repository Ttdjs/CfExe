package example.Access;

import example.Manager.TestDataManager;
import org.executor.domain.Context;
import org.executor.domain.DTO.DataDTO;

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
        if (res.getException() != null) throw new RuntimeException(res.getException());
        return res.getData();
    }

}
