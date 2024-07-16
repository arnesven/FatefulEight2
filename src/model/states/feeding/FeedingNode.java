package model.states.feeding;

import model.Model;
import model.characters.GameCharacter;
import model.quests.QuestEdge;
import model.states.GameState;
import view.MyColors;

import java.awt.*;

class FeedingNode extends FeedingSubScene {
    private final int sleeping;
    private final VampireFeedingHouse house;

    public FeedingNode(int col, int row, GameCharacter vampire, VampireFeedingHouse house) {
        super(col, row, vampire);
        this.sleeping = house.getSleeping();
        this.house = house;
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        model.getScreenHandler().register(VampireFeedingHouse.BITE_SPRITE.getName(), new Point(xPos, yPos), VampireFeedingHouse.BITE_SPRITE, 1);
    }

    @Override
    protected QuestEdge specificDoAction(Model model, GameState state, GameCharacter vampire) {
        if (sleeping == 0) {
            state.println("There is nobody sleeping in this house.");
            if (!house.canDoMesmerize()) {
                state.println(vampire.getFirstName() + " quietly leaves the house.");
            }
            return getFailEdge();
        }
        for (int i = 0; i < sleeping; ++i) {
            if (house.feedOnVictim(model, state, true)) {
                return getSuccessEdge();
            }
        }
        if (!house.canDoMesmerize()) {
            state.println("Unable to find a suitable victim, " + vampire.getFirstName() + " quietly leaves the house.");
        } else {
            state.println("There are no more people asleep in the house.");
        }
        return getFailEdge();
    }

    @Override
    protected MyColors getSuccessEdgeColor() {
        return MyColors.WHITE;
    }
}
