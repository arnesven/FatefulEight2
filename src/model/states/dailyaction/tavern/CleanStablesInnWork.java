package model.states.dailyaction.tavern;

import model.Model;
import model.classes.Skill;

public class CleanStablesInnWork extends InnWorkAction {
    public CleanStablesInnWork() {
        super("Clean stables", "The stable needs cleaning. Make it tidy in there and I'll pay you.");
    }

    @Override
    public void doWork(Model model, TalkToBartenderState state) {
        state.println("You spend the rest of the day cleaning up the filthy stables.");
        boolean success = model.getParty().doCollaborativeSkillCheck(model, state, Skill.Labor, 5);
        if (success) {
            state.bartenderSay(model, "Wow! I'm sure the ponies will be pleased. Good work. Here's your pay.");
            model.getParty().addToGold(5);
            state.println("You got 5 gold.");
        } else {
            state.bartenderSay(model, "What's this? This place is even messier " +
                    "than it was before? I won't pay for such shoddy work!");
            state.leaderSay("Rats...");
        }
    }
}
