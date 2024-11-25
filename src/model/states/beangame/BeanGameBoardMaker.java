package model.states.beangame;

import util.MyPair;
import util.MyRandom;
import view.MyColors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BeanGameBoardMaker {

    public static final MyColors BACK_COLOR = MyColors.BROWN;
    public static final MyColors FRAME_COLOR = MyColors.GRAY_RED;

    private static final List<MyPair<Integer, Integer>> POCKET_SIZES = List.of(
            new MyPair<>(5, 6),
            new MyPair<>(6, 5),
            new MyPair<>(7, 4));

    private static final List<Character> COLOR_CHARS = List.of(
            'g', 'p', 'y',
            'b', 'G', 'w',
            'k');

    private static final List<MyColors> COLORS = List.of(
            MyColors.LIGHT_GRAY, MyColors.LIGHT_RED, MyColors.YELLOW,
            MyColors.LIGHT_BLUE, MyColors.GREEN, MyColors.WHITE,
            MyColors.DARK_GRAY);

    private static final int DEFAULT_LENGTH = 90;

    public static BeanGameBoard generateBeanMatrix() {
        MyPair<Integer, Integer> pocketsAndSize = MyRandom.sample(POCKET_SIZES);
        char[][] matrix = new char[pocketsAndSize.first * pocketsAndSize.second - 1][DEFAULT_LENGTH];
        clear(matrix);
        makeDiagonalMatrix(matrix);
        makePockets(matrix, pocketsAndSize.second);
        List<Integer> prizes = generatePrizes(pocketsAndSize.first);
        return new BeanGameBoard("Abstract", matrix, pocketsAndSize.first, pocketsAndSize.second, prizes);
    }

    protected static void clear(char[][] matrix) {
        for (int row = 0; row < matrix[0].length; ++row) {
            for (int col = 0; col < matrix.length; ++col) {
                matrix[col][row] = ' ';
            }
        }
    }

    protected static void makePockets(char[][] matrix, int pocketLength) {
        for (int row = 1; row < matrix[0].length; ++row) {
            for (int col = 0; col < matrix.length; ++col) {
                if (row > matrix[0].length - 3 && col % pocketLength == pocketLength-1) {
                    matrix[col][row] = 'g';
                }
            }
        }
    }

    private static void makeDiagonalMatrix(char[][] matrix) {
        char color = randomColor();
        int density = 8;
        for (int row = 1; row < matrix[0].length; ++row) {
            for (int col = 0; col <= matrix.length / 2; ++col) {
                if (row < matrix[0].length - 4) {
                    boolean pin = MyRandom.randInt(density) == 0;
                    if (pin) {
                        matrix[col][row] = color;
                        matrix[matrix.length - col - 1][row] = color;
                    }
                    if (MyRandom.randInt(32) == 0) {
                        color = randomColor();
                        density = MyRandom.randInt(6, 10);
                    }
                }
            }
        }
    }


    public static List<Integer> generatePrizes(int pockets) {
        int prizeTotal = pockets + 1;
        List<Integer> prizes = List.of(2, 3, 5, 7);
        List<Integer> result = new ArrayList<>();
        while (result.size() < pockets - 1 && prizeTotal > 1) {
            int prize = MyRandom.sample(prizes);
            if (prize <= prizeTotal) {
                result.add(prize);
                prizeTotal -= prize;
            }
        }
        while (result.size() < pockets) {
            result.add(0);
        }
        Collections.shuffle(result);
        return result;
    }


    private static char randomColor() {
        return MyRandom.sample(COLOR_CHARS);
    }

    public static MyColors getColor(char c) {
        int index = COLOR_CHARS.indexOf(c);
        if (index < 0) {
            System.err.println("Erroneous character: " + c);
        }
        return COLORS.get(index);
    }
}
