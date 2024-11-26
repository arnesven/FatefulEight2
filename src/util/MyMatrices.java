package util;

import java.awt.*;
import java.util.function.Predicate;

public class MyMatrices {
    public static <E> void traversRowWise(E[][] matrix, MatrixFunction<E> fun) {
        for (int y = 0; y < matrix[0].length; ++y) {
            for (int x = 0; x < matrix.length; ++x) {
                fun.apply(matrix, x, y);
            }
        }
    }

    public static <E> Point findElement(E[][] matrix, Predicate<E> pred) {
        for (int y = 0; y < matrix[0].length; ++y) {
            for (int x = 0; x < matrix.length; ++x) {
                if (pred.test(matrix[x][y])) {
                    return new Point(x, y);
                }
            }
        }
        return null;
    }
}
