package model.states;

import model.Model;
import model.ruins.DungeonMaker;
import model.ruins.RuinsDungeon;

public class ExploreTombState extends ExploreRuinsState {
    public ExploreTombState(Model model, String name) {
        super(model, savedOrNewDungeon(model, name), "Tomb");
    }

    private static RuinsDungeon savedOrNewDungeon(Model model, String name) {
        // FEATURE: If you don't have it, add task for clearing the two tombs in the extended part of the map
        if (model.hasVisitedDungeon(name)) {
            return model.getDungeon(name, true);
        }
        RuinsDungeon dungeon = new RuinsDungeon(
                DungeonMaker.makeRandomDungeon(model, 300, 5, 12, true));
        model.visitDungeon(name, dungeon);
        return dungeon;
    }
}
