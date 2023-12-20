package model.map;

import model.Model;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.events.EveningAtSeaState;
import model.states.events.NoEventState;
import view.sprites.Sprite;
import view.subviews.CombatTheme;
import view.subviews.ImageSubView;
import view.subviews.ShipCombatTheme;
import view.subviews.SubView;
import view.MyColors;
import view.sprites.HexSprite;
import view.sprites.SeaHexSprite;

public class SeaHex extends WorldHex {
    private static final SeaHexSprite SPRITE_UL = new SeaHexSprite(0xA0);
    private static final SeaHexSprite SPRITE_UR = new SeaHexSprite(0xA4);
    private static final SeaHexSprite SPRITE_LL = new SeaHexSprite(0xB0);
    private static final SeaHexSprite SPRITE_LR = new SeaHexSprite(0xB4);

    public static SubView SUB_VIEW = new ImageSubView("ship", "THE SEA", "You are sailing the sea.");

    public SeaHex(int state) {
        super(MyColors.LIGHT_BLUE, 0, Direction.ALL, null, state);
    }

    @Override
    protected Sprite getUpperLeftSprite(MyColors color, int roads, int rivers) {
        return SPRITE_UL;
    }

    @Override
    protected Sprite getUpperRightSprite(MyColors color, int roads, int rivers) {
        return SPRITE_UR;
    }

    @Override
    protected Sprite getLowerLeftSprite(MyColors color, int roads, int rivers) {
        return SPRITE_LL;
    }

    @Override
    protected Sprite getLowerRightSprite(MyColors color, int roads, int rivers) {
        return SPRITE_LR;
    }

    @Override
    public String getTerrainName() {
        return "water";
    }

    public String getDescription() {
        return getTerrainName();
    }

    @Override
    protected SubView getSubView() {
        return SUB_VIEW;
    }

    @Override
    protected DailyEventState generateTerrainSpecificEvent(Model model) {
        return new NoEventState(model);
    }

    @Override
    public boolean canTravelTo(Model model) {
        return false;
    }

    @Override
    public String getTerrainDescription() {
        return "The sea can be traversed by boat. Either by traveling on a standard line from port to port, or by " +
                "chartering a boat.";
    }

    @Override
    public CombatTheme getCombatTheme() {
        return new ShipCombatTheme();
    }

    @Override
    public GameState getEveningState(Model model, boolean freeLodging, boolean freeRations) {
        return new EveningAtSeaState(model);
    }
}
