package model.horses;

import view.MyColors;
import view.sprites.Sprite;

import java.io.Serializable;

public abstract class Horse implements Serializable {
    private static final Sprite BACKGROUND = new HorseSprite(0, 3, MyColors.LIGHT_GRAY, MyColors.GREEN, MyColors.DARK_GREEN, MyColors.LIGHT_BLUE);
    private final String name;
    private final MyColors avatarColor;
    private int cost;
    private String type;

    public Horse(String type, String name, int cost, MyColors avatarColor) {
        this.type = type;
        this.name = name;
        this.cost = cost;
        this.avatarColor = avatarColor;
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

    public abstract Horse copy();

    public MyColors getAvatarColor() {
        return avatarColor;
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
