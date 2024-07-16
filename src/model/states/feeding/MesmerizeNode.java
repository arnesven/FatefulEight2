package model.states.feeding;

import model.Model;
import model.characters.GameCharacter;
import model.quests.QuestEdge;
import model.states.GameState;
import view.MyColors;
import view.sprites.Sprite32x32;

import java.awt.*;

class MesmerizeNode extends FeedingSubScene {
    public static final Sprite32x32 EYE_SPRITE = new Sprite32x32("mesmerizesubscene", "quest.png", 0xA2,
            MyColors.BLACK, MyColors.WHITE, MyColors.RED, MyColors.BLACK);

    private final int awake;
    private final VampireFeedingHouse house;

    public MesmerizeNode(int col, int row, GameCharacter vampire, VampireFeedingHouse house) {
        super(col, row, vampire);
        this.awake = house.getDwellers() - house.getSleeping();
        this.house = house;
    }

    @Override
    protected MyColors getSuccessEdgeColor() {
        return MyColors.WHITE;
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        model.getScreenHandler().register(EYE_SPRITE.getName(), new Point(xPos, yPos), EYE_SPRITE, 1);
    }

    @Override
    protected QuestEdge specificDoAction(Model model, GameState state, GameCharacter vampire) {
        if (awake == 0) {
            state.println("With no other suitable victims in the house, " + vampire.getFirstName() + " quietly leaves.");
            return getFailEdge();
        }
        for (int i = 0; i < awake; ++i) {
            if (house.feedOnVictim(model, state, false)) {
                return getSuccessEdge();
            }
        }
        state.println("Unable to find a suitable victim, " + vampire.getFirstName() + " quietly leaves the house.");
        return getFailEdge();
    }
}
