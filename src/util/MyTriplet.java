package util;

import java.io.Serializable;

public class MyTriplet<T1, T2, T3> implements Serializable {
    public T1 first;
    public T2 second;
    public T3 third;

    public MyTriplet(T1 t1, T2 t2, T3 t3) {
        first = t1;
        second = t2;
        third = t3;
    }
}
