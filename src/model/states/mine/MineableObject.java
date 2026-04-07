package model.states.mine;

import model.Model;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public abstract class MineableObject extends MineObject {

    private static final Sprite DEBRIS_SPRITE = new Sprite32x32("rockdebris",
            "warehouse.png", 0x43, MyColors.DARK_GRAY, MyColors.GRAY, MyColors.YELLOW, MyColors.GRAY);

    private final int diff;
    private String name;

    public MineableObject(String name, int difficulty) {
        this.name = name;
        this.diff = difficulty;
    }

    public String getName() {
        return name;
    }

    public int getDifficulty() {
        return diff;
    }

    public abstract void giveReward(Model model, AdvancedMineEvent advancedMineEvent);

    public abstract MyColors getAnimationColor();

    public Sprite getDebrisSprite() {
        return DEBRIS_SPRITE;
    }
}
