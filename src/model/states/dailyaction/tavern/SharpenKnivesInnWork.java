package model.states.dailyaction.tavern;

import model.Model;
import model.classes.Skill;

public class SharpenKnivesInnWork extends InnWorkAction {
    public SharpenKnivesInnWork() {
        super("Sharpen knives", "The knives in the kitchen are very blunt. Could you sharpen them for me?");
    }

    @Override
    public void doWork(Model model, TalkToBartenderState state) {
        state.println("You spend the rest of the day sharpening all the knives in the kitchen.");
        boolean success = model.getParty().doSoloSkillCheck(model, state, Skill.Blades, 6);
        if (success) {
            state.bartenderSay(model, "Excellent. You've saved me a trip to a town to have this done." +
                    " Here's your pay.");
            model.getParty().earnGold(8);
            state.println("You got 8 gold.");
        } else {
            state.bartenderSay(model, "... These knives are blunter than a sledgehammer. " +
                    "I completely overestimated your ability. " +
                    "I won't pay for such shoddy work!");
        }

    }
}
