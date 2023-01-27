package model.items.weapons;

import model.characters.GameCharacter;
import model.classes.Skill;
import model.items.Item;
import model.items.weapons.Weapon;
import view.sprites.Sprite;

public class UnarmedCombatWeapon extends Weapon {
    public UnarmedCombatWeapon() {
        super("Unarmed", 0, Skill.UnarmedCombat, new int[]{7,10});
    }

    @Override
    public int getDamage(int modifiedRoll, GameCharacter damager) {
        if (damager.getEquipment().getAccessory() != null &&
                damager.getEquipment().getAccessory().getDamageTable() != null) {
            int[] damageTable = damager.getEquipment().getAccessory().getDamageTable();
            int dmg = 0;
            for (int i = 0; i < damageTable.length; ++i) {
                if (modifiedRoll < damageTable[i]) {
                    break;
                }
                dmg++;
            }
            return dmg;
        }
        return super.getDamage(modifiedRoll, damager);
    }

    @Override
    protected Sprite getSprite() {
        return EMPTY_ITEM_SPRITE;
    }

    @Override
    public Item copy() {
        throw new IllegalStateException("UnarmedCombatWeapon should never be copied!");
    }

    @Override
    public boolean allowsCriticalHits() {
        return false;
    }

    @Override
    public String getSound() {
        return "";
    }
}
