package model.states.feeding;

import model.Model;
import model.quests.QuestEdge;
import model.states.GameState;

import java.util.List;

class GoToNextHouseNode extends FeedingJunction {

    public GoToNextHouseNode() {
        super(1, 4, List.of());
    }

    @Override
    protected QuestEdge specificDoAction(Model model, GameState state) {
        return null; // Unused
    }
}
