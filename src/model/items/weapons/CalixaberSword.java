package model.items.weapons;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.combat.conditions.BleedingCondition;
import model.combat.conditions.TimedParalysisCondition;
import model.enemies.HumanoidEnemy;
import model.items.Prevalence;
import model.states.CombatEvent;
import util.MyRandom;
import view.MyColors;
import view.sprites.DamageValueEffect;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class CalixaberSword extends TwoHandedSword {

    private static final Sprite SPRITE = new TwoHandedItemSprite(14, 14, MyColors.DARK_RED, MyColors.WHITE, MyColors.GOLD);

    @Override
    public String getName() {
        return "Calixaber";
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.unique;
    }

    @Override
    public boolean isCraftable() {
        return false;
    }

    @Override
    public void didOneAttackWith(Model model, CombatEvent combatEvent, GameCharacter gameCharacter, Combatant target, int damage, int critical) {
        if (!target.isDead() && target instanceof HumanoidEnemy && MyRandom.rollD10() <= 3) {
            combatEvent.println(gameCharacter.getName() + " stunned " + target.getName() + " with Calixaber!");
            target.addCondition(new TimedParalysisCondition(1));
        }
    }

    @Override
    public String getExtraText() {
        return ", 30% chance of stunning humanoid opponents for 1 turn.";
    }

}
