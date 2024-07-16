package model.states.feeding;

import model.Model;
import model.characters.GameCharacter;
import model.quests.QuestEdge;
import model.quests.QuestNode;
import model.states.GameState;
import util.MyStrings;

import java.util.List;

class FeedingStartNode extends FeedingJunction {
    private final GameCharacter vampire;
    private final VampireFeedingHouse house;

    public FeedingStartNode(QuestNode nextNdde, GameCharacter vampire, VampireFeedingHouse house) {
        super(0, 2, List.of(new QuestEdge(nextNdde, QuestEdge.VERTICAL)));
        this.vampire = vampire;
        this.house = house;
    }

    @Override
    protected QuestEdge specificDoAction(Model model, GameState state) {
        state.println(state.heOrSheCap(vampire.getGender()) + " approaches a house, it's a " +
                MyStrings.numberWord(house.getStories()) + " story building.");
        return getConnection(0);
    }
}
