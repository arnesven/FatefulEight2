package model.tasks;

import model.Model;
import model.Summon;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.map.UrbanLocation;
import sound.SoundEffects;
import util.MyPair;

import java.util.List;

public class InspireMeForAdventureTask extends SummonTask {
    private final Summon summon;
    private final UrbanLocation location;

    public InspireMeForAdventureTask(Summon summon, Model model, UrbanLocation location) {
        super(model);
        this.summon = summon;
        this.location = location;
    }

    @Override
    protected void doEvent(Model model) {
        println(location.getLordName() + ": \"I've been meaning to do this for quite some time. I'm leaving!\"");
        leaderSay("What do you mean?");
        println(location.getLordName() + ": \"I'm sick of doing this. I want to leave. I want to go out and have an adventure!\"");
        leaderSay("Don't take this the right way, but you don't really look the adventuring type.");
        println(location.getLordName() + ": \"Nonsense! I'm all about trekking into the wilds. Fighting fierce adversaries! Treasure hunting!\"");
        leaderSay("It's not all glory. Sometimes we run into real trouble. And sometimes we're cold, hungry and lost...");
        println(location.getLordName() + ": \"You're just saying that.\"");
        leaderSay("No really. It's not easy. By the way, aren't you doing important work here?");
        println(location.getLordName() + ": \"Sure... but anybody could do it. It's mostly just cutting ribbons, kissing babies. You know boring stuff...\"");
        leaderSay("Don't you get to make important decisions?");
        println(location.getLordName() + ": \"Yes, I do... But It's dull. I don't think I con go on doing it any longer. Unless I was really inspired somehow.\"");
        print("Do you wish to try and inspire the " + location.getLordTitle() + "? (Y/N) ");
        if (yesNoInput()) {
            MyPair<Boolean, GameCharacter> pair = model.getParty().doSoloSkillCheckWithPerformer(model, this, Skill.Leadership, 9);
            if (pair.first) {
                model.getParty().partyMemberSay(model, pair.second, "Come on now. Don't you know that this town depends on you? " +
                        "Just think about it. The work you do will inspire others to do even grater things. In 100 years, people will " +
                        "talk about the great leaders of this town, you can be among them. Maybe they'll even sing songs about you?");
                println(location.getLordName() + ": \"You think so?\"");
                model.getParty().partyMemberSay(model, pair.second, "Sure they will. If you really put some effort into this. " +
                        "Don't get distracted by ideas of running aimlessly out into the woods!");
                println(location.getLordName() + ": \"I guess that would be rather foolish. And besides, I do have a very comfortable life here. " +
                        "And people do like me as their " + location.getLordTitle() + ".\"");
                leaderSay("There you go!");
                summon.increaseStep();
                println(location.getLordName() + ": \"Wow, I feel much better. Maybe I just had some kind of personal crisis?\"");
                leaderSay("The important thing now is to get back to work.");
                println(location.getLordName() + ": \"Right!\"");
                model.getParty().randomPartyMemberSay(model, List.of("And to appreciate those around you."));
                println(location.getLordName() + ": \"Yes of course! Here, please let me give you something for your time.\"");
                println("The party receives 15 gold.");
                model.getParty().addToGold(15);
            }
        } else {
            leaderSay("I'm sorry you feel that way. But we just can't take you on. You understand.");
            println(location.getLordName() + ": \"Uh, okay. But come back if you change your mind!\"");
        }
    }
}