package model.items.weapons;

import model.Inventory;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.items.EquipableItem;
import model.items.Item;
import util.MyPair;
import util.MyStrings;

public abstract class Weapon extends EquipableItem {

    private final Skill skill;
    private final int[] damageTable;

    public Weapon(String name, int cost, Skill skill, int[] damageTable) {
        super(name, cost);
        this.skill = skill;
        this.damageTable = damageTable;
    }

    public int getDamage(int modifiedRoll, GameCharacter damager) {
        int dmg = 0;
        for (int i = 0; i < damageTable.length; ++i) {
            if (modifiedRoll < damageTable[i]) {
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
        for (int i = 0; i < damageTable.length; ++i) {
            bldr.append(damageTable[i]+"");
            bldr.append("/");
        }
        return bldr.substring(0, bldr.length()-1);
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
}
