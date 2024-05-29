package model.states;

import model.Model;
import model.ruins.*;

public class ExploreFortressAtUtmostEdgeState extends ExploreRuinsState {
    public ExploreFortressAtUtmostEdgeState(Model model, String name) {
        super(model, makeOrLoadDungeon(model, name), name);
    }

    private static RuinsDungeon makeOrLoadDungeon(Model model, String name) {
        if (model.hasVisitedDungeon(name)) {
            return model.getDungeon(name, false);
        }
        RuinsDungeon dungeon = new RuinsDungeon(DungeonMaker.makeFortressAtUtmostEdge()); // TODO: Make FatuE cooler!
        FinalFortressRoom finalRoom = new FinalFortressRoom();
        FinalDungeonLevel finalLevel = (FinalDungeonLevel) dungeon.getLevel(dungeon.getNumberOfLevels()-1);
        finalLevel.setFinalRoom(finalRoom);
        model.storeDungeon(name, dungeon);
        return dungeon;
    }

    private static class FinalFortressRoom extends BossRoom {

    }
}
