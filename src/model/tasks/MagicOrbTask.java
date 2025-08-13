package model.tasks;

import model.Model;
import model.Summon;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.map.UrbanLocation;
import util.MyPair;
import util.MyRandom;

import java.util.List;

public class MagicOrbTask extends SummonTask {
    private static final List<String> ORB_TYPES =
            List.of("cursed orb. You should dispose of it immediately!",
                    "healing orb. You could use it to cure those who are ill.",
                    "nature orb. It will make your crops grow well.",
                    "gateway orb. You could use it to travel to other dimensions, but I wouldn't recommend it.",
                    "seeing orb. You could use it to see spy on people, but you don't know who's spying on you!",
                    "weather orb. With it, you can influence the rain and wind.");
    private final Summon summon;
    private final UrbanLocation location;

    public MagicOrbTask(Summon summon, Model model, UrbanLocation location) {
        super(model);
        this.summon = summon;
        this.location = location;
    }

    @Override
    protected void doEvent(Model model) {
        portraitSay("You see, some of the townsfolk recently found this magical orb in a nearby swamp. " +
                "I've let my most learned scholars look at it but nobody can tell me what it is. I'm tempted to just chuck it " +
                "back into the swamp, but then again, what if it has some useful power? Would you examine it for me?");
        print("Do you wish to examine the magical orb now? (Y/N) ");
        if (yesNoInput()) {
            MyPair<Boolean, GameCharacter> pair = model.getParty().doSoloSkillCheckWithPerformer(model, this, Skill.SpellCasting, 8);
            if (pair.first) {
                summon.increaseStep();
                model.getParty().partyMemberSay(model, pair.second, "This is a " + MyRandom.sample(ORB_TYPES));
                portraitSay("That's good to know, thank you. Here, let me pay you for your trouble.");
                println("The party receives 25 gold.");
                model.getParty().earnGold(25);
            }
        } else {
            portraitSay("Uh, okay. But come back if you change your mind! " +
                    "What is this thing...");
        }
    }

    @Override
    public String getJournalDescription() {
        return heOrSheCap(location.getLordGender()) + " wants a magic orb identified.";
    }
}
