package util;

public class Arithmetics {
    public static int decrementWithWrap(int x, int bound) {
        x--;
        if (x < 0) {
            x = bound-1;
        }
        return x;
    }

    public static int incrementWithWrap(int x, int bound) {
        x = (x + 1) % bound;
        return x;
    }
}
