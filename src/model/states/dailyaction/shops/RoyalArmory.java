package model.states.dailyaction.shops;

import model.Model;
import model.items.Item;
import view.MyColors;
import view.sprites.MiniItemSprite;
import view.sprites.SignSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoyalArmory extends GeneralShopNode {

    private static final Sprite[] SHOP_DECORATIONS = new Sprite[]{
            new Sprite32x32("sign", "world_foreground.png", 0x7F,
                    MyColors.BLACK, MyColors.GOLD, MyColors.BLACK),
            new MiniItemSprite(1, MyColors.LIGHT_GRAY, MyColors.DARK_GRAY),
            new MiniItemSprite(3, MyColors.LIGHT_GRAY, MyColors.RED),
    };

    private static final Sprite SIGN = new SignSprite("royalarmorysign", 0x17,
            MyColors.BLACK, MyColors.GOLD);

    public RoyalArmory(Model model, int x, int y) {
        super(model, x, y, "Royal Armory");
    }

    @Override
    protected List<Item> makeInventory(Model model) {
        WeaponShopNode wsn = new WeaponShopNode(model, 0, 0);
        ArmorShopNode asn = new ArmorShopNode(model, 0, 0);
        List<Item> inventory = new ArrayList<>();
        inventory.addAll(wsn.makeInventory(model));
        inventory.addAll(asn.makeInventory(model));
        Collections.sort(inventory);
        return inventory;
    }

    @Override
    protected Sprite[] getShopDecorations() {
        return SHOP_DECORATIONS;
    }

    @Override
    protected int getShopSecurity() {
        return 11;
    }

    @Override
    public Sprite getForegroundSprite() {
        return SIGN;
    }
}
