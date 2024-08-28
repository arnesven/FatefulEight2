package model.items.weapons;

import model.classes.Skill;
import model.items.Item;
import model.items.Prevalence;
import util.MyPair;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

import java.util.List;

public class TwinHatchets extends WeaponPair {

    private static final Sprite SPRITE = new TwoHandedItemSprite(7, 11);

    public TwinHatchets() {
        super(new Hatchet(), new Hatchet());
    }

    @Override
    public Item copy() {
        return new TwinHatchets();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.uncommon;
    }
}
