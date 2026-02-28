package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.*;

public class Javelins extends OneHandedPolearmWeapon {
    private static final Sprite SPRITE = new ItemSprite(2, 4);

    public Javelins() {
        super("Javelins", 16, new int[]{10,10,10});
    }

    @Override
    public boolean isTwoHanded() {
        return true; // Javelins still extends OneHandedPolearmWeapon to get the OnAvatarSprite right.
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Javelins();
    }

    @Override
    public boolean isRangedAttack() {
        return true;
    }

    @Override
    public RunOnceAnimationSprite getEffectSprite() {
        return new RangedStrikeEffect();
    }

    @Override
    public int getWeight() {
        return 1000;
    }
}
