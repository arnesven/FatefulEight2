package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.combat.Condition;
import model.items.Item;
import model.items.weapons.UnarmedCombatWeapon;
import model.items.weapons.Weapon;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.CharSprite;
import view.sprites.CombatSpellSprite;
import view.sprites.Sprite;

public class BurningWeaponSpell extends CombatSpell {
    private static final Sprite SPRITE = new CombatSpellSprite(10, 8, MyColors.BROWN, MyColors.DARK_RED, MyColors.WHITE);
    private static final Sprite CONDITION_SPRITE = CharSprite.make((char) (0xD2), MyColors.LIGHT_BLUE, MyColors.BLACK, MyColors.CYAN);

    public BurningWeaponSpell() {
        super("Burning Weapon", 20, MyColors.RED, 9, 2);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new BurningWeaponSpell();
    }

    @Override
    public boolean canBeCastOn(Model model, Combatant target) {
        if (! (target instanceof GameCharacter)) {
            return false;
        }
        Weapon weapon = ((GameCharacter) target).getEquipment().getWeapon();
        if (weapon instanceof UnarmedCombatWeapon) {
            return false;
        }
        return !target.hasCondition(BurningWeaponCondition.class);
    }

    @Override
    public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        target.addCondition(new BurningWeaponCondition());
        Weapon weapon = ((GameCharacter) target).getEquipment().getWeapon();
        weapon.setBurning(true);
    }

    @Override
    public String getDescription() {
        return "Increases the damage of a weapon during combat.";
    }

    private static class BurningWeaponCondition extends Condition {
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
}
