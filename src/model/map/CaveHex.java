package model.map;

import model.Model;
import model.actions.*;
import model.combat.CaveTheme;
import model.map.locations.Stalagmites;
import model.states.DailyEventState;
import model.states.TravelState;
import model.states.events.*;
import util.MyPair;
import util.MyRandom;
import view.MyColors;
import view.sprites.HexSprite;
import view.subviews.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CaveHex extends WorldHex {
    private static final SubView subView = new ImageSubView("thecaves", "CAVES", "You are exploring a system of caves...", true);;
    private static Random random;
    public static final MyColors GROUND_COLOR = MyColors.GRAY_RED;

    public CaveHex(int roads, MyColors color) {
        super(color, roads, 0x0, randomLocation());
    }

    private static HexLocation randomLocation() {
        if (random.nextInt(3) == 0) {
            return new Stalagmites();
        }
        return null;
    }

    public CaveHex(int roads) {
        this(roads, GROUND_COLOR);
    }

    public static void setRandom(Random rnd) {
        random = rnd;
    }

    @Override
    public String getTerrainName() {
        return "caves";
    }

    @Override
    public String getDescription() {
        return getTerrainName();
    }

    @Override
    public CombatTheme getCombatTheme() {
        return new CaveTheme();
    }

    @Override
    protected DailyEventState generateTerrainSpecificEvent(Model model) {
        List<DailyEventState> events = new ArrayList<>(List.of(
                new BatsEvent(model),
                // new UndergroundLakeEvent(model),
                // new PitfallEvent(model),
                new MineEvent(model),
                // new HideoutEvent(model),
                // new DwarvenCityEvent(model),
                new DeadBodyEvent(model),
                new GoblinsEvent(model),
                new OrcsEvent(model),
                new WoundedAdventurerEvent(model),
                new MushroomsEvent(model),
                new ChestEvent(model)
        ));
        if (canHaveExit()) {
            events.addAll(List.of(new ExitCaveEvent(model), new ExitCaveEvent(model), new ExitCaveEvent(model)));
        }
        return MyRandom.sample(events);
    }

    public java.util.List<DailyAction> getDailyActions(Model model) {
        List<DailyAction> actions;
        actions = new ArrayList<>();
        actions.add(new DailyAction("Travel", new TravelState(model)));
        actions.add(new StayInHexAction(model));
        if (canHaveExit()) {
            actions.add(new ExitCavesAction(model));
        }
        return actions;
    }

    protected boolean canHaveExit() {
        return true;
    }

    @Override
    protected SubView getSubView() {
        return subView;
    }

    protected HexSprite getUpperLeftSprite(MyColors color, int roads, int rivers) {
        return new HexSprite(getTerrainName()+"ul", 0x60 + roads % 4 + random.nextInt(4)*4, color);
    }

    protected HexSprite getUpperRightSprite(MyColors color, int roads, int rivers) {
        return new HexSprite(getTerrainName()+"ur", 0x80  + (roads / 4) % 4 + random.nextInt(4)*4, color);
    }

    protected HexSprite getLowerLeftSprite(MyColors color, int roads, int rivers) {
        return new HexSprite(getTerrainName()+"ll", 0x70 + (roads / 64) + random.nextInt(4)*4, color);
    }

    protected HexSprite getLowerRightSprite(MyColors color, int roads, int rivers) {
        return  new HexSprite(getTerrainName()+"lr", 0x90 + (roads / 16) % 4 + random.nextInt(4)*4, color);
    }

    public MyPair<Point, Integer> getDailyActionMenuPositionAndAnchor() {
        return DailyActionMenu.LOWER_LEFT_CORNER;
    }
}