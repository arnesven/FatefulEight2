package model.items.weapons;

import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class WerewolfClawsWeapon extends UnarmedCombatWeapon {
    private static final Sprite SPRITE = new ItemSprite(0, 14, MyColors.BEIGE, MyColors.TAN, MyColors.TAN);
    private static final int[] DAMAGE_TABLE = new int[]{6, 7, 10};

    @Override
    public String getName() {
        return "Claws";
    }

    @Override
    public int[] getDamageTable() {
        return DAMAGE_TABLE;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public boolean allowsCriticalHits() {
        return true;
    }
}
