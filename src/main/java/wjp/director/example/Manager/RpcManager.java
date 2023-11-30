package wjp.director.example.Manager;

import java.util.concurrent.CompletableFuture;

public class RpcManager {
    public CompletableFuture<String> get(String echo) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(50l);
//                System.out.println(System.currentTimeMillis() +" " + Thread.currentThread().getName() + echo);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return echo;
        });
    }
}
