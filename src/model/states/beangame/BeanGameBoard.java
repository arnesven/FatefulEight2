package model.states.beangame;

import sprites.BeanGameMiniSprite;
import view.sprites.Sprite;

import java.util.List;

public class BeanGameBoard {

    private final char[][] matrix;
    private final int numberOfPockets;
    private final int pocketWidth;
    private final List<Integer> prizes;
    private final String name;

    public BeanGameBoard(String name, char[][] matrix, int numberOfPockets, int pocketWidth, List<Integer> prizes) {
        this.name = name;
        this.matrix = matrix;
        this.numberOfPockets = numberOfPockets;
        this.pocketWidth = pocketWidth;
        this.prizes = prizes;
    }

    public int boardLength() {
        return matrix[0].length;
    }

    public int boardWidth() {
        return matrix.length;
    }

    public boolean isAPin(int x, int y) {
        return 0 <= x && x < matrix.length &&
                0 <= y && y < matrix[0].length &&
                matrix[x][y] != ' ';
    }

    public char getCell(int col, int row) {
        return matrix[col][row];
    }

    public int getNumberOfPockets() {
        return numberOfPockets;
    }

    public int pocketLength() {
        return pocketWidth;
    }

    public int getPrize(int pocket) {
        return prizes.get(pocket);
    }

    public Sprite makeMiniSprite() {
        return new BeanGameMiniSprite(this);
    }

    public String getName() {
        return name;
    }
}
