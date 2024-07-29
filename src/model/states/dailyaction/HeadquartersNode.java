package model.states.dailyaction;

import model.Model;
import model.headquarters.Headquarters;
import model.map.UrbanLocation;
import model.states.GameState;
import view.MyColors;
import view.sprites.SignSprite;
import view.sprites.Sprite;

import java.awt.*;

public class HeadquartersNode extends DailyActionNode {
    private final Headquarters headQuarters;

    public HeadquartersNode(Model model) {
        super("Go to headquarters");
        this.headQuarters = model.getParty().getHeadquarters();
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new HeadquartersDailyActionState(model, state);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return null;
    }

    @Override
    public void drawYourself(Model model, Point p) {
        headQuarters.drawYourself(model, p);
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) { }

    @Override
    public boolean returnNextState() {
        return true;
    }
}
