package model.states.dailyaction;

import model.Model;
import model.items.Item;
import model.items.Prevalence;
import model.items.accessories.LargeShield;
import model.items.accessories.PirateCaptainsHat;
import model.items.accessories.Spyglass;
import model.items.clothing.PirateVest;
import model.items.potions.RumPotion;
import model.items.weapons.*;
import model.states.dailyaction.shops.GeneralShopNode;
import util.MyRandom;
import view.MyColors;
import view.sprites.SignSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.TownSubView;

import java.util.Collections;
import java.util.List;

public class PirateShop extends GeneralShopNode {

    private static final Sprite SPRITE = new Sprite32x32("yeoldshoppe", "world_foreground.png", 0x22,
            MyColors.PEACH, TownSubView.PATH_COLOR, MyColors.DARK_RED, MyColors.PEACH);
    private static final Sprite SIGN = new SignSprite("yeaoldesign", 0x1B0,
            MyColors.BLACK, MyColors.GOLD);

    private static final List<Item> ITEMS = List.of(
            new Scimitar(), new Rapier(), new Cutlass(), new Cutlass(),
            new BoatHook(), new Pistol(), new LargeShield(), new Harpoons(),
            new Spyglass(), new PirateVest(), new PirateCaptainsHat()
    );

    public PirateShop(Model model, int col, int row) {
        super(model, col, row, "Ye Old Shoppe");
    }

    @Override
    public Sprite getBackgroundSprite() {
        return SPRITE;
    }

    @Override
    public Sprite getForegroundSprite() {
        return SIGN;
    }

    @Override
    protected List<Item> makeInventory(Model model) {
        List<Item> list = model.getItemDeck().draw(ITEMS, MyRandom.randInt(10, 20),
                Prevalence.unspecified, 0.05);
        if (MyRandom.randInt(3) == 0) {
            list.add(new Musket());
        }
        for (int i = 0; i < MyRandom.randInt(5); ++i) {
            list.add(new RumPotion());
        }
        Collections.sort(list);
        return list;
    }

    public static List<Item> getPirateItems() {
        return ITEMS;
    }

    @Override
    protected int getShopSecurity() {
        return 9;
    }
}
