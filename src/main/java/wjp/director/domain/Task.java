package wjp.director.domain;

import java.lang.reflect.Method;
import java.util.List;

public class Task {
    private Method doHandlermethod;

    public void dohandler(ApiContext apiContext) {
        List<Task> dependencyTask = apiContext.queryDependencyByTask(this);

    }

}
