package model.map;

import model.Model;
import model.SteppingMatrix;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import model.states.dailyaction.GeneralShopNode;
import model.states.dailyaction.TownDailyActionState;
import model.states.events.NoEventState;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import view.MyColors;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;
import view.subviews.DailyActionSubView;
import view.subviews.ImageSubView;
import view.subviews.SubView;
import view.subviews.TownSubView;

import java.awt.Point;
import java.util.List;

public class CastleLocation extends HexLocation implements UrbanLocation {
    private final String lordName;
    private MyColors castleColor;
    private final SubView subView;

    public CastleLocation(String castleName, MyColors castleColor, String lordName) {
        super(castleName);
        this.castleColor = castleColor;
        this.lordName = lordName;
        subView = new ImageSubView("castle", "CASTLE", castleName, true);
    }

    @Override
    protected Sprite getUpperSprite() {
        return HexLocationSprite.make("castleupper", 0xC0, MyColors.BLACK, MyColors.LIGHT_GRAY, this.castleColor);
    }

    @Override
    protected Sprite getLowerSprite() {
        return HexLocationSprite.make("castlelower", 0xD0, MyColors.BLACK, MyColors.LIGHT_GRAY, this.castleColor);
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
    public boolean isDecoration() {
        return false;
    }

    @Override
    public boolean hasLodging() {
        return true;
    }

    @Override
    public String getLordName() {
        return lordName;
    }

    @Override
    public String getPlaceName() {
        return getName();
    }

    @Override
    public Point getTavernPosition() {
        return new Point(6, 3);
    }

    @Override
    public boolean noBoat() {
        return true;
    }

    @Override
    public List<GeneralShopNode> getShops(Model model) {
        return List.of(new GeneralShopNode(model, 1, 3)); // TODO: make method abstract
    }

    @Override
    public DailyActionSubView makeActionSubView(Model model, AdvancedDailyActionState advancedDailyActionState, SteppingMatrix<DailyActionNode> matrix) {
        return new TownSubView(advancedDailyActionState, matrix, false, getPlaceName());
    }

    @Override
    public boolean givesQuests() {
        return true;
    }

    @Override
    public DailyEventState generateEvent(Model model) {
        return new NoEventState(model);
    }

    @Override
    public GameState getDailyActionState(Model model) {
        return new TownDailyActionState(model, false, this);
    }

    @Override
    public GameState getEveningState(Model model, boolean freeLodge, boolean freeRations) {
        return new TownDailyActionState(model, false, this, freeLodge, freeRations);
    }


}
