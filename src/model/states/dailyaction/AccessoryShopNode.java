package model.states.dailyaction;

import model.items.ItemDeck;
import model.Model;
import model.items.Item;
import model.items.Prevalence;
import util.MyRandom;
import view.MyColors;
import view.sprites.SignSprite;
import view.sprites.Sprite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AccessoryShopNode extends GeneralShopNode {

    private static final Sprite SIGN = new SignSprite("accessorysign", 0x26,
            MyColors.GOLD, MyColors.WHITE);

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
}
