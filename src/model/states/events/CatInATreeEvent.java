package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.states.DailyEventState;
import util.MyPair;

import java.util.List;

public class CatInATreeEvent extends DailyEventState {
    public CatInATreeEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        showSilhouettePortrait(model, "Old Woman");
        println("As you cut through a small park you spot an old woman standing under a tree.");
        portraitSay("Come down Quincy. Come down... Oh, excuse me sir. Would you mind helping me?");
        leaderSay("What's the problem then?");
        portraitSay("My cat, Quincy. He won't come down. I think he chased a squirrel up there, " +
                "and now he can't get down.");
        model.getParty().randomPartyMemberSay(model, List.of("Oh bother...", "You gotto be kitten me!",
                "What a CATastrophe!"));
        do {
            print("Climb the tree? (Y/N) ");
            if (yesNoInput()) {
                MyPair<Boolean, GameCharacter> result = model.getParty().doSoloSkillCheckWithPerformer(model, this, Skill.Acrobatics, 6);
                if (result.first) {
                    println(result.second.getName() + " manages to climb the tree and retrieve Quincy the cat.");
                    SkillCheckResult spotting = result.second.testSkill(Skill.Perception, 6);
                    if (spotting.isSuccessful()) {
                        println(result.second.getName() + " also spots a shiny trinket (Perception " + spotting.asString() + "). How did that get there?");
                        model.getParty().addToGold(15);
                        println("The party gains 10 gold.");
                    }
                    portraitSay("Oh thank you so much for getting my beloved Quincy for me! Here, " +
                            "please take this, It's all I have on me.");
                    model.getParty().addToGold(5);
                    println("The party gains 5 gold.");
                    break;
                } else {
                    println(result.second.getName() + " falls out of the tree");
                    model.getParty().partyMemberSay(model, result.second, "Ouch!#");
                }
            } else {
                println("You excuse yourself and carry on with your day.");
                break;
            }
        } while (true);
    }
}
