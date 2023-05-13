package model.combat;

import model.enemies.CrowEnemy;
import model.enemies.RatEnemy;
import model.states.events.PeskyCrowEvent;
import util.MyRandom;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;

public class TransfiguredCondition extends ParalysisCondition {
    private static final Sprite32x32 PIG = new Sprite32x32("transfigured", "enemies.png", 0xA6,
            MyColors.BLACK, MyColors.PINK, MyColors.LIGHT_RED, MyColors.PURPLE);

    private final int hpLost;
    private Sprite sprite;

    public TransfiguredCondition(int hpLost) {
        setDuration(3);
        this.hpLost = hpLost;
        int roll = MyRandom.randInt(3);
        if (roll == 0) {
            sprite = PIG;
        } else if (roll == 1) {
            sprite = RatEnemy.SPRITE;
        } else {
            sprite = CrowEnemy.SPRITE;
        }
    }



    public void drawYourself(ScreenHandler screenHandler, int xpos, int ypos) {
        screenHandler.register(sprite.getName(), new Point(xpos, ypos), sprite);
    }

    @Override
    public void wasRemoved(Combatant combatant) {
        combatant.addToHP(hpLost);
    }
}
