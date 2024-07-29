package model.horses;

import util.MyRandom;
import view.MyColors;
import view.sprites.HorseSprite;
import view.sprites.Sprite;
import view.sprites.Sprite16x16;

import java.io.Serializable;

public abstract class Horse implements Serializable {
    private static final Sprite BACKGROUND = new HorseSprite(0, 3, MyColors.LIGHT_GRAY, MyColors.GREEN, MyColors.DARK_GREEN, MyColors.LIGHT_BLUE);
    private final String name;
    private final MyColors avatarColor;
    private int cost;
    private String type;
    private boolean gender;
    private Sprite16x16 miniSprite;

    public Horse(String type, String name, int cost, MyColors avatarColor) {
        this.type = type;
        this.name = name;
        this.cost = cost;
        this.avatarColor = avatarColor;
        this.gender = MyRandom.flipCoin();
    }

    public static Sprite getBackgroundSprite() {
        return BACKGROUND;
    }

    public abstract HorseSprite getSprite();

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

    public boolean getGender() {
        return gender;
    }

    public Sprite getMiniSprite() {
        if (miniSprite == null) {
            this.miniSprite = getSprite().getMini();
        }
        return miniSprite;
    }
}
