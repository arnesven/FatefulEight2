package model.states.dailyaction;

import model.Model;
import model.states.dailyaction.town.CampOutsideOfTownNode;
import view.MyColors;
import view.sprites.Sprite;

import java.awt.*;

public class GoToSleepNode extends CampOutsideOfTownNode {
    public GoToSleepNode(boolean freeRations, Model model) {
        super(freeRations, model, MyColors.TRANSPARENT, MyColors.TRANSPARENT, "Go to sleep");
    }

    @Override
    public void drawYourself(Model model, Point p) {
        Sprite sprite = getBackgroundSprite();
        model.getScreenHandler().register(sprite.getName(), p, sprite);
    }
}
