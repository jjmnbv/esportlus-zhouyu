package com.kaihei.esportingplus.common.tools;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;

/**
 * 集合操作工具
 *
 * @author 谢思勇
 * @date 2018年8月23日 10:50:02
 */
public final class CollectionUtils {

    private CollectionUtils() {

    }

    /**
     * 从array中找出符合Predicate的元素
     */
    public static <T> Optional<T> find(@NotNull T[] arr, @NotNull Predicate<T> predicate) {
        return find(Arrays.stream(arr), predicate);
    }

    /**
     * 从collection中找出符合Predicate的元素
     */
    public static <T> Optional<T> find(@NotNull Collection<T> collection,
            @NotNull Predicate<T> predicate) {
        return find(collection.stream(), predicate);
    }

    /**
     * 从stream中找出符合Predicate的元素
     */
    private static <T> Optional<T> find(@NotNull Stream<T> stream,
            @NotNull Predicate<T> predicate) {
        return stream.filter(predicate).findFirst();
    }

    /**
     * 从stream中找出符合Predicate的元素
     */
    private static <T> List<T> finds(@NotNull Stream<T> stream,
            @NotNull Predicate<T> predicate) {
        return stream.filter(predicate).collect(Collectors.toList());
    }


    /**
     * 从Collection中找出符合Predicate的元素
     */
    public static <T> List<T> finds(@NotNull Collection<T> collection,
            @NotNull Predicate<T> predicate) {
        return finds(collection.stream(), predicate);
    }

    /**
     * 从集合中找出符合Predicate的元素
     */
    public static <T> List<T> finds(@NotNull T[] ts,
            @NotNull Predicate<T> predicate) {
        return finds(Arrays.stream(ts), predicate);
    }

}
