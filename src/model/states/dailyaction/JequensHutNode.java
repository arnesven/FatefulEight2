package model.states.dailyaction;

import model.Model;
import model.states.GameState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;

public class JequensHutNode extends DailyActionNode {

    private static final Sprite SPRITE = new Sprite32x32("jequenshut", "world_foreground.png", 0x9C,
            MyColors.BLACK, MyColors.DARK_GRAY, MyColors.DARK_GREEN, MyColors.BROWN);


    public JequensHutNode() {
        super("Jequen's Hut");
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return null;
    }

    @Override
    public Sprite getBackgroundSprite() {
        return SPRITE;
    }

    public void drawYourself(Model model, Point p) {
        model.getScreenHandler().register(getBackgroundSprite().getName(), new Point(p), getBackgroundSprite());
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        return false;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {

    }
}
