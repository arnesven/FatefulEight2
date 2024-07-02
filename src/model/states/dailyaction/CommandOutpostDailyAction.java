package model.states.dailyaction;

import model.Model;
import model.actions.DailyAction;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.journal.JournalEntry;
import model.map.CastleLocation;
import model.map.UrbanLocation;
import model.map.wars.KingdomWar;
import model.states.DailyEventState;
import model.states.battle.BattleState;
import model.states.battle.BattleUnit;
import model.tasks.BattleDestinationTask;
import view.JournalView;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class CommandOutpostDailyAction extends DailyAction {
    public CommandOutpostDailyAction(Model model, KingdomWar war, boolean givenByAggressor,
                                     BattleDestinationTask battleDestinationTask) {
        super("Visit Outpost", new CommandOutpostDailyEventState(model, war, givenByAggressor,
                battleDestinationTask));
    }

    private static class CommandOutpostDailyEventState extends DailyEventState {

        private final CharacterAppearance fieldGeneralAppearance = PortraitSubView.makeRandomPortrait(Classes.PAL);
        private final KingdomWar war;
        private final boolean givenByAggressor;
        private final BattleDestinationTask task;

        public CommandOutpostDailyEventState(Model model, KingdomWar war, boolean givenByAggressor,
                                             BattleDestinationTask battleDestinationTask) {
            super(model);
            this.war = war;
            this.givenByAggressor = givenByAggressor;
            this.task = battleDestinationTask;
        }

        @Override
        protected void doEvent(Model model) {
            println("You wander into the army camp. It is a large camp with tents, wagons and training dummies everywhere. " +
                    "There are plenty of soldiers about along with armor, weapons, clothes, provisions and everything " +
                    "else an army needs to sustain itself. There's a larger, more colorful tent up on a little hill. You " +
                    "assume this is the command tent. You walk up to it and step inside. A muscular " +
                    fieldGeneralAppearance.getRace().getName().toLowerCase() + " greets you as you enter.");
            showExplicitPortrait(model, fieldGeneralAppearance, "Field General");
            portraitSay("Hello there! You must be the ones the Commander mentioned in his message. " +
                    "I've been expecting you.");
            leaderSay("Are you preparing for battle?");
            portraitSay("Yes. Our scouts have spotted the enemy's units. We're just about to move out and deploy in the field. " +
                    "You got here just in time!");
            leaderSay("Good. What can we help with?");
            portraitSay("Well, you can go join any unit you like. Or you can stay with me and direct the battle. " +
                    "Which would you prefer?");
            int choice = multipleOptionArrowMenu(model, 26, 26, List.of("Join a unit", "Direct the battle"));
            // TODO: Implement joining a unit
            leaderSay("I think we'll stay with you. I'm sure we'll have some tactical insights we could share.");
            portraitSay("All right! Let's go give the enemy a taste of our zeal!");
            BattleState battle = new BattleState(model, war, givenByAggressor);
            battle.run(model);
            setCurrentTerrainSubview(model);
            task.setCompleted(true);
            showExplicitPortrait(model, fieldGeneralAppearance, "Field General");
            if (battle.wasVictorious()) {
                portraitSay("What a glorious day, victory is ours!");
                leaderSay("Yes. The troops fought well.");
                portraitSay("Don't discount yourself. Without your effort, defeat would have surely found us.");
                leaderSay("Perhaps. Now it found the enemy instead. What now General?");
                portraitSay("We must advance our positions. Our scouts have already discovered " +
                        "the location of the enemy's rear camp. But it will take some time for our troops to reach it. " +
                        "I'll mark it on your map for now.");
                task.setSuccess(true);

                giveNewTask(model, givenByAggressor);

                leaderSay("Splendid. We'll meet you on the front line then.");
                portraitSay("Yes. But before you leave. I was wondering if you could offer some advice.");
                leaderSay("About what?");
                println("We may be able to bring up some reinforcements. Muster some more troops even. " +
                        "But there is limited time and resources. I would love to hear your input on what kind of " +
                        "units we should focus on.");
                // TODO: Reinforcements dialog instead of generalReinforce for one side.
                generalReinforce(war.getAggressorUnits());
                generalReinforce(war.getDefenderUnits());

                leaderSay("... That's my view of the situation.");
                portraitSay("Thank you. Your insights are much appreciated. Now, then... Until we meet again friend.");
                leaderSay("So long General.");
            } else {
                println("In the hectic aftermath of the lost battle, you see the general barking order at " +
                        "the confused units, or what remains of them.");
                portraitSay("Retreat! Retreat I say!");
                leaderSay("General... we did our best but...");
                portraitSay("Fortune was not with us today, needless to say. We must make haste to our " +
                        "rear positions before the enemy overruns us.");
                leaderSay("Of course. Where is that?");
                portraitSay("I'll mark it on your map.");
                task.setCompleted(false);
                task.setSuccess(false);
                giveNewTask(model, !givenByAggressor);
                portraitSay("Now make haste friend or we shall surely meet the same fate as many of our fallen comrades.");
                leaderSay("Yes General. See you on the other side.");
                generalReinforce(war.getAggressorUnits());
                generalReinforce(war.getDefenderUnits());
            }
        }

        private void giveNewTask(Model model, boolean aggressorAdvances) {
            war.advance(givenByAggressor);
            CastleLocation loc = (CastleLocation) model.getWorld().getUrbanLocationByPlaceName(
                    givenByAggressor ? war.getAggressor() : war.getDefender());
            model.getParty().addDestinationTask(new BattleDestinationTask(war, loc));
            JournalEntry.printJournalUpdateMessage(model);
        }

        private void generalReinforce(List<BattleUnit> units) {
            List<BattleUnit> newUnits = new ArrayList<>();
            for (BattleUnit bu : units) {
                int reinforceTotal = bu.getCount() + bu.getReinforceCount();
                if (reinforceTotal <= bu.maximumUnitSize()) {
                    bu.setCount(reinforceTotal);
                    newUnits.add(bu);
                } else {
                    newUnits.add(bu.createNew((int)Math.ceil(reinforceTotal/2.0)));
                    newUnits.add(bu.createNew(reinforceTotal/2));
                }
            }

            units.clear();
            units.addAll(newUnits);
        }
    }
}
