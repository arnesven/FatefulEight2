package model.items.weapons;

import model.items.Item;
import view.sprites.AvatarItemSprite;
import view.sprites.Sprite;
import view.sprites.HigherTierItemSprite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HigherTierWeapon extends Weapon {
    private final Weapon inner;
    private final int tier;
    private Sprite sprite;

    public HigherTierWeapon(Weapon inner, int tier) {
        super(Item.getHigherTierPrefix(tier) + " " + inner.getName(),
                inner.getCost()*(tier*2+1), inner.getSkill(), makeDamageTable(inner.getDamageTable(), tier));
        this.inner = inner;
        sprite = new HigherTierItemSprite(inner.getSpriteForHigherTier(tier), tier);
        this.tier = tier;
    }

    private static int[] makeDamageTable(int[] damageTable, int tier) {
        List<Integer> list = new ArrayList<>();
        for (int i : damageTable) {
            list.add(i);
        }
        switch (tier) {
            case 3:
                list.add(8);
            case 1:
                list.add(8);
                break;
            default:
                list.add(7);
            case 2:
                list.add(6);
                list.add(9);
        }
        Collections.sort(list);
        int[] result = new int[list.size()];
        for (int i = 0; i < list.size(); ++i) {
            result[i] = list.get(i);
        }
        return result;
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public Item copy() {
        return new HigherTierWeapon((Weapon) inner.copy(), tier);
    }

    @Override
    public String getSound() {
        return inner.getSound();
    }

    @Override
    protected AvatarItemSprite getOnAvatarSprite(int index) {
        return inner.getOnAvatarSprite(index);
    }

    @Override
    public boolean isRangedAttack() {
        return inner.isRangedAttack();
    }
}
