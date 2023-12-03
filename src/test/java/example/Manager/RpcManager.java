package example.Manager;

import java.util.concurrent.CompletableFuture;

public class RpcManager {
    private long time = 50L;
    public CompletableFuture<String> get(String echo) {
        return CompletableFuture.supplyAsync(() -> {
            try {
//                System.out.println(Thread.currentThread() + " " + System.currentTimeMillis() / 1000 + " ");
                Thread.sleep(time);
//                System.out.println(System.currentTimeMillis() +" " + Thread.currentThread().getName() + echo);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return echo;
        });
    }
}
