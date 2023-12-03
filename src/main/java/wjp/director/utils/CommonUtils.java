package wjp.director.utils;

import java.util.Collection;
import java.util.Objects;

public class CommonUtils {
    public static boolean noDuplicates(Collection<?> collection) {
        Objects.requireNonNull(collection);
        long count = collection.stream().distinct().count();
        return count == collection.size();
    }
}
