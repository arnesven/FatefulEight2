package model.states.events;

import model.Model;
import model.enemies.Enemy;
import model.enemies.ScorpionEnemy;
import model.enemies.SmallScorpionEnemy;
import model.states.DailyEventState;
import view.combat.DesertNightCombatTheme;

import java.util.ArrayList;
import java.util.List;

public class ScorpionNightAttackEvent extends NightTimeAttackEvent {
    public ScorpionNightAttackEvent(Model model) {
        super(model, 9, "notices a bunch of scorpions crawling into the camp",
                new DesertNightCombatTheme(), "scorpions are crawling all over the camp");
    }

    @Override
    protected List<Enemy> getEnemies(Model model) {
        List<Enemy> enemies = new ArrayList<>();
        System.out.println("Party strength: " + model.getParty().partyStrength());
        System.out.println("Scorpion threat: " + (new SmallScorpionEnemy('A')).getThreat());
        int numberOfEnemies = Math.max(1, model.getParty().partyStrength() / (new SmallScorpionEnemy('A')).getThreat());
        for (int i = numberOfEnemies; i > 0; --i) {
            enemies.add(new SmallScorpionEnemy('A'));
        }
        return enemies;
    }
}
