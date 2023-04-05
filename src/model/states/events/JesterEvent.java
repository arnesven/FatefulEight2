package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Classes;
import model.races.Race;
import model.states.DailyEventState;

public class JesterEvent extends DailyEventState {
    private final String fullName;
    private final String shortName;
    private Race race;

    public JesterEvent(Model model, String fullName, String shortName) {
        super(model);
        this.fullName = fullName;
        this.shortName = shortName;
        this.race = Race.ALL;
    }

    public JesterEvent(Model model) {
        this(model, "Court Jester", "jester");
    }

    public void setRace(Race race) {
        this.race = race;
    }

    @Override
    protected void doEvent(Model model) {
        showRandomPortrait(model, Classes.BRD, race, fullName);
        println("The " + fullName.toLowerCase() + " is not only a funny fellow, but has the " +
                "voice of an angel. He sings a lovely ballad of a long " +
                "forgotten kingdom and the romance between an elf prince " +
                "and a human princess. The party feels much refreshed " +
                "when going to bed tonight.");
        println("Each character recovers 1 SP.");
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            gc.addToSP(1);
        }
        print("The " + shortName + " is kind enough to offer to train you in the ways of being a Bard, ");
        ChangeClassEvent change = new ChangeClassEvent(model, Classes.BRD);
        change.areYouInterested(model);
    }
}
