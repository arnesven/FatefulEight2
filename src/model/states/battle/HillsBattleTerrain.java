package model.states.battle;

import model.map.HillsLocation;
import view.MyColors;

public class HillsBattleTerrain extends BattleTerrain {

    public HillsBattleTerrain(MyColors hillColor) {
        super("Hills", new HillsLocation(hillColor));
    }

    @Override
    public int checkForMeleeDefenseBonus(BattleState battleState, BattleUnit defender) {
        battleState.println("Defender has the high ground (+1 to defense).");
        return 1;
    }

    @Override
    public String getHelpNote() {
        return "+1 Defense";
    }
}
