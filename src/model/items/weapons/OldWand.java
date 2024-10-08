package model.items.weapons;

import model.classes.Skill;
import model.items.Item;
import model.items.Prevalence;
import model.items.StartingItem;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class OldWand extends WandWeapon implements StartingItem, PairableWeapon {
    private static final Sprite SPRITE = new ItemSprite(0, 6);

    public OldWand() {
        super("Old Wand", 5, Skill.MagicAny, new int[]{9,11});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new OldWand();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.common;
    }

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(8, 15);
    }
}
