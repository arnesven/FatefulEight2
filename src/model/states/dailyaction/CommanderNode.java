package model.states.dailyaction;

import model.Model;
import model.journal.JournalEntry;
import model.map.CastleLocation;
import model.map.wars.KingdomWar;
import model.states.GameState;
import model.tasks.BattleDestinationTask;
import model.tasks.DestinationTask;
import util.MyLists;
import view.JournalView;
import view.sprites.Sprite;
import view.subviews.KeepSubView;

import java.util.List;

public class CommanderNode extends DailyActionNode {
    private final CastleLocation castle;

    public CommanderNode(CastleLocation location) {
        super("Talk to commander");
        this.castle = location;
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new TalkToCommanderState(model, castle);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return KeepSubView.RUG;
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {

    }

    private static class TalkToCommanderState extends GameState {
        private final CastleLocation castle;

        public TalkToCommanderState(Model model, CastleLocation castle) {
            super(model);
            this.castle = castle;
        }

        @Override
        public GameState run(Model model) {
            if (model.getWarHandler().getWarsForKingdom(castle).isEmpty()) {
                printQuote("Commander", "Nothing to see here. Move along civilian.");
                return null;
            }
            if (alreadyHasTask(model, castle)) {
                commanderSay("You better head out to the command outpost. The battle will be commencing soon.");
                return null;
            }
            KingdomWar war = model.getWarHandler().getWarsForKingdom(castle).get(0);
            commanderSay("I'm sorry, I'm very busy at the moment. I've got to draw up these battle plans.");
            leaderSay("What's the trouble?");
            String line = "We're at war with the " + CastleLocation.placeNameToKingdom(war.getDefender());
            if (war.isDefender(castle)) {
                line = "We've been invaded by the " + CastleLocation.placeNameToKingdom(war.getAggressor());
            }
            commanderSay("You don't know? " + line + "!");
            model.getTutorial().kingdomWars(model);
            leaderSay("Oh my gosh. Is there anything we can do to help?");
            println("The commander looks seriously at the party.");
            commanderSay("I don't know... what are your skills?");
            List<String> thingsToSay = List.of(
                    "We're pretty good fighters.",
                    "We're good with spells.",
                    "We could help with tactics.");
            int choice = multipleOptionArrowMenu(model, 24, 26, thingsToSay);
            leaderSay(thingsToSay.get(choice));
            if (choice == 0) {
                commanderSay("Well we'll need all the soldiers we can find.");
            } else if (choice == 1) {
                commanderSay("Interesting. Perhaps you could cast some destruction spells at our enemies?");
            } else {
                commanderSay("Tactics? Well perhaps. We need good brains to go along with our brawn.");
            }
            commanderSay("In either case, you could go to the command outpost. I'll mark it on your map. " +
                    "Talk to the field general there.");

            JournalEntry.printJournalUpdateMessage(model);
            model.getParty().addDestinationTask(new BattleDestinationTask(war, castle));
            return null;
        }

        private boolean alreadyHasTask(Model model, CastleLocation castle) {
            return MyLists.any(model.getParty().getDestinationTasks(), (DestinationTask dt) -> dt instanceof BattleDestinationTask &&
                    ((BattleDestinationTask) dt).isForKingdom(castle));
        }

        private void commanderSay(String s) {
            printQuote("Commander", s);
        }
    }
}
