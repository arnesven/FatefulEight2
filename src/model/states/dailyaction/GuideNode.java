package model.states.dailyaction;

import model.Model;
import model.races.AllRaces;
import model.races.Race;
import model.states.GameState;
import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.TavernSubView;

import java.awt.*;

public class GuideNode extends DailyActionNode {

    private final Sprite32x32 sprite;

    public GuideNode() {
        super("Guide");
        Race race = MyRandom.sample(AllRaces.getAllRaces());
        if (race.isShort()) {
            this.sprite = new Sprite32x32("guideshort", "world_foreground.png", 0xBA,
                    MyColors.BLACK, MyColors.DARK_GREEN, race.getColor(), MyColors.BROWN);
        } else {
            this.sprite = new Sprite32x32("guidetall", "world_foreground.png", 0xCA,
                    MyColors.BLACK, MyColors.DARK_GREEN, race.getColor(), MyColors.BROWN);
        }
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new HireGuideAction(model);
    }

    public void drawYourself(Model model, Point p) {
        p.y -= 4;
        model.getScreenHandler().register(getForegroundSprite().getName(), new Point(p), getForegroundSprite(), 2);
    }
    @Override
    public Sprite getBackgroundSprite() {
        return TavernSubView.FLOOR;
    }

    @Override
    public Sprite getForegroundSprite() {
        return sprite;
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        if (model.getParty().getGuide() > 0) {
            model.getLog().addAnimated("You have hired the guide. The guide is waiting for the party to leave.\n");
            return false;
        }
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) { }
}
