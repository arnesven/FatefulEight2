package util;


import model.characters.GameCharacter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
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

    public static <E> void forEach(List<E> source, MyUnaryFunction<E> fun) {
        for (E e : source) {
            fun.apply(e);
        }
    }

    public static <E, R> List<R> transform(List<E> source, Function<E, R> fun) {
        List<R> result = new ArrayList<>();
        for (E e : source) {
            result.add(fun.apply(e));
        }
        return result;
    }
}
