package model.states.digging;

import model.SteppingMatrix;
import util.MyLists;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.Sprite;
import view.sprites.Sprite16x16;

import java.awt.*;
import java.util.List;

public class DiggingSpace {
    private static final Sprite UNDUG = new Sprite16x16("undug", "lotto.png",
            0x31, MyColors.BLACK, MyColors.GREEN, MyColors.BEIGE, MyColors.CYAN);
    private static final Sprite MARKED = new Sprite16x16("marked", "lotto.png",
            0x33, MyColors.RED, MyColors.GREEN, MyColors.BEIGE, MyColors.CYAN);
    private static final Sprite DUG = new Sprite16x16("dug", "lotto.png",
            0x32, MyColors.TAN, MyColors.GREEN, MyColors.BROWN, MyColors.CYAN);
    private static final Sprite BOULDER_SPRITE = new Sprite16x16("boulder", "lotto.png",
            0x34, MyColors.BLACK, MyColors.GRAY, MyColors.BEIGE, MyColors.CYAN);
    private static final MyColors[] NUMBER_COLORS = new MyColors[]{MyColors.BLUE,
            MyColors.RED, MyColors.ORC_GREEN, MyColors.YELLOW, MyColors.PURPLE,
            MyColors.ORANGE, MyColors.WHITE, MyColors.BLACK};
    private static final Sprite16x16[] NUMBER_SPRITES = makeNumberSprites();

    private final int x;
    private final int y;
    private boolean isDug = false;
    private boolean boulder;
    private int number = 0;
    private boolean marked = false;

    public DiggingSpace(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void drawYourself(ScreenHandler screenHandler, int x, int y) {
        if (isDug) {
            screenHandler.put(x, y, DUG);
            if (number > 0) {
                screenHandler.register(NUMBER_SPRITES[number-1].getName(),
                        new Point(x, y), NUMBER_SPRITES[number-1]);
            } else if (hasBoulder()) {
                screenHandler.register(BOULDER_SPRITE.getName(),
                        new Point(x, y), BOULDER_SPRITE);
            }
        } else {
            if (marked) {
                screenHandler.put(x, y, MARKED);
            } else {
                screenHandler.put(x, y, UNDUG);
            }
        }
    }

    public void setBoulder(boolean b) {
        this.boulder = b;
    }

    public boolean isDug() {
        return isDug;
    }

    public void setDug(boolean b) {
        isDug = b;
    }

    public void setNumber(SteppingMatrix<DiggingSpace> matrix) {
        if (!hasBoulder()) {
            List<DiggingSpace> adjacent = findAdjacentSpaces(matrix);
            this.number = MyLists.intAccumulate(adjacent, (DiggingSpace space) -> space.hasBoulder() ? 1 : 0);
        }
    }

    public boolean hasBoulder() {
        return boulder;
    }


    private static Sprite16x16[] makeNumberSprites() {
        Sprite16x16[] result = new Sprite16x16[8];
        for (int i = 0; i < result.length; ++i) {
            result[i] = new Sprite16x16("dignumber"+(i+1), "lotto.png", 0x40 + i,
                    NUMBER_COLORS[i]);
        }
        return result;
    }

    public void printYourself() {
        if (hasBoulder()) {
            System.out.print("##");
        } else {
            if (number > 0) {
                System.out.print("_" + number);
            } else {
                System.out.print("__");
            }
        }
    }

    public void toggleMarked() {
        this.marked = !marked;
    }

    public int getNumber() {
        return number;
    }

    public List<DiggingSpace> findAdjacentSpaces(SteppingMatrix<DiggingSpace> matrix) {
        return MyLists.filter(matrix.getElementList(),
                (DiggingSpace space) -> Math.abs(space.x - x) <= 1 && Math.abs(space.y - y) <= 1);
    }

    public boolean isMarked() {
        return marked;
    }
}
