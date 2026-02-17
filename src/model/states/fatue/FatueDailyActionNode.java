package model.states.fatue;

import model.Model;
import model.states.dailyaction.DailyActionNode;
import view.sprites.Sprite;

import java.awt.*;

abstract class FatueDailyActionNode extends DailyActionNode {
    public FatueDailyActionNode(String name) {
        super(name);
    }


    @Override
    public boolean blocksPassage() {
        return false;
    }

    @Override
    public Sprite getBackgroundSprite() {
        return null;
    }

    @Override
    public void drawYourself(Model model, Point p) {
    }
}
