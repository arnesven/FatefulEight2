package model.states.events;

import model.Model;
import model.enemies.Enemy;
import model.enemies.WolfEnemy;
import model.states.DailyEventState;
import view.combat.NightGrassCombatTheme;

import java.util.ArrayList;
import java.util.List;

public class OrcNightAttackEvent extends NightTimeAttackEvent {
    public OrcNightAttackEvent(Model model) {
        super(model, 6, "spots a group of orcs sneaking up on the camp",
                new NightGrassCombatTheme(), "orcs run into the camp causing chaos and confusion");
    }

    @Override
    protected List<Enemy> getEnemies(Model model) {
        Enemy orc = OrcBandEvent.makeRandomOrcEnemy();
        List<Enemy> enemies = new ArrayList<>();
        int numberOfEnemies = Math.max(1, model.getParty().partyStrength() / (orc).getThreat());
        for (int i = numberOfEnemies; i > 0; --i) {
            enemies.add(OrcBandEvent.makeRandomOrcEnemy());
        }
        return enemies;
    }
}
