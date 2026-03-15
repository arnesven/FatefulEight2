package model.items.weapons;

import view.MyColors;
import view.sprites.AvatarItemSprite;

public abstract class Pickaxe extends AxeWeapon {

    public Pickaxe(String name, int cost, int[] damage) {
        super(name, cost, damage, false);
    }
}
