package model.states.events;

import model.Model;
import model.combat.conditions.CowardlyCondition;
import model.enemies.AutomatonEnemy;
import model.enemies.Enemy;
import model.enemies.FormerPartyMemberEnemy;
import model.tasks.AssassinationDestinationTask;
import view.combat.MansionTheme;

import java.util.ArrayList;
import java.util.List;

public class EnchanterAssassinationEndingEvent extends AssassinationEndingEvent {
    public EnchanterAssassinationEndingEvent(Model model) {
        super(model, true, 4);
    }

    @Override
    protected Ending sneakAroundInside(Model model, AssassinationDestinationTask task, String place) {
        return encounterAutomatons(model, task, place);
    }

    @Override
    protected Ending enterHomePartTwo(Model model, AssassinationDestinationTask task, String place) {
        return encounterAutomatons(model, task, place);
    }

    private Ending encounterAutomatons(Model model, AssassinationDestinationTask task, String place) {
        println("You immediately detect that the " + place + " is crawling with some kind of " +
                "autonomous machines.");
        leaderSay("What in the world are those?");
        println("The machines are moving about from room to room, and there is no way to avoid them.");
        model.getLog().waitForAnimationToFinish();
        runCombat(getVictimCompanions(model, task, false), new MansionTheme(), true);
        if (haveFledCombat()) {
            println("You escape from the " + place + ".");
            leaderSay("That was pretty crazy. If " + task.getWrit().getName() + " was in there I'm sure " +
                    heOrShe(task.getWrit().getGender()) + " taken off by now.");
        }
        return Ending.failure;
    }

    @Override
    protected List<Enemy> getVictimCompanions(Model model, AssassinationDestinationTask task, boolean isOutside) {
        if (isOutside) {
            return new ArrayList<>();
        }
        List<Enemy> enemies = new ArrayList<>();
        for (int i = getSuggestedNumberOfEnemies(model, new AutomatonEnemy('A')) + 1; i > 0; --i) {
            enemies.add(new AutomatonEnemy('A'));
        }
        return enemies;
    }

    @Override
    protected void addConditionsToVictimEnemy(Model model, AssassinationDestinationTask task, FormerPartyMemberEnemy enemy,
                                              List<Enemy> companions, int victimFleeChance, boolean isOutside) {
        if (!isOutside) {
            enemy.addCondition(new CowardlyCondition(companions));
        } else {
            super.addConditionsToVictimEnemy(model, task, enemy, companions, victimFleeChance, true);
        }
    }
}
