package model.states.feeding;

import model.Model;
import model.TimeOfDay;
import model.characters.GameCharacter;
import model.quests.QuestEdge;
import model.quests.QuestFailNode;
import model.quests.QuestNode;
import model.quests.QuestSuccessfulNode;
import model.states.GameState;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import view.subviews.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VampireFeedingState extends GameState {
    public static final int NOTORIETY_FOR_BEING_SPOTTED = 25;
    private final GameCharacter vampire;
    private final int noOfAttempts;
    private final boolean onFarm;
    private QuestNode currentNode = null;

    public VampireFeedingState(Model model, GameCharacter vampire, boolean onFarm) {
        super(model);
        this.vampire = vampire;
        this.onFarm = onFarm;
        this.noOfAttempts = onFarm ? 1 : 3;
    }

    @Override
    public GameState run(Model model) {
        SubView oldSubView = model.getSubView();
        List<GameCharacter> others = new ArrayList<>(model.getParty().getPartyMembers());
        others.remove(vampire);
        model.getParty().benchPartyMembers(others);
        model.setTimeOfDay(TimeOfDay.NIGHT);
        println(vampire.getFirstName() + " sneaks out at night to find a victim to feed on.");
        model.getTutorial().vampireFeeding(model);
        ClientSoundManager.playBackgroundMusic(BackgroundMusic.mysticSong);
        for (int i = 0; i < noOfAttempts; ++i) {
            VampireFeedingHouse house = onFarm ? VampireFeedingHouse.makeFarmHouse(vampire)
                    : VampireFeedingHouse.makeTownHouse(vampire);
            currentNode = house.getJunctions().get(0);
            VampireFeedingSubView subView = new VampireFeedingSubView(this, vampire, house, onFarm);
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

            if (i < noOfAttempts - 1) {
                println(vampire.getFirstName() + " leaves the house. But the night is still young. Press enter to continue.");
                waitForReturn();
            } else {
                println(vampire.getFirstName() + " leaves the house.");
            }
        }
        if (model.getParty().size() > 1) {
            println("Dawn is rapidly approaching and " + vampire.getFullName() +
                    " must return to the party before anybody notices " + heOrShe(vampire.getGender()) + " is missing.");
        } else {
            println("Dawn is rapidly approaching.");
        }
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
