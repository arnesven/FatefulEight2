package model.states.battle;

import model.map.HexLocation;
import model.map.HillsLocation;

public class HillsBattleTerrain extends BattleTerrain {
    private static final HexLocation loc = new HillsLocation();

    public HillsBattleTerrain() {
        super("Hills", loc);
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
