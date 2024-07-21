package model.states;

import model.Model;
import model.states.dailyaction.HireGuideAction;
import util.MyLists;
import util.MyRandom;
import util.MyStrings;

import java.util.ArrayList;
import java.util.List;

public abstract class GuideGivesChoiceState extends GameState {
    private static final int MAX_ATTEMPTS = 5;
    private final DailyEventState event1;
    private DailyEventState event2 = null;
    private DailyEventState selectedEvent;

    public GuideGivesChoiceState(Model model) {
        super(model);
        this.event1 = generateEvent(model);
        if (event1.isGuidable()) {
            DailyEventState tmpEvent = null;
            for (int i = 0; i < MAX_ATTEMPTS; ++i) {
                tmpEvent = generateEvent(model);
                if (tmpEvent.isGuidable() && !event1.getGuideData().getName().equals(tmpEvent.getGuideData().getName())) {
                    event2 = tmpEvent;
                    break;
                }
            }
        }
        this.selectedEvent = event1;
    }

    protected abstract DailyEventState generateEvent(Model model);

    @Override
    public GameState run(Model model) {
        if (model.getParty().getGuide() == 0 || !event1.isGuidable()) {
            return model.getCurrentHex().getDailyActionState(model);
        }
        leaderSay("Hey guide, " + MyRandom.sample(List.of("you know where we are?",
                "anything interesting around here?", "Do you know these parts?",
                "can you tell me anything about this place?")));
        String option2;
        if (event2 != null) {
            HireGuideAction.guideSay(model, this, "Yes, of course! " + MyStrings.capitalize(event1.getGuideData().getDescription()) +
                    ". And " + event2.getGuideData().getDescription().toLowerCase() + ".");
            option2 = event2.getGuideData().getName();
        } else {
            HireGuideAction.guideSay(model, this, "Yes, of course! " + MyStrings.capitalize(event1.getGuideData().getDescription()));
            option2 = "Go somewhere else";
        }
        print("What would you like to do? ");
        List<String> options = new ArrayList<>(List.of(event1.getGuideData().getName(), option2));
        if (event2 != null) {
            options.add("Go somewhere else");
        }
        int choice = multipleOptionArrowMenu(model, 24, 4, options);
        if (choice == 1) {
            selectedEvent = event2;
        } else if (choice == 2) {
            selectedEvent = null;
        }

        if (selectedEvent != null) {
            leaderSay("Let's " + selectedEvent.getGuideData().getName().toLowerCase() + ".");
            HireGuideAction.guideSay(model, this, "Alright, just follow me.");
        } else {
            leaderSay("I think we'll pass on that today.");
            HireGuideAction.guideSay(model, this, "That's fine, we'll avoid that.");
            selectedEvent = generateOtherEvent(model);
        }

        return model.getCurrentHex().getDailyActionState(model);
    }

    private DailyEventState generateOtherEvent(Model model) {
        for (int i = 0; i < 100; ++i) {
            DailyEventState tmp = generateEvent(model);
            if (!tmp.isGuidable() || !tmp.getGuideData().getName().equals(event1.getGuideData().getName())) {
                return tmp;
            }
        }
        return generateEvent(model);
    }

    public DailyEventState getSelectedEvent() {
        return selectedEvent;
    }
}
