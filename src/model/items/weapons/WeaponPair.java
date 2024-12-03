package model.items.weapons;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.Combatant;
import model.combat.conditions.Condition;
import model.items.Item;
import model.items.Prevalence;
import model.states.CombatEvent;
import util.MyPair;
import view.GameView;
import view.MyColors;
import view.YesNoMessageView;
import view.party.SelectableListMenu;
import view.sprites.AvatarItemSprite;
import view.sprites.RunOnceAnimationSprite;
import view.sprites.Sprite;
import view.sprites.WeaponPairSprite;

import java.util.ArrayList;
import java.util.List;

public class WeaponPair extends Weapon {
    private static final AvatarItemSprite[] TWIN_BLADES =  makeShiftedSpriteSet(
            new AvatarItemSprite(0x40, MyColors.GOLD, MyColors.GRAY, MyColors.BROWN, MyColors.BEIGE));
    private static final AvatarItemSprite[] OTHER_WEAPONS =  makeShiftedSpriteSet(
            new AvatarItemSprite(0x54, MyColors.BLACK, MyColors.GRAY, MyColors.BROWN, MyColors.BEIGE));

    private final Weapon mainHand;
    private final Weapon offHand;
    private final WeaponPairSprite sprite;
    private int attackCounter = 0;

    public WeaponPair(Weapon weapon1, Weapon weapon2) {
        super(makeName(weapon1, weapon2), weapon1.getCost() + weapon2.getCost(),
                weapon1.getSkill(), makeDamageTable(weapon1, weapon2));
        this.mainHand = weapon1;
        this.offHand = weapon2;
        this.sprite = new WeaponPairSprite(getName(), (PairableWeapon) weapon1, (PairableWeapon) weapon2);
    }

    private static int[] makeDamageTable(Weapon weapon1, Weapon weapon2) {
        int[] table1 = weapon1.getDamageTable();
        int[] table2 = weapon2.getDamageTable();
        int[] result = new int[Math.min(table1.length, table2.length)];
        for (int i = result.length-1; i >= 0; --i) {
            result[i] = (int)Math.floor((table1[i] + table2[i]) / 2.0);
        }
        return result;
    }

