package model.map.locations;

import model.Model;
import model.SteppingMatrix;
import model.map.Direction;
import model.map.HexLocation;
import model.map.UrbanLocation;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import model.states.dailyaction.FishingVillageActionState;
import model.states.dailyaction.TownishDailyActionState;
import model.states.dailyaction.shops.GeneralShopNode;
import model.states.events.FishingVillageCommonerEvent;
import model.states.events.FishingVillageFisherEvent;
import model.states.events.MerchantEvent;
import util.MyRandom;
import view.GameView;
import view.MyColors;
import view.help.FishingVillageHelpDialog;
import view.help.HelpDialog;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;
import view.subviews.*;

import java.awt.*;
import java.util.List;

public class FishingVillageLocation extends TownishLocation {
    private static final MyColors HUT_COLOR = MyColors.BROWN;
    private static final MyColors ROOF_COLOR = MyColors.GRAY;
    private final ImageSubView subView;
    private final int direction;
    private final MyColors waterColor;

    public FishingVillageLocation(int direction, MyColors waterColor) {
        super("Fishing Village");
        this.direction = direction;
        this.waterColor = waterColor;
        subView = new ImageSubView("fishing_village", getName().toUpperCase(), "You are in a " + getName(), true);
    }

    public FishingVillageLocation(int direction) {
        this(direction, MyColors.LIGHT_BLUE);
    }

    @Override
    public boolean showNameOnMap() {
        return false;
    }

    @Override
    public SubView getImageSubView(Model model) {
        return subView;
    }


    @Override
    public GameState getDailyActionState(Model model) {
        return new FishingVillageActionState(model, this);
    }

    @Override
    public boolean inhibitOnRoadSubview() {
        return true;
    }

    @Override
    public GameState getEveningState(Model model, boolean freeLodge, boolean freeRations) {
        return new FishingVillageActionState(model, this);
    }

    @Override
    public DailyEventState generateEvent(Model model) {
        return MyRandom.sample(List.of(
                new MerchantEvent(model),
                new FishingVillageFisherEvent(model),
                new FishingVillageCommonerEvent(model),
                new FishingVillageCommonerEvent(model)
                ));
    }

    @Override
    protected Sprite getUpperSprite() {
        int num = getSpriteNumForDirection(direction);
        HexLocationSprite spr = HexLocationSprite.make("fishingvillageupper"+direction, num, MyColors.BLACK, HUT_COLOR, ROOF_COLOR, waterColor);
        if (direction == Direction.NORTH_WEST || direction == Direction.SOUTH_WEST) {
            spr.setFlip(true);
        }
        return spr;
    }

    @Override
    protected Sprite getLowerSprite() {
        int num = 0x10 + getSpriteNumForDirection(direction);
        HexLocationSprite spr = HexLocationSprite.make("fishingvillagelower"+direction, num, MyColors.BLACK, HUT_COLOR, ROOF_COLOR, waterColor);
        if (direction == Direction.NORTH_WEST || direction == Direction.SOUTH_WEST) {
            spr.setFlip(true);
        }
        return spr;
    }


    private int getSpriteNumForDirection(int direction) {
        switch (direction) {
            case Direction.NORTH:
                return 0x16F;
            case Direction.NORTH_WEST:
            case Direction.NORTH_EAST:
                return 0x16E;
            case Direction.SOUTH_WEST:
            case Direction.SOUTH_EAST:
                return 0x18F;
            case Direction.SOUTH:
                return 0x18E;
        }
        throw new IllegalArgumentException("Can't make fishing village in that direction: " + direction);
    }

    @Override
    public HelpDialog getHelpDialog(GameView view) {
        return new FishingVillageHelpDialog(view);
    }

    @Override
    public String getPlaceName() {
        return "Fishing Village";
    }

    @Override
    public Point getTavernPosition() {
        return null;
    }

    @Override
    public boolean noBoat() {
        return false;
    }

    @Override
    public List<GeneralShopNode> getShops(Model model) {
        return List.of();
    }

    @Override
    public DailyActionSubView makeActionSubView(Model model, AdvancedDailyActionState advancedDailyActionState, SteppingMatrix<DailyActionNode> matrix) {
        return new FishingVillageSubView(advancedDailyActionState, matrix);
    }

    @Override
    public int charterBoatEveryNDays() {
        return 1;
    }

    @Override
    public boolean bothBoatAndCarriage() {
        return false;
    }

    @Override
    public String getGeographicalDescription() {
        return "It's by the ocean.";
    }

    @Override
    public HexLocation makePastSelf() {
        return new PastFishingVillage(direction);
    }
}
