package model.items.weapons;

import model.characters.GameCharacter;
import model.classes.Skill;
import model.items.HigherTierItem;
import model.items.Item;
import model.items.Prevalence;
import model.items.imbuements.WeaponImbuement;
import util.MyPair;
import view.sprites.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HigherTierWeapon extends Weapon implements HigherTierItem, PairableWeapon {
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

    @Override
    public int getSpeedModifier() {
        return inner.getSpeedModifier();
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return inner.getSkillBonuses();
    }

    @Override
    public Prevalence getPrevalence() {
        return inner.getPrevalence();
    }

    @Override
    public RunOnceAnimationSprite getEffectSprite() {
        return inner.getEffectSprite();
    }

    @Override
    public LoopingSprite getOnAvatarSprite(GameCharacter gameCharacter) {
        return inner.getOnAvatarSprite(gameCharacter);
    }

    @Override
    public int getNumberOfAttacks() {
        return inner.getNumberOfAttacks();
    }

    @Override
    public String getExtraText() {
        return inner.getExtraText();
    }

    @Override
    public int getCriticalTarget() {
        return inner.getCriticalTarget();
    }

    @Override
    public <E> boolean isOfType(Class<E> weaponClass) {
        return inner.isOfType(weaponClass);
    }

    @Override
    public int getWeight() {
        return inner.getWeight();
    }

    @Override
    public int getTier() {
        return tier;
    }

    @Override
    public Item getInnerItem() {
        return inner;
    }

    @Override
    public boolean pairingAllowed() {
        return inner instanceof PairableWeapon;
    }

    @Override
    public Sprite makePairSprite() {
        return ((PairableWeapon)inner).makePairSprite();
    }

    @Override
    public boolean isImbued() {
        return inner.isImbued();
    }

    @Override
    public void setImbuement(WeaponImbuement imbuement) {
        inner.setImbuement(imbuement);
    }

    @Override
    public void removeImbuement() {
        inner.removeImbuement();
    }

    @Override
    protected WeaponImbuement getImbuement() {
        return inner.getImbuement();
    }

    @Override
    public int getAttackBonus() {
        return inner.getAttackBonus();
    }

    @Override
    public boolean isTwoHanded() {
        return inner.isTwoHanded();
    }

    @Override
    public boolean isSellable() {
        return inner.isSellable();
    }

    @Override
    public boolean isCraftable() {
        return inner.isCraftable();
    }
}
