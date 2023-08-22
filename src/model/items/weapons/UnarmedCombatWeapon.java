package model.items.weapons;

import model.characters.GameCharacter;
import model.classes.Skill;
import model.items.Item;
import view.sprites.Sprite;
import view.sprites.AvatarItemSprite;

public class UnarmedCombatWeapon extends NaturalWeapon {
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
    public Item copy() {
        throw new IllegalStateException("UnarmedCombatWeapon should never be copied!");
    }

    @Override
    public boolean allowsCriticalHits() {
        return false;
    }

    @Override
    protected AvatarItemSprite getOnAvatarSprite(int index) {
        return null;
    }

    @Override
    public String getSound() {
        return "";
    }
}
