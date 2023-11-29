package wjp.director.example;

import wjp.director.domain.ApiContext;
import wjp.director.domain.DTO.DataDTO;
import wjp.director.domain.PlayBook;
import wjp.director.example.Manager.PlayBookManager;

public class Tester {
    static PlayBookManager  playBookManager =  new PlayBookManager();
    private static void testOne() {
        PlayBook one = playBookManager.getPlayBook("one");
        ApiContext apiContext = ApiContext.builder().playBook(one).paramDTO("wjp").build();
        DataDTO<String> res = one.execute(apiContext);
        System.out.println(res.getData());
    }
    public static void main(String[] args) {
        testOne();
    }
}
