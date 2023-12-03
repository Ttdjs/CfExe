package example.Access;

import example.Manager.TestDataManager;
import lombok.Data;

import java.util.Map;

@Data
public abstract class AccessService {

    public abstract Map<String, String> testSimple(String param);

    public abstract Map<String, String> testComplex(String param);
}
