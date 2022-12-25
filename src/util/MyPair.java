package util;

import java.io.Serializable;

public class MyPair<T, U> implements Serializable {
    public T first;
    public U second;

    public MyPair(T x, U y) {
        this.first = x;
        this.second = y;
    }
}
