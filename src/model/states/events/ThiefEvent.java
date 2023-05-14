package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.states.DailyEventState;

import java.util.List;

public class ThiefEvent extends DailyEventState {
    private final boolean withIntro;

    public ThiefEvent(Model model, boolean withIntro) {
        super(model);
        this.withIntro = withIntro;
    }

    public ThiefEvent(Model model) {
        this(model, true);
    }

    @Override
    protected void doEvent(Model model) {
        showSilhouettePortrait(model, "Stranger");
        if (withIntro) {
            println("A stranger comes up to you and asks for directions.");
        }
        if (spotThief(model)) {
            model.getParty().randomPartyMemberSay(model, List.of("Did you think you could pull a fast one on us?"));
            println("Thief: \"Come on guys, I just need enough for food, and maybe a beer.\"");
            print("Do you treat the thief to some rations and a drink? (Y/N) ");
            if (yesNoInput()) {
                model.getParty().partyMemberSay(model, model.getParty().getLeader(),
                        "Well, why didn't you say so. You're welcome to " +
                                "spend the evening with us, we've got plenty of food and drink.");
                model.getParty().addToFood(-1);
                ChangeClassEvent change = new ChangeClassEvent(model, Classes.THF);
                print("The thief is surprised by your sudden generosity and gladly joins you for the evening. " +
                        "The thief is willing to show you some tricks, ");
                change.areYouInterested(model);
            } else {
                model.getParty().randomPartyMemberSay(model, List.of("Shove off you! Don't let us catch you near us again!"));
            }
        } else {
            println("Slightly confused over the stranger's line of questions you try your best to answer. " +
                    "Later you realize that one of your money purses has disappeared");
            model.getParty().randomPartyMemberSay(model, List.of("Dammit. I knew that guy was trouble."));
            int lost = 10;
            if (model.getParty().getGold() < 10) {
                lost = model.getParty().getGold();
            }
            println("The party loses " + lost + " gold.");
            model.getParty().addToGold(-lost);
        }

    }

    private boolean spotThief(Model model) {
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            SkillCheckResult result = gc.testSkill(Skill.Perception, 8);
            if (result.isSuccessful()) {
                println(gc.getName() + " spots the stranger trying to snatch your purse. (Perception roll of " + result.asString() + ")");
                model.getParty().partyMemberSay(model, gc, "Hey, there! THIEF!");
                return true;
            }
        }
        return false;
    }
}
