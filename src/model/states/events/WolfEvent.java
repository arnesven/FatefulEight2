package model.states.events;

import model.Model;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.enemies.Enemy;
import model.enemies.WolfEnemy;
import model.states.DailyEventState;

import java.util.ArrayList;
import java.util.List;

public class WolfEvent extends DailyEventState {
    public WolfEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("The sound of wolves howling is now unmistakable and the pack finally catches up with you come nightfall." + getExtraText());
        if (!canSneak(model)) {
            model.getLog().waitForAnimationToFinish();
            List<Enemy> enemies = new ArrayList<>();
            int numberOfEnemies = Math.max(1, model.getParty().partyStrength() / (new WolfEnemy('A')).getThreat());
            for (int i = numberOfEnemies; i > 0; --i) {
                enemies.add(getWolf());
            }
            runCombat(enemies);
        }
    }

    protected Enemy getWolf() {
        return new WolfEnemy('A');
    }

    protected String getExtraText() {
        return "";
    }

    private boolean canSneak(Model model) {
        if (model.getParty().size() == 1) {
            SkillCheckResult result = model.getParty().getPartyMember(0).testSkill(model, Skill.Sneak, 6);
            println("You quickly try to find a place to hide. (Sneak " + result.asString() + ")");
            if (result.isSuccessful()) {
                println("Fortunately you find a little cranny to hide in and the wolves pass by.");
                return true;
            } else {
                println("But the wolves pick up your scent!");
            }
        }
        return false;
    }
}
