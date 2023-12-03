package example;

import example.Manager.RpcManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class CfTest {
    @Test
    public void testCompose() {
        CompletableFuture<String> cfA = RpcManager.get("a");
        CompletableFuture<String> cfB = cfA.thenCompose(x -> {
            return RpcManager.get("b").thenApply(ele -> {
                throw new RuntimeException("a");
            });
        });
        Assertions.assertThrows(CompletionException.class, cfB::join);
    }
}
