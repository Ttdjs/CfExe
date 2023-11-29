package wjp.director.example.Manager;

import java.util.concurrent.CompletableFuture;

public class RpcManager {
    public CompletableFuture<String> get(String echo) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000l);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return echo;
        });
    }
}
