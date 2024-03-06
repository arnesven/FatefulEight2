package util;


import model.characters.GameCharacter;
import model.items.spells.Spell;

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

    public static <E> boolean all(List<E> source, Predicate<E> pred) {
        for (E e : source) {
            if (!pred.test(e)) {
                return false;
            }
        }
        return true;
    }

    public static <E> int intAccumulate(List<E> source,
                                        MyUnaryIntFunction<E> fun) {
        int result = 0;
        for (E e : source) {
            result += fun.apply(e);
        }
        return result;
    }

    public static <E> boolean any(List<E> source, Predicate<E> pred) {
        for (E e : source) {
            if (pred.test(e)) {
                return true;
            }
        }
        return false;
    }

    public static <E> double doubleAccumulate(List<E> source,
                                             MyUnaryDoubleFunction<E> fun) {
        double result = 0.0;
        for (E e : source) {
            result += fun.apply(e);
        }
        return result;
    }

    public static <E> E find(List<E> source, Predicate<E> pred) {
        for (E e : source) {
            if (pred.test(e)) {
                return e;
            }
        }
        return null;
    }

    public static <E> int maximum(List<E> source, MyUnaryIntFunction<E> fun) {
        int max = 0;
        for (E e : source) {
            int val = fun.apply(e);
            if (val > max) {
                max = val;
            }
        }
        return max;
    }
}
