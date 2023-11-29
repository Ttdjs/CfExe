package wjp.director.example.Manager;

import wjp.director.domain.PlayBook;
import wjp.director.example.Task.RpcTaskA;
import wjp.director.example.Task.RpcTaskB;
import wjp.director.example.Task.RpcTaskC;
import wjp.director.example.Task.SimpleAggreTask;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class PlayBookManager {
    private Map<String, PlayBook> playBookMap = new HashMap<>();
    private RpcTaskA taskA =  new RpcTaskA();
    private RpcTaskB taskB =  new RpcTaskB();
    private RpcTaskC taskC =  new RpcTaskC();
    private SimpleAggreTask simpleAggreTask = new SimpleAggreTask();
    public PlayBookManager() {
        initPlayBookOne();
    }
    private void initPlayBookOne() {
        PlayBook one =  PlayBook.getInstance().api("one").executeTask(taskA, taskB, taskC)
                .aggreTask(simpleAggreTask)
                .dependencys(taskB, taskA)
                .dependencys(taskC, taskA)
                .aggreTaskDepedency(taskA, taskB, taskC)
                .init();
        playBookMap.put(one.getApiName(), one);
    }
    public PlayBook getPlayBook(String a) {
        PlayBook res = this.playBookMap.get(a);
        if (res == null) throw new NoSuchElementException("不存在" + a + "playBook");
        return res;
    }
}
