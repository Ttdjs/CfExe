package wjp.director.example.Access;

import lombok.Data;
import wjp.director.example.Manager.PlayBookManager;

import java.util.Map;

@Data
public abstract class AccessService {
    private  PlayBookManager playBookManager =  new PlayBookManager();
    public abstract Map<String, String> testSimple();
}