package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.*;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.enemies.Enemy;
import model.races.Race;
import util.MyPair;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class CatInATreeEvent extends GeneralInteractionEvent {
    private CharacterAppearance portrait;

    public CatInATreeEvent(Model model) {
        super(model, "Talk to", MyRandom.randInt(2, 10));
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        this.portrait = PortraitSubView.makeOldPortrait(Classes.None, Race.randomRace(), true);
        showExplicitPortrait(model, portrait, "Old Woman");
        println("As you cut through a small park you spot an old woman standing under a tree.");
        return true;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        println("");
        portraitSay("Come down Quincy. Come down... Oh, excuse me sir. Would you mind helping me?");
        leaderSay("What's the problem then?");
        portraitSay("My cat, Quincy. He won't come down. I think he chased a squirrel up there, " +
                "and now he can't get down.");
        model.getParty().randomPartyMemberSay(model, List.of("Oh bother...", "You got to be kitten me!",
                "What a CATastrophe!"));
        randomSayIfPersonality(PersonalityTrait.benevolent, List.of(model.getParty().getLeader()),
                "We simply must help...");
        do {
            print("Climb the tree? (Y/N) ");
            if (yesNoInput()) {
                MyPair<Boolean, GameCharacter> result = model.getParty().doSoloSkillCheckWithPerformer(model, this, Skill.Acrobatics, 6);
                if (result.first) {
                    println(result.second.getName() + " manages to climb the tree and retrieve Quincy the cat.");
                    SkillCheckResult spotting = result.second.testSkillHidden(Skill.Perception, 6, 0);
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
        return true;
    }

    @Override
    protected String getVictimSelfTalk() {
        return "I'm just an old woman. Won't you help me out?";
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        return new GameCharacter("Old Woman", "", portrait.getRace(), Classes.None, portrait,
                Classes.NO_OTHER_CLASSES);
    }

    @Override
    protected List<Enemy> getVictimCompanions(Model model) {
        return new ArrayList<>();
    }

    @Override
    protected ProvokedStrategy getProvokedStrategy() {
        return ProvokedStrategy.ALWAYS_ESCAPE;
    }

}
