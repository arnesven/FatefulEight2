package model.states.mine;

import model.Model;
import view.ScreenHandler;
import view.sprites.Sprite;

import java.awt.*;

public abstract class MineOreObject extends MineableObject {

    private final Sprite sprite;

    public MineOreObject(String name, int difficulty, Sprite sprite) {
        super(name + " Rock", difficulty);
        this.sprite = sprite;
    }

    protected static String qualityForRichness(int richness) {
        if (richness == 1) {
            return "Rich ";
        }
        if (richness == 2) {
            return "Ultra Rich ";
        }
        return "";
    }

    @Override
    public void drawYourself(ScreenHandler screenHandler, LogicalMine mine, Point screenPosition) {
        screenHandler.put(screenPosition.x, screenPosition.y, sprite);
    }

    @Override
    public boolean gotBumpedInto(Model model, AdvancedMineEvent state, Point currentLocation) {
        state.askToMineObject(model, this);
        return false;
    }
}
