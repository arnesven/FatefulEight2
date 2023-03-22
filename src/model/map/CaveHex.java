package model.map;

import model.Model;
import model.actions.*;
import model.states.DailyEventState;
import model.states.TravelState;
import model.states.events.NoEventState;
import util.MyPair;
import view.MyColors;
import view.sprites.HexSprite;
import view.subviews.DailyActionMenu;
import view.subviews.ImageSubView;
import view.subviews.SubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CaveHex extends WorldHex {
    private static SubView subView = new ImageSubView("thecaves", "CAVES", "You are exploring a system of caves...", true);;

    public CaveHex(int roads) {
        super(MyColors.GRAY_RED, roads, 0x0, null);
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
    protected DailyEventState generateTerrainSpecificEvent(Model model) {
        return new NoEventState(model);
    }

    public java.util.List<DailyAction> getDailyActions(Model model) {
        List<DailyAction> actions;
        actions = new ArrayList<>();
        actions.add(new DailyAction("Travel", new TravelState(model)));
        actions.add(new StayInHexAction(model));
        actions.add(new ExitCavesAction(model));
        return actions;
    }

    @Override
    protected SubView getSubView() {
        return subView;
    }

    protected HexSprite getUpperLeftSprite(MyColors color, int roads, int rivers) {
        return new HexSprite(getTerrainName()+"ul", 0x60 + roads % 4, color);
    }

    protected HexSprite getUpperRightSprite(MyColors color, int roads, int rivers) {
        return new HexSprite(getTerrainName()+"ur", 0x80  + (roads / 4) % 4, color);
    }

    protected HexSprite getLowerLeftSprite(MyColors color, int roads, int rivers) {
        return new HexSprite(getTerrainName()+"ll", 0x70 + (roads / 64), color);
    }

    protected HexSprite getLowerRightSprite(MyColors color, int roads, int rivers) {
        return  new HexSprite(getTerrainName()+"lr", 0x90 + (roads / 16) % 4, color);
    }

    public MyPair<Point, Integer> getDailyActionMenuPositionAndAnchor() {
        return DailyActionMenu.LOWER_LEFT_CORNER;
    }
}
