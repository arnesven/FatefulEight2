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
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class IvyRod extends WandWeapon {
    private static final Sprite SPRITE = new ItemSprite(14, 11,
            MyColors.PEACH, MyColors.DARK_GREEN, MyColors.GOLD);

    public IvyRod() {
        super("Ivy Rod", 36, Skill.MagicGreen, new int[]{8,10,13});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new IvyRod();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public String getExtraText() {
        return "20% Chance to apply Poison Gas";
    }

    @Override
    public void didOneAttackWith(Model model, CombatEvent combatEvent, GameCharacter gameCharacter, Combatant target, int damage, int critical) {
        PoisonGasSpell spell = new PoisonGasSpell();
        if (!target.isDead() && MyRandom.rollD10() > 8) {
            spell.addPoisonGasEffect(combatEvent, gameCharacter, target);
        }
    }
}
