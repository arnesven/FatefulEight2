package model.map;

import model.Model;
import model.states.DailyEventState;
import model.states.events.*;
import util.MyRandom;

import java.util.List;

public class JungleHex extends WoodsHex {
    public JungleHex(int roads, int rivers, int state) {
        super(roads, rivers, new JungleLocation(), state);
    }

    @Override
    public String getTerrainName() {
        return "jungle";
    }

    protected DailyEventState generateTerrainSpecificEvent(Model model) {
        return MyRandom.sample(List.of(
                new JungleMonsterEvent(model),
                new JungleMonsterEvent(model),
                new DehydrationEvent(model),
                new LostEvent(model),
                new LostEvent(model),
                new MosquitoesEvent(model),
                new MushroomsEvent(model),
                new AbandonedShackEvent(model),
                new NoEventState(model)
        ));
    }
}
