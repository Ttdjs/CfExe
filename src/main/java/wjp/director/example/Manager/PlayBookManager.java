package wjp.director.example.Manager;

import lombok.Getter;
import wjp.director.domain.PlayBook;
import wjp.director.example.Task.*;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Getter
public class PlayBookManager {
    private Map<String, PlayBook> playBookMap = new HashMap<>();
    private RpcTaskA taskA =  new RpcTaskA();
    private RpcTaskB taskB =  new RpcTaskB();
    private RpcTaskC taskC =  new RpcTaskC();
    private RpcTaskD taskD =  new RpcTaskD();
    private RpcTaskE taskE =  new RpcTaskE();
    private RpcTaskF taskF =  new RpcTaskF();
    private RpcTaskG taskG =  new RpcTaskG();
    private RpcTaskH taskH =  new RpcTaskH();
    private SimpleAggreTask simpleAggreTask = new SimpleAggreTask();
    private ComplexAggreTask complexAggreTask = new ComplexAggreTask();
    public PlayBookManager() {
        initPlayBookSimple();
        initPlayBookComplex();
    }
    private void initPlayBookSimple() {
        PlayBook simple =  PlayBook.getInstance().api("simple").executeTask(taskA, taskB, taskC)
                .aggreTask(simpleAggreTask)
                .dependencys(taskB, taskA)
                .dependencys(taskC, taskA)
                .aggreTaskDepedency(taskA, taskB, taskC)
                .init();
        playBookMap.put(simple.getApiName(), simple);
    }
    // b -> a
    // c -> a
    // d -> b and d -> c
    // e -> c and e -> a
    // f -> c and f -> d
    // g -> a and g -> c and g -> e
    // h -> b and h -> a and h -> f and h -> c

    // a
    // b , c
    // d , e
    // f, g
    // h
    private void initPlayBookComplex() {
        PlayBook complex = PlayBook.getInstance().api("complex")
                .executeTask(taskA, taskB, taskC, taskD, taskE, taskF, taskG, taskH)
                .aggreTask(complexAggreTask)
                .aggreTaskDepedency(taskA, taskB, taskC, taskD, taskE, taskF, taskG, taskH)
                .dependencys(taskB, taskA)
                .dependencys(taskC, taskA)
                .dependencys(taskD, taskB, taskC)
                .dependencys(taskE, taskC, taskA)
                .dependencys(taskF, taskC, taskD)
                .dependencys(taskG, taskA, taskC, taskE)
                .dependencys(taskH, taskB, taskA, taskF, taskC)
                .init();
        playBookMap.put("complex", complex);
    }
    public PlayBook getPlayBook(String a) {
        PlayBook res = this.playBookMap.get(a);
        if (res == null) throw new NoSuchElementException("不存在" + a + "playBook");
        return res;
    }
}
