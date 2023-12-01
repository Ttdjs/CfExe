package wjp.director.example;

import wjp.director.domain.ApiContext;
import wjp.director.domain.DTO.DataDTO;
import wjp.director.domain.PlayBook;
import wjp.director.example.Access.CFAccessService;
import wjp.director.example.Access.DirectrAccessService;
import wjp.director.example.Manager.PlayBookManager;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class Tester {


    private static void testSimple(int count) {
        CFAccessService cfAccessService = new CFAccessService();
        DirectrAccessService directrAccessService = new DirectrAccessService();
        Object res = Collections.emptyMap();
        long startTime = System.currentTimeMillis();
        for (int i  = 0; i < count; i++) {
            res = directrAccessService.testSimple("");
        }

        long midTime = System.currentTimeMillis();
        System.out.println(String.format("Director执行次数%d, 耗时%d, 结果%s", count, midTime - startTime, res.toString()));
        for (int i = 0; i < count; i++) {
           res = cfAccessService.testSimple("");
        }
        long endTime = System.currentTimeMillis();
        System.out.println(String.format("CF执行次数%d, 耗时%d, 结果%s", count, endTime - midTime, res.toString()));
//        System.out.println(res);
    }
    private static void test(int count, Function<String, Map<String, String>> directorFun, Function<String, Map<String, String>> cfFun, String param) {
        long startTime = System.currentTimeMillis();
        Map<String, String> directorRes = null;
        for (int i = 0; i < count; i++) {
            directorRes = directorFun.apply(param);
        }

        long midTime = System.currentTimeMillis();
        System.out.println(String.format("Director执行次数%d, 耗时%d, 结果%s", count, midTime - startTime, directorRes.toString()));
        Map<String, String> cfRes = null;
        for (int i = 0; i < count; i++) {
            cfRes = cfFun.apply(param);
        }
        long endTime = System.currentTimeMillis();
        System.out.println(String.format("CF执行次数%d, 耗时%d, 结果%s", count, endTime - midTime, cfRes.toString()));

        Map<String, String> finalCfRes = cfRes;
        cfRes.forEach((key, value) -> {
            System.out.println("key : " + key + " " + "directorRes: " + value + " cfRes: " + finalCfRes.get(key) + " eauqls: " + value.equals(finalCfRes.get(key)));
        });
    }
    public static void main(String[] args) {
        DirectrAccessService directrAccessService = new DirectrAccessService();
        CFAccessService cfAccessService = new CFAccessService();
        test(10, directrAccessService::testSimple, cfAccessService::testSimple, "simple");
        test(10, directrAccessService::testComplex, cfAccessService::testComplex, "complex");
    }
}
