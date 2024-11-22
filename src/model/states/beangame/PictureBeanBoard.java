package model.states.beangame;

import view.sprites.Sprite;

import java.util.Map;

public class PictureBeanBoard extends BeanGameBoard {

    public PictureBeanBoard(String name, Sprite sprite, Map<Character, Character> colorMap, int numberOfPockets, int pocketLength) {
        super(name, makeMatrix(sprite, colorMap, pocketLength), numberOfPockets, pocketLength,
                BeanGameBoardMaker.generatePrizes(numberOfPockets));
    }

    private static void remapColors(char[][] oldMatrix, char[][] newMatrix, Map<Character, Character> colorMap) {
        for (int y = 0; y < oldMatrix[0].length; ++y) {
            for (int x = 0; x < oldMatrix.length; ++x) {
                newMatrix[x][y] = colorMap.get(oldMatrix[x][y]);
            }
        }
    }

    private static char[][] makeMatrix(Sprite spr, Map<Character, Character> map, int pocketLength) {
        char[][] oldMatrix = spr.getCharArray();
        char[][] newMatrix = new char[oldMatrix.length][oldMatrix[0].length+3];
        BeanGameBoardMaker.clear(newMatrix);
        remapColors(oldMatrix, newMatrix, map);
        BeanGameBoardMaker.makePockets(newMatrix, pocketLength);
        return newMatrix;
    }

}
