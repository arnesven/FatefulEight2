package model.states.events;

import model.Model;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.enemies.BearEnemy;
import model.states.DailyEventState;
import view.subviews.TundraCombatTheme;

import java.util.List;

public class BearEvent extends DailyEventState {
    public BearEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("A large bear grunts at the party. It seems hungry.");
        print("Do you want to try and avoid fighting the bear? (Y/N) ");
        if (yesNoInput()) {
            model.getParty().partyMemberSay(model, model.getParty().getLeader(),
                    List.of("Hey, what are you supposed to do? Climb a tree, or play dead?"));
            boolean passed = model.getParty().doCollectiveSkillCheck(model, this, Skill.Survival, 4);
            if (passed) {
                println("The bear seems dissuaded and goes away.");
                return;
            } else if (model.getParty().getFood() > 1) {
                print("Do you want to try to distract the bear with some of your food (half of your rations)? (Y/N) ");
                if (yesNoInput()) {
                    model.getParty().addToFood(-model.getParty().getFood()/2);
                    println("The bear is distracted and the party can sneak away.");
                    return;
                }
            }
        }
        runCombat(List.of(new BearEnemy('A')));
    }
}
