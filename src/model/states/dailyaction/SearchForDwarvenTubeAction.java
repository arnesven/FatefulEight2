package model.states.dailyaction;

import model.Model;
import model.actions.DailyAction;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.items.puzzletube.FindPuzzleDestinationTask;
import model.states.DailyEventState;
import model.states.events.FindPuzzleTubeEvent;
import model.tasks.Destination;

public class SearchForDwarvenTubeAction extends DailyAction {


    public SearchForDwarvenTubeAction(Model model, Destination dest) {
        super("Serach for Puzzle Tube", new SearchForDwarvenTubeEvent(model, dest));

    }

    private static class SearchForDwarvenTubeEvent extends DailyEventState {
        private final Destination destination;

        public SearchForDwarvenTubeEvent(Model model, Destination dest) {
            super(model);
            this.destination = dest;
        }

        @Override
        protected void doEvent(Model model) {
            if (FindPuzzleTubeEvent.alreadyFoundInCurrentLocation(model)) {
                leaderSay("Now where is could that tube be hidden?");
                if (model.getParty().size() > 1) {
                    GameCharacter other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
                    partyMemberSay(other, "Wait a minute...");
                    leaderSay("What is it " + other.getFirstName() + "?");
                    partyMemberSay(other, "I think we already found the puzzle tube that was hidden here.");
                    leaderSay("Oh yeah... I think you're right. It was a waste of time coming here.");
                } else {
                    leaderSay("Wait a minute... I think I already found the puzzle tube that was hidden here. " +
                            "I guess it was a waste of time coming here.");
                }
                return;
            }

            println("You start looking for " + destination.getShortDescription() +
                    ", in hopes that you can find the Dwarven Puzzle tube.");
            boolean success = model.getParty().doCollaborativeSkillCheck(model, this, Skill.Search, 8);
            if (success) {
                new FindPuzzleTubeEvent(model).doTheEvent(model);
            } else {
                println("You search for hours, but find nothing.");
            }
            model.getLog().waitForAnimationToFinish();
        }

    }
}
