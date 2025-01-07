package model.states.events;

import model.Model;
import model.enemies.Enemy;
import model.enemies.FormerPartyMemberEnemy;
import model.tasks.AssassinationDestinationTask;

import java.util.List;

public class KnightAssassinationEndingEvent extends AssassinationEndingEvent {
    public KnightAssassinationEndingEvent(Model model) {
        super(model, true, 4);
    }

    @Override
    protected void victimReactToAttack(String name) {
        printQuote(name, "You are making a big mistake. Come on then!");
    }

    protected void addConditionsToVictimEnemy(Model model, AssassinationDestinationTask task,
                                              FormerPartyMemberEnemy enemy, List<Enemy> companions,
                                              int victimFleeChance, boolean isOutside) {
        // Don't add the condition
    }
}
