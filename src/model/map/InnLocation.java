package model.map;

import model.Model;
import model.actions.DailyAction;
import model.actions.RecruitAction;
import model.actions.SaveGameAction;
import model.items.*;
import model.states.*;
import model.states.dailyaction.TavernDailyActionState;
import model.states.events.SilentNoEventState;
import util.MyRandom;
import view.MyColors;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;
import view.subviews.ImageSubView;
import view.subviews.SubView;

import java.util.*;

public class InnLocation extends HexLocation {

    private static SubView subView = new ImageSubView("theinn", "THE INN", "A cozy inn at the side of the road.");

    public InnLocation(String name) {
        super(name);
    }

    @Override
    protected Sprite getUpperSprite() {
        return HexLocationSprite.make("crossroadsinnupper", 0x20, MyColors.BROWN, MyColors.BLACK, MyColors.DARK_GRAY);
    }

    @Override
    public boolean isDecoration() {
        return false;
    }

    @Override
    protected Sprite getLowerSprite() {
        return  HexLocationSprite.make("crossroadsinlower", 0x30, MyColors.BROWN, MyColors.BLACK, MyColors.DARK_GRAY);
    }

    @Override
    public boolean hasLodging() {
        return true;
    }

    @Override
    public SubView getImageSubView() {
        return subView;
    }

    public static SubView getSubView() {
        return subView;
    }

    @Override
    public boolean hasDailyActions() {
        return true;
    }

    @Override
    public List<DailyAction> getDailyActions(Model model) {
        List<DailyAction> list = new ArrayList<>();
        List<Item> shopInventory = ShopState.makeGeneralShopInventory(model,
                MyRandom.randInt(5, 9), MyRandom.randInt(4,6), MyRandom.randInt(2));
        list.add(new DailyAction("Shop", new ShopState(model, "innkeeper", shopInventory, null)));
        list.add(new RecruitAction(model));
        list.add(new SaveGameAction(model));
        return list;
    }

    @Override
    public DailyEventState generateEvent(Model model) {
        return new SilentNoEventState(model);
    }

    @Override
    public GameState getDailyActionState(Model model) {
        return new TavernDailyActionState(model, false, false);
    }

    @Override
    public GameState getEveningState(Model model, boolean freeLodge, boolean freeRations) {
        return new TavernDailyActionState(model, freeLodge, false);
    }
}
