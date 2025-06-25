package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.DogAppearance;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.enemies.Enemy;
import model.states.DailyEventState;
import view.combat.CombatTheme;
import java.util.List;

public abstract class NightTimeAttackEvent extends NightTimeEvent {
    private final int perceptionDifficulty;
    private final String perceptionSuccessString;
    private final CombatTheme combatTheme;
    private final String ambushIntro;

    public NightTimeAttackEvent(Model model, int perceptionDifficulty, String perceptSuccess,
                                CombatTheme combatTheme, String ambushIntro) {
        super(model);
        this.perceptionDifficulty = perceptionDifficulty;
        this.perceptionSuccessString = perceptSuccess;
        this.combatTheme = combatTheme;
        this.ambushIntro = ambushIntro;
    }

    protected abstract List<Enemy> getEnemies(Model model);

    @Override
    protected final void doEvent(Model model) {
        GameCharacter rando = model.getParty().getRandomPartyMember();
        println("It is night and " + rando.getName() + " is keeping watch.");
        boolean spotted = false;
        if (model.getParty().hasDog()) {
            DogAppearance app = new DogAppearance();
            showExplicitPortrait(model, app, "Dog");
            portraitSay("Ruff ruff ruff!");
            partyMemberSay(rando, "What is it " + boyOrGirl(app.getGender()) + "? You sense something?");
            println(rando.getName() + " looks around worriedly. Then suddenly " +
                    heOrShe(rando.getGender()) + " " + perceptionSuccessString + "!");
            spotted = true;
        } else {
            SkillCheckResult result = rando.testSkill(model, Skill.Perception, perceptionDifficulty);
            if (result.isSuccessful()) {
                spotted = true;
                println(rando.getFirstName() + " " + perceptionSuccessString + " (Perception " + result.asString() + ")");
            }
        }
        List<Enemy> enemies = getEnemies(model);
        if (spotted) {
            leaderSay("Everybody wake up! We are under attack!");
            model.getLog().waitForAnimationToFinish();
            runCombat(enemies, combatTheme, true);
        } else {
            println("Before " + rando.getFirstName() + " has a chance to react " + ambushIntro + "!");
            model.getLog().waitForAnimationToFinish();
            runAmbushCombat(enemies, combatTheme, true);
        }
    }
}
