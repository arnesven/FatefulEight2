package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.FacialExpression;
import model.characters.appearance.InvisibleAppearance;
import model.classes.Classes;
import model.map.UrbanLocation;
import model.races.ElvenRace;
import model.races.Race;
import util.MyLists;

public class InvisibleEvent extends PersonalityTraitEvent {
    public InvisibleEvent(Model model, PersonalityTrait trait, GameCharacter main) {
        super(model, trait, main);
    }

    @Override
    public boolean isApplicable(Model model) {
        return model.getCurrentHex().getLocation() instanceof UrbanLocation;
    }

    @Override
    protected void doEvent(Model model) {
        GameCharacter main = getMainCharacter();
        model.getParty().benchPartyMembers(
                MyLists.filter(model.getParty().getPartyMembers(), gc -> gc != main));
        println(main.getName() + " is browsing the market and comes to a fruit stand.");
        showRandomPortrait(model, Classes.MERCHANT, "Fruitseller");
        portraitSay("Care for an apple " + (main.getGender() ? "madam":"sir") + ". Or a pear perhaps?");
        partyMemberSay(main, "That does sound rather yummy. I think I'll have one... " +
                "Wait, what are those fruit back there?");
        portraitSay("You mean these Ogeon?");
        println("The fruitseller points to a bunch of very bright orange things in a basket.");
        partyMemberSay(main, "Yes, those look absolutely delicious. I'll have one of those.");
        portraitSay("They are, but I'm afraid I can't sell you those " + (main.getGender() ? "madam":"sir") + ".",
                FacialExpression.sad);
        partyMemberSay(main, "Why not?", FacialExpression.disappointed);
        portraitSay("They're known to be poisonous to " + makeRacePlural(main.getRace()) + "!");
        partyMemberSay(main, "Rubbish! I have a belly like an iron pot. I simply must taste it. " +
                "Now bring it here.");
        portraitSay("I'm sorry, but I can't sell it to you. I could lose my license!", FacialExpression.disappointed);
        partyMemberSay(main, "Hmmph! Then I shan't have anything else either!", FacialExpression.disappointed);
        portraitSay("Fine. Come back if you change your mind.");
        println("Another customer approaches the fruit stand and the fruitseller turns " +
                hisOrHer(getPortraitGender()) + " attention away from " + main.getFirstName() + ".");
        print("Do you grab the Ogeon and run? (Y/N) ");
        if (yesNoInput()) {
            main.setAppearance(new InvisibleAppearance(main));
        }
        model.getParty().unbenchAll();
    }

    private String makeRacePlural(Race race) {
        if (race.id() == Race.DWARF.id()) {
            return "dwarves";
        }
        if (race instanceof ElvenRace) {
            return "elves";
        }
        return race.getName().toLowerCase();
    }
}
