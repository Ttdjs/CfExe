package wjp.director.example.Access;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CFAccessService extends AccessService{
    @Override
    public Map<String, String> testSimple() {
        CompletableFuture<String> cfa = this.getPlayBookManager().getTaskA().doHandler("wjp");
        CompletableFuture<String> cfb = cfa.thenCompose(x -> this.getPlayBookManager().getTaskB().doHandler(x));
        CompletableFuture<String> cfc = cfa.thenCompose(x -> this.getPlayBookManager().getTaskC().doHandler(x));
        return CompletableFuture.allOf(cfa,cfb,cfc).thenApply(x -> {
            Map<String, String> res = new HashMap<>();
            res.put("A", cfa.join());
            res.put("B", cfb.join());
            res.put("C", cfc.join());
            return res;
        }).join();
    }
}
