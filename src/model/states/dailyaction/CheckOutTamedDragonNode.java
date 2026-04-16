package model.states.dailyaction;

import model.Model;
import model.characters.TamedDragonCharacter;
import model.states.GameState;
import view.sprites.AvatarSprite;
import view.sprites.Sprite;

import java.awt.*;

public class CheckOutTamedDragonNode extends DailyActionNode {
    private final TamedDragonCharacter dragon;
    private final AvatarSprite sprite;

    public CheckOutTamedDragonNode(TamedDragonCharacter tamedDragonCharacter) {
        super("Check on " + tamedDragonCharacter.getName());
        this.dragon = tamedDragonCharacter;
        this.sprite = dragon.getAvatarSprite();
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new GameState(model) {
            @Override
            public GameState run(Model model) {
                state.println("The dragon snarls grumpily at you, but then goes back to its nap.");
                return null;
            }
        };
    }

    @Override
    public Sprite getBackgroundSprite() {
        return null;
    }

    @Override
    public void drawYourself(Model model, Point p) {
        Point p2 = new Point(p.x-4, p.y);
        sprite.synch();
        model.getScreenHandler().register(sprite.getName(), p2, sprite);
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) { }
}
