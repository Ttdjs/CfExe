package example.Access;

import example.Manager.PlayBookManager;
import lombok.Data;

import java.util.Map;

@Data
public abstract class AccessService {
    private PlayBookManager playBookManager =  new PlayBookManager();

    public abstract Map<String, String> testSimple(String param);

    public abstract Map<String, String> testComplex(String param);
}
