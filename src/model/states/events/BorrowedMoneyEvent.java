package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.List;

public class BorrowedMoneyEvent extends DailyEventState {
    public BorrowedMoneyEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        GameCharacter main = model.getParty().getRandomPartyMember();
        model.getParty().partyMemberSay(model, main, "Hey, I have a friend who moved to this town a while ago.");
        String genderWord = MyRandom.sample(List.of("he", "she"));
        String genderWord2 = "her";
        if (genderWord.equals("he")) {
            genderWord2 = "him";
        }
        if (model.getParty().size() > 1) {
            GameCharacter other = model.getParty().getRandomPartyMember(main);
            model.getParty().partyMemberSay(model, other, "Oh, is " + genderWord + " nice?");
            model.getParty().partyMemberSay(model, main, "Not really.");
        }
        model.getParty().partyMemberSay(model, main, "But " + genderWord + " owes me money. Let's track " +
                genderWord2 + " down and make " + genderWord2 + " pay up.");
        boolean success = model.getParty().doCollaborativeSkillCheck(model, this, Skill.SeekInfo, 7);
        if (success) {
            String name = "Bianca";
            if (genderWord.equals("he")) {
                name = "Johnny";
            }
            model.getParty().partyMemberSay(model, main, "Hey " + name + ", long time no see!");
            println(name + ": \"Oh, hi... it's you.\"");
            model.getParty().partyMemberSay(model, main, "Remember that gold you borrowed a little while back?");
            SkillCheckResult result = model.getParty().doSkillCheckWithReRoll(model, this, main,  Skill.Persuade,5, 10, 0);
            if (result.isSuccessful()) {
                println(name + ": \"Okay okay, here, take it. I was only gonna spend it on dice anyway.\"");
                println("The party receives 15 gold!");
                model.getParty().addToGold(15);
            } else {
                println(name + ": \"I don't know what you're talking about. Now please excuse me, I have somewhere to be.\"");
                if (model.getParty().size() > 1) {
                    GameCharacter other = model.getParty().getRandomPartyMember(main);
                    model.getParty().partyMemberSay(model, other, "No, " + genderWord + " wasn't very nice.");
                } else {
                    model.getParty().partyMemberSay(model, main, "Damn. I'm not going to see that money again#");
                }
            }
        } else {
            model.getParty().partyMemberSay(model, main, "Maybe " + genderWord + " doesn't live here after all.");
        }
        print("Press enter to continue.");
        waitForReturn();
    }
}
