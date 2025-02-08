package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.enemies.Enemy;
import model.items.Item;
import model.items.Prevalence;
import model.states.CombatEvent;
import sound.SoundEffects;
import util.MyRandom;
import view.MyColors;
import view.sprites.*;

import java.util.ArrayList;
import java.util.List;

public class ChainLightningSpell extends CombatSpell {

    public static final String SPELL_NAME = "Chain Lightning";
    private static final Sprite SPRITE = new CombatSpellSprite(9, 8,
            MyColors.BROWN, MyColors.DARK_RED, MyColors.WHITE);

    public ChainLightningSpell() {
        super(SPELL_NAME, 55, MyColors.RED, 11, 3, false);
    }

    public static String getMagicExpertTips() {
        return "The more you practice Chain Lightning, the more enemies you'll be able to hit.";
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public Item copy() {
        return new ChainLightningSpell();
    }

    @Override
    public boolean canBeCastOn(Model model, Combatant target) {
        return target instanceof Enemy;
    }

    @Override
    public boolean masteriesEnabled() {
        return true;
    }

    @Override
    public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        List<Enemy> targets = new ArrayList<>(combat.getEnemies());
        targets.remove(target);

        int mastery = getMasteryLevel(performer);

        int damage = mastery + 2;
        SoundEffects.playSound("thunder");
        combat.println(target.getName() + " was hit by lightning, took " + damage + " damage.");
        ChainLightningEffect effect = new ChainLightningEffect();
        combat.addSpecialEffectsBetween(performer, target, effect);

        List<Combatant> victims = new ArrayList<>();
        victims.add(target);

        int waves = mastery + 1;
        int split = 0;
        for (int i = 0; i < waves && !targets.isEmpty(); ++i) {
            combat.waitUntil(effect, RunOnceAnimationSprite::isDone);
            Combatant oldTarget = target;
            List<Combatant> newTargets = new ArrayList<>();
            int numberOfNewTargets = MyRandom.randInt(1, 2 + mastery);
            for (int j = 0; j < numberOfNewTargets && !targets.isEmpty(); ++j) {
                target = MyRandom.sample(targets);
                newTargets.add(target);
                targets.remove(target);
                victims.add(target);
            }
            SoundEffects.playSound("thunder");
            for (Combatant newTarget : newTargets) {
                effect = new ChainLightningEffect();
                combat.addSpecialEffectsBetween(oldTarget, newTarget, effect);
                split++;
            }
        }
        if (split > 0) {
            combat.println("The lightning chained " + split + " times, dealing " + damage + " damage to each target.");
        }
        model.getLog().waitForAnimationToFinish();
        for (Combatant c : victims){
            combat.addFloatyDamage(c, damage, DamageValueEffect.MAGICAL_DAMAGE);
            combat.doDamageToEnemy(c, damage, performer);
        }
    }

    @Override
    public String getDescription() {
        return "A deadly discharge that can fork several times and hit multiple enemies.";
    }

    private static class ChainLightningEffect extends RunOnceAnimationSprite {
        public ChainLightningEffect() {
            super("chainlightning", "combat.png", 12, 9, 32, 32, 1, MyColors.WHITE);
            setColor2(MyColors.LIGHT_YELLOW);
            setAnimationDelay(30);
        }
    }
}
