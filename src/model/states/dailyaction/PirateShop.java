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
import view.sprites.MiniItemSprite;
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
    private static final Sprite BIG_SIGN = new Sprite32x32("sign", "world_foreground.png", 0x8D,
            MyColors.BLACK, MyColors.BROWN, MyColors.BEIGE);
    protected static final Sprite WALL = new Sprite32x32("lowerwall", "world_foreground.png", 0x24,
            MyColors.DARK_GRAY, MyColors.PEACH, MyColors.TAN);
    public static final Sprite DOOR = new Sprite32x32("door", "world_foreground.png", 0x34,
            MyColors.DARK_GRAY, MyColors.PEACH, MyColors.TAN, MyColors.DARK_RED);
    public static final Sprite OVER_DOOR = new Sprite32x32("overdoor", "world_foreground.png", 0x06,
            MyColors.DARK_GRAY, MyColors.PEACH, MyColors.TAN, MyColors.BLACK);

    private static final Sprite[] SHOP_DECORATIONS = new Sprite[]{
            new MiniItemSprite(0, MyColors.LIGHT_GRAY, MyColors.BROWN),
            new MiniItemSprite(8, MyColors.DARK_BROWN, MyColors.BEIGE),
    };


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
        list.addAll(model.getItemDeck().draw(MyRandom.randInt(3, 6), Prevalence.common));
        Collections.sort(list);
        return list;
    }

    @Override
    public Sprite[] getCounterItemSprites() {
        return SHOP_DECORATIONS;
    }

    @Override
    public Sprite getBigSignSprite() {
        return BIG_SIGN;
    }

    @Override
    public Sprite getLowerWallSprite() {
        return WALL;
    }

    @Override
    public Sprite getDoorSprite() {
        return DOOR;
    }

    @Override
    public Sprite getOverDoorSprite() {
        return OVER_DOOR;
    }

    public static List<Item> getPirateItems() {
        return ITEMS;
    }

    @Override
    protected int getShopSecurity() {
        return 9;
    }
}
