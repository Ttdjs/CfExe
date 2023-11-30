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

public class Tester {


    private static void testSimple(int count) {
        CFAccessService cfAccessService = new CFAccessService();
        DirectrAccessService directrAccessService = new DirectrAccessService();
        Object res = Collections.emptyMap();
        long startTime = System.currentTimeMillis();
        for (int i  = 0; i < count; i++) {
            res = directrAccessService.testSimple();
        }

        long midTime = System.currentTimeMillis();
        System.out.println(String.format("Director执行次数%d, 耗时%d, 结果%s", count, midTime - startTime, res.toString()));
        for (int i = 0; i < count; i++) {
           res = cfAccessService.testSimple();
        }
        long endTime = System.currentTimeMillis();
        System.out.println(String.format("CF执行次数%d, 耗时%d, 结果%s", count, endTime - midTime, res.toString()));
//        System.out.println(res);
    }
    private static void testComplex(int count) {
        long startTime = System.currentTimeMillis();
        DirectrAccessService directrAccessService = new DirectrAccessService();
        Map<String, String> res = directrAccessService.testComplex("");
        long midTime = System.currentTimeMillis();
        System.out.println(String.format("Director执行次数%d, 耗时%d, 结果%s", count, midTime - startTime, res.toString()));
    }
    public static void main(String[] args) {
//        testOneByDirector();
//        testSimple(100);
        testComplex(1);
    }
}
