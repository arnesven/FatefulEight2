package model.states.dailyaction.shops;

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

public class ArmorShopNode extends GeneralShopNode {
    private static final Sprite ARMOR_SIGN = new SignSprite("armorsign", 0x17,
            MyColors.DARK_GREEN, MyColors.WHITE);

    public ArmorShopNode(Model model, int column, int row) {
        super(model, column, row, "Armor Shop");
    }

    @Override
    protected List<Item> makeInventory(Model model) {
        List<Item> inventory = new ArrayList<>();
        inventory.addAll(model.getItemDeck().draw(ItemDeck.allApparel(), MyRandom.randInt(4,10), Prevalence.unspecified, 0.0));
        inventory.addAll(model.getItemDeck().draw(ItemDeck.allShields(), MyRandom.randInt(1,6), Prevalence.unspecified, 0.0));
        inventory.addAll(model.getItemDeck().draw(ItemDeck.allShoes(), MyRandom.randInt(3), Prevalence.unspecified, 0.0));
        inventory.addAll(model.getItemDeck().draw(ItemDeck.allHeadGear(), MyRandom.randInt(3), Prevalence.unspecified, 0.0));
        inventory.addAll(model.getItemDeck().draw(ItemDeck.allGloves(), MyRandom.randInt(3), Prevalence.unspecified, 0.0));
        Collections.sort(inventory);
        return inventory;
    }

    @Override
    protected int getShopSecurity() {
        return 10;
    }

    @Override
    public Sprite getForegroundSprite() {
        return ARMOR_SIGN;
    }
}
