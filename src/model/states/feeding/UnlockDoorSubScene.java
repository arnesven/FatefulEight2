package model.states.feeding;

import model.Model;
import model.characters.GameCharacter;
import model.quests.QuestEdge;
import model.states.GameState;
import view.MyColors;

class UnlockDoorSubScene extends FeedingSubScene {
    private final int difficulty;

    public UnlockDoorSubScene(int col, int row, int difficulty, GameCharacter vampire) {
        super(col, row, vampire);
        this.difficulty = difficulty;
    }

    @Override
    protected MyColors getSuccessEdgeColor() {
        return MyColors.LIGHT_GREEN;
    }

    @Override
    protected QuestEdge specificDoAction(Model model, GameState state, GameCharacter vampire) {
        if (difficulty == 0) {
            state.println("The front door is not locked! " + vampire.getFirstName() + " quietly enters the house.");
            return getSuccessEdge();
        }
        state.println("The front door is locked.");
        boolean success = model.getParty().doSoloLockpickCheck(model, state, difficulty);
        if (!success) {
            state.println("The door to the house remains firmly locked.");
            return getFailEdge();
        }
        state.println(vampire.getFirstName() + " quietly enters the house through the front door.");
        return getSuccessEdge();
    }
}
