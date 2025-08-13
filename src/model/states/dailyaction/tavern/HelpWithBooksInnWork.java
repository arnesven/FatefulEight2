package model.states.dailyaction.tavern;

import model.Model;
import model.classes.Skill;

public class HelpWithBooksInnWork extends InnWorkAction {
    public HelpWithBooksInnWork() {
        super("Look at books", "The previous owner of this place left the books in complete disarray. " +
                "Can you have a look at them and set them straight?");
    }

    @Override
    public void doWork(Model model, TalkToBartenderState state) {
        state.println("You spend the rest of the day trying to make sense of the bartender's economical situation.");
        boolean success = model.getParty().doSoloSkillCheck(model, state, Skill.Logic, 6);
        if (success) {
            state.println("You finally manage to understand the system of symbols and numbers, then transcribe them into " +
                    "notes which are more generally understandable.");
            state.bartenderSay(model, "Oh, now I understand. Thank you for clearing this up. Good work. Here's your pay.");
            model.getParty().earnGold(6);
            state.println("You got 6 gold.");
        } else {
            state.println("Despite your best efforts, the ledgers and papers are completely beyond your understanding. " +
                    "After many hours you are forced to admit that you aren't getting anywhere.");
            state.bartenderSay(model, "That's a shame. Well, at least you tried. Here's something for your troubles.");
            model.getParty().earnGold(2);
            state.println("You got 2 gold.");
        }
    }
}
