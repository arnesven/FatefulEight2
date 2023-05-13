package model.combat;

import model.characters.GameCharacter;
import model.items.weapons.Weapon;
import view.MyColors;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class BurningWeaponCondition extends Condition {
    public static final Sprite CONDITION_SPRITE = CharSprite.make((char) (0xD2), MyColors.LIGHT_BLUE, MyColors.BLACK, MyColors.CYAN);

    public BurningWeaponCondition() {
        super("Burning Weapon", "BrW");
    }

    @Override
    protected boolean noCombatTurn() {
        return false;
    }

    @Override
    public boolean removeAtEndOfCombat() {
        return true;
    }

    @Override
    public Sprite getSymbol() {
        return CONDITION_SPRITE;
    }

    @Override
    public void wasRemoved(Combatant combatant) {
        Weapon weapon = ((GameCharacter) combatant).getEquipment().getWeapon();
        weapon.setBurning(false);
    }
}
