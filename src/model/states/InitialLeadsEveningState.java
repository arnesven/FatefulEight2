package model.states;

import model.Model;
import model.characters.GameCharacter;
import model.journal.JournalEntry;
import model.map.TownLocation;

public class InitialLeadsEveningState extends EveningState {
    public InitialLeadsEveningState(Model model, boolean freeLodging, boolean freeRations) {
        super(model, freeLodging, freeRations, true);
    }

    @Override
    protected void locationSpecificEvening(Model model) {
        super.locationSpecificEvening(model);
        leaderSay("Anybody have any leads? I mean, like jobs or quests?");
        {
            GameCharacter gc = notTheLeader(model);
            model.getParty().partyMemberSay(model, gc, "Well, there's usually some work in towns and castles.");
            leaderSay("Yeah. I think there's a town not far from here. Maybe we should head in that direction.");
        }
        {
            GameCharacter gc = notTheLeader(model);
            model.getParty().partyMemberSay(model, gc, "And I've heard there are ruins that contain riches.");
            leaderSay("Riches? But dangers too right?");
            model.getParty().partyMemberSay(model, gc, "Probably. We should make sure we're ready for that before " +
                    "we get us into more trouble than we can handle.");
            leaderSay("That sounds reasonable. If we could recruit some more party members, " +
                    "get our hands on some good gear, and perhaps train up a bit...");
        }
        {
            GameCharacter gc = notTheLeader(model);
            model.getParty().partyMemberSay(model, gc, "I hear the monks at the temples offer training. For a fee of course.");
            leaderSay("It's always good to get some pointers from somebody who knows the what they're doing.");
        }
        {
            GameCharacter gc = notTheLeader(model);
            model.getMainStory().setupStory(gc);
            TownLocation loc = model.getMainStory().getStartingLocation(model);
            model.getParty().partyMemberSay(model, gc, "Oh, and I have an uncle in " + loc.getTownName() +
                    " who's been asking me to bring in a group of fighters to deal with a problem.");
            leaderSay("What's the trouble?");
            model.getParty().partyMemberSay(model, gc, "Apparently some trouble with the local population of frogmen.");
            leaderSay("Could be interesting... Well, good night everybody.");
        }
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (gc != model.getParty().getLeader()) {
                model.getParty().partyMemberSay(model, gc, "Good night.");
            }
        }
        JournalEntry.printJournalUpdateMessage(model);
    }

    private GameCharacter notTheLeader(Model model) {
        return model.getParty().getRandomPartyMember(model.getParty().getLeader());
    }
}
