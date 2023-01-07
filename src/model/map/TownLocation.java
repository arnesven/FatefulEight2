package model.map;

import model.Model;
import model.actions.DailyAction;
import model.actions.RecruitAction;
import model.actions.SaveGameAction;
import model.items.Item;
import model.states.*;
import model.states.dailyaction.TownDailyActionState;
import model.states.events.*;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import util.MyRandom;
import view.MyColors;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;
import view.subviews.ImageSubView;
import view.subviews.SubView;

import java.util.ArrayList;
import java.util.List;

public class TownLocation extends HexLocation implements LordLocation {
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
    public boolean hasDailyActions() {
        return true;
    }

    @Override
    public List<DailyAction> getDailyActions(Model model) {
        List<DailyAction> list = new ArrayList<>();
        List<Item> shopInventory = ShopState.makeRandomShopInventory(model,
                MyRandom.randInt(10, 20), MyRandom.randInt(5,39), MyRandom.randInt(6));
        list.add(new DailyAction("Shop", new ShopState(model, "town merchant", shopInventory, null)));
        list.add(new RecruitAction(model));
        list.add(new SaveGameAction(model));
        return list;
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
                    new CourierEvent(model)
            ));
        }
        return new NoEventState(model);
    }

    @Override
    public GameState getDailyActionState(Model model) {
        return new TownDailyActionState(model, isCoastal);
    }

    @Override
    public GameState getEveningState(Model model, boolean freeLodge, boolean freeRations) {
        return new TownDailyActionState(model, isCoastal);
    }
}
