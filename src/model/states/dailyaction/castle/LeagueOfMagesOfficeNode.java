package model.states.dailyaction.castle;

import model.Model;
import model.TimeOfDay;
import model.states.GameState;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import model.states.events.LeagueOfMagesEvent;
import view.MyColors;
import view.sprites.SignSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.TownSubView;

import java.awt.*;

public class LeagueOfMagesOfficeNode extends DailyActionNode {

    private static final Sprite SPRITE = new Sprite32x32("shopping", "world_foreground.png", 0x22,
            MyColors.YELLOW, TownSubView.PATH_COLOR, MyColors.BROWN, MyColors.LIGHT_YELLOW);
    private static final Sprite SIGN = new SignSprite("leaguesign", 0x1A0, MyColors.RED, MyColors.LIGHT_YELLOW);

    public LeagueOfMagesOfficeNode() {
        super("League of mages office");
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new LeagueOfMagesEvent(model);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return SPRITE;
    }

    @Override
    public Sprite getForegroundSprite() {
        return SIGN;
    }

    public void drawYourself(Model model, Point p) {
        model.getScreenHandler().register(getBackgroundSprite().getName(), new Point(p), getBackgroundSprite());
        Sprite fg = getForegroundSprite();
        if (fg != null) {
            p.x += 2;
            p.y += 2;
            model.getScreenHandler().register("objectforeground", p, fg, 1);
        }
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {
        model.setTimeOfDay(TimeOfDay.EVENING);
    }
}
