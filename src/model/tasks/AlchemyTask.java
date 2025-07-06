package model.tasks;

import model.GameStatistics;
import model.Model;
import model.actions.DailyAction;
import model.characters.GameCharacter;
import model.items.spells.AlchemySpell;
import model.journal.JournalEntry;
import model.states.DailyEventState;
import util.MyLists;
import util.MyStrings;

import java.awt.*;

public abstract class AlchemyTask extends DestinationTask {
    private final String verb;

    public AlchemyTask(String verb) {
        super(null, "Use the Alchemy spell to " + verb + " a potion.");
        this.verb = verb;
    }

    public static DailyEventState makeAlchemySpellGottenEvent(Model model) {
        if (MyLists.any(model.getParty().getSpells(), sp -> sp instanceof AlchemySpell) &&
                !MyLists.any(model.getParty().getDestinationTasks(), dt -> dt instanceof AlchemyTask)) {
            return new AlchemySpellGottenEvent(model);
        }
        return null;
    }

    @Override
    public JournalEntry getJournalEntry(Model model) {
        return new BrewPotionJournalEntry();
    }

    @Override
    public JournalEntry getFailedJournalEntry(Model model) {
        return new BrewPotionJournalEntry();
    }

    @Override
    public DailyAction getDailyAction(Model model) {
        return null;
    }

    @Override
    public boolean isFailed(Model model) {
        return false;
    }

    @Override
    public boolean givesDailyAction(Model model) {
        return false;
    }

    @Override
    public abstract boolean isCompleted();

    private static class AlchemySpellGottenEvent extends DailyEventState {
        public AlchemySpellGottenEvent(Model model) {
            super(model);
        }

        @Override
        protected void doEvent(Model model) {
            println(model.getParty().getLeader().getName() + " is inspecting the Alchemy spell book.");
            if (model.getParty().size() > 1) {
                GameCharacter other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
                leaderSay("Alchemy? Isn't that about turning lesser compounds into gold?.");
                partyMemberSay(other, "It's more than that. You can brew all kinds of potions. " +
                        "We just need some ingredients first, or we can distill any potion we already have.");
                partyMemberSay(other, "In fact. I found these earlier. Maybe we can use them!");
                println("The party got 5 ingredients.");
                model.getParty().getInventory().addToIngredients(5);
                leaderSay("Okay, let's just not blow ourselves up.");

            } else {
                leaderSay("With this spell I can brew all kinds of potions. " +
                        "I just need some ingredients first, or I can distill any potion I already have.");
            }
            model.getParty().addDestinationTask(new BrewPotionTask());
            model.getParty().addDestinationTask(new DistillPotionTask());
            JournalEntry.printJournalUpdateMessage(model);
            model.getLog().waitForAnimationToFinish();
        }
    }

    private static class BrewPotionTask extends AlchemyTask {
        public BrewPotionTask() {
            super("brew");
        }

        @Override
        public boolean isCompleted() {
            return GameStatistics.getPotionsBrewed() > 0;
        }
    }

    private static class DistillPotionTask extends AlchemyTask {
        public DistillPotionTask() {
            super("distill");
        }

        @Override
        public boolean isCompleted() {
            return GameStatistics.getPotionsDistilled() > 0;
        }
    }

    private class BrewPotionJournalEntry implements JournalEntry {

        @Override
        public String getName() {
            return MyStrings.capitalize(verb) + " a potion";
        }

        @Override
        public String getText() {
            return AlchemyTask.this.getDestinationDescription() +
                    (isComplete() ? "\n\nCompleted" : "");
        }

        @Override
        public boolean isComplete() {
            return AlchemyTask.this.isCompleted();
        }

        @Override
        public boolean isFailed() {
            return false;
        }

        @Override
        public boolean isTask() {
            return true;
        }

        @Override
        public Point getPosition(Model model) {
            return null;
        }
    }
}
