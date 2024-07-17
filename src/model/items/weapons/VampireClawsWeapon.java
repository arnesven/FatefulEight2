package model.items.weapons;

import model.combat.conditions.ClawsVampireAbility;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class VampireClawsWeapon extends UnarmedCombatWeapon {
    private static final Sprite SPRITE = new ItemSprite(0, 14, MyColors.BEIGE, MyColors.RED, MyColors.BEIGE);

    @Override
    public String getName() {
        return "Claws";
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int[] getDamageTable() {
        return ClawsVampireAbility.getDamageTable();
    }

    @Override
    public int getNumberOfAttacks() {
        return 2;
    }

    @Override
    public boolean allowsCriticalHits() {
        return true;
    }
}
