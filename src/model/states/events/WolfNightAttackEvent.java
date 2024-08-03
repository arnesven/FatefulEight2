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

public class WolfNightAttackEvent extends DailyEventState { // TODO: Probably make superclass for night time events.

    public WolfNightAttackEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        GameCharacter rando = model.getParty().getRandomPartyMember();
        println("It is night and " + rando.getName() + " is keeping watch."); // TODO: Get help from dog
        SkillCheckResult result = rando.testSkill(model, Skill.Perception, 8);
        boolean spotted = false;
        if (result.isSuccessful()) {
            spotted = true;
            println(rando.getFirstName() + " can hear wolves howling (Perception " + result.asString() + ")");
        }
        List<Enemy> enemies = new ArrayList<>();
        int numberOfEnemies = Math.max(1, model.getParty().partyStrength() / (new WolfEnemy('A')).getThreat());
        for (int i = numberOfEnemies; i > 0; --i) {
            enemies.add(new WolfEnemy('A'));
        }
        if (spotted) {
            leaderSay("Everybody wake up! We are under attack!");
            model.getLog().waitForAnimationToFinish();
            runCombat(enemies, new NightGrassCombatTheme(), true);
        } else {
            println("Before " + rando.getFirstName() + " has a chance to react the camp is full of vicious wolves!");
            model.getLog().waitForAnimationToFinish();
            runAmbushCombat(enemies, new NightGrassCombatTheme(), true);
        }
    }
}
