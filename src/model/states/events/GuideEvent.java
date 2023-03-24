package model.states.events;

import model.Model;
import model.states.DailyEventState;
import view.subviews.ArrowMenuSubView;

import java.util.List;

public class GuideEvent extends DailyEventState {
    public GuideEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        model.getParty().randomPartyMemberSay(model, List.of("This place is pretty big. I wonder where..."));
        println("Guide: \"Where the tavern is? Where the general store is? The smith?\"");
        model.getParty().randomPartyMemberSay(model, List.of("Uhm... can we help you?"));
        println("Guide: \"No, hehe, but for a few coins I'll gladly show you around. How 'bout it?\"");
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
        println("Guide: \"Thank you! Now where would you like to go?\"");
        List<DailyEventState> events = List.of(
                new CaptainEvent(model),
                new SmithEvent(model),
                new ArcherEvent(model),
                new NoblemanEvent(model),
                new PriestEvent(model),
                new CourtWizardEvent(model),
                new ArmoryEvent(model),
                new JesterEvent(model));
        final DailyEventState[] selectedEvent = {null};
        model.setSubView(new ArrowMenuSubView(model.getSubView(),
                List.of("Master-at-arms", "Smith", "Archer", "Some Nobles", "Priest", "Court Wizard", "Armory", "Jester"),
                28, 12, ArrowMenuSubView.NORTH_WEST) {
            @Override
            protected void enterPressed(Model model, int cursorPos) {
                selectedEvent[0] = events.get(cursorPos);
                model.setSubView(getPrevious());
            }
        });
        waitForReturn();
        selectedEvent[0].doTheEvent(model);

    }

    private void decline(Model model) {
        model.getParty().randomPartyMemberSay(model, List.of("I'm sure we'll find it ourselves."));
        println("Guide: \"Of course. You lot look like you're on top of things.\"");
    }
}
