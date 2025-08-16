package model.states.dailyaction.shops;

import model.items.ItemDeck;
import model.Model;
import model.items.Item;
import model.items.Prevalence;
import model.states.dailyaction.shops.GeneralShopNode;
import util.MyRandom;
import view.MyColors;
import view.sprites.MiniItemSprite;
import view.sprites.SignSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AccessoryShopNode extends GeneralShopNode {

    private static final Sprite SIGN = new SignSprite("accessorysign", 0x26,
            MyColors.GOLD, MyColors.WHITE);
    private static final Sprite BIG_SIGN = new Sprite32x32("sign", "world_foreground.png", 0x8E,
            MyColors.BLACK, MyColors.BROWN, MyColors.BEIGE);
    private static final Sprite[] SHOP_DECORATIONS = new Sprite[]{
            new MiniItemSprite(2, MyColors.LIGHT_GRAY, MyColors.DARK_GRAY),
            new MiniItemSprite(8, MyColors.BROWN, MyColors.BEIGE),
    };

    public AccessoryShopNode(Model model, int column, int row) {
        super(model, column, row, "Accessory Shop");
    }

    @Override
    public Sprite getForegroundSprite() {
        return SIGN;
    }

    @Override
    protected List<Item> makeInventory(Model model) {
        List<Item> inventory = new ArrayList<>();
        inventory.addAll(model.getItemDeck().draw(ItemDeck.allJewelry(), MyRandom.randInt(3, 10), Prevalence.unspecified, 0.0));
        inventory.addAll(model.getItemDeck().draw(ItemDeck.allHeadGear(), MyRandom.randInt(1,7), Prevalence.unspecified, 0.0));
        inventory.addAll(model.getItemDeck().draw(ItemDeck.allGloves(), MyRandom.randInt(1,7), Prevalence.unspecified, 0.0));
        Collections.sort(inventory);
        return inventory;
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
    protected int getShopSecurity() {
        return 8;
    }
}
