package model.states.events;

import model.Model;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.enemies.Enemy;
import model.enemies.GoblinWolfRiderEnemy;
import model.enemies.WolfEnemy;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class WolfEvent extends DailyEventState {
    private final boolean isWolfRiders;

    public WolfEvent(Model model) {
        super(model);
        this.isWolfRiders = calculateAverageLevel(model) >= 3.0 && MyRandom.flipCoin();
    }

    @Override
    protected void doEvent(Model model) {
        println("You suddenly hear something.");
        leaderSay("Is that howling? Maybe it's just the wind.");
        println("Convinced that it's just your imagination, you trudge onward.");
        println("A little while later you hear it again.");
        leaderSay("Yeah... that's howling alright.");
        println("The sound of wolves howling is now unmistakable and the pack finally catches up with you." + getExtraText());
        if (!canSneak(model)) {
            print("Do you want to try to run away? (Y/N) ");
            if (yesNoInput()) {
                setFledCombat(true);
                return;
            }
            model.getLog().waitForAnimationToFinish();
            List<Enemy> enemies = new ArrayList<>();
            int numberOfEnemies = Math.max(1, model.getParty().partyStrength() / (getWolf()).getThreat());
            for (int i = numberOfEnemies; i > 0; --i) {
                enemies.add(getWolf());
            }
            runCombat(enemies);
        }
    }

    protected Enemy getWolf() {
        return isWolfRiders ? new GoblinWolfRiderEnemy('A') : new WolfEnemy('A');
    }

    protected String getExtraText() {
        if (isWolfRiders) {
            return " When the wolves come closer, you can clearly see that they have riders - goblins!";
        }
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
