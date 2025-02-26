package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.classes.SkillChecks;
import model.enemies.CrocodileEnemy;
import model.enemies.Enemy;
import model.states.DailyEventState;
import util.MyPair;

import java.util.List;

public class CrocodilesEvent extends DailyEventState {
    public CrocodilesEvent(Model model) {
        super(model);
    }

    @Override
    public String getDistantDescription() {
        return "some large reptilian creatures";
    }

    @Override
    protected void doEvent(Model model) {
        println("As the party tries to cross a wetland by stepping on " +
                "floating logs they soon realize, they are not logs at all! " +
                "The creatures come alive and snap at you with deadly jaws.");
        boolean spotted = false;
        MyPair<SkillCheckResult, GameCharacter> result = doPassiveSkillCheck(Skill.Perception, 8);
        if (result.first.isSuccessful()) {
            spotted = true;
            println(result.second.getName() + " spots the crocodiles and raises the alarm (Perception " + result.first.asString() + ")!");
        }
        List<Enemy> enemies = List.of(new CrocodileEnemy('A'), new CrocodileEnemy('A'), new CrocodileEnemy('A'));
        if (spotted) {
            runCombat(enemies, false);
        } else {
            runAmbushCombat(enemies, model.getCurrentHex().getCombatTheme(), false);
        }
    }
}
