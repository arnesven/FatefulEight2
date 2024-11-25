package util;

public class MyMatrices {
    public static <E> void traversRowWise(E[][] matrix, MatrixFunction<E> fun) {
        for (int y = 0; y < matrix[0].length; ++y) {
            for (int x = 0; x < matrix.length; ++x) {
                fun.apply(matrix, x, y);
            }
        }
    }
}
