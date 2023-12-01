package example.Access;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CFAccessService extends AccessService {
    @Override
    public Map<String, String> testSimple(String param) {
        CompletableFuture<String> cfa = this.getPlayBookManager().getTaskA().doHandler(param);
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

    // b -> a
    // c -> a
    // d -> b and d -> c
    // e -> c and e -> a
    // f -> c and f -> d
    // g -> a and g -> c and g -> e
    // h -> b and h -> a and h -> f and h -> c
    @Override
    public Map<String, String> testComplex(String param) {
        CompletableFuture<String> cfa = this.getPlayBookManager().getTaskA().doHandler(param);
        CompletableFuture<String> cfb = cfa.thenCompose(x -> this.getPlayBookManager().getTaskB().doHandler(x));
        CompletableFuture<String> cfc = cfa.thenCompose(x -> this.getPlayBookManager().getTaskC().doHandler(x));
        CompletableFuture<String> cfd = CompletableFuture.allOf(cfb, cfc).thenCompose(x -> this.getPlayBookManager().getTaskD().doHandler(cfb.join(), cfc.join()));
        CompletableFuture<String> cfe = CompletableFuture.allOf(cfa, cfc).thenCompose(x -> this.getPlayBookManager().getTaskE().doHandler(cfc.join(), cfa.join()));
        CompletableFuture<String> cff = CompletableFuture.allOf(cfc, cfd).thenCompose(x -> this.getPlayBookManager().getTaskF().doHandler(cfc.join(), cfd.join()));
        CompletableFuture<String> cfg = CompletableFuture.allOf(cfa, cfc, cfe).thenCompose(x -> this.getPlayBookManager().getTaskG().doHandler(cfa.join(), cfc.join(), cfe.join()));
        CompletableFuture<String> cfh = CompletableFuture.allOf(cfb, cfa, cff, cfc).thenCompose(x -> this.getPlayBookManager().getTaskH().doHandler(cfb.join(), cfa.join(), cff.join(), cfc.join()));
        return CompletableFuture.allOf(cfa, cfb, cfc, cfd, cfe, cff, cfg, cfh).thenApply(x-> {
            Map<String, String> res = new HashMap<>();
            res.put("A", cfa.join());
            res.put("B", cfb.join());
            res.put("C", cfc.join());
            res.put("D", cfd.join());
            res.put("E", cfe.join());
            res.put("F", cff.join());
            res.put("G", cfg.join());
            res.put("H", cfh.join());
            return res;
        }).join();
    }
}
