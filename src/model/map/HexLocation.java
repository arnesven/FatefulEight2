package model.map;

import model.Model;
import model.Party;
import model.actions.DailyAction;
import model.states.DailyActionState;
import model.states.DailyEventState;
import model.states.EveningState;
import model.states.GameState;
import model.states.events.NoEventState;
import util.MyPair;
import view.GameView;
import view.MyColors;
import view.help.HelpDialog;
import view.sprites.Sprite16x16;
import view.subviews.SubView;
import view.subviews.ImageSubView;
import view.ScreenHandler;
import view.sprites.Sprite;

import java.awt.*;
import java.io.Serializable;
import java.util.List;

public abstract class HexLocation implements Serializable {
    public static final int FLAG_NONE = 0;
    public static final int FLAG_FAILURE = 1;
    public static final int FLAG_SUCCESS = 2;
    private static final Sprite SUCCESS_FLAG = new Sprite16x16("successflag", "world_foreground.png", 0x63,
            MyColors.BLACK, MyColors.WHITE, MyColors.GREEN, MyColors.GREEN);
    private static final Sprite FAILURE_FLAG = new Sprite16x16("failureflag", "world_foreground.png", 0x73,
            MyColors.BLACK, MyColors.WHITE, MyColors.RED, MyColors.BLACK);
    private final String name;
    private WorldHex hex;

    public HexLocation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void drawUpperHalf(ScreenHandler screenHandler, int x, int y, int flag) {
        screenHandler.register(name + "uppersprite"+x+""+y, new Point(x, y), getUpperSprite());
        if (flag == FLAG_SUCCESS) {
            screenHandler.register("flagsuccess"+x+""+y, new Point(x+2, y), SUCCESS_FLAG, 1);
        } else if (flag == FLAG_FAILURE) {
            screenHandler.register("flagfailure"+x+""+y, new Point(x+2, y), FAILURE_FLAG, 1);
        }
    }

    public void drawLowerHalf(ScreenHandler screenHandler, int x, int y) {
        screenHandler.register(name + "lowersprite"+x+""+y, new Point(x, y+2), getLowerSprite());
    }

    protected abstract Sprite getLowerSprite();

    protected abstract Sprite getUpperSprite();

    public abstract boolean isDecoration();

    public boolean hasLodging() { return false; }

    public SubView getImageSubView() { return null; }

    public boolean hasDailyActions() {
        return false;
    }

    public List<DailyAction> getDailyActions(Model model) {
        return null;
    }

    public void travelTo(Model model) { }

    public void travelFrom(Model model) { }

    public boolean givesQuests() {
        return false;
    }

    public DailyEventState generateEvent(Model model) {
        return new NoEventState(model);
    }

    public GameState getDailyActionState(Model model) {
        return new DailyActionState(model);
    }

    public GameState getEveningState(Model model, boolean freeLodge, boolean freeRations) {
        return new EveningState(model, freeLodge, freeRations, true);
    }

    public void setHex(WorldHex hex) {
        this.hex = hex;
    }

    public WorldHex getHex() {
        return hex;
    }

    public Sprite getTownOrCastleSprite() {
        return null;
    }

    public MyPair<Point, Integer> getDailyActionMenuAnchor() { return null; }

    public boolean inhibitOnRoadSubview() {
        return false;
    }

    public abstract HelpDialog getHelpDialog(GameView view);

    public HexLocation makePastSelf() {
        if (isDecoration()) {
            return this;
        }
        return null;
    }
}
