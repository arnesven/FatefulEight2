package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.normal.BardClass;
import model.classes.normal.MagicianClass;
import model.classes.Skill;
import model.classes.normal.WizardClass;
import model.states.DailyEventState;
import util.MyLists;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class PartyEntertainmentEvent extends DailyEventState {
    public PartyEntertainmentEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        List<GameCharacter> entertainers = new ArrayList<>();
        for (GameCharacter chara : model.getParty().getPartyMembers()) {
            if (chara.testSkill(Skill.Entertain, 10).isSuccessful()) {
                entertainers.add(chara);
            }
            if (entertainers.size() == model.getParty().size()-1) {
                break;
            }
        }

        if (entertainers.isEmpty() || model.getParty().size() < 2) {
            new NoEventState(model).doEvent(model);
            return;
        }

        if (entertainers.size() == 1) {
            GameCharacter entertainer = entertainers.get(0);
            if (entertainer.getCharClass() instanceof BardClass) {
                println(entertainer.getFirstName() + " has offered to sing a ballad for the party.");
            } else if (entertainer.getCharClass() instanceof MagicianClass || entertainer.getCharClass() instanceof WizardClass) {
                println(entertainer.getFirstName() + " has offered to do a magic trick for the party.");
            } else {
                println(entertainer.getFirstName() + " has offered to tell a grand tale for the party.");
            }
        } else {
            StringBuilder bldr = new StringBuilder();
            for (GameCharacter gc : entertainers) {
                bldr.append(gc.getFirstName() + ", ");
            }
            println(bldr.substring(0, bldr.length()-2) + " has offered to put on a show for the party.");
        }

        print("Do you permit the entertainment? (Y/N) ");
        if (!yesNoInput()) {
            model.getParty().partyMemberSay(model, model.getParty().getLeader(),
                    List.of("Perhaps it's just better with some peace and quiet for now.",
                            "Let's try to stay focused on the task at hand.",
                            "We can do that some other time.",
                            "This isn't the right time for that."));
            return;
        }

        int entertainmentQuality = 0;
        for (GameCharacter gc : entertainers) {
            entertainmentQuality += gc.testSkill(Skill.Entertain).getModifiedRoll();
            if (gc.getSP() > 0) {
                gc.addToSP(-1);
                println(gc.getName() + " exhausts 1 Stamina Point while entertaining the party.");
            } else {
                println(gc.getName() + " is tired (0 SP) and makes a sub-par performance.");
                entertainmentQuality -= 5;
            }
        }

        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (!entertainers.contains(gc)) {
                int roll = MyRandom.rollD10();
                int diff = entertainmentQuality - roll;
                if (diff > 0) {
                    println(gc.getName() + " is entertained by the performance.");
                    if (gc.hasPersonality(PersonalityTrait.encouraging) || gc.hasPersonality(PersonalityTrait.friendly)) {
                        model.getParty().partyMemberSay(model, gc,
                        List.of("Spectacular!", "Wonderful!", "Please, do more!",
                                "Encore!", "I love it!", "You are great!", "This was some high quality entertainment."));
                    } else {
                        model.getParty().partyMemberSay(model, gc,
                                List.of("I like what I see.", "Good stuff!", "Satisfying."));
                    }
                    MyLists.forEach(entertainers, (GameCharacter ent) -> {
                        gc.addToAttitude(ent, diff);
                        ent.addToAttitude(gc, diff/2);
                    });
                } else {
                    println(gc.getName() + " is not entertained by the performance.");
                    if (gc.hasPersonality(PersonalityTrait.critical) || gc.hasPersonality(PersonalityTrait.unkind)) {
                        model.getParty().partyMemberSay(model, gc,
                                List.of("You are terrible at this.", "This was a poor show.",
                                        "I feel Ill.", "This was like watching paint dry.",
                                        "Really really bad."));
                    } else {
                        model.getParty().partyMemberSay(model, gc,
                                List.of("I've seen better.", "Mediocre.", "Please stop.",
                                        "Meh.", "Can't say I love this", "Really?", "This was some low quality entertainment.",
                                        "Not my cup of tea.", "Less than good.", "Not so great."));
                    }
                    MyLists.forEach(entertainers, (GameCharacter ent) -> {
                        gc.addToAttitude(ent, diff/2);
                        ent.addToAttitude(gc, diff/4);
                    });
                }
            }
        }
        model.getLog().waitForAnimationToFinish();
        showPartyAttitudesSubView(model);
    }
}
