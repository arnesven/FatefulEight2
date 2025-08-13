package model.states.events;

import model.GameStatistics;
import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.map.TownLocation;
import model.states.GameState;
import util.MyLists;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.List;

public class FamousPainterEvent extends PersonalityTraitEvent {
    private static final int ENTRANCE_FEE = 2;

    public FamousPainterEvent(Model model, PersonalityTrait personalityTrait, GameCharacter mainCharacter) {
        super(model, personalityTrait, mainCharacter);
    }

    @Override
    public boolean isApplicable(Model model) {
        return model.getParty().size() > 1 && model.getCurrentHex().getLocation() instanceof TownLocation &&
                model.getParty().getGold() >= ENTRANCE_FEE * 2;
    }

    @Override
    protected void doEvent(Model model) {
        GameCharacter main = getMainCharacter();
        GameCharacter leader = model.getParty().getLeader();
        List<GameCharacter> others = MyLists.filter(model.getParty().getPartyMembers(),
                gc -> gc != leader && gc != main);
        model.getParty().benchPartyMembers(others);
        CharacterAppearance artist = PortraitSubView.makeRandomPortrait(Classes.ARTIST);
        String title = artist.getGender() ? "Ms":"Mr";
        String name = GameState.randomFirstName(artist.getGender()) + " Bouvier";
        println("As " + leader.getFirstName() + " and " + main.getFirstName() + " walk down the street, " +
                "they pass by a boutique with large windows. In front of the boutique a sign has been placed, it reads: " +
                "'Exclusive Art Exhibition - By " + title + " " + name +
                " - only this week. - Entrance: " + ENTRANCE_FEE + " gold'");
        leaderSay("I think I've heard of the artist. " + heOrSheCap(artist.getGender()) + " is pretty famous around these parts.");
        partyMemberSay(main, name + "... The name does sound familiar. You know, I think I actually met " +
                himOrHer(artist.getGender()) + " once. It was a long time ago. I remember " +
                hisOrHer(artist.getGender()) + " paintings were quite good.");
        int cost = ENTRANCE_FEE * 2;
        leaderSay("We should check out the exhibition!");
        partyMemberSay(main, "Yes. Why not?");
        print("Do you pay " + cost + " gold to view the art exhibition? (Y/N) ");
        if (!yesNoInput()) {
            partyMemberSay(main, "Now is not the time. Maybe I'll get the opportunity some time in the future.");
            model.getParty().unbenchAll();
            return;
        }
        model.getParty().spendGold(cost);
        println("The boutique is not large but it still holds about a dozen canvases. There are a few other patrons " +
                "quietly inspecting the pieces of art. You take your time to look at them yourself.");
        leaderSay("Is this the price down here?");
        partyMemberSay(main, "550 gold? Can that really be right?");
        leaderSay("You better not sneeze on any of these paintings!");
        println(main.getFirstName() + " keeps looking at the paintings, from a safe distance, when suddenly, " +
                "somebody comes up from behind.");
        model.getLog().waitForAnimationToFinish();
        showExplicitPortrait(model, artist, name);
        portraitSay(main.getName() + "? Is it really you?");
        partyMemberSay(main, "Yes... It's me. Good to see you again. How many years has it been?");
        portraitSay("Sixteen years, almost to the day.");
        partyMemberSay(main, "You must have a good memory!");
        portraitSay("How could I ever forget what you did for me?");
        partyMemberSay(main, "...what I did for you?");
        portraitSay("I was a young and ambitious painter. I was selling my paintings...");
        partyMemberSay(main, "...off the back of a wagon! Yes now I remember. I really liked your " +
                "style, even back then. I bought one of them I think. But it couldn't have been much money? " +
                "I can't see that it would have helped you that much.");
        portraitSay("Oh no, it was a small sum, probably like ten gold or so. But it wasn't your money that helped me. " +
                "You see, up until that point nobody had believed in me, apart from myself that is. And I was about to give up. " +
                "I figured my mother had been right when she told me I could never make a living as a painter.");
        partyMemberSay(main, "But you did...");
        portraitSay("Thanks to you. You told me my work was really good. That my style was unique and that I should " +
                "pursue my dream to become a famous painter no matter what.");
        leaderSay("That does sound like you " + main.getFirstName() + ".");
        partyMemberSay(main, "Yeah... maybe...");
        portraitSay("After that day, I put all other distractions out of my mind and never doubted my own potential again. " +
                "Pretty soon I wasn't selling my pieces of a wagon any more, but in cafes and shops. My art was noticed by " +
                "the rich and powerful. Slowly I began making a name for myself, but I never forgot my encounter with you. " +
                "You were the only one who believed in me when I first started out. For that, I will be forever grateful.");
        leaderSay(main.getFirstName() + "!");
        partyMemberSay(main, "I, I don't know what to say...");
        portraitSay("Sorry to embarrass you like this in front of your friend. But please, let me return the favor now.");
        partyMemberSay(main, "What do you mean?");
        portraitSay("Take this.");
        println("The artist hands you a heavy bag. It's obviously filled with coins.");
        partyMemberSay(main, "I don't know if I can...");
        leaderSay("'find the words to thank you' I'm sure is what " + main.getFirstName() + " was about to say. " +
                "We humbly accept this gift!");
        partyMemberSay(main, "Yes thank you...");
        println("The party receives 100 gold!");
        model.getParty().earnGold(100);
        portraitSay("Now please, enjoy the exhibition, I won't bother you any more.");
        model.getLog().waitForAnimationToFinish();
        removePortraitSubView(model);
        println("The artist walks away. You spend a little more time at the exhibition, but then leave the boutique.");
        leaderSay("Good deeds huh?");
        partyMemberSay(main, "Good deeds.");
        leaderSay("You don't happen to still have that painting somewhere, do you?");
        partyMemberSay(main, "I'm pretty sure I don't.");
        leaderSay("Don't tell me you sold it for about 10 gold.");
        partyMemberSay(main, "No.");
        leaderSay("That's a relief.");
        partyMemberSay(main, "I'm pretty sure I gave it away.");
        leaderSay("Of course you did.");
        model.getLog().waitForAnimationToFinish();
        model.getParty().unbenchAll();
    }
}
