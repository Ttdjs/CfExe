package example;

import example.Access.CFAccessService;
import example.Access.DirectrAccessService;
import example.Manager.PlayBookManager;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import wjp.director.domain.ApiContext;
import wjp.director.domain.DTO.DataDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class SceneTest {
    CFAccessService cfAccessService = new CFAccessService();
    DirectrAccessService directrAccessService = new DirectrAccessService();
    PlayBookManager playBookManager = new PlayBookManager();
    int count = 10;

    @Test
    void testSimple() {
        Assertions.assertTrue(sceneTest(count, directrAccessService::testSimple, cfAccessService::testSimple, "simple"));
    }

    @Test
    void testComplex() {
        Assertions.assertTrue(sceneTest(count, directrAccessService::testComplex, cfAccessService::testComplex, "complex"));
    }

    @Test
    void testRpcTaskA() {
        ApiContext apiContext = ApiContext.builder().playBook(playBookManager.getPlayBook("simple")).paramDTO(null).build();
        DataDTO<Map<String, String>> simple = playBookManager.getPlayBook("simple").execute(apiContext);
        Map<String, String> res = new HashMap<>();
        res.put("A", null);
        res.put("B", "Bnull");
        res.put("C", "Cnull");
        Assertions.assertEquals(simple.getData(), res);
    }



    private  boolean sceneTest(int count, Function<String, Map<String, String>> directorFun, Function<String, Map<String, String>> cfFun, String param) {
        boolean res = true;
        long startTime = System.currentTimeMillis();
        Map<String, String> directorRes = null;
        for (int i = 0; i < count; i++) {
            directorRes = directorFun.apply(param);
        }
        long midTime = System.currentTimeMillis();
        Map<String, String> cfRes = null;
        for (int i = 0; i < count; i++) {
            cfRes = cfFun.apply(param);
        }
        long endTime = System.currentTimeMillis();
        long directorTime = midTime - startTime;
        long cfTime = endTime - midTime;
        if (Math.abs(directorTime - cfTime) / count > 5) res = false;
        System.out.println(String.format("CF执行次数%d, 耗时%d, 结果%s", count, endTime - midTime, cfRes.toString()));
        System.out.println(String.format("Director执行次数%d, 耗时%d, 结果%s", count, midTime - startTime, directorRes.toString()));
        Map<String, String> finalCfRes = cfRes;
        cfRes.forEach((key, value) -> {
            System.out.println("key : " + key + " " + "directorRes: " + value + " cfRes: " + finalCfRes.get(key) + " eauqls: " + value.equals(finalCfRes.get(key)));
        });
        if (!Objects.equals(finalCfRes, directorRes)) res = false;
        return res;

    }
}
