package model.enemies;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.states.CombatEvent;
import sound.SoundEffects;
import util.MyRandom;
import view.MyColors;
import view.sprites.DamageValueEffect;
import view.sprites.ExplosionAnimation;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class BlackGelatinousBlobEnemy extends GelatinousBlobEnemy {
    private final LoopingSprite shakeSprite;
    private Sprite altSprite;

    public BlackGelatinousBlobEnemy(char a) {
        super(a, MyColors.DARK_GRAY, MyColors.DARK_BROWN);
        setCurrentHp(getMaxHP());
        this.shakeSprite = makeShakeSprite(MyColors.DARK_GRAY, MyColors.DARK_BROWN);
        setAttackBehavior(new DecomposingBlobAttackBehavior());
    }

    @Override
    protected Sprite getSprite() {
        if (this.altSprite != null) {
            return altSprite;
        }
        return super.getSprite();
    }

    private void shakeAnimation() {
        this.altSprite = shakeSprite;
        mySleep(2000);
        this.altSprite = null;
    }

    private void mySleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void takeCombatDamage(CombatEvent combatEvent, int damage, GameCharacter damager) {
        super.takeCombatDamage(combatEvent, damage, damager);
        if (this.isDead() && damager != null) {
            shakeAnimation();
            combatEvent.println(getName() + " erupts in a violent explosion!");
            combatEvent.addSpecialEffect(this, new ExplosionAnimation());
            SoundEffects.playBoom();
            for (Combatant comb : combatEvent.getAllCombatants()) {
                if (comb instanceof GameCharacter) {
                    int exploDamage = MyRandom.randInt(13, 17);
                    comb.takeCombatDamage(combatEvent, exploDamage, null);
                    combatEvent.addFloatyDamage(comb, exploDamage, DamageValueEffect.STANDARD_DAMAGE);
                }
            }
        }
    }

    @Override
    public String getDeathSound() {
        return null;
    }

    @Override
    public GelatinousBlobEnemy copy() {
        return new BlackGelatinousBlobEnemy(getEnemyGroup());
    }

    private class DecomposingBlobAttackBehavior extends BlobAttackBehavior {
        public DecomposingBlobAttackBehavior() {
            super(6);
        }

        @Override
        public void performAttack(Model model, Enemy enemy, GameCharacter target, CombatEvent combatEvent) {
            if (MyRandom.rollD6() == 1) {
                shakeAnimation();
                combatEvent.println(getName() + " seems to be decomposing.");
                combatEvent.doDamageToEnemy(enemy, 1, null);
                combatEvent.addFloatyDamage(enemy, 1, DamageValueEffect.STANDARD_DAMAGE);
            } else {
                super.performAttack(model, enemy, target, combatEvent);
            }
        }
    }
}
