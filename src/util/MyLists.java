package util;

import model.characters.GameCharacter;
import model.journal.StoryPart;
import model.states.DailyEventState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public static <E> void removeFirstIf(List<E> source, Predicate<E> pred) {
        for (E e : new ArrayList<>(source)) {
            if (pred.test(e)) {
                source.remove(e);
                return;
            }
        }
    }

    public static <E> int maximum(List<E> source, MyUnaryIntFunction<E> fun) {
        int max = Integer.MIN_VALUE;
        for (E e : source) {
            int val = fun.apply(e);
            if (val > max) {
                max = val;
            }
        }
        return max;
    }

    public static <E> int minimum(List<E> source, MyUnaryIntFunction<E> fun) {
        return -1 * maximum(source, e -> -1 * fun.apply(e));
    }

    public static <E> List<E> uniqueify(List<E> listOfEs, MyStringFunction<E> fun) {
        Map<String, E> map = new HashMap<>();
        for (E e : listOfEs) {
            map.put(fun.getString(e), e);
        }
        return new ArrayList<E>(map.values());
    }

    public static <E> List<E> take(List<E> lines, int amount) {
        List<E> result = new ArrayList<>();
        for (int i = 0; i < amount && !lines.isEmpty(); ++i) {
            result.add(lines.get(0));
            lines.remove(0);
        }
        return result;
    }

    public static <E> List<E> toNullessList(E[] array) {
        List<E> result = new ArrayList<>();
        for (E e : array) {
            if (e != null) {
                result.add(e);
            }
        }
        return result;
    }

    public static <E> String commaAndJoin(List<E> list, MyStringFunction<E> fun) {
        if (list.isEmpty()) {
            return "";
        }
        if (list.size() == 1) {
            return fun.getString(list.get(0));
        }
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < list.size() - 1; ++i) {
            buf.append(fun.getString(list.get(i)));
            if (i < list.size() - 2) {
                buf.append(", ");
            }
        }
        buf.append(" and ");
        buf.append(fun.getString(list.get(list.size()-1)));
        return buf.toString();
    }

    public static <E> List<E> generate(int length, MyGeneratorFunction<E> fun) {
        List<E> result = new ArrayList<>();
        for (int i = 0; i < length; ++i) {
            result.add(fun.generate());
        }
        return result;
    }

    public static <E> boolean nonNullAdd(List<E> list, E item) {
        if (item != null) {
            list.add(item);
            return true;
        }
        return false;
    }

    public static <E> E last(List<E> list) {
        return list.get(list.size()-1);
    }

    public static <E> List<E> merge(List<E> left, List<E> right) {
        List<E> result = new ArrayList<>(left);
        result.addAll(right);
        return result;
    }

    public static <E> boolean equal(List<E> left, List<E> right) {
        if (left.size() != right.size()) {
            return false;
        }
        for (int i = 0; i < left.size(); ++i) {
            if (!left.get(i).equals(right.get(i))) {
                return false;
            }
        }
        return true;
    }
}
