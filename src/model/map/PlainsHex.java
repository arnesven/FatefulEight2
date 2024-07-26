package model.map;

import model.Model;
import model.states.DailyEventState;
import model.states.events.*;
import util.MyPair;
import util.MyRandom;
import view.subviews.DailyActionMenu;
import view.subviews.SubView;
import view.subviews.ImageSubView;
import view.MyColors;

import java.awt.*;
import java.util.List;

public class PlainsHex extends WorldHex {
    private static SubView subView = new ImageSubView("theplains", "THE PLAINS", "You are on the plains.", true);

    public PlainsHex(int roads, int rivers, HexLocation location, int state) {
        super(MyColors.GREEN, roads, rivers, location, state);
    }

    @Override
    public String getTerrainName() {
        return "plains";
    }

    @Override
    protected SubView getSubView() {
        return subView;
    }


    @Override
    protected DailyEventState generateTerrainSpecificEvent(Model model) {
        int dieRoll = MyRandom.rollD10();
        if (dieRoll == 4) {
            return new DogEvent(model);
        } else if (dieRoll >= 5) {
            return MyRandom.sample(List.of(
                    new StormEvent(model),
                    new UnicornEvent(model),
                    new StoneCircleEvent(model),
                    new BerriesEvent(model),
                    new NomadCampEvent(model),
                    new HalflingVillage(model),
                    new BrokenWagonEvent(model),
                    new ElfEvent(model),
                    new CairnEvent(model),
                    new WatchtowerEvent(model),
                    new HuntingEvent(model),
                    new EnchantressEvent(model),
                    new FarmersHorseRaceEvent(model),
                    new CryptEvent(model),
                    new HealingRitualEvent(model),
                    new WildHorseEvent(model),
                    new OrcsBattleEvent(model)
            ));
        }
        return new NoEventState(model);
    }

    @Override
    public DailyEventState generateDogEvent(Model model) {
        return MyRandom.sample(List.of(new BrokenWagonEvent(model), new BerriesEvent(model), new CairnEvent(model)));
    }

    @Override
    public MyPair<Point, Integer> getDailyActionMenuPositionAndAnchor() {
        return DailyActionMenu.UPPER_LEFT_CORNER;
    }

    @Override
    public String getTerrainDescription() {
        return "Plains are open fields of grass. Nomads and hunters normally roam these lands but here and there " +
                "halflings can also be found. Plains are not particularly dangerous terrain.";
    }
}
