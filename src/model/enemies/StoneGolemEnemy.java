package model.enemies;

import model.Model;
import model.characters.GameCharacter;
import model.combat.conditions.Condition;
import model.combat.loot.CombatLoot;
import model.combat.loot.NoCombatLoot;
import model.enemies.behaviors.EnemyAttackBehavior;
import model.enemies.behaviors.MixedAttackBehavior;
import model.enemies.behaviors.MultiKnockDownBehavior;
import model.enemies.behaviors.RangedAttackBehavior;
import model.states.CombatEvent;
import util.MyRandom;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;


public class StoneGolemEnemy extends Enemy {
    private static final Sprite SPRITE = new StoneGolemSprite();

    public StoneGolemEnemy(char a) {
        super(a, "Stone Golem");
        setAttackBehavior(makeRandomAttackBehavior());
    }

    private EnemyAttackBehavior makeRandomAttackBehavior() {
        if (MyRandom.flipCoin()) {
            return new MultiKnockDownBehavior(2, 3);
        }
        return new MixedAttackBehavior();
    }

    @Override
    public void takeCombatDamage(CombatEvent combatEvent, int damage, GameCharacter damager) {
        super.takeCombatDamage(combatEvent, damage, damager);
        setAttackBehavior(makeRandomAttackBehavior());
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getDamage() {
        return 6;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new NoCombatLoot();
    }

    @Override
    public int getMaxHP() {
        return 16;
    }

    @Override
    public int getDamageReduction() {
        return 1;
    }

    @Override
    public String getDeathSound() {
        return "lever";
    }

    @Override
    public boolean isFearless() {
        return true;
    }

    @Override
    public int getSpeed() {
        return -1;
    }

    @Override
    public void addCondition(Condition cond) {
        super.addCondition(cond);
        if (getHP() > 2) {
            addToHP(-2);
        }
    }

    private static class StoneGolemSprite extends LoopingSprite {
        public StoneGolemSprite() {
            super("stonegolem", "enemies.png", 0x154, 32);
            setFrames(4);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.GRAY_RED);
            setColor3(MyColors.DARK_GREEN);
            setColor4(MyColors.ORANGE);
        }
    }
}
