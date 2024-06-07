package model.items.weapons;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.conditions.BurningCondition;
import model.combat.Combatant;
import model.items.Item;
import model.items.Prevalence;
import model.states.CombatEvent;
import util.MyRandom;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class FireRod extends WandWeapon {
    private static final Sprite SPRITE = new ItemSprite(13, 11,
            MyColors.YELLOW, MyColors.ORANGE, MyColors.PEACH);

    public FireRod() {
        super("Fire Rod", 36, Skill.MagicRed, new int[]{9, 11, 12, 14});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new FireRod();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public String getExtraText() {
        return "20% Chance to apply Burn";
    }

    @Override
    public void didOneAttackWith(Model model, CombatEvent combatEvent, GameCharacter gameCharacter, Combatant target, int damage, int critical) {
        if (MyRandom.rollD10() > 8 && !target.hasCondition(BurningCondition.class)) {
            combatEvent.println(target.getName() + " starts burning!");
            target.addCondition(new BurningCondition(gameCharacter));
        }
    }
}
