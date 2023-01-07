package model.states.dailyaction;

import model.Model;
import model.states.GameState;
import model.states.SaveGameState;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

import java.awt.*;

public class SaveGameNode extends DailyActionNode {
    private static final Sprite SPRITE = makeSprite();

    public SaveGameNode() {
        super("Save Game");
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new SaveGameState(model);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return SPRITE;
    }

    @Override
    public void drawYourself(Model model, Point p) {
        model.getScreenHandler().register(SPRITE.getName(), p, SPRITE);
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) { }


    private static Sprite makeSprite() {
        LoopingSprite sprite = new LoopingSprite("saveicon", "world_foreground.png", 0x74, 32, 32);
        sprite.setColor1(MyColors.DARK_GRAY);
        sprite.setColor2(MyColors.LIGHT_GRAY);
        sprite.setColor3(MyColors.GRAY);
        sprite.setColor4(MyColors.WHITE);
        sprite.setFrames(4);
        sprite.setDelay(4);
        return sprite;
    }
}
