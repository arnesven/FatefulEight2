package model.ruins.objects;

import model.Model;
import model.ruins.themes.DungeonTheme;
import model.states.ExploreRuinsState;
import sound.SoundEffects;
import view.sprites.Sprite;

import java.awt.*;
import java.util.Random;

public class LeverObject extends CenterDungeonObject {
    private boolean on;

    public LeverObject(Random random) {
        on = random.nextDouble() > 0.667;
    }

    @Override
    protected Sprite getSprite(DungeonTheme theme) {
        return theme.getLever(on);
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
