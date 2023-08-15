package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.states.CombatEvent;
import view.subviews.CollapsingTransition;
import view.subviews.NPCCombatSubView;

import java.util.ArrayList;

public class NPCCombatEvent extends CombatEvent {
    private final GameCharacter fighter1;
    private final GameCharacter fighter2;
    private final NPCCombatSubView subView;

    public NPCCombatEvent(Model model, GameCharacter fighter1, GameCharacter fighter2) {
        super(model, new ArrayList<>());
        this.fighter1 = fighter1;
        this.fighter2 = fighter2;
        this.subView = new NPCCombatSubView(this, fighter1, fighter2);
    }

    @Override
    protected void doEvent(Model model) {
        CollapsingTransition.transition(model, subView);
        int round = 1;
        while (!endOfCombat(fighter1)) {
            waitForReturnSilently();
            fighter1.getCombatActions(model, fighter2, this).get(0).doAction(model, this, fighter1, fighter2);
            waitForReturnSilently();
            if (endOfCombat(fighter2)) {
                return;
            }
            fighter2.getCombatActions(model, fighter1, this).get(0).doAction(model, this, fighter2, fighter1);
        }
    }

    private boolean endOfCombat(GameCharacter fighter) {
        if (fighter.isDead()) {
            return true;
        }
        if (fighter.getHP() <= 2) {
            println(fighter.getName() + ": \"I yield!\"");
            return true;
        }
        return false;
    }

    @Override
    public void addStrikeEffect(Combatant target, int damage, boolean critical) {
        subView.addStrikeEffect(target, damage, critical);
    }

    public void doDamageToEnemy(Combatant target, int damage, GameCharacter damager) {
        int hp = target.getHP();
        target.takeCombatDamage(this, damage); // TODO: This completely bypasses armor, no blocking or evading... fix..

    }
}
