package model.states.events;

import model.Model;
import model.ruins.DungeonMaker;
import model.ruins.DungeonRoom;
import model.ruins.FinalDungeonLevel;
import model.ruins.RuinsDungeon;
import model.states.DailyEventState;
import model.states.ExploreRuinsState;
import view.subviews.CaveTheme;
import view.subviews.CombatTheme;
import view.subviews.DungeonTheme;
import view.subviews.GrassCombatTheme;

public class SecretGardenEvent extends DailyEventState {
    public SecretGardenEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("The party comes to a neat hedge.");
        leaderSay("Hey, these bushes look like they've been planted...");
        leaderSay("And here's a gate.");
        print("Do you enter the secret garden? (Y/N) ");
        if (yesNoInput()) {
            RuinsDungeon dungeon =  new RuinsDungeon(DungeonMaker.makeGardenDungeon(3)); // TODO: 8
            FinalGardenRoom finalRoom = new FinalGardenRoom();
            FinalDungeonLevel finalLevel = (FinalDungeonLevel) dungeon.getLevel(dungeon.getNumberOfLevels()-1);
            finalLevel.setFinalRoom(finalRoom);

            ExploreRuinsState explore = new ExploreSecretGardenState(model, dungeon, "Secret Garden");
            explore.run(model);
            println("You left the secret garden.");
            //setCurrentTerrainSubview(model);
        } else {
            leaderSay("Perhaps it's better not to trespass here.");
        }
    }

    private static class ExploreSecretGardenState extends ExploreRuinsState {
        public ExploreSecretGardenState(Model model, RuinsDungeon dungeon, String ruinsType) {
            super(model, dungeon, ruinsType);
        }

        @Override
        public CombatTheme getCombatTheme() {
            if (getCurrentLevel() == 1) {
                return new DungeonTheme();
            }
            return new GrassCombatTheme();
        }
    }

    private static class FinalGardenRoom extends DungeonRoom {
        public FinalGardenRoom() {
            super(4, 4);
        }

        @Override
        public void entryTrigger(Model model, ExploreRuinsState exploreRuinsState) {
            super.entryTrigger(model, exploreRuinsState);
            exploreRuinsState.leaderSay("I guess that's it folks.");
            exploreRuinsState.print("Press enter to continue."); // TODO: make a better reward at the end of the secret garden
            exploreRuinsState.waitForReturn(); // TODO: E.g. 50 ingredients. Rescue a high level elf/druid, an enchanted spring that blesses each party member for 10 days.
            exploreRuinsState.setDungeonExited(true); // TODO: Or a grand fairy who improves one item for three characters.
        }
    }
}
