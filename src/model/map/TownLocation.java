package model.map;

import model.Model;
import model.SteppingMatrix;
import model.states.*;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import model.states.dailyaction.GeneralShopNode;
import model.states.dailyaction.TownDailyActionState;
import model.states.events.*;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import util.MyRandom;
import view.MyColors;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;
import view.subviews.DailyActionSubView;
import view.subviews.ImageSubView;
import view.subviews.SubView;
import view.subviews.TownSubView;

import java.awt.*;
import java.util.List;

public class TownLocation extends HexLocation implements UrbanLocation {
    private final String townName;
    private final SubView subView;
    private final String lordName;
    private final boolean isCoastal;

    public TownLocation(String townName, String lordName, boolean isCoastal) {
        super("Town of " + townName);
        this.townName = townName;
        this.lordName = lordName;
        subView = new ImageSubView("town", "TOWN", "Town of " + townName, true);
        this.isCoastal = isCoastal;
    }

    @Override
    protected Sprite getLowerSprite() {
        return HexLocationSprite.make("townlower", 0x90, MyColors.BLACK, MyColors.LIGHT_YELLOW, MyColors.GRAY);
    }

    @Override
    protected Sprite getUpperSprite() {
        return HexLocationSprite.make("townupper", 0x80, MyColors.BLACK, MyColors.LIGHT_YELLOW, MyColors.GRAY);
    }

    @Override
    public boolean isDecoration() {
        return false;
    }

    @Override
    public boolean hasLodging() {
        return true;
    }

    public SubView getImageSubView() {
        return subView;
                }

    @Override
    public void travelTo(Model model) {
        ClientSoundManager.playBackgroundMusic(BackgroundMusic.citySong);
    }

    @Override
    public void travelFrom(Model model) {
        model.playMainSong();
    }

    @Override
    public String getLordName() {
        return lordName;
    }

    @Override
    public String getPlaceName() {
        return "the Town of " + townName;
    }

    @Override
    public boolean givesQuests() {
        return true;
    }

    @Override
    public DailyEventState generateEvent(Model model) {
        if (MyRandom.rollD10() >= 3) {
            return MyRandom.sample(List.of(
                    new MuggingEvent(model),
                    new AssassinEvent(model),
                    new MarketEvent(model),
                    new CourierEvent(model),
                    new BorrowedMoneyEvent(model)
            ));
        }
        return new NoEventState(model);
    }

    @Override
    public GameState getDailyActionState(Model model) {
        return new TownDailyActionState(model, isCoastal, this);
    }

    public Point getTavernPosition() {
        return new Point(1, 4);
    }

    @Override
    public GameState getEveningState(Model model, boolean freeLodge, boolean freeRations) {
        return new TownDailyActionState(model, isCoastal, this, freeLodge, freeRations);
    }

    public List<GeneralShopNode> getShops(Model model) {
        return List.of(new GeneralShopNode(model, 6, 1));
    }

    @Override
    public DailyActionSubView makeActionSubView(Model model, AdvancedDailyActionState advancedDailyActionState, SteppingMatrix<DailyActionNode> matrix) {
        return new TownSubView(advancedDailyActionState, matrix, isCoastal, getTownName());
    }

    @Override
    public Point getTravelNodePosition() {
        return new Point(AdvancedDailyActionState.TOWN_MATRIX_COLUMNS-1, AdvancedDailyActionState.TOWN_MATRIX_ROWS-2);
    }

    public String getTownName() {
        return townName;
    }

    public boolean noBoat() {
        return false;
    }
}
