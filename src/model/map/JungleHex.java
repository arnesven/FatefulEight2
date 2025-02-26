package model.map;

import model.Model;
import model.states.DailyEventState;
import model.states.events.*;
import util.MyRandom;
import view.MyColors;
import view.subviews.ImageSubView;
import view.subviews.SubView;

import java.util.List;

public class JungleHex extends WorldHex {
    private static final SubView subView = new ImageSubView("thejungle", "THE JUNGLE", "You are in the jungle.", true);

    public JungleHex(int roads, int rivers, int state) {
        super(MyColors.GREEN, roads, rivers, new JungleLocation(), state);
    }

    @Override
    public String getTerrainName() {
        return "jungle";
    }

    public DailyEventState generateTerrainSpecificEvent(Model model) {
        int dieRoll = MyRandom.rollD10();
        if (dieRoll == 2) {
            return new DogEvent(model);
        } else if (dieRoll >= 3) {
            return MyRandom.sample(List.of(
                    new JungleMonsterEvent(model),
                    new JungleMonsterEvent(model),
                    new DehydrationEvent(model),
                    new LostEvent(model),
                    new LostEvent(model),
                    new MosquitoesEvent(model),
                    new MushroomsEvent(model),
                    new AbandonedShackEvent(model),
                    new RareBirdEvent(model),
                    new NoEventState(model)
            ));
        }
        return new NoEventState(model);
    }

    @Override
    public DailyEventState generateDogEvent(Model model) {
        return MyRandom.sample(List.of(new MushroomsEvent(model), new WoundedAdventurerEvent(model), new AbandonedShackEvent(model)));
    }

    @Override
    public DailyEventState getNightTimeAmbushEvent(Model model) {
        if (MyRandom.rollD10() <= 2) {
            return new SpiderNightAttackEvent(model);
        }
        return null;
    }

    @Override
    protected SubView getSubView() {
        return subView;
    }

    @Override
    public String getTerrainDescription() {
        return "Jungles are heavily wooded areas filled with dangers. ...!";
    }

    @Override
    public ResourcePrevalence getResourcePrevalences() {
        return new ResourcePrevalence(ResourcePrevalence.GOOD, ResourcePrevalence.POOR);
    }
}
