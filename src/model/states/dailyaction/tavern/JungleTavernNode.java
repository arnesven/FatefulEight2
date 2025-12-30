package model.states.dailyaction.tavern;

import model.Model;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.TownSubView;

import java.awt.*;

public class JungleTavernNode extends TavernNode {

    private static final Sprite SPRITE = new Sprite32x32("jungleinnlower", "world_foreground.png", 0xCD,
            MyColors.BLACK, TownSubView.PATH_COLOR, MyColors.DARK_GREEN, MyColors.BROWN);
    private static final Sprite SPRITE2 = new Sprite32x32("jungleinnupper", "world_foreground.png", 0xBD,
            MyColors.BLACK, TownSubView.PATH_COLOR, MyColors.DARK_GREEN, MyColors.BROWN);

    public JungleTavernNode(boolean freeLodging) {
        super(freeLodging);
    }

    @Override
    protected Sprite getBackgroundSpriteTop() {
        return SPRITE2;
    }

    @Override
    public Sprite getBackgroundSprite() {
        return SPRITE;
    }

    @Override
    public void drawYourself(Model model, Point p) {
        model.getScreenHandler().register(getBackgroundSprite().getName(), new Point(p), getBackgroundSprite());
        Point p2 = new Point(p);
        p2.y -= 4;
        model.getScreenHandler().register(getBackgroundSpriteTop().getName(), p2, getBackgroundSpriteTop());
        Sprite fg = getForegroundSprite();
        if (fg != null) {
            p.x += 2;
            p.y += 2;
            model.getScreenHandler().register("objectforeground", p, fg);
        }
    }
}
