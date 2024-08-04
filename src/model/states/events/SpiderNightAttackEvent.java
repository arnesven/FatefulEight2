package model.states.events;

import model.Model;
import model.enemies.Enemy;
import model.enemies.SmallScorpionEnemy;
import model.enemies.SpiderEnemy;
import model.states.DailyEventState;
import view.combat.NightGrassCombatTheme;

import java.util.ArrayList;
import java.util.List;

public class SpiderNightAttackEvent extends NightTimeAttackEvent {
    public SpiderNightAttackEvent(Model model) {
        super(model, 9, "giant spiders nimbly descending on the camp",
                new NightGrassCombatTheme(), "giant spiders a crawling all over the camp");
    }

    @Override
    protected List<Enemy> getEnemies(Model model) {
        List<Enemy> enemies = new ArrayList<>();
        System.out.println("Party strength: " + model.getParty().partyStrength());
        System.out.println("Spider threat: " + (new SmallScorpionEnemy('A')).getThreat());
        int numberOfEnemies = Math.max(1, model.getParty().partyStrength() / (new SpiderEnemy('A')).getThreat());
        for (int i = numberOfEnemies; i > 0; --i) {
            enemies.add(new SpiderEnemy('A'));
        }
        return enemies;
    }
}
