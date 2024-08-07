package model.items.weapons;

import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.conditions.ClawsVampireAbility;
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
            return useOtherDamageTable(damager.getEquipment().getAccessory().getDamageTable(), modifiedRoll);
        }
        return super.getDamage(modifiedRoll, damager);
    }

    protected static int useOtherDamageTable(int[] damageTable, int modifiedRoll) {
        int dmg = 0;
        for (int i = 0; i < damageTable.length; ++i) {
            if (modifiedRoll < damageTable[i]) {
                break;
            }
            dmg++;
        }
        return dmg;
    }

    @Override
    public int getWeight() {
        return 0;
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

    @Override
    public String getExtraText() {
        return "Unarmed Combat, " + getDamageTableAsString();
    }
}
