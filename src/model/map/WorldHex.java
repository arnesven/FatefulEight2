package model.map;

import model.Model;
import model.actions.DailyAction;
import model.actions.GetOffRoadAction;
import model.actions.GetOnRoadAction;
import model.actions.StayInHexAction;
import model.eve.SaberfishEvent;
import model.states.*;
import model.states.events.*;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import util.MyRandom;
import view.subviews.SubView;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.HexSprite;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class WorldHex implements Serializable {

    public static final int NORTH_WEST = 0x01;
    public static final int NORTH      = 0x0A;
    public static final int NORTH_EAST = 0x04;
    public static final int SOUTH_EAST = 0x10;
    public static final int SOUTH      = 0xA0;
    public static final int SOUTH_WEST = 0x40;
    public static final int ALL = 0xFF;
    private MyColors color;

    private HexSprite upperLeft;
    private HexSprite upperRight;
    private HexSprite lowerLeft;
    private HexSprite lowerRight;
    private int roads;
    private int rivers;
    private HexLocation hexLocation;
    private BackgroundMusic music;


    public WorldHex(MyColors color, int roads, int rivers, HexLocation location) {
        this.color = color;
        this.roads = roads;
        this.rivers = rivers;
        setHexSprites();
        this.hexLocation = location;
    }

    public void setColor(MyColors col) {
        color = col;
    }

    public abstract String getTerrainName();

    public DailyEventState generateEvent(Model model) {
        if (model.getParty().isOnRoad()) {
            return generateOnRoadEvent(model);
        }
        return generateTerrainSpecificEvent(model);
    }

    protected abstract DailyEventState generateTerrainSpecificEvent(Model model);

    private void setHexSprites() {
        this.upperLeft = getUpperLeftSprite(color, roads, rivers);
        this.upperRight = getUpperRightSprite(color, roads, rivers);
        this.lowerLeft = getLowerLeftSprite(color, roads, rivers);
        this.lowerRight = getLowerRightSprite(color, roads, rivers);
    }

    protected HexSprite getUpperLeftSprite(MyColors color, int roads, int rivers) {
        return new HexSprite(getTerrainName()+"ul", 0x00 + (roads % 4)*4 + rivers % 4, color);
    }

    protected HexSprite getUpperRightSprite(MyColors color, int roads, int rivers) {
        return new HexSprite(getTerrainName()+"ur", 0x20  + ((roads / 4) % 4)*4 + (rivers / 4) % 4, color);
    }

    protected HexSprite getLowerLeftSprite(MyColors color, int roads, int rivers) {
        return new HexSprite(getTerrainName()+"ll", 0x10 + (roads / 64)*4 + (rivers / 64), color);
    }

    protected HexSprite getLowerRightSprite(MyColors color, int roads, int rivers) {
        return  new HexSprite(getTerrainName()+"lr", 0x30 + ((roads / 16) % 4) * 4 + (rivers / 16) % 4, color);
    }

    public WorldHex(MyColors color) {
        this(color, 0, 0, null);
    }

    public void drawYourself(ScreenHandler screenHandler, int x, int y) {
        drawUpperHalf(screenHandler, x, y);
        drawLowerHalf(screenHandler, x, y);
    }

    public void drawUpperHalf(ScreenHandler screenHandler, int x, int y) {
        screenHandler.put(x, y, upperLeft);
        screenHandler.put(x+2, y, upperRight);
        if (hexLocation != null) {
            hexLocation.drawUpperHalf(screenHandler, x, y);
        }
    }

    public void drawLowerHalf(ScreenHandler screenHandler, int x, int y) {
        screenHandler.put(x, y+2, lowerLeft);
        screenHandler.put(x+2, y+2, lowerRight);
        if (hexLocation != null) {
            hexLocation.drawLowerHalf(screenHandler, x, y);
        }
    }

    public String getDescription() {
        StringBuilder bldr = new StringBuilder();
        if (hexLocation != null && !hexLocation.isDecoration()) {
            bldr.append(hexLocation.getName() + ", ");
        }
        if (rivers != 0) {
            bldr.append("coastal ");
        }
        bldr.append(getTerrainName());
        if (roads != 0) {
            bldr.append(" with roads");
        }
        return bldr.toString();
    }

    public int getRivers() {
        return rivers;
    }

    public void setRivers(int i) {
        this.rivers = i;
        this.setHexSprites();
    }

    public List<DailyAction> getDailyActions(Model model) {
        List<DailyAction> actions;
        actions = new ArrayList<>();
        actions.add(new DailyAction("Travel", new TravelState(model)));
        actions.add(new StayInHexAction(model));
        if (hexLocation != null && hexLocation.hasDailyActions()) {
            actions.addAll(hexLocation.getDailyActions(model));
        }
        if (model.getParty().isOnRoad()) {
            actions.add(new GetOffRoadAction(model));
        } else if (hasRoad()) {
            actions.add(new GetOnRoadAction(model));
        }
        return actions;
    }

    public String getPlaceName() {
        if (hexLocation != null && !hexLocation.isDecoration()) {
            return "at the " + hexLocation.getName();
        }
        return "in the " + getDescription();
    }

    public boolean hasLodging() {
        if (hexLocation != null && hexLocation.hasLodging()) {
            return true;
        }
        return false;
    }

    public SubView getImageSubView() {
        if (hexLocation != null && !hexLocation.isDecoration()) {
            return hexLocation.getImageSubView();
        }
        return getSubView();
    }

    public boolean hasRoad() {
        return roads != 0;
    }

    protected abstract SubView getSubView();

    private DailyEventState generateOnRoadEvent(Model model) {
        int dieRoll = MyRandom.rollD10();
        if (5 <= dieRoll && dieRoll <= 8 ) {
            List<DailyEventState> events = new ArrayList<>();
            events.add(new MerchantEvent(model));
            events.add(new BanditEvent(model));
            events.add(new MagicianEvent(model));
            events.add(new ArtisanEvent(model));
            events.add(new PriestEvent(model));
            events.add(new CourierEvent(model));
            events.add(new MonumentEvent(model));
            events.add(new CompanyEvent(model));
            events.add(new NoblemanEvent(model));
            events.add(new FriendEvent(model));
            return MyRandom.sample(events);
        } else if (dieRoll >= 9) {
            return generateTerrainSpecificEvent(model);
        }
        return new NoEventState(model);
    }

    public void travelTo(Model model) {
        if (hexLocation != null) {
            hexLocation.travelTo(model);
        }
        if (hasMusic()) {
            ClientSoundManager.playBackgroundMusic(music);
        }
    }

    private boolean hasMusic() {
        return this.music != null;
    }

    public void travelFrom(Model model) {
        if (hexLocation != null) {
            hexLocation.travelFrom(model);
        }
    }

    protected void setMusic(BackgroundMusic bg) {
        this.music = bg;
    }

    public boolean hasLord() {
        if (hexLocation != null) {
            return hexLocation instanceof LordLocation;
        }
        return false;
    }

    public HexLocation getLocation() {
        return hexLocation;
    }

    public boolean canTravelTo(Model model) {
        return true;
    }

    public boolean givesQuests() {
        return hexLocation != null && hexLocation.givesQuests();
    }

    private boolean getRoadOrRiverInDirection(String directionName, int roadOrRiver) {
        if (directionName.equals("SE")) {
            return (roadOrRiver & SOUTH_EAST) != 0;
        }
        if (directionName.equals("S")) {
            return (roadOrRiver & SOUTH) != 0;
        }
        if (directionName.equals("SW")) {
            return (roadOrRiver & SOUTH_WEST) != 0;
        }
        if (directionName.equals("NE")) {
            return (roadOrRiver & NORTH_EAST) != 0;
        }
        if (directionName.equals("N")) {
            return (roadOrRiver & NORTH) != 0;
        }
        if (directionName.equals("NW")) {
            return (roadOrRiver & NORTH_WEST) != 0;
        }
        throw new IllegalStateException("Illegal direction \"" + directionName + "\"");
    }

    public boolean getRiversInDirection(String directionName) {
        return getRoadOrRiverInDirection(directionName, rivers);
    }

    public boolean getRoadInDirection(String directionName) {
        return getRoadOrRiverInDirection(directionName, roads);
    }

    public RiverEvent generateRiverEvent(Model model) {
        int d10Roll = MyRandom.rollD10();
        if (d10Roll <= 4) {
            return new ShallowsEvent(model);
        } else if (d10Roll >= 9) {
            return new NoRiverCrossingEvent(model);
        }
        return MyRandom.sample(List.of(
                new FerryEvent(model),
                new BlackKnightEvent(model),
                new RopeBridgeEvent(model),
                new DeadBodyEvent(model),
                new UndertowEvent(model),
                new RapidsEvent(model),
                new SaberfishEvent(model)
//                new RaftEvent(model) // TODO: implement
        ));
    }

}
