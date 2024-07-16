package model.states.feeding;

import model.Model;
import model.characters.GameCharacter;
import model.quests.QuestEdge;
import model.quests.QuestFailNode;
import model.quests.QuestNode;
import model.quests.QuestSuccessfulNode;
import model.states.GameState;
import view.subviews.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VampireFeedingState extends GameState {
    public static final int NOTORIETY_FOR_BEING_SPOTTED = 25;
    private final GameCharacter vampire;
    private static final int NO_OF_ATTEMPTS = 3;
    private QuestNode currentNode = null;

    public VampireFeedingState(Model model, GameCharacter vampire) {
        super(model);
        this.vampire = vampire;
    }

    @Override
    public GameState run(Model model) {
        SubView oldSubView = model.getSubView();
        List<GameCharacter> others = new ArrayList<>(model.getParty().getPartyMembers());
        others.remove(vampire);
        model.getParty().benchPartyMembers(others);
        println(vampire.getFirstName() + " sneaks out at night to find a victim to feed on.");
        for (int i = 0; i < NO_OF_ATTEMPTS; ++i) {
            VampireFeedingHouse house = new VampireFeedingHouse(vampire);
            currentNode = house.getJunctions().get(0);
            VampireFeedingSubView subView = new VampireFeedingSubView(this, vampire, house);
            SnakeTransition.transition(model, subView);

            do {
                QuestEdge edgeToFollow = currentNode.doAction(model, this);
                model.getLog().waitForAnimationToFinish();
                subView.moveAlongEdge(new Point(currentNode.getColumn(), currentNode.getRow()), edgeToFollow);
                currentNode = edgeToFollow.getNode();
            } while (!(currentNode instanceof QuestFailNode) &&
                     !(currentNode instanceof QuestSuccessfulNode) &&
                     !(currentNode instanceof GoToNextHouseNode));

            if (currentNode instanceof QuestFailNode || currentNode instanceof QuestSuccessfulNode) {
                break;
            }

            if (i < NO_OF_ATTEMPTS - 1) {
                println(vampire.getFirstName() + " leaves the house. But the night is still young. Press enter to continue.");
                waitForReturn();
            } else {
                println(vampire.getFirstName() + " leaves the house.");
            }
        }

        println("Dawn is rapidly approaching and " + vampire.getFullName() +
                " must return to the party before anybody notices " + heOrShe(vampire.getGender()) + " is missing.");
        print("Press enter to continue.");
        waitForReturn();
        CollapsingTransition.transition(model, oldSubView);
        model.getParty().unbenchAll();
        return model.getCurrentHex().getEveningState(model, false, false);
    }

    public QuestNode getCurrentPosition() {
        return currentNode;
    }
}
