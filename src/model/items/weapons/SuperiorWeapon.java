package model.items.weapons;

import model.items.Item;
import view.sprites.AvatarItemSprite;
import view.sprites.Sprite;
import view.sprites.SuperiorItemSprite;

public class SuperiorWeapon extends Weapon {
    private static final int COST_MULTIPLIER = 3;
    private static final int EXTRA_HIT_NUMBER = 8;
    private final Weapon inner;

    public SuperiorWeapon(Weapon inner) {
        super("Superior " + inner.getName(), inner.getCost()*COST_MULTIPLIER, inner.getSkill(), makeDamageTable(inner.getDamageTable()));
        this.inner = inner;
    }

    private static int[] makeDamageTable(int[] damageTable) {
        int[] result = new int[damageTable.length+1];
        int j = 0;
        boolean alreadyInserted = false;
        for (int k : damageTable) {
            if (k > EXTRA_HIT_NUMBER && !alreadyInserted) {
                result[j] = EXTRA_HIT_NUMBER;
                j++;
                alreadyInserted = true;
            }
            result[j] = k;
            j++;
        }
        if (!alreadyInserted) {
            result[result.length-1] = EXTRA_HIT_NUMBER;
        }
        return result;
    }

    @Override
    protected Sprite getSprite() {
        return new SuperiorItemSprite(inner.getSpriteForHigherTier());
    }

    @Override
    public Item copy() {
        return new SuperiorWeapon((Weapon) inner.copy());
    }

    @Override
    public String getSound() {
        return inner.getSound();
    }

    @Override
    protected AvatarItemSprite getOnAvatarSprite(int index) {
        return inner.getOnAvatarSprite(index);
    }
}
