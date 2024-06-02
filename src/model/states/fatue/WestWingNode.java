package model.states.fatue;

import model.Model;
import model.ruins.DungeonMaker;
import model.ruins.FinalDungeonLevel;
import model.ruins.RuinsDungeon;
import model.states.dailyaction.AdvancedDailyActionState;

class WestWingNode extends FatueDungeonNode {
    public WestWingNode() {
        super("West Wing");
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        if (!super.canBeDoneRightNow(state, model)) {
            return false;
        }
        state.print("This passage leads to a dilapidated wing of the fortress. Would you like to explore it? (Y/N) ");
        return state.yesNoInput();
    }

    @Override
    protected RuinsDungeon makeDungeon(Model model) {
        RuinsDungeon dungeon = new RuinsDungeon(DungeonMaker.makeWestWingDungeon(model));
        FinalDungeonLevel finalLevel = (FinalDungeonLevel) dungeon.getLevel(dungeon.getNumberOfLevels() - 1);
        finalLevel.setFinalRoom(new FatueStaffRoom());
        return dungeon;
    }
}
