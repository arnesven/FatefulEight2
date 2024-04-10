package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.journal.JournalEntry;
import model.map.TownLocation;
import model.map.UrbanLocation;
import model.races.AllRaces;
import model.races.Race;
import model.states.DailyEventState;
import model.tasks.Bounty;
import model.tasks.BountyDestinationTask;
import util.MyRandom;
import view.subviews.PortraitSubView;
import view.subviews.SubView;
import view.subviews.WantedPosterSubView;

import java.util.ArrayList;
import java.util.List;

public class WantedPosterEvent extends DailyEventState {
    public WantedPosterEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        SubView previous = model.getSubView();
        println("You pass by a poster hanging on the wall of a building.");
        String townName = null;
        if (model.getCurrentHex().getLocation() instanceof TownLocation) {
            townName = ((TownLocation) model.getCurrentHex().getLocation()).getTownName();
        } else {
            townName = ((UrbanLocation)model.getCurrentHex().getLocation()).getPlaceName();
        }
        if (model.getParty().getNotoriety() >= 50) {
            seeOwnFace(model, previous, townName);
        } else {
            generateBountyEvent(model, previous, townName);
        }

    }

    private void seeOwnFace(Model model, SubView previous, String town) {
        leaderSay("Oh uh... I know that face.");
        WantedPosterSubView subView = new WantedPosterSubView(model.getSubView(),
                model.getParty().getLeader(), model.getParty().size() > 1, 100, town);
        model.setSubView(subView);
        if (model.getParty().size() > 1) {
            GameCharacter other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
            partyMemberSay(other, "That's a good picture of you, " + model.getParty().getLeader().getFirstName() + ". " +
                    "How does it feel to be a wanted " +
                    (model.getParty().getLeader().getGender() ? "woman" : "man") + "? 'Dead or Alive' " +
                    "Good heavens. Well, it's been nice knowing you.");
            leaderSay("Don't get so cocky " + other.getName() + ", you're wanted too. " +
                    "Look here, it says: 'and companions'");
            partyMemberSay(other, "Rats...");
        }
        print("Do you tear down the poster? (Y/N)");
        if (yesNoInput()) {
            model.setSubView(previous);
            int reduction = Math.min(10, model.getParty().getNotoriety());
            println("Your notoriety was reduced by " + reduction + ".");
            model.getParty().addToNotoriety(-reduction);
            if (MyRandom.rollD6() < 3) {
                new ConstableEvent(model).doEvent(model);
            }
            randomSayIfPersonality(PersonalityTrait.jovial, List.of(),
                    MyRandom.sample(List.of("Are you going to frame that?",
                            "Save that for when I go to the outhouse later!")));
            leaderSay("I'll just put this in the trash.");
        } else {
            leaderSay("There are people watching, let's just keep walking and pretend we're somebody else.");
        }

    }


    private void generateBountyEvent(Model model, SubView previous, String townName) {
        String townPlaceName = ((UrbanLocation)model.getCurrentHex().getLocation()).getPlaceName();
        Bounty bounty = Bounty.generate(model, townPlaceName);
        WantedPosterSubView subView = new WantedPosterSubView(model.getSubView(),
                bounty.getFullName(),
                bounty.getAppearance(), Classes.None, bounty.getWithCompanions(), bounty.getReward(), townName);
        model.setSubView(subView);

        leaderSay("Hmm... a bounty.");
        boolean didSay = randomSayIfPersonality(PersonalityTrait.cowardly, new ArrayList<>(),
                "I wouldn't pick a fight with " + himOrHer(bounty.getGender()) + ".");
        if (didSay) {
            leaderSay("You say that just from looking at " + hisOrHer(bounty.getGender()) + " face?");
        }
        randomSayIfPersonality(PersonalityTrait.anxious, new ArrayList<>(),
                "'And companions' what does that mean, two little henchmen or a whole gang of bandits?");

        if (model.getParty().size() > 1) {
            GameCharacter other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
            partyMemberSay(other, "Dead or alive, " + bounty.getReward() + " gold. Could be worth it.");
            leaderSay("Perhaps...");
        }
        print("Do you take the poster? (Y/N) ");
        if (yesNoInput()) {
            model.getTutorial().bounties(model);
            model.getParty().addDestinationTask(new BountyDestinationTask(bounty));
            JournalEntry.printJournalUpdateMessage(model);
            leaderSay("We'll have to ask around a bit to find " + himOrHer(bounty.getGender()) + ".");
        } else {
            leaderSay("But why risk it? We've got other prospects. Moving on!");
        }
    }
}
