package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Classes;
import model.classes.Skill;
import model.items.weapons.BowWeapon;
import model.items.weapons.ShortBow;
import model.states.ArcheryState;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.List;

public class ArcheryRangeEvent extends DailyEventState {
    private static final int COST_TO_PLAY = 5;
    private static final int WIN_SUM = 30;

    public ArcheryRangeEvent(Model model) {
        super(model);
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Visit archery range", "There's an archery range just outside of town");
    }

    @Override
    protected void doEvent(Model model) {
        showEventCard("Archery Range", "There is an archery range on the outskirts of town. The marksman there approaches you.");
        // TODO: Make real portrait
        showRandomPortrait(model, Classes.MAR, "Marksman");
        portraitSay("Hello there. Want to practice your marksmanship? For 5 gold I'll lend you " +
                "a bow if you don't have one and some arrows.");
        model.getParty().randomPartyMemberSay(model, List.of("I don't know... Do we really have time for this?"));
        portraitSay("Let's make it more interesting. If you hit the bull's eye, I'll give you " + WIN_SUM + " gold. Deal?");
        while (model.getParty().getGold() >= COST_TO_PLAY) {
            print("Do you pay " + COST_TO_PLAY + " gold to play? (Y/N) ");
            if (yesNoInput()) {
                model.getParty().spendGold(COST_TO_PLAY);
                print("Which party member do you want to use? ");
                GameCharacter shooter = model.getParty().partyMemberInput(model, this, model.getParty().getPartyMember(0));
                BowWeapon bowToUse = ArcheryState.getCharactersBowOrDefault(shooter);
                ArcheryState archeryState = new ArcheryState(model, shooter, bowToUse, ArcheryState.FAR_DISTANCE);
                archeryState.setShots(3);
                archeryState.run(model);
                List<Integer> results = archeryState.getDetailedResults();
                if (results.contains(ArcheryState.getPointsForBullseye())) {
                    model.getParty().randomPartyMemberSay(model, List.of("Bullseye!", "Right on target!",
                            "Dead-center.", "Can't get much more in the middle than that.",
                            "Nice shot!"));
                    println("The marksman looks rather surprised.");
                    portraitSay("Nice shot indeed. Here's your gold.");
                    println("The party receives " + WIN_SUM + " gold.");
                    model.getParty().earnGold(WIN_SUM);
                    portraitSay("If you'll excuse me, I have to go now. Please come and" +
                            " see me again some time.");
                    break;
                } else if (results.contains(ArcheryState.getPointsForRing(1))) {
                    partyMemberSay(shooter, "Aww, so close!");
                    portraitSay("Still pretty impressive though. I'll give you your money back.");
                    println("You got " + COST_TO_PLAY + " gold back from the marksman.");
                    model.getParty().earnGold(COST_TO_PLAY);
                } else {
                    partyMemberSay(shooter, MyRandom.sample(List.of("Hmm. Not my best performance.",
                            "That's a miss.", "Aaw, I thought I'd do better.")));
                    portraitSay("That's too bad. But you can always try again. Whaddaya say?");
                }
            } else {
                break;
            }
        }
        model.getParty().randomPartyMemberSay(model, List.of("Maybe we can come back later."));
    }
}
