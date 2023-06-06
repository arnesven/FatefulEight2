package model.states.dailyaction;

import model.ItemDeck;
import model.Model;
import model.items.Item;
import model.items.Prevalence;
import model.items.weapons.MagesStaff;
import model.items.weapons.OldStaff;
import util.MyRandom;
import view.MyColors;
import view.sprites.SignSprite;
import view.sprites.Sprite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MagicShopNode extends GeneralShopNode {

    private static final Sprite MAGIC_SIGN = new SignSprite("magicsign", 0x27,
            MyColors.RED, MyColors.WHITE);

    public MagicShopNode(Model model, int col, int row) {
        super(model, col, row, "Magic Shop");
    }

    @Override
    public Sprite getForegroundSprite() {
        return MAGIC_SIGN;
    }

    @Override
    protected List<Item> makeInventory(Model model) {
        List<Item> inventory = new ArrayList<>();
        inventory.addAll(model.getItemDeck().draw(ItemDeck.allWands(), MyRandom.randInt(2,4), Prevalence.unspecified));
        inventory.addAll(model.getItemDeck().draw(ItemDeck.allSpells(), MyRandom.randInt(4, 7), Prevalence.unspecified));
        inventory.addAll(model.getItemDeck().draw(ItemDeck.allPotions(), MyRandom.randInt(1, 6), Prevalence.unspecified));
        inventory.addAll(model.getItemDeck().draw(ItemDeck.allScrolls(), MyRandom.randInt(4), Prevalence.unspecified));
        inventory.addAll(model.getItemDeck().draw(ItemDeck.allPotionRecipes(), MyRandom.randInt(2), Prevalence.unspecified));

        for (int i = 0; i < 4; ++i) {
            if (MyRandom.randInt(2) == 0) {
                if (i % 2 == 0) {
                    inventory.add(new MagesStaff());
                } else {
                    inventory.add(new OldStaff());
                }
            }
        }
        Collections.sort(inventory);
        return inventory;
    }
}
