package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Classes;
import model.races.Race;
import model.states.DailyEventState;
import util.MyStrings;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.CombatTheme;
import view.subviews.MansionTheme;

import java.util.List;

public class PsychicRitual extends RitualEvent {
    private static final Sprite SLEEPER = new Sprite32x32("sleeperritual", "ritual.png", 0x11,
            MyColors.BLACK, MyColors.BEIGE, Race.NORTHERN_HUMAN.getColor(), MyColors.LIGHT_RED);

    public PsychicRitual(Model model) {
        super(model, MyColors.BLUE);
    }

    @Override
    protected CombatTheme getTheme() {
        return new MansionTheme();
    }

    @Override
    protected boolean runEventIntro(Model model, List<GameCharacter> ritualists) {
        GameCharacter magician = makeRandomCharacter(3);
        magician.setClass(Classes.MAG);
        magician.addToHP(999);
        ritualists.set(0, magician);
        println("You see several mages enter a house nearby. Your curiosity gets the better of you and you wander inside. " +
                "You come into a large room. In the middle stands a bed, a child sleeping soundly in it.");
        println("There are " + MyStrings.numberWord(ritualists.size()) + " mages here who are about to do a psychic ritual. " +
                "They are looking for some extra mages to join them in performing it.");
        if (ritualists.size() + model.getParty().size() < 5) {
            println("Unfortunately you do not have enough party members to join the ritual.");
            model.getLog().waitForAnimationToFinish();
            return false;
        }
        showExplicitPortrait(model, ritualists.get(0).getAppearance(), ritualists.get(0).getName());
        portraitSay("This girl has been sleeping for many months now. Her parents have tried everything from doctors to priests. " +
                "They're desperate. We've offered to perform a ritual in which we will enter her dreams and try to wake her " +
                "up from within. We're a little short on mages at the moment. Do you think you could help?");
        return true;
    }

    @Override
    protected void runEventOutro(Model model, boolean success, int power) {
        if (success) {
            println("Through a shared dream state you manage to find the little girl in one of her own dreams and " +
                    "lead her to the exit. Back in the real world, she awakes. Her parents rush to her side and rejoice.");
            portraitSay("She'll be weak for some time, but she'll recover. Ah, my friends. Allow me to heal you.");
            healParty(model);
            portraitSay("Thank you so much for helping us with this problem. We really were at our wits end with " +
                    "how to get her to wake up.");
            println("Each party member gains " + 50*power + " XP!");
            for (GameCharacter gc : model.getParty().getPartyMembers()) {
                model.getParty().giveXP(model, gc, 50*power);
            }
            println("You leave the house of the sleepy girl.");
        } else {
            println("The girl remains fast asleep, despite the best efforts of the mages.");
            portraitSay("Well, we'll just have to try something else I guess.");
            println("You part ways with the mages.");
        }
    }

    @Override
    public Sprite getCenterSprite() {
        return SLEEPER;
    }

}
