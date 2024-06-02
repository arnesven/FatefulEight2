package model.states.fatue;

import model.Model;
import model.ruins.DungeonMaker;
import model.ruins.FinalDungeonLevel;
import model.ruins.RuinsDungeon;
import model.states.dailyaction.AdvancedDailyActionState;

class MinesOfMiseryNode extends FatueDungeonNode {
    public MinesOfMiseryNode() {
        super("Mines of Misery");
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        if (!super.canBeDoneRightNow(state, model)) {
            return false;
        }
        state.print("These narrow and treacherous steps lead down to the dank mines below the fortress. " +
                "Would you like to explore them? (Y/N) ");
        return state.yesNoInput();
    }

    @Override
    protected RuinsDungeon makeDungeon(Model model) {
        RuinsDungeon dungeon = new RuinsDungeon(DungeonMaker.makeMinesOfMiseryDungeon(model));
        FinalDungeonLevel finalLevel = (FinalDungeonLevel) dungeon.getLevel(dungeon.getNumberOfLevels() - 1);
        finalLevel.setFinalRoom(new FatueStaffRoom());
        return dungeon;
    }
}
