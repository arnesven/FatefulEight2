package model.states.events;

import model.Model;
import model.classes.Classes;
import model.states.DailyEventState;

import java.util.List;

public class GuideEvent extends DailyEventState {
    public GuideEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        showRandomPortrait(model, Classes.None, "Guide");
        model.getParty().randomPartyMemberSay(model, List.of("This place is pretty big. I wonder where..."));
        portraitSay("Where the tavern is? Where the general store is? The smith?");
        model.getParty().randomPartyMemberSay(model, List.of("Uhm... can we help you?"));
        portraitSay("No, hehe, but for a few coins I'll gladly show you around. How 'bout it?");
        if (model.getParty().getGold() < 2) {
            decline(model);
            return;
        }
        print("Give the guide 2 gold? (Y/N) ");
        if (!yesNoInput()) {
            decline(model);
            return;
        }

        model.getParty().addToGold(-2);
        portraitSay("Thank you! Now where would you like to go?");
        List<DailyEventState> events = List.of(
                new CaptainEvent(model),
                new SmithEvent(model),
                new ArcherEvent(model),
                new NoblemanEvent(model),
                new PriestEvent(model),
                new CourtWizardEvent(model),
                new ArmoryEvent(model),
                new JesterEvent(model),
                new BarbershopEvent(model));
        int result = multipleOptionArrowMenu(model, 30, 12,
                List.of("Master-at-arms", "Smith", "Archer", "Some Nobles",
                        "Priest", "Court Wizard", "Armory", "Jester", "Barber"));
        events.get(result).doTheEvent(model);

    }

    private void decline(Model model) {
        model.getParty().randomPartyMemberSay(model, List.of("I'm sure we'll find it ourselves."));
        portraitSay("Of course. You lot look like you're on top of things.");
    }
}
