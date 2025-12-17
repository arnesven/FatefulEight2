package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.races.Race;
import model.states.DailyEventState;
import util.MyStrings;
import view.subviews.ChangeClassTransitionSubView;
import view.subviews.ChangeHairStyleSubView;
import view.subviews.PortraitSubView;

import java.util.ArrayList;

public class BarbershopEvent extends DailyEventState {
    private static final int COST = 5;
    private final Race barberRace;

    public BarbershopEvent(Model model, Race barberRace) {
        super(model);
        this.barberRace = barberRace;
    }

    public BarbershopEvent(Model model) {
        this(model, Race.randomRace());
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Visit barber", "If your hair needs a trim, there's a barbershop here");
    }

    @Override
    protected void doEvent(Model model) {
        showEventCard("Barber Shop", "You enter a shop...");
        leaderSay("Wait this isn't a shop...");
        CharacterAppearance barberAppearance = PortraitSubView.makeRandomPortrait(Classes.BARBER, barberRace);
        showExplicitPortrait(model, barberAppearance, "Barber");
        portraitSay("This sir, is a barber shop!");
        leaderSay("Uh...");
        portraitSay("A sharp look sir? Or perhaps something more traditional? I'm quick and nimble. " +
                "I can even dye your hair. You won't be disappointed!");
        leaderSay("Maybe... what's the charge?");
        portraitSay("The cost is " + MyStrings.numberWord(COST) +
                " gold, I guarantee you it's worth it! And dare I say, you look like you could need it!");
        randomSayIfPersonality(PersonalityTrait.narcissistic, new ArrayList<>(), "Hey, that's rude!");
        if (COST > model.getParty().getGold()) {
            leaderSay("Sorry, can't afford it...");
            portraitSay("That's unfortunate. I'm afraid I can't give you a discount today. " +
                    "Times are tough enough as they are.");
            leaderSay("I understand.");
            portraitSay("Goodbye sir.");
            return;
        }
        print("Let the barber cut your hair? (Y/N) ");
        if (!yesNoInput()) {
            leaderSay("I think we'll pass on it today.");
            portraitSay("Please come back if you change your mind.");
            return;
        }
        leaderSay("Here's the money.");
        model.getParty().spendGold(COST);
        print("Which party member do you want to give a hair cut?");
        GameCharacter gc = null;
        do {
            gc = model.getParty().partyMemberInput(model, this, model.getParty().getPartyMember(0));
            if (!(gc.getAppearance() instanceof AdvancedAppearance)) {
                print("That character is not eligible for a new hair cut. " +
                        "Do you still want to give a party member a new haircut? (Y/N) ");
                if (!yesNoInput()) {
                    leaderSay("I've changed my mind.");
                    portraitSay("Alright. Here's your money back. " +
                            "Please come back if you change your mind.");
                    model.getParty().earnGold(COST);
                    return;
                }
            } else {
                break;
            }
        } while (true);
        gc.setSpecificClothing(new SimpleTunicPortraitClothing());
        ChangeHairStyleSubView subView = new ChangeHairStyleSubView(model, gc);
        model.setSubView(subView);
        do {
            waitForReturnSilently();
        } while (!subView.isDone());
        GameCharacter charCopy = gc.copy();
        charCopy.setAppearance(subView.getFinalAppearance());
        ChangeClassTransitionSubView.transition(model, subView, gc, charCopy);
        gc.setAppearance(subView.getFinalAppearance());

        print(gc.getName() + " got a new haircut! Press enter to continue.");
        waitForReturn();
        gc.removeSpecificClothing();

        portraitSay("Please come again soon!");
        leaderSay("Goodbye.");
    }

}
