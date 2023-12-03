package example.Manager;

import example.Task.*;
import lombok.Getter;
import wjp.director.domain.Executor;
import wjp.director.domain.Scene;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Getter
public class TestDataManager {
    public static Executor executor = new Executor("sceneTest");
    public static RpcTaskA taskA =  new RpcTaskA();
    public static RpcTaskB taskB =  new RpcTaskB();
    public static RpcTaskC taskC =  new RpcTaskC();
    public static RpcTaskD taskD =  new RpcTaskD();
    public static RpcTaskE taskE =  new RpcTaskE();
    public static RpcTaskF taskF =  new RpcTaskF();
    public static RpcTaskG taskG =  new RpcTaskG();
    public static RpcTaskH taskH =  new RpcTaskH();
    public static RpcTaskSyncB taskSync = new RpcTaskSyncB();
    public static RpcTaskSyncC taskSync2 = new RpcTaskSyncC();
    public static RpcTaskRetVoid taskRetVoid = new RpcTaskRetVoid();
    public static RpcTaskForceAsyncB taskForceAsync = new RpcTaskForceAsyncB();
    public static RpcTaskForceAsyncC taskForceAsync2 = new RpcTaskForceAsyncC();
    public static RpcTaskDataDTO taskDataDTO = new RpcTaskDataDTO();
    public static SimpleAggreTask simpleAggreTask = new SimpleAggreTask();
    public static ComplexAggreTask complexAggreTask = new ComplexAggreTask();
    public static FourTaskSyncAggreTask fourTaskSyncAggreTask =  new FourTaskSyncAggreTask();

    static  {
        initPlayBookSimple();
        initPlayBookComplex();
        initPlayBookDataDTO();
        initPlayBookForTaskForceAsync();
        initPlayBookForTaskSync();
        initPlayBookRetVoid();
        initPlayBookSimpleDefaultValue();
    }
    public static void initPlayBookSimple() {
        Scene simple =  Scene.getInstance().sceneName("simple").executeTask(taskA, taskB, taskC)
                .aggreTask(simpleAggreTask)
                .dependency(taskB, taskA)
                .dependency(taskC, taskA)
                .aggreTaskDepedency(taskA, taskB, taskC)
                .init(executor);
    }
    public static void initPlayBookSimpleDefaultValue() {
        Scene simple =  Scene.getInstance().sceneName("simpleDefaultValue").executeTask(taskA, taskB, taskC)
                .aggreTask(simpleAggreTask)
                .dependency(taskB, taskA)
                .dependency(taskC, taskA)
                .aggreTaskDepedency(taskA, taskB, taskC)
                .defaultValue(taskA, "Awjp")
                .init(executor);
    }

    public static void initPlayBookRetVoid() {
        Scene simple =  Scene.getInstance().sceneName("retVoid").executeTask(taskA, taskB, taskRetVoid)
                .aggreTask(simpleAggreTask)
                .dependency(taskB, taskA)
                .dependency(taskRetVoid, taskA)
                .aggreTaskDepedency(taskA, taskB, taskRetVoid)
                .init(executor);
    }
   // a
    // taskSync, taskSync2
    // c
    public static void initPlayBookForTaskSync() {
        Scene simple =  Scene.getInstance().sceneName("fourTaskSync").executeTask(taskA, taskSync2,  taskSync)
                .aggreTask(simpleAggreTask)
                .dependency(taskSync, taskA)
                .dependency(taskSync2, taskA)
//                .dependencys(taskC, taskSync)
                .aggreTaskDepedency(taskA, taskSync, taskSync2)
                .init(executor);
    }
    public static void initPlayBookForTaskForceAsync() {
        Scene simple =  Scene.getInstance().sceneName("fourTaskForceAsync").executeTask(taskA, taskForceAsync,  taskForceAsync2)
                .aggreTask(simpleAggreTask)
                .dependency(taskForceAsync, taskA)
                .dependency(taskForceAsync2, taskA)
                .aggreTaskDepedency(taskA, taskForceAsync, taskForceAsync2)
                .init(executor);
    }

    // a
    // b , c
    // d , e
    // f, g
    // h
    public static void initPlayBookComplex() {
        Scene complex = Scene.getInstance().sceneName("complex")
                .executeTask(taskA, taskB, taskC, taskD, taskE, taskF, taskG, taskH)
                .aggreTask(complexAggreTask)
                .aggreTaskDepedency(taskA, taskB, taskC, taskD, taskE, taskF, taskG, taskH)
                .dependency(taskB, taskA)
                .dependency(taskC, taskA)
                .dependency(taskD, taskB, taskC)
                .dependency(taskE, taskC, taskA)
                .dependency(taskF, taskC, taskD)
                .dependency(taskG, taskA, taskC, taskE)
                .dependency(taskH, taskB, taskA, taskF, taskC)
                .init(executor);
    }
    public static void initPlayBookDataDTO() {
        Scene dataDTO =  Scene.getInstance().sceneName("dataDTO")
                .executeTask(taskA, taskB, taskC, taskDataDTO)
                .aggreTask(simpleAggreTask)
                .dependency(taskB, taskA)
                .dependency(taskC, taskA)
                .dependency(taskDataDTO, taskB, taskC)
                .aggreTaskDepedency(taskA, taskB, taskDataDTO)
                .init(executor);
    }
}
