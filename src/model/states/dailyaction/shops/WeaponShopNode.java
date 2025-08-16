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

public class WeaponShopNode extends GeneralShopNode {

    private static final Sprite WEAPON_SIGN = new SignSprite("weaponsign", 0x16,
            MyColors.PURPLE, MyColors.WHITE);
    private static final Sprite BIG_SIGN = new Sprite32x32("shopsign", "world_foreground.png", 0x7E,
            MyColors.BLACK, MyColors.BROWN, MyColors.BEIGE);

    private static final Sprite[] SHOP_DECORATIONS = new Sprite[]{
            new MiniItemSprite(0, MyColors.LIGHT_GRAY, MyColors.BROWN),
            new MiniItemSprite(0, MyColors.GOLD, MyColors.BLUE),
    };

    public WeaponShopNode(Model model, int column, int row) {
        super(model, column, row, "Weapon Shop");
    }

    @Override
    protected List<Item> makeInventory(Model model) {
        List<Item> inventory = new ArrayList<>();
        inventory.addAll(model.getItemDeck().draw(ItemDeck.allWeapons(), MyRandom.randInt(2,10), Prevalence.common, 0.0));
        inventory.addAll(model.getItemDeck().draw(ItemDeck.allWeapons(), MyRandom.randInt(3,10), Prevalence.uncommon, 0.0));
        inventory.addAll(model.getItemDeck().draw(ItemDeck.allWeapons(), MyRandom.randInt(1,5), Prevalence.rare, 0.0));
        Collections.sort(inventory);
        return inventory;
    }

    @Override
    public Sprite getBigSignSprite() {
        return BIG_SIGN;
    }

    @Override
    public Sprite[] getCounterItemSprites() {
        return SHOP_DECORATIONS;
    }

    @Override
    protected int getShopSecurity() {
        return 9;
    }

    @Override
    public Sprite getForegroundSprite() {
        return WEAPON_SIGN;
    }
}
