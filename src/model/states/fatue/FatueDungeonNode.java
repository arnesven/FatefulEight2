package model.states.fatue;

import model.Model;
import model.ruins.RuinsDungeon;
import model.states.ExploreRuinsState;
import model.states.GameState;
import model.states.dailyaction.AdvancedDailyActionState;
import view.combat.CombatTheme;

abstract class FatueDungeonNode extends FatueDailyActionNode {
    private final String name;
    private final String promptMessage;
    private final boolean isDownward;

    public FatueDungeonNode(String name, boolean isDownward, String promptMessage) {
        super("Enter " + name);
        this.name = name;
        this.isDownward = isDownward;
        this.promptMessage = promptMessage;
    }

    protected abstract RuinsDungeon makeDungeon(Model model);

    protected abstract CombatTheme getCombatTheme();
    
    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {
    }

    @Override
    public final boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        if (state.getCurrentPosition().equals(FortressAtUtmostEdgeState.getStartingPoint())) {
            state.println("That location is not accessible from your current position.");
            return false;
        }
        state.print(promptMessage + " (Y/N) ");
        return state.yesNoInput();
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        RuinsDungeon dungeon;
        String key = "F.A.T.U.E - " + name;
        if (model.hasVisitedDungeon(key)) {
            dungeon = model.getDungeon(key, false);
        } else {
            dungeon = makeDungeon(model);
            model.storeDungeon(key, dungeon);
        }
        return new ExploreFatueDungeonState(model, dungeon, key, getCombatTheme(), isDownward);
    }

    private static class ExploreFatueDungeonState extends ExploreRuinsState {
        private final CombatTheme combatTheme;
        private final boolean isDownward;

        public ExploreFatueDungeonState(Model model, RuinsDungeon dungeon, String key, CombatTheme combatTheme, boolean isDownward) {
            super(model, dungeon, key);
            this.combatTheme = combatTheme;
            this.isDownward = isDownward;
            if (!isDownward) {
                super.setCurrentLevelAndPosition(dungeon.getNumberOfLevels()-1,
                        dungeon.getLevel(dungeon.getNumberOfLevels()-1).getStartingPoint());
            }
        }

        @Override
        public CombatTheme getCombatTheme() {
            return combatTheme;
        }

        public String getCurrentRoomInfo() {
            if (isDownward) {
                return super.getCurrentRoomInfo();
            }
            int floor = getDungeon().getNumberOfLevels() - getCurrentLevel() - 1;
            if (floor == 0) {
                return "Ground Floor";
            }
            if (getCurrentLevel() == 0) {
                return "";
            }
            return "Floor " + floor + ", Room " + (getPartyPosition().x+1) + "-" + (getPartyPosition().y+1);
        }
    }
}
