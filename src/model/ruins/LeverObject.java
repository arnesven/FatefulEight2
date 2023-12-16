package model.ruins;

import model.Model;
import model.states.ExploreRuinsState;
import sound.SoundEffects;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.Random;

public class LeverObject extends CenterDungeonObject {
    private static final Sprite ON_SPRITE = new Sprite32x32("onlever", "dungeon.png", 0x43,
            MyColors.BLACK, MyColors.GRAY, MyColors.DARK_BROWN, MyColors.PINK);
    private static final Sprite OFF_SPRITE = new Sprite32x32("offlever", "dungeon.png", 0x44,
            MyColors.BLACK, MyColors.GRAY, MyColors.DARK_BROWN, MyColors.PINK);
    private boolean on;

    public LeverObject(Random random) {
        on = random.nextDouble() > 0.667;
    }

    @Override
    protected Sprite getSprite(DungeonTheme theme) {
        if (on) {
            return ON_SPRITE;
        }
        return OFF_SPRITE;
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos, DungeonTheme theme) {
        model.getScreenHandler().register(getSprite(theme).getName(), new Point(xPos, yPos), getSprite(theme));
    }

    @Override
    public String getDescription() {
        return "A lever";
    }

    @Override
    public void doAction(Model model, ExploreRuinsState state) {
        on = !on;
        state.println("You pulled the lever. Did anything happen?");
        SoundEffects.playSound("lever");
    }

    public boolean isOn() {
        return on;
    }
}
