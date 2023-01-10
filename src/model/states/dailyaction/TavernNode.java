package model.states.dailyaction;

import model.Model;
import model.states.GameState;
import view.MyColors;
import view.sprites.SignSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.TownSubView;

import java.awt.*;

public class TavernNode extends DailyActionNode {
    private static final Sprite SPRITE = new Sprite32x32("innlower", "world_foreground.png", 0x33,
            MyColors.YELLOW, TownSubView.PATH_COLOR, MyColors.BROWN, MyColors.LIGHT_YELLOW);
    private static final Sprite SPRITE2 = new Sprite32x32("innupper", "world_foreground.png", 0x23,
            MyColors.YELLOW, TownSubView.PATH_COLOR, MyColors.BROWN, MyColors.LIGHT_YELLOW);
    private static final Sprite INN_SIGN = new SignSprite("innisgn", 0x07, MyColors.BLACK, MyColors.WHITE);
    private final boolean freeLodging;

    public TavernNode(boolean freeLodging) {
        super("Visit Tavern");
        this.freeLodging = freeLodging;
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new TavernDailyActionState(model, freeLodging, true);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return SPRITE;
    }

    @Override
    public Sprite getForegroundSprite() {
        return INN_SIGN;
    }

    @Override
    public void drawYourself(Model model, Point p) {
        model.getScreenHandler().register(getBackgroundSprite().getName(), new Point(p), getBackgroundSprite());
        Point p2 = new Point(p);
        p2.y -= 4;
        model.getScreenHandler().register(SPRITE2.getName(), p2, SPRITE2);
        Sprite fg = getForegroundSprite();
        if (fg != null) {
            p.x += 2;
            model.getScreenHandler().register("objectforeground", p, fg);
        }
    }

    @Override
    public Point getCursorShift() {
        Point p = super.getCursorShift();
        p.y -= 2;
        return p;
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState townDailyActionState, Model model) {
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) { }

    @Override
    public boolean returnNextState() {
        return true;
    }
}