    @Override
    public Skill getSkillToUse(GameCharacter gc) {
        if (attackCounter % 2 == 0) {
            return mainHand.getSkillToUse(gc);
        }
        return offHand.getSkillToUse(gc);
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public boolean isCraftable() {
        return false;
    }

    @Override
    public boolean isSellable() {
        return true;
    }

    @Override
    public boolean isRangedAttack() {
        return mainHand.isRangedAttack() && offHand.isRangedAttack();
    }

    @Override
    public boolean isTwoHanded() {
        return true;
    }

    @Override
    public int getWeight() {
        return mainHand.getWeight() + offHand.getWeight();
    }

    @Override
    public Item copy() {
        throw new IllegalStateException("Should not be called!");
    }

    @Override
    public String getSound() {
        if (attackCounter % 2 == 0) {
            return mainHand.getSound();
        }
        return offHand.getSound();
    }

    @Override
    protected AvatarItemSprite getOnAvatarSprite(int index) {
        if (mainHand.isOfType(BladedWeapon.class) && offHand.isOfType(BladedWeapon.class)) {
            return TWIN_BLADES[index];
        }
        return OTHER_WEAPONS[index];
    }

    private static String makeName(Weapon w1, Weapon w2) {
        if (w1.getName().equals(w2.getName())) {
            return "Twin " + w1.getName() + "s";
        }
        return w1.getName() + "/" + w2.getName();
    }

    @Override
    public boolean allowsCriticalHits() {
        return mainHand.allowsCriticalHits() && offHand.allowsCriticalHits();
    }

    @Override
    public int getNumberOfAttacks() {
        return 2;
    }

    @Override
    public boolean supportsHigherTier() {
        return false;
    }

    @Override
    public int getCriticalTarget() {
        return Math.max(mainHand.getCriticalTarget(),
                offHand.getCriticalTarget());
    }

    @Override
    public RunOnceAnimationSprite getEffectSprite() {
        return mainHand.getEffectSprite();
    }

    @Override
    public <E> boolean isOfType(Class<E> weaponClass) {
        return weaponClass.isAssignableFrom(mainHand.getClass()) ||
                weaponClass.isAssignableFrom(offHand.getClass());
    }

    @Override
    public void didOneAttackWith(Model model, CombatEvent combatEvent, GameCharacter gameCharacter, Combatant target, int damage, int critical) {
        if (attackCounter % 2 == 0) {
            mainHand.didOneAttackWith(model, combatEvent, gameCharacter, target, damage, critical);
        } else {
            offHand.didOneAttackWith(model, combatEvent, gameCharacter, target, damage, critical);
        }
        attackCounter++;
    }

    @Override
    public String getAttackSound() {
        if (attackCounter % 2 == 0) {
            return mainHand.getAttackSound();
        }
        return offHand.getAttackSound();
    }

    @Override
    public boolean isPhysicalDamage() {
        if (attackCounter % 2 == 0) {
            return mainHand.isPhysicalDamage();
        }
        return offHand.isPhysicalDamage();
    }

    @Override
    public String getExtraText() {
        if (super.getExtraText().equals("")) {
            return "(Paired Weapons)";
        }
        return super.getExtraText() + ", (Paired Weapons)";
    }


    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        List<MyPair<Skill, Integer>> bonuses = new ArrayList<>(mainHand.getSkillBonuses());
        bonuses.addAll(offHand.getSkillBonuses());
        int penalty;
        if (mainHand.getClass() == offHand.getClass() ||
                mainHand.isOfType(offHand.getClass()) ||
                offHand.isOfType(mainHand.getClass())) {
            penalty = -2;
        } else if (offHand.isOfType(mainHand.getClass().getSuperclass()) ||
                    mainHand.isOfType(offHand.getClass().getSuperclass())) {
            penalty = -3;
        } else {
            penalty = -4;
        }
        if (offHand.isOfType(SmallBladedWeapon.class)) {
            penalty += 1;
        }
        if (mainHand.getSkill() != Skill.MagicAny) {
            if (Skill.isMagicSkill(mainHand.getSkill())) {
                bonuses.add(new MyPair<>(mainHand.getSkill(), penalty+1));
            } else {
                bonuses.add(new MyPair<>(mainHand.getSkill(), penalty));
            }
        }
        if (mainHand.getSkill() != offHand.getSkill() && offHand.getSkill() != Skill.MagicAny) {
            if (Skill.isMagicSkill(offHand.getSkill())) {
                bonuses.add(new MyPair<>(offHand.getSkill(), penalty+1));
            } else {
                bonuses.add(new MyPair<>(offHand.getSkill(), penalty));
            }
        }
        return bonuses;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public int getSpeedModifier() {
        return Math.max(mainHand.getSpeedModifier(), offHand.getSpeedModifier());
    }

    @Override
    public boolean grantsConditionImmunity(Condition cond) {
        return mainHand.grantsConditionImmunity(cond) ||
                offHand.grantsConditionImmunity(cond);
    }

    @Override
    public boolean hasDualUseInMenu() {
        return true;
    }

    @Override
    public String getDualUseLabel() {
        return "Split";
    }



    @Override
    public SelectableListMenu getDualUseMenu(GameView innerView, int x, int y) {
        return new YesNoMessageView(innerView, "Split this weapon into the two underlying weapons?") {
            @Override
            protected void doAction(Model model) {
                splitToInventory(model);
            }
        };
    }

    public void splitToInventory(Model model) {
        model.getParty().getInventory().remove(this);
        mainHand.addYourself(model.getParty().getInventory());
        offHand.addYourself(model.getParty().getInventory());
    }
}
