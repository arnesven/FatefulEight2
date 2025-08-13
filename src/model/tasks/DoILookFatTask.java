package model.tasks;

import model.Model;
import model.Summon;
import model.classes.Skill;
import model.map.UrbanLocation;

import java.util.List;

public class DoILookFatTask extends SummonTask {
    private final Summon summon;
    private final UrbanLocation location;

    public DoILookFatTask(Summon summon, Model model, UrbanLocation location) {
        super(model);
        this.summon = summon;
        this.location = location;
    }

    @Override
    protected void doEvent(Model model) {
        portraitSay("I have a very important dinner party coming up and I need an outsiders opinion on this.");
        println(location.getLordName() + " slowly turns around, as to show off " + hisOrHer(location.getLordGender()) + " clothes.");
        portraitSay("Do I look fat in this?");
        model.getLog().waitForAnimationToFinish();

        int choice = multipleOptionArrowMenu(model, 24, 20, List.of("Of course not!", "No, but wear something else.", "Uhm, yes..."));
        if (choice < 2) {
            portraitSay("Liar! You're just like everybody else. You just say what I want to hear.");
            model.getParty().randomPartyMemberSay(model, List.of("What a vain person..."));
        } else {
            portraitSay("Hmm... Well at least you're honest. Maybe something in another color.");
            model.getParty().randomPartyMemberSay(model, List.of("It's not the garment... it's your body shape."));
            println(location.getLordName() + " gasps and stares at you with wide eyes!");
            model.getParty().randomPartyMemberSay(model, List.of("You just need to get into shape. I'm sure we could give you some advice on how to exercise."));
            portraitSay("Oh. Okay...");
            boolean success = model.getParty().doCollaborativeSkillCheck(model, this, Skill.Endurance, 10);
            if (success) {
                summon.increaseStep();
                portraitSay("Thanks for the workout session! But what about the dinner party?");
                model.getParty().randomPartyMemberSay(model, List.of("You'll be fine. Just tell your guests about your new workout " +
                        "routine and they'll be impressed by your self awareness and forget about everything else."));
                portraitSay("You really think so?");
                model.getParty().randomPartyMemberSay(model, List.of("Of course."));
                portraitSay("Well thanks again. Please allow me to compensate you for your trouble.");
                println("The party receives 25 gold.");
                model.getParty().earnGold(25);
            } else {
                portraitSay("This is exhausting... I'm never going to be able to do this. I give up.");
                model.getParty().randomPartyMemberSay(model, List.of("Come on people " + heOrShe(location.getLordGender()) + "'s hopeless..."));
            }
        }




    }

    @Override
    public String getJournalDescription() {
        return heOrSheCap(location.getLordGender()) + " needs some advice, about fashion or perhaps something else.";
    }
}
