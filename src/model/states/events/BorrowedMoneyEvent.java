package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.CharacterAppearance;
import model.characters.appearance.FacialExpression;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.races.Race;
import model.states.DailyEventState;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.List;

public class BorrowedMoneyEvent extends DailyEventState {
    public BorrowedMoneyEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        GameCharacter main = model.getParty().getRandomPartyMember();
        model.getParty().partyMemberSay(model, main, "Hey, I have a friend who moved to this town a while ago.");
        boolean gender = MyRandom.flipCoin();
        if (model.getParty().size() > 1) {
            GameCharacter other = model.getParty().getRandomPartyMember(main);
            model.getParty().partyMemberSay(model, other, "Oh, is " + heOrShe(gender) + " nice?");
            model.getParty().partyMemberSay(model, main, "Not really.");
        }
        model.getParty().partyMemberSay(model, main, "But " + heOrShe(gender) + " owes me money. Let's track " +
                himOrHer(gender) + " down and make " + himOrHer(gender) + " pay up.");
        boolean success = model.getParty().doCollaborativeSkillCheck(model, this, Skill.SeekInfo, 7);
        if (success) {
            String name = randomFirstName(gender);
            CharacterAppearance randAppearance = PortraitSubView.makeRandomPortrait(Classes.None, Race.ALL, gender);
            showExplicitPortrait(model, randAppearance, name);
            model.getParty().partyMemberSay(model, main, "Hey " + name + ", long time no see!");
            portraitSay("Oh, hi... it's you.");
            model.getParty().partyMemberSay(model, main, "Remember that gold you borrowed a little while back?");
            SkillCheckResult result = model.getParty().doSkillCheckWithReRoll(model, this, main,  Skill.Persuade,5, 10, 0);
            if (result.isSuccessful()) {
                portraitSay("Okay okay, here, take it. I was only gonna spend it on dice anyway.");
                println("The party receives 15 gold!");
                model.getParty().earnGold(15);
                partyMemberSay(main, "Thank you.");
                randomSayIfPersonality(PersonalityTrait.greedy, List.of(main),
                        "That's it? What about the interest?", FacialExpression.questioning);
                randomSayIfPersonality(PersonalityTrait.generous, List.of(main),
                        "Oh, it was only 15 gold. Why bother with it?");
            } else {
                portraitSay("I don't know what you're talking about. Now please excuse me, I have somewhere I have to be.");
                if (model.getParty().size() > 1) {
                    GameCharacter other = model.getParty().getRandomPartyMember(main);
                    model.getParty().partyMemberSay(model, other, "No, " + heOrShe(gender) + " wasn't very nice.");
                } else {
                    model.getParty().partyMemberSay(model, main, "Damn. I'm not going to see that money again#");
                }
            }
        } else {
            model.getParty().partyMemberSay(model, main, "Maybe " + heOrShe(gender) + " doesn't live here after all.");
        }
        print("Press enter to continue.");
        waitForReturn();
    }
}
