package model.tasks;

import model.Model;
import model.Summon;
import model.classes.Skill;
import model.items.Lockpick;
import model.map.UrbanLocation;
import sound.SoundEffects;

import java.util.List;

public class UnlockThisBathroom extends SummonTask {
    private final Summon summon;
    private final UrbanLocation location;

    public UnlockThisBathroom(Summon summon, Model model, UrbanLocation location) {
        super(model);
        this.summon = summon;
        this.location = location;
    }

    @Override
    protected void doEvent(Model model) {
        println(location.getLordName() + ": \"You see, it's quite embarrassing, but I've locked myself out of my bathroom. " +
                "There must be something wrong with the lock! I've been running to a neighbors house to relieve myself, and I have " +
                "quite the small bladder so I really want to get this issue fixed. Will you please help me with the lock?" +
                " I'll pay you of course!\"");
        print("Do you wish to work the lock now? (Y/N) ");
        if (yesNoInput()) {
            boolean success = model.getParty().doSoloSkillCheck(model, this, Skill.Security,
                    Lockpick.askToUseLockpick(model, this, 9));
            if (success) {
                summon.increaseStep();
                SoundEffects.playUnlock();
                println("The lock clicks open loudly and the door swings open.");
                println("Kid: \"Hey, I'm in here!\"");
                println(location.getLordName() + ": \"What! Somebody was in there?\"");
                leaderSay("I thought you said the lock was jammed for a while!");
                println(location.getLordName() + ": \"Yes! It's been at least fifteen minutes!\"");
                model.getParty().randomPartyMemberSay(model, List.of("Jeez... this " + (location.getLordGender()?"girl":"guy") + "..."));
                println(location.getLordName() + ": \"Well, I do have a small bladder... good thing the lock is fixed. " +
                        "Here, let me pay you for your trouble.\"");
                println("The party receives 5 gold.");
                model.getParty().addToGold(5);
            }
        } else {
            println(location.getLordName() + ": \"Uh, okay. But come back if you change your mind! " +
                    "Darn it, now I have to go again.\"");
            leaderSay("Maybe stop drinking so much...");
        }
    }

    @Override
    public String getJournalDescription() {
        return "A jammed lock on a bathroom door is causing some trouble.";
    }
}
