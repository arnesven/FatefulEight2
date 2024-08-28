package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class TwinKukris extends WeaponPair {

    public TwinKukris() {
        super(new Kukri(), new Kukri());
    }

    @Override
    public Item copy() {
        return new TwinKukris();
    }

}
