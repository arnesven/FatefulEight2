package model.states.events;

import model.Model;
import model.Party;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.Classes;
import model.states.DailyEventState;

import java.util.ArrayList;
import java.util.List;

public class StarvingFarmerEvent extends DailyEventState {
    private static final int FOOD_AMOUNT = 5;
    private static final int GOLD_AMOUNT = 3;

    public StarvingFarmerEvent(Model model) {
        super(model);
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Visit starving farmers", "I know of a farm, not far from here. " +
                "The people there are very poor. If you could help them, they would appreciate it very much");
    }

    @Override
    public String getDistantDescription() {
        return "Two farmers";
    }

    @Override
    protected void doEvent(Model model) {
        showEventCard("The party comes to a farmstead. Working in a nearby field is a man and a woman, both look " +
                "pale and thin.");
        showRandomPortrait(model, Classes.FARMER, "Farmer");
        portraitSay("Greetings traveller.");
        leaderSay("Hello. How go things out here on the countryside.");
        portraitSay("Not too well I'm afraid. We've had a bad year, or a string of bad years really.");
        if (model.getParty().getFood() <= FOOD_AMOUNT && model.getParty().getGold() <= GOLD_AMOUNT) {
            sayDontShare(model);
            println("You leave the farm.");
            return;
        }
        GameCharacter other = null;
        if (model.getParty().size() > 1) {
            other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
            println(other.getFirstName() + " comes up next to you.");
            partyMemberSay(other, "We could share a little food or gold with these people. It only seems right.");
        }

        List<String> options = new ArrayList<>();
        if (model.getParty().getFood() >= FOOD_AMOUNT) {
            options.add("Share " + FOOD_AMOUNT + " food");
        }
        if (model.getParty().getGold() >= GOLD_AMOUNT) {
            options.add("Share " + GOLD_AMOUNT + " gold");
        }
        options.add("Don't share anything");
        int selected = multipleOptionArrowMenu(model, 24, 24, options);
        if (selected == options.size()-1) {
            sayDontShare(model);
            removePortraitSubView(model);
            if (model.getParty().size() > 1) {
                println("As you leave the farm, you hear " + other.getFirstName() +
                        " muttering to " + himOrHer(other.getGender()) + "self.");
                partyMemberSay(other, "I feel a little sick...#");
                for (GameCharacter gc : model.getParty().getPartyMembers()) {
                    if (gc != model.getParty().getLeader() &&
                            !gc.hasPersonality(PersonalityTrait.stingy)) {
                        gc.addToAttitude(model.getParty().getLeader(), -5);
                        println(gc.getName() + "'s attitude towards " + model.getParty().getLeader().getFirstName() + " has worsened.");
                    }
                }
            }
        } else if (options.get(selected).contains("gold")) {
            portraitSay("Bless you traveller, this will help us more than you can imagine!");
            leaderSay("I'm just glad we can help.");
        } else {
            leaderSay("Here, take some of our rations. They're nothing fancy, but they fill up the belly.");
            portraitSay("Thank you traveller, you've done a good deed today.");
            leaderSay("I'm just glad we can help.");
        }
        println("You leave the farm.");
    }

    private void sayDontShare(Model model) {
        leaderSay("That's terrible to hear. I'm afraid we're not too well off ourselves. We have nothing to share.");
        portraitSay("Don't worry about us. Somehow we'll get by.");
        leaderSay("Best of luck to you.");
    }
}
