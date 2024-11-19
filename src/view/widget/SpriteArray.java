package view.widget;

import view.MyColors;
import view.ScreenHandler;
import view.sprites.Sprite;

public class SpriteArray {
    private static final int CHUNK_SIZE_PX = 8;
    public Sprite[][] array;
    public SpriteArray(String name, String map, int width, int height, boolean transform) {
        array = new Sprite[width / CHUNK_SIZE_PX][height / CHUNK_SIZE_PX];
        for (int row = 0; row < array[0].length; ++row) {
            for (int col = 0; col < array.length; ++col) {
                array[col][row] = new Sprite(name + "-" + col + "-" + row, map, col, row, CHUNK_SIZE_PX, CHUNK_SIZE_PX);
                if (transform) {
                    MyColors.transformImage(array[col][row]);
                }
            }
        }
    }

    public void drawYourself(ScreenHandler screenHandler, int x, int y) {
        for (int row = 0; row < array[0].length; ++row) {
            for (int col = 0; col < array.length; ++col) {
                screenHandler.put(x + col, y + row, array[col][row]);
            }
        }
    }

    public int getHeight() {
        return array[0].length * CHUNK_SIZE_PX;
    }

    public int getWidth() {
        return array.length * CHUNK_SIZE_PX;
    }
}
