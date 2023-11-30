package wjp.director.example.Access;

import wjp.director.domain.ApiContext;
import wjp.director.domain.DTO.DataDTO;
import wjp.director.domain.PlayBook;

import java.util.Map;

public class DirectrAccessService extends AccessService{

    @Override
    public Map<String, String> testSimple() {
        PlayBook one = this.getPlayBookManager().getPlayBook("simple");
        ApiContext apiContext = ApiContext.builder().playBook(one).paramDTO("wjp").build();
        DataDTO<Map<String, String>> res = one.execute(apiContext);
        return res.getData();
    }
    public Map<String, String> testComplex(String param) {
        PlayBook one = this.getPlayBookManager().getPlayBook("complex");
        ApiContext apiContext = ApiContext.builder().playBook(one).paramDTO(param).build();
        DataDTO<Map<String, String>> res = one.execute(apiContext);
        return res.getData();
    }

}