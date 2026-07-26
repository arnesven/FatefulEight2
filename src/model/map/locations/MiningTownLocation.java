package model.map.locations;

import model.Model;
import model.SteppingMatrix;
import model.states.GameState;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import model.states.dailyaction.MiningTownDailyActionState;
import model.states.dailyaction.shops.GeneralShopNode;
import view.GameView;
import view.MyColors;
import view.help.HelpDialog;
import view.help.MiningTownHelpDialog;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;
import view.subviews.DailyActionSubView;
import view.subviews.ImageSubView;
import view.subviews.MiningTownSubView;
import view.subviews.SubView;

import java.awt.*;
import java.util.List;

public class MiningTownLocation extends TownishLocation {
    private final String name;
    private final ImageSubView subView;

    public MiningTownLocation(String name) {
        super("Mines of " + name);
        this.name = name;
        subView = new ImageSubView("ancient_city", "MINING TOWN",
                "You are in a Mining Town.", true);
    }

    @Override
    protected Sprite getUpperSprite() {
        return HexLocationSprite.make("miningtownupper", "past_foreground.png", 0x06, MyColors.BLACK, MyColors.BEIGE, MyColors.GRAY_RED, MyColors.DARK_BROWN);
    }

    @Override
    protected Sprite getLowerSprite() {
        return HexLocationSprite.make("miningtownlower", "past_foreground.png", 0x16, MyColors.BLACK, MyColors.BEIGE, MyColors.GRAY_RED, MyColors.DARK_BROWN);
    }

    @Override
    public GameState getDailyActionState(Model model) {
        return new MiningTownDailyActionState(model, this);
    }

    @Override
    public GameState getEveningState(Model model, boolean freeLodge, boolean freeRations) {
        return new MiningTownDailyActionState(model, this);
    }

    @Override
    public HelpDialog getHelpDialog(GameView view) {
        return new MiningTownHelpDialog(view);
    }

    @Override
    public String getPlaceName() {
        return "the Mines of " + name;
    }

    @Override
    public boolean showNameOnMap() {
        return false;
    }

    @Override
    public Point getTavernPosition() {
        return new Point(6, 6);
    }

    @Override
    public boolean noBoat() {
        return true;
    }

    @Override
    public SubView getImageSubView(Model model) {
        return subView;
    }

    @Override
    public List<GeneralShopNode> getShops(Model model) {
        return List.of();
    }

    @Override
    public DailyActionSubView makeActionSubView(Model model, AdvancedDailyActionState advancedDailyActionState, SteppingMatrix<DailyActionNode> matrix) {
        return new MiningTownSubView("Mines of a " + name, advancedDailyActionState, matrix);
    }

    @Override
    public int charterBoatEveryNDays() {
        return 0;
    }

    @Override
    public boolean bothBoatAndCarriage() {
        return false;
    }

    @Override
    public String getGeographicalDescription() {
        return "A mining town";
    }
}
