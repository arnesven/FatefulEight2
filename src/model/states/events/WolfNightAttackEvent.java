package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.enemies.Enemy;
import model.enemies.WolfEnemy;
import model.states.DailyEventState;
import view.combat.NightGrassCombatTheme;

import java.util.ArrayList;
import java.util.List;

public class WolfNightAttackEvent extends NightTimeAttackEvent {

    public WolfNightAttackEvent(Model model) {
        super(model, 8, "can hear wolves howling",
                new NightGrassCombatTheme(), "the camp is full of vicious wolves");
    }

    @Override
    protected List<Enemy> getEnemies(Model model) {
        List<Enemy> enemies = new ArrayList<>();
        int numberOfEnemies = Math.max(1, model.getParty().partyStrength() / (new WolfEnemy('A')).getThreat());
        for (int i = numberOfEnemies; i > 0; --i) {
            enemies.add(new WolfEnemy('A'));
        }
        return enemies;
    }
}
