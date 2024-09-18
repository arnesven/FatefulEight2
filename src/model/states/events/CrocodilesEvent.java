package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.enemies.CrocodileEnemy;
import model.enemies.Enemy;
import model.states.DailyEventState;

import java.util.List;

public class CrocodilesEvent extends DailyEventState {
    public CrocodilesEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("As the party tries to cross a wetland by stepping on " +
                "floating logs they soon realize, they are not logs at all! " +
                "The creatures come alive and snap at you with deadly jaws.");
        boolean spotted = false;
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (gc.testSkillHidden(Skill.Perception, 8, 0).isSuccessful()) {
                println(gc.getName() + " spots the crocodiles and raises the alarm!");
                spotted = true;
                break;
            }
        }
        List<Enemy> enemies = List.of(new CrocodileEnemy('A'), new CrocodileEnemy('A'), new CrocodileEnemy('A'));
        if (spotted) {
            runCombat(enemies, false);
        } else {
            runAmbushCombat(enemies, model.getCurrentHex().getCombatTheme(), false);
        }
    }
}
