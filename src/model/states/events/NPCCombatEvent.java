package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.combat.CombatLoot;
import model.combat.Combatant;
import model.combat.NoCombatLoot;
import model.enemies.Enemy;
import model.states.CombatEvent;
import util.MyPair;
import view.sprites.AnimationManager;
import view.sprites.Sprite;
import view.subviews.CollapsingTransition;
import view.subviews.NPCCombatSubView;

import java.util.ArrayList;

public class NPCCombatEvent extends CombatEvent {
    private final GameCharacter fighter1;
    private final GameCharacter fighter2;
    private final NPCCombatSubView subView;
    private int round;

    public NPCCombatEvent(Model model, GameCharacter fighter1, GameCharacter fighter2) {
        super(model, new ArrayList<>());
        this.fighter1 = fighter1;
        this.fighter2 = fighter2;
        this.subView = new NPCCombatSubView(this, fighter1, fighter2);
    }

    @Override
    protected void doEvent(Model model) {
        CollapsingTransition.transition(model, subView);
        AnimationManager.synchAnimations();
        round = 1;
        while (!endOfCombat(fighter1)) {
            waitForReturnSilently();
            fighter1.getCombatActions(model, fighter2, this).get(0).executeCombatAction(model, this, fighter1, fighter2);
            waitForReturnSilently();
            if (endOfCombat(fighter2)) {
                return;
            }
            fighter2.getCombatActions(model, fighter1, this).get(0).executeCombatAction(model, this, fighter2, fighter1);
            round++;
        }
        fighter1.removeCombatConditions();
        fighter2.removeCombatConditions();
        waitForReturnSilently();
    }

    @Override
    public int getCurrentRound() {
        return round;
    }

    private boolean endOfCombat(GameCharacter fighter) {
        if (fighter.isDead()) {
            return true;
        }
        if (fighter.getHP() <= 2 && fighter.getHP() != fighter.getMaxHP()) {
            println(fighter.getName() + ": \"I yield!\"");
            return true;
        }
        return false;
    }

    @Override
    public void addStrikeEffect(Combatant target, int damage, boolean critical) {
        subView.addStrikeEffect(target, damage, critical);
    }

    @Override
    public void addStrikeTextEffect(Combatant target, boolean evade) {
        subView.addStrikeTextEffect(target, evade);
    }

    public void doDamageToEnemy(Combatant target, int damage, GameCharacter damager) {
        if (damager == null) {
            target.takeCombatDamage(this, damage);
        } else {
            CharacterWrappedEnemy enemy = new CharacterWrappedEnemy(damager, damage);
            int hpBefore = enemy.getHP();
            ((GameCharacter)target).getAttackedBy(enemy, getModel(), this);
            target.addToHP(enemy.getHP() - hpBefore);
        }
    }

    private static class CharacterWrappedEnemy extends Enemy {
        private final GameCharacter chara;
        private final int damageDealt;

        public CharacterWrappedEnemy(GameCharacter target, int damage) {
            super('A', target.getName());
            this.chara = target;
            setCurrentHp(chara.getHP());
            this.damageDealt = damage;
        }

        @Override
        public int getMaxHP() {
            if (chara == null) {
                return 1;
            }
            return chara.getHP();
        }

        @Override
        public int getSpeed() {
            return chara.getSpeed();
        }

        @Override
        protected Sprite getSprite() {
            return chara.getAvatarSprite();
        }

        @Override
        public int getDamage() {
            return 0;
        }

        @Override
        public CombatLoot getLoot(Model model) {
            return new NoCombatLoot();
        }

        @Override
        public MyPair<Integer, Boolean> calculateBaseDamage(boolean isRanged) {
            return new MyPair<>(damageDealt, false);
        }
    }
}
