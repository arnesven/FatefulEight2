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
            TownSubView.STREET_COLOR, TownSubView.PATH_COLOR, MyColors.BROWN, MyColors.LIGHT_YELLOW);
    private static final Sprite SPRITE2 = new Sprite32x32("innupper", "world_foreground.png", 0x23,
            TownSubView.STREET_COLOR, TownSubView.PATH_COLOR, MyColors.BROWN, MyColors.LIGHT_YELLOW);
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
        model.getScreenHandler().put(p.x, p.y, getBackgroundSprite());
        model.getScreenHandler().put(p.x, p.y-4, SPRITE2);
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
