package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.characters.appearance.CharacterEyes;
import model.classes.Classes;
import model.map.UrbanLocation;
import util.MyLists;
import util.MyRandom;
import view.party.CharacterCreationView;
import view.subviews.DoublePortraitSubView;
import view.subviews.PortraitSubView;
import view.subviews.SubView;

import java.util.ArrayList;
import java.util.List;

public class DoppelgangerEvent extends PersonalityTraitEvent {
    public DoppelgangerEvent(Model model, GameCharacter mainCharacter) {
        super(model, PersonalityTrait.narcissistic, mainCharacter);
    }

    @Override
    public boolean isApplicable(Model model) {
        return model.getParty().size() > 2 && model.getCurrentHex().getLocation() instanceof UrbanLocation &&
                getMainCharacter().getAppearance() instanceof AdvancedAppearance;
    }

    @Override
    protected void doEvent(Model model) {
        GameCharacter main = getMainCharacter();
        GameCharacter other = null;
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (gc != main && gc != model.getParty().getLeader()) {
                other = gc;
                break;
            }
        }
        if (other == null) {
            new NoEventState(model).doEvent(model);
            return;
        }
        doEvent(model, main, model.getParty().getLeader(), other);
    }

    private void doEvent(Model model, GameCharacter main, GameCharacter leader, GameCharacter other) {
        println("While going about your business today, the party members decide to split up to get their chores done more quickly.");
        List<GameCharacter> toBench = MyLists.filter(model.getParty().getPartyMembers(),
                (GameCharacter gc) -> gc != other && gc != leader);
        model.getParty().benchPartyMembers(toBench);
        println(other.getName() + " and " + leader.getName() + " are casually strolling down a street when suddenly...");
        model.getLog().waitForAnimationToFinish();
        AdvancedAppearance doppelGanger = (AdvancedAppearance) main.getAppearance().copy();
        changeSlightly(doppelGanger);
        doppelGanger.setClass(Classes.None);
        showExplicitPortrait(model, doppelGanger, main.getName());
        leaderSay(main.getFirstName() + ", weren't you going to do some shopping in the market?");
        println("At first, " + main.getFirstName() + " doesn't seem to react.");
        portraitSay("Excuse me, are you talking to me?");
        partyMemberSay(other, "What's wrong " + main.getFirstName() + "? And, uh, why did you change your clothes?");
        println(main.getFirstName() + " looks completely bewildered.");
        portraitSay("I'm sorry, I think you have me confused with somebody else.");
        println("The person turns and walks away down the street. " + leader.getFirstName() + " and " +
                other.getFirstName() + " stand in silence as they watch the person enter into a small house.");
        model.getLog().waitForAnimationToFinish();
        removePortraitSubView(model);
        leaderSay("That was strange.");
        partyMemberSay(other, "Very. Was " + heOrShe(doppelGanger.getGender()) +
                " just playing a prank on us. Or maybe, could it be... It wasn't " + main.getFirstName() + " at all?");
        leaderSay("It, can't have been. Even though " + heOrShe(doppelGanger.getGender()) +
                " looked exactly like " + main.getFirstName() + ", I think it was a different person, a doppelganger.");
        partyMemberSay(other, "Uncanny... let's head back to the market and tell the rest of the party.");
        println("As you come back to the town center, you quickly spot " + main.getFirstName() + " among the market stalls.");
        model.getLog().waitForAnimationToFinish();
        model.getParty().unbenchAll();
        partyMemberSay(other, main.getFirstName() + ", you're not going to believe this. We met a person who " +
                "looks exactly like you.");
        partyMemberSay(main, "Really? How disturbing.");
        leaderSay("Yes. Both me and " + other.getFirstName() + " were so sure it was you, we thought " +
                "you were playing a prank on us!");
        partyMemberSay(main, "I think you're probably exaggerating. I've never met anybody with an " +
                "appearance as beautiful and unique as mine.");
        partyMemberSay(other, "No no, it's true! " + heOrSheCap(doppelGanger.getGender()) +
                " is a carbon copy, I swear!");
        leaderSay("Why don't you go look for yourself? We know where " + heOrShe(doppelGanger.getGender()) + " lives.");
        partyMemberSay(main, "Alright. This is probably a waste of time, but I'll do it to put the matter to rest.");
        println("You return to the street where " + leader.getFirstName() + " and " + other.getFirstName() +
                " met the doppelganger.");
        leaderSay("It's that house over there. Why don't you go over there and knock on the door. We'll hang back.");
        model.getLog().waitForAnimationToFinish();
        model.getParty().benchPartyMembers(List.of(main));
        println(main.getFirstName() + " wanders over to the house and knocks on the door. The doppelganger opens the door. You can see " +
                "the two of them talking for a bit, then " + main.getFirstName() + " steps inside the house, leaving the door ajar.");
        partyMemberSay(other, "What's going on?");
        leaderSay("I don't know...");
        println("You walk up to the house. As you approach the door, you hear " + main.getFirstName() + " voice.");
        printQuote(main.getName(), leader.getFirstName() + ", are you out there?");
        leaderSay("Yeah. What's going on " + main.getFirstName() + "?");
        printQuote(main.getName(), "This person looks nothing like me, and I'm surprised and offended that " +
                "you consider " + himOrHer(doppelGanger.getGender()) + " to be a doppelganger to me.");
        leaderSay("Seriously, " + main.getFirstName() + ", the likeness is obvious.");
        printQuote(main.getName(), "So here's the deal, this person has been good enough to assist me in setting up a little test for you. " +
                "We're going to come out, and you will have to tell us which one is the real me. If you get it wrong, I'm leaving the party.");
        leaderSay("Don't be ridiculous!");
        printQuote(main.getName(), "Too late, we're doing this!");
        println("The two " + main.getFirstName() + "s come out from the house. They look very much alike, even their clothes are the same.");
        CharacterAppearance realPerson = main.getAppearance().copy();
        realPerson.setClass(Classes.None);
        CharacterAppearance left = realPerson;
        CharacterAppearance right = doppelGanger;
        if (MyRandom.flipCoin()) {
            left = doppelGanger;
            right = realPerson;
        }
        SubView previousSubView = model.getSubView();
        DoublePortraitSubView subView = new DoublePortraitSubView(model.getSubView(), left, right);
        model.setSubView(subView);
        println("Which one is the real " + main.getFullName() + "?");
        int count = multipleOptionArrowMenu(model, 24, 26, List.of("Left person", "Right person", "Refuse to choose"));
        if (count == 0) {
            leaderSay("I think it's the left one.");
            makeGuess(model, left, realPerson, subView, previousSubView);
        } else if (count == 1) {
            leaderSay("I think it's the right one.");
            makeGuess(model, right, realPerson, subView, previousSubView);
        } else {
            leaderSay("I'm not giving in to this. " + main.getFirstName() + " I demand you reveal yourself!");
            model.getLog().waitForAnimationToFinish();
            subView.portraitSay(model, this, realPerson, main.getName(),
                    "Fine! But I'm still offended!");
            main.addToAttitude(leader, -10);
            model.getLog().waitForAnimationToFinish();
            model.setSubView(previousSubView);
            model.getParty().unbenchAll();
        }
    }

    private void makeGuess(Model model, CharacterAppearance app, CharacterAppearance realPerson, DoublePortraitSubView subView,
                           SubView previous) {
        boolean guessedWrong = false;
        if (app == realPerson) {
            subView.portraitSay(model, this, realPerson, getMainCharacter().getName(),
                    "See! No problem telling us apart.");
        } else {
            subView.portraitSay(model, this, realPerson, getMainCharacter().getName(),
                    "Wrong! I can't believe you would confuse me with somebody so different from me. " +
                            "That's it " + model.getParty().getLeader().getFirstName() + ", I'm leaving.");
            guessedWrong = true;
        }
        model.getLog().waitForAnimationToFinish();
        model.setSubView(previous);
        model.getParty().unbenchAll();

        if (guessedWrong) {
            new PartyMemberWantsToLeaveEvent(model).wantsToLeave(model, getMainCharacter(), false);
        } else {
            leaderSay("I still think you are very similar though.");
            partyMemberSay(getMainCharacter(), "I am unique, end of story.");
            leaderSay("Whatever...");
        }
    }

    private void changeSlightly(AdvancedAppearance doppelGanger) {
        int roll = MyRandom.rollD6();
        if (roll <= 2) {
            doppelGanger.setNose(CharacterCreationView.noseSet[MyRandom.randInt(CharacterCreationView.noseSet.length)]);
        } else if (roll <= 4) {
            int newMouth;
            do {
                newMouth = CharacterCreationView.mouthSet[MyRandom.randInt(CharacterCreationView.mouthSet.length)];
            } while (PortraitSubView.isBeardyMouth(newMouth) && doppelGanger.getGender()); // beardy lady
            doppelGanger.setMouth(newMouth);
        } else {
            doppelGanger.setEyes(CharacterEyes.allEyes[MyRandom.randInt(CharacterEyes.allEyes.length)]);
        }
        doppelGanger.reset();
    }
}
