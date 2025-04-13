package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.conditions.BurningWeaponCondition;
import model.combat.Combatant;
import model.items.Item;
import model.items.imbuements.ExtraDamageImbuement;
import model.items.weapons.UnarmedCombatWeapon;
import model.items.weapons.Weapon;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.RedSpellSprite;
import view.sprites.Sprite;
import view.sprites.UpArrowAnimation;

public class BurningWeaponSpell extends CombatSpell {
    private static final Sprite SPRITE = new RedSpellSprite(4, true);

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
        if (!((GameCharacter)target).getEquipment().getWeapon().isImbued()) {
            target.addCondition(new BurningWeaponCondition());
            Weapon weapon = ((GameCharacter) target).getEquipment().getWeapon();
            weapon.setImbuement(new ExtraDamageImbuement());
            combat.addSpecialEffect(target, new UpArrowAnimation());
        } else {
            combat.println(getName() + " had no effect on " + target.getName() + "'s weapon!");
        }
    }

    @Override
    public String getDescription() {
        return "Increases the damage of a weapon during combat.";
    }

}
