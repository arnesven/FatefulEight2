package model.states.dailyaction;

import model.Model;
import view.sprites.Sprite;

import java.awt.*;

public abstract class DailyActionNodeWithSign extends DailyActionNode {

    public DailyActionNodeWithSign(String name) {
        super(name);
    }

    public void drawYourself(Model model, Point p) {
        model.getScreenHandler().put(p.x, p.y, getBackgroundSprite());
        Sprite fg = getForegroundSprite();
        if (fg != null) {
            p.x += 2;
            p.y += 2;
            model.getScreenHandler().register("objectforeground", p, fg);
        }
    }
}
