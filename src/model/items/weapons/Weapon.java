package model.items.weapons;

import model.combat.Combatant;
import model.items.Inventory;
import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.items.EquipableItem;
import model.items.Item;
import model.items.imbuements.WeaponImbuement;
import model.states.CombatEvent;
import util.MyStrings;
import view.*;
import view.party.SelectableListMenu;
import view.sprites.*;

public abstract class Weapon extends EquipableItem {

    private final Skill skill;
    private final int[] damageTable;
    private WeaponImbuement imbuement = null;

    public Weapon(String name, int cost, Skill skill, int[] damageTable) {
        super(name, cost);
        this.skill = skill;
        this.damageTable = damageTable;
    }

    @Override
    public String getName() {
        if (isImbued()) {
            return "Imbued " + super.getName();
        }
        return super.getName();
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
        if (isImbued()) {
            return getImbuement().makeDamageTable(damageTable);
        }
        return damageTable;
    }

    protected WeaponImbuement getImbuement() {
        return imbuement;
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
        result.append(", ").append(getSkill().getName().replace(" Weapons", "")).append(" [").append(getDamageTableAsString()).append("]");
        if (getSpeedModifier() != 0) {
            result.append(", Speed ").append(MyStrings.withPlus(getSpeedModifier()));
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
        if (isImbued()) {
            return getImbuement().getText();
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

    public void setImbuement(WeaponImbuement imbuement) {
        this.imbuement = imbuement;
    }

    public boolean isImbued() {
        return this.imbuement != null;
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
        getImbuement().didOneAttackWith(model, combatEvent, gameCharacter, target, damage, critical);
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
        if (isImbued()) {
            getImbuement().drawYourself(screenHandler, col + 3, row + 3);
        }
    }

    public void removeImbuement() {
        this.imbuement = null;
    }

    public int getAttackBonus() {
        if (isImbued()) {
            return getImbuement().getAttackBonus();
        }
        return 0;
    }
}
