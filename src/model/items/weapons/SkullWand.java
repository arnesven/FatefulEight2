package model.items.weapons;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.Combatant;
import model.items.Item;
import model.items.Prevalence;
import model.items.spells.PoisonGasSpell;
import model.states.CombatEvent;
import util.MyRandom;
import view.MyColors;
import view.sprites.DamageValueEffect;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class SkullWand extends WandWeapon {
    private static final Sprite SPRITE = new ItemSprite(6, 6, MyColors.BROWN, MyColors.BEIGE);

    public SkullWand() {
        super("Skull Wand", 28, Skill.MagicBlack, new int[]{8,11,13,14});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new SkullWand();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public String getExtraText() {
        return "20% Chance to absorb damage as HP";
    }

    @Override
    public void didOneAttackWith(Model model, CombatEvent combatEvent, GameCharacter gameCharacter, Combatant target, int damage, int critical) {
        if (MyRandom.rollD10() > 8) {
            combatEvent.println("Absorbed " + damage + " health points from " + target.getName() + ".");
            gameCharacter.addToHP(damage);
            combatEvent.addFloatyDamage(target, damage, DamageValueEffect.HEALING);
        }
    }
}
