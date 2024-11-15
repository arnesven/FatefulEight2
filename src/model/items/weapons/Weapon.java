package model.items.weapons;

import model.combat.Combatant;
import model.items.Inventory;
import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.items.EquipableItem;
import model.items.Item;
import model.states.CombatEvent;
import util.MyStrings;
import view.*;
import view.party.SelectableListMenu;
import view.sprites.*;

public abstract class Weapon extends EquipableItem {

    private static final Sprite BURNING_SPRITE = CharSprite.make(0xDD, MyColors.LIGHT_GRAY, MyColors.RED, MyColors.BEIGE);
    private final Skill skill;
    private final int[] damageTable;
    private boolean isBurning;

    public Weapon(String name, int cost, Skill skill, int[] damageTable) {
        super(name, cost);
        this.skill = skill;
        this.damageTable = damageTable;
    }

    @Override
    public boolean isCraftable() {
        return true;
    }

    @Override
    public boolean isSellable() {
        return true;
    }

    public int getDamage(int modifiedRoll, GameCharacter damager) {
        int dmg = 0;
        int[] tableToUse = getDamageTable();
        for (int i = 0; i < tableToUse.length; ++i) {
            if (modifiedRoll < tableToUse[i]) {
                break;
            }
            dmg++;
        }
        return dmg;
    }

    public Skill getSkill() {
        return skill;
    }

    public String getDamageTableAsString() {
        StringBuilder bldr = new StringBuilder();
        int[] tableToUse = getDamageTable();
        for (int i = 0; i < tableToUse.length; ++i) {
            bldr.append(tableToUse[i]+"");
            bldr.append("/");
        }
        return bldr.substring(0, bldr.length()-1);
    }

    public int[] getDamageTable() {
        if (!isBurning) {
            return damageTable;
        }
        int[] burningTable = new int[damageTable.length+1];
        burningTable[0] = damageTable[0];
        for (int i = 0; i < damageTable.length; ++i) {
            burningTable[i+1] = damageTable[i];
        }
        return burningTable;
    }

    public boolean isRangedAttack() {
        return false;
    }

    public boolean isTwoHanded() {return false; }

    @Override
    public void addYourself(Inventory inventory) {
        inventory.add(this);
    }

    @Override
    public final String getShoppingDetails() {
        StringBuilder result = new StringBuilder();
        result.append(", " + getSkill().getName().replace(" Weapons", "") + " [" + getDamageTableAsString() + "]");
        if (getSpeedModifier() != 0) {
            result.append(", Speed " + MyStrings.withPlus(getSpeedModifier()));
        }
        if (isTwoHanded()) {
            result.append(", Two-handed");
        }
        if (!isPhysicalDamage()) {
            result.append(", Magic Damage");
        }
        result.append(getSkillBonusesAsString());
        if (!getExtraText().equals("")) {
            result.append(", " + getExtraText());
        }
        return result.toString();
    }

    public boolean allowsCriticalHits() {
        return true;
    }

    public String getExtraText() {
        if (getCriticalTarget() != 10) {
            int chance = (11 - getCriticalTarget()) * 10;
            return chance + "% Critical Hit Chance";
        }
        if (isBurning) {
            return "Enchanted";
        }
        return "";
    }

    public int getNumberOfAttacks() { return 1; }

    public Skill getSkillToUse(GameCharacter gc) {
        return getSkill();
    }

    @Override
    public void equipYourself(GameCharacter gc) {
        gc.equipWeaponFromInventory(this);
    }

    public void setBurning(boolean b) {
        isBurning = b;
    }

    public LoopingSprite getOnAvatarSprite(GameCharacter gameCharacter) {
        return getOnAvatarSprite(gameCharacter.getCharClass().getWeaponShift(gameCharacter) + 1);
    }

    protected abstract AvatarItemSprite getOnAvatarSprite(int index);

    @Override
    public boolean isAnalyzable() {
        return true;
    }

    @Override
    public AnalyzeDialog getAnalysisDialog(Model model) {
        return new AnalyzeWeaponDialog(model, this);
    }

    @Override
    public String getAnalysisType() {
        return "Damage Analysis";
    }

    @Override
    public boolean supportsHigherTier() {
        return true;
    }

    @Override
    public Item makeHigherTierCopy(int tier) {
        return new HigherTierWeapon((Weapon)copy(), tier);
    }

    public int getCriticalTarget() {
        return 10;
    }

    public RunOnceAnimationSprite getEffectSprite() {
        return new StrikeEffectSprite();
    }

    public <E> boolean isOfType(Class<E> weaponClass) {
        return weaponClass.isAssignableFrom(this.getClass());
    }

    public void didOneAttackWith(Model model, CombatEvent combatEvent, GameCharacter gameCharacter,
                                 Combatant target, int damage, int critical) {

    }

    public String getAttackSound() {
        return "default_attack";
    }

    public boolean isPhysicalDamage() { return true; }

    @Override
    public boolean hasDualUseInMenu() {
        return this instanceof PairableWeapon && ((PairableWeapon) this).pairingAllowed();
    }

    @Override
    public String getDualUseLabel() {
        return "Pair with";
    }

    @Override
    public SelectableListMenu getDualUseMenu(GameView innerView, int x, int y) {
        return new WeaponPairingDialog(innerView, this);
    }

    @Override
    public void drawYourself(ScreenHandler screenHandler, int col, int row) {
        super.drawYourself(screenHandler, col, row);
        if (isBurning) {
            screenHandler.put(col+3, row+3, BURNING_SPRITE);
        }
    }
}
