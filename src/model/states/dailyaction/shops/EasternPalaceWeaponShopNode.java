package model.states.dailyaction.shops;

import model.Model;
import model.items.Item;
import model.items.ItemDeck;
import model.items.Prevalence;
import model.items.weapons.*;
import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.TownSubView;

import java.util.Collections;
import java.util.List;

public class EasternPalaceWeaponShopNode extends WeaponShopNode {
    private static final Sprite SPRITE = new Sprite32x32("shopping", "world_foreground.png", 0x22,
            MyColors.YELLOW, TownSubView.PATH_COLOR, MyColors.RED, MyColors.WHITE);

    private static final List<Weapon> EASTERN_WEAPONS = List.of(
            new Katana(), new DaiKatana(), new Wakizashi(), new Naginata(), new ThrowingStars(), new ThrowingKnives()
    );

    public EasternPalaceWeaponShopNode(Model model, int col, int row) {
        super(model, col, row);
    }

    @Override
    protected List<Item> makeInventory(Model model) {
        List<Item> inventory = super.makeInventory(model);
        int itemsToDraw = MyRandom.randInt(3, 5);
        for (int i = 0; i < itemsToDraw && !inventory.isEmpty(); ++i) {
            inventory.remove(MyRandom.sample(inventory));
        }
        inventory.addAll(model.getItemDeck().draw(EASTERN_WEAPONS, itemsToDraw, Prevalence.unspecified,
                model.getItemDeck().getStandardHigherTierChance()));
        Collections.sort(inventory);
        return inventory;
    }

    @Override
    public Sprite getBackgroundSprite() {
        return SPRITE;
    }
}
