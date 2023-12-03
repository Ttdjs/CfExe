package example;

import example.Access.CFAccessService;
import example.Access.DirectrAccessService;
import example.Manager.TestDataManager;
import example.Task.RpcTaskParamDot;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import wjp.director.annotation.HandlerMethod;
import wjp.director.domain.Context;
import wjp.director.domain.DTO.DataDTO;
import wjp.director.domain.Scene;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SceneTest {
    CFAccessService cfAccessService = new CFAccessService();
    DirectrAccessService directrAccessService = new DirectrAccessService();
    private final Res allRight =  new Res(true, true);
    int count = 10;

    @Test
    void testSimple() {
        Assertions.assertEquals(allRight ,sceneTest(count, directrAccessService::testSimple, cfAccessService::testSimple, "simple"));
    }

    @Test
    void testComplex() {
        Assertions.assertEquals(allRight, sceneTest(count, directrAccessService::testComplex, cfAccessService::testComplex, "complex"));
    }
    @Test
    void testSimpleDefaultValue() {
        Assertions.assertEquals(allRight ,sceneTest(count, x -> directrAccessService.test("simpleDefaultValue", x), directrAccessService::testSimple, "wjp"));
    }

    @Test
    void testSimpleError() {
        Map<String, String> simple = directrAccessService.test("simple", null);
        Map<String, String> res = new HashMap<>();
        res.put("A", null);
        res.put("B", "Bnull");
        res.put("C", "Cnull");
        Assertions.assertEquals(simple, res);
    }
    @Test
    void testRpcDataDTO() {
        Map<String, String> reaRes = directrAccessService.test("dataDTO", null);
        Map<String, String> res = new HashMap<>();
        res.put("A", null);
        res.put("B", "Bnull");
        res.put("C", "DDataDTO(code=0, message=null, exception=null, data=Bnull)Cnull");
        Assertions.assertEquals(reaRes, res);
    }

    @Test
    void testForce() {
        Assertions.assertEquals(allRight ,sceneTest(10, x -> directrAccessService.test("fourTaskForceAsync", x), x -> directrAccessService.test("simple", x), "async"));
    }
    @Test
    void testSync() {
        Assertions.assertEquals(new Res(false, true) ,sceneTest(10, x -> directrAccessService.test("fourTaskSync", x), x -> directrAccessService.test("simple", x), "async"));
    }
    @Test
    public void testRetVoid() {
        Map<String, String> expectRes = directrAccessService.test("retVoid", "retVoid");
        Map<String, String> res = new HashMap<>();
        res.put("A", "AretVoid");
        res.put("B", "BAretVoid");
        res.put("C", null);
        Assertions.assertEquals(expectRes, res);
    }

    @Test
    public void testParamDot() {
        RpcTaskParamDot rpcTaskParamDot = new RpcTaskParamDot();
        Method[] declaredMethods = rpcTaskParamDot.getClass().getDeclaredMethods();
        List<Method> methods = Arrays.stream(declaredMethods).filter(x -> x.isAnnotationPresent(HandlerMethod.class)).collect(Collectors.toList());
        Method method = methods.get(0);
        Assertions.assertEquals(1,  method.getParameterCount());
    }
    private  Res sceneTest(int count, Function<String, Map<String, String>> fun1, Function<String, Map<String, String>> fun2, String param) {
        Res res = new Res();
        long startTime = System.currentTimeMillis();
        Map<String, String> directorRes = null;
        for (int i = 0; i < count; i++) {
            directorRes = fun1.apply(param);
        }
        long midTime = System.currentTimeMillis();
        Map<String, String> cfRes = null;
        for (int i = 0; i < count; i++) {
            cfRes = fun2.apply(param);
        }
        long endTime = System.currentTimeMillis();
        long directorTime = midTime - startTime;
        long cfTime = endTime - midTime;
        res.timeRes = Math.abs(directorTime - cfTime) / count <= 5;
        System.out.println(String.format("fun1执行次数%d, 耗时%d, 结果%s", count, midTime - startTime, directorRes.toString()));
        System.out.println(String.format("fun2执行次数%d, 耗时%d, 结果%s", count, endTime - midTime, cfRes.toString()));
        Map<String, String> finalCfRes = cfRes;
        res.dataRes = Objects.equals(finalCfRes, directorRes);
        return res;
    }


    @EqualsAndHashCode
    @AllArgsConstructor
    @NoArgsConstructor
    class Res {
        boolean timeRes;
        boolean dataRes;
    }


}
