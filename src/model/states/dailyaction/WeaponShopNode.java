package model.states.dailyaction;

import model.ItemDeck;
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

public class WeaponShopNode extends GeneralShopNode {

    private static final Sprite WEAPON_SIGN = new SignSprite("weaponsign", 0x16,
            MyColors.PURPLE, MyColors.WHITE);

    public WeaponShopNode(Model model, int column, int row) {
        super(model, column, row, "Weapon Shop");
    }

    @Override
    protected List<Item> makeInventory(Model model) {
        List<Item> inventory = new ArrayList<>();
        inventory.addAll(model.getItemDeck().draw(ItemDeck.allWeapons(), MyRandom.randInt(2,10), Prevalence.common));
        inventory.addAll(model.getItemDeck().draw(ItemDeck.allWeapons(), MyRandom.randInt(3,10), Prevalence.uncommon));
        inventory.addAll(model.getItemDeck().draw(ItemDeck.allWeapons(), MyRandom.randInt(1,5), Prevalence.rare));
        Collections.sort(inventory);
        return inventory;
    }

    @Override
    public Sprite getForegroundSprite() {
        return WEAPON_SIGN;
    }
}
