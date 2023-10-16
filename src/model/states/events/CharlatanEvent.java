package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.items.Lockpick;
import model.states.DailyEventState;

import java.util.List;

public class CharlatanEvent extends DailyEventState {
    public CharlatanEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("Next to a small covered wagon, standing on a wooden box, is a man in extravagant clothing. " +
                "He is shouting to a crowd while holding up a small bottle. " +
                "Intrigued, you wander closer.");
        showRandomPortrait(model, Classes.CHARLATAN, "Doctor");
        portraitSay("... yes, it's true, this elixir will cure your stomach ailments sir. But not only that! " +
                "Have a problem with warts madam? The elixir will remove them for you!");
        portraitSay("In fact, my famous draft will cure just about anything. And it tastes good too!");
        portraitSay("Only available here and now! For the incredible price of just 20 gold pieces, you may " +
                "secure the remedy of your current malediction, or any future one.");
        model.getParty().randomPartyMemberSay(model, List.of("This guy is shady.", "We should walk away from this.",
                "If it's half as good as his boasting, we ought to buy it.", "Is this for real?", "I've heard stuff like this before",
                "Sounds like a good deal!", "Quick, where's our money?"));
        portraitSay("I guarantee it ladies and " +
                "gentlemen, if your illnesses, sicknesses, wounds and aches won't disappear with this brew, I'll " +
                "give you your money back.");
        if (model.getParty().getGold() < 20) {
            model.getParty().randomPartyMemberSay(model, List.of("If only we could afford it..."));
        } else {
            print("Buy the doctor's elixir? (Y/N) ");
            if (yesNoInput()) {
                model.getParty().addToGold(-20);
                for (GameCharacter gc : model.getParty().getPartyMembers()) {
                    SkillCheckResult result = gc.testSkill(Skill.MagicGreen, 10);
                    if (result.isSuccessful()) {
                        charlatanFoundOut(model, gc, result);
                        return;
                    }
                }

                println("You manage to get a hold on one of the last of the doctor's bottles. The doctor " +
                        "gives you a big smile as you hand over your gold.");
                portraitSay("A bargain, I guarantee.");
                println("Later you bring out the little bottle to have a tiny taste. As soon as a " +
                        "single drop lands on your tongue you realize you've been cheated.");
                leaderSay("This is cherry juice...");
                model.getParty().randomPartyMemberSay(model, List.of("Expensive cherry juice."));
            } else {
                leaderSay("I think we will pass on this one.");
            }
        }
    }

    private void charlatanFoundOut(Model model, GameCharacter gc, SkillCheckResult result) {
        println(gc.getName() + " realizes the the potion is not brewed through alchemical means. " +
                "(Magic (Green) roll of " + result.asString() + ")");
        model.getParty().partyMemberSay(model, gc, "Hey, this is just berry juice!");
        println("The crowd starts murmuring. Some of the doctor's customers are opening their flask " +
                "to taste the contents.");
        showRandomPortrait(model, Classes.None, "Angry Commoner");
        portraitSay(heOrSheCap(gc.getGender()) + " is right. It is juice!");
        println("The crowd quickly turns into an angry mob. The doctor scrambles to get away and " +
                "the townsfolk chase after him, throwing his elixir at him and yelling insults.");
        println("You search through the doctor's wagon and find bags of cherries and a small chest.");
        boolean lockPicked = model.getParty().doSoloLockpickCheck(model, this, 8);
        if (lockPicked) {
            println("The little chest contains a large number of coins, gems and jewels.");
            model.getParty().randomPartyMemberSay(model, List.of("Looks like business has been good for the Doctor."));
            println("The party gains 100 gold!");
            model.getParty().addToGold(100);
        } else {
            println("You cannot manage to get the lock open.");
            model.getParty().randomPartyMemberSay(model, List.of("Let's leave it. It's just dead weight."));
        }
    }
}
