package model.states.feeding;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.quests.QuestEdge;
import model.states.GameState;
import model.states.events.GeneralInteractionEvent;
import util.MyRandom;
import view.MyColors;

import java.util.List;

class SneakingSubScene extends FeedingSubScene {
    private final int peopleAwake;

    public SneakingSubScene(int col, int row, int peopleAwake, GameCharacter vampire) {
        super(col, row, vampire);
        this.peopleAwake = peopleAwake;
    }

    @Override
    protected MyColors getSuccessEdgeColor() {
        return MyColors.LIGHT_GREEN;
    }

    @Override
    protected QuestEdge specificDoAction(Model model, GameState state, GameCharacter vampire) {
        if (peopleAwake > 0) {
            state.println(vampire.getFirstName() + " must attempt to remain undetected " +
                    "by the inhabitants who are still awake.");
            boolean result = model.getParty().doSoloSkillCheck(model, state, Skill.Sneak, 6 + peopleAwake);
            if (!result) {
                state.println("You have been spotted!");
                state.printQuote(GameState.manOrWomanCap(MyRandom.flipCoin()), "HEY! Get out of here you creep!");
                GeneralInteractionEvent.addToNotoriety(model, state, VampireFeedingState.NOTORIETY_FOR_BEING_SPOTTED);
                state.println(vampire.getFirstName() + " flees the house with haste before the constables arrive. " +
                        "There is now much commotion among the townspeople and there is no point in " +
                        "continuing the prowl tonight.");
                return getFailEdge();
            }
            state.printQuote(GameState.manOrWomanCap(MyRandom.flipCoin()),
                    MyRandom.sample(List.of("Did I hear something? It's probably just the wind.",
                            "What was that? Hmm... naw, it was nothing.",
                            "Huh, someone there? No... just my mind playing tricks on me.")));
        } else {
            state.println("It appears nobody is awake in the house, and " + vampire.getFirstName() +
                    " can move about freely without fear for being detected.");
        }
        return getSuccessEdge();
    }
}
