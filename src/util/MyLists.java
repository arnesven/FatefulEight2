package util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class MyLists {

    public static <E> List<E> filter(List<E> source, Predicate<E> pred) {
        List<E> result = new ArrayList<>();
        for (E e : source) {
            if (pred.test(e)) {
                result.add(e);
            }
        }
        return result;
    }

}
