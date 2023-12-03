package wjp.director.domain;

import org.apache.commons.lang3.Validate;
import wjp.director.domain.DTO.DataDTO;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lingse
 * 暂不支持执行器中scene的更新和删除，因为觉得业务上一般不会有动态更新的需求
 */

public class Executor {
    private final String name;
    private final Map<String, Scene> sceneMap= new ConcurrentHashMap<>();
    private final Map<String, Script> scriptMap = new ConcurrentHashMap<>();

    public synchronized void register(Scene scene) {
        Validate.isTrue(Objects.isNull(sceneMap.get(scene.getSceneName())), scene.getSceneName() + "已经被注册进执行器" + name );
        sceneMap.put(scene.getSceneName(), scene);
        Script script = new Script(scene);
        scriptMap.put(scene.getSceneName(), script);
    }
    @SuppressWarnings("unchecked")
    public <T> DataDTO<T> execute(String sceneName ,Context context) {
        Validate.notNull(scriptMap.get(sceneName));
        return (DataDTO<T>) scriptMap.get(sceneName).execute(context);
    }

    public Executor(String name) {
        this.name = name;
    }
}
