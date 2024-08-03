package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.combat.conditions.SummonCondition;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.SmokeBallAnimation;

import java.util.List;

public abstract class SummonCombatSpell extends CombatSpell {
    public SummonCombatSpell(String name, int cost, MyColors color, int difficulty, int hpCost) {
        super(name, cost, color, difficulty, hpCost, false);
    }

    @Override
    public boolean canBeCastOn(Model model, Combatant target) {
        return target instanceof GameCharacter;
    }

    @Override
    public boolean masteriesEnabled() {
        return true;
    }

    @Override
    public final void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        if (performer.hasCondition(SummonCondition.class)) {
            SummonCondition sumCond = (SummonCondition) performer.getCondition(SummonCondition.class);
            GameCharacter gc = sumCond.getSummon();
            combat.removeAlly(gc);
            performer.removeCondition(SummonCondition.class);
            combat.println("Your summon has been replaced.");
        }
        GameCharacter summon = makeSummon(model, combat, performer, target);
        combat.addAllies(List.of(summon));
        performer.addCondition(new SummonCondition(summon));
        combat.addSpecialEffect(summon, new SmokeBallAnimation());
    }

    protected abstract GameCharacter makeSummon(Model model, CombatEvent combat, GameCharacter performer, Combatant target);

}
