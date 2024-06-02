package model.states.fatue;

import model.Model;
import model.ruins.RuinsDungeon;
import model.states.ExploreRuinsState;
import model.states.GameState;
import model.states.dailyaction.AdvancedDailyActionState;

abstract class FatueDungeonNode extends FatueDailyActionNode {
    private final String name;

    public FatueDungeonNode(String name) {
        super("Enter " + name);
        this.name = name;
    }

    protected abstract RuinsDungeon makeDungeon(Model model);

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        return !state.getCurrentPosition().equals(FortressAtUtmostEdgeState.getStartingPoint());
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
        return new ExploreRuinsState(model, dungeon, key);
    }
}
