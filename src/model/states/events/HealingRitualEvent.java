package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Classes;
import util.MyLists;
import util.MyStrings;
import view.MyColors;
import view.sprites.Sprite;
import view.combat.CombatTheme;
import view.combat.GrassCombatTheme;

import java.util.List;

public class HealingRitualEvent extends RitualEvent {
    public HealingRitualEvent(Model model) {
        super(model, MyColors.WHITE);
    }

    @Override
    protected CombatTheme getTheme() {
        return new GrassCombatTheme();
    }

    @Override
    protected boolean runEventIntro(Model model, List<GameCharacter> ritualists) {
        GameCharacter priest = makeRandomCharacter(3);
        priest.setClass(Classes.PRI);
        priest.addToHP(999);
        ritualists.set(0, priest);
        showEventCard("Healing Ritual",
                "There is a small village here. As you stroll through by the huts you see a lot of sick and weak people. " +
                "You continue onward until you reach a little chapel at the end of the road.");
        print("Do you want to go to the chapel (Y) or continue on with your journey (N). ");
        if (!yesNoInput()) {
            return false;
        }
        println("In front of the chapel, a priest is talking to a few ragged villagers. " +
                heOrSheCap(priest.getGender()) +" sends them off with a " +
                "few provisions and then turns to you.");
        model.getLog().waitForAnimationToFinish();
        showExplicitPortrait(model, ritualists.get(0).getAppearance(), "Priest");
        portraitSay("Hello there outsider. What can I do for you?");
        leaderSay("We're just passing through. Say, why are there so many sick people here? It's not contagious I hope.");
        portraitSay("Not at all outsider. The malady affects everyone who is born and grows up in these parts. I came " +
                "here only last year and I try to treat these poor souls. But it's a tricky affliction. Without proper care " +
                "and medication, many suffer and some even die at you ages.");
        leaderSay("Why don't the villagers just move somewhere else?");
        portraitSay("Many live in utter poverty, brought on by generations of individuals ravage by the sickness.");
        leaderSay("What's the cause of the affliction? Is there something in the water, or the air?");
        portraitSay("Yes, perhaps. Actually, me and my fellow priests have been waiting for some time for somebody to come " +
                "so we can perform a cleansing ritual on the land here. There are only " + MyStrings.numberWord(ritualists.size()) +
                " of us you see.");
        model.getLog().waitForAnimationToFinish();
        if (ritualists.size() + model.getParty().size() < 5) {
            portraitSay("Unfortunately there aren't enough of you either for us to perform the ritual. We'll just have " +
                    "to wait a little longer I guess.");
            model.getLog().waitForAnimationToFinish();
            return false;
        }
        portraitSay("Say, there are enough of you to help out with the ritual. Do you think you could?");
        return true;
    }

    @Override
    protected void runEventOutro(Model model, boolean success, int power) {
        if (success) {
            println("The ritual is a success, but you notice no difference really.");
            portraitSay("Wonderful, now these lands have been purged from an evil presence.");
            leaderSay("Really, I can't really say I feel any difference.");
            portraitSay("Oh, I am certain of it. Now please let me heal you.");
            healParty(model);
            portraitSay("Thank you so much for helping us with this problem.");
            println("Each party member gains " + 50*power + " XP!");
            MyLists.forEach(model.getParty().getPartyMembers(), (GameCharacter gc) ->
                model.getParty().giveXP(model, gc, 50*power));
            println("You leave the little village.");
        } else {
            println("The priest seems disappointed.");
            portraitSay("I was certain that would work. Well, we'll just have to try again some time.");
            println("You part ways with the priests.");
        }
    }

    @Override
    public Sprite getCenterSprite() {
        return null;
    }
}
