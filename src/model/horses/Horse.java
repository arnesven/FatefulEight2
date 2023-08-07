package model.horses;

import view.MyColors;
import view.sprites.Sprite;

import java.io.Serializable;

public abstract class Horse implements Serializable {
    private static final Sprite BACKGROUND = new HorseSprite(0, 3, MyColors.LIGHT_GRAY, MyColors.GREEN, MyColors.DARK_GREEN, MyColors.LIGHT_BLUE);
    private final String name;
    private int cost;
    private String type;

    public Horse(String type, String name, int cost) {
        this.type = type;
        this.name = name;
        this.cost = cost;
    }

    public static Sprite getBackgroundSprite() {
        return BACKGROUND;
    }

    public abstract Sprite getSprite();

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public abstract String getInfo();

    public String getType() {
        return type;
    }

    protected static class HorseSprite extends Sprite {
        public HorseSprite(int col, int row, MyColors color1, MyColors color2, MyColors color3, MyColors color4) {
            super("horse", "horses.png", col, row, 64, 64);
            setColor1(color1);
            setColor2(color2);
            setColor3(color3);
            setColor4(color4);
        }
    }
}
