package model.states.events;

import model.Model;
import model.classes.Classes;
import model.races.Race;
import model.states.DailyEventState;
import util.MyLists;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class GuideEvent extends DailyEventState {
    private final int cost;
    private final Race guideRace;

    public interface GuideTalkInterface {
        void guideTalk(String line);
    }

    public GuideEvent(Model model, int cost, Race guideRace) {
        super(model);
        this.cost = cost;
        this.guideRace = guideRace;
    }

    public GuideEvent(Model model, int cost) {
        this(model, cost, Race.randomRace());
    }

                      @Override
    protected void doEvent(Model model) {
        showRandomPortrait(model, Classes.None, guideRace, "Guide");
        model.getParty().randomPartyMemberSay(model, List.of("This place is pretty big. I wonder where..."));
        portraitSay("Where the tavern is? Where the general store is? The smith?");
        model.getParty().randomPartyMemberSay(model, List.of("Uhm... can we help you?"));
        portraitSay("No, hehe, but for a few coins I'll gladly show you around. How 'bout it?");
        model.getTutorial().guides(model);
        if (model.getParty().getGold() < cost) {
            decline(model);
            return;
        }
        print("Give the guide " + cost + " gold? (Y/N) ");
        if (!yesNoInput()) {
            decline(model);
            return;
        }

        model.getParty().spendGold(cost);
        portraitSay("Thank you! Now where would you like to go?");
        leaderSay("You tell me. What are the sights?");
        chooseFromGuidableEvents(model, this::portraitSay);
    }

    private void decline(Model model) {
        model.getParty().randomPartyMemberSay(model, List.of("I'm sure we'll find it ourselves."));
        portraitSay("Of course. You lot look like you're on top of things.");
    }

    public void chooseFromGuidableEvents(Model model, GuideTalkInterface talkFunc) {
        List<DailyEventState> events = new ArrayList<>();
        int numberOfEvents = MyRandom.randInt(4, 6);
        int tries = 0;
        while (events.size() < numberOfEvents && tries < 100) {
            DailyEventState event = model.getCurrentHex().getLocation().generateEvent(model);
            if (event.isGuidable() && !MyLists.any(events,
                    (DailyEventState ev) -> ev.getGuideData().getName().equals(event.getGuideData().getName()))) {
                events.add(event);
            }
            tries++;
        }

        if (events.isEmpty()) {
            talkFunc.guideTalk("Actually... I can't think of a single thing!");
            leaderSay("Well that's disappointing!");
            return;
        }

        talkFunc.guideTalk("Let's see... ");
        for (DailyEventState event : events) {
            talkFunc.guideTalk(event.getGuideData().getDescription() + ".");
        }
        talkFunc.guideTalk("Any of that sound interesting?");
        List<String> options = MyLists.transform(events, (DailyEventState ev) -> ev.getGuideData().getName());
        int result = multipleOptionArrowMenu(model, 24, 12, options);
        leaderSay("I think we'll " + events.get(result).getGuideData().getName().toLowerCase() + ".");
        talkFunc.guideTalk("Okay! Just come with me and I'll show you where it is.");
        model.getLog().waitForAnimationToFinish();
        removePortraitSubView(model);
        events.get(result).doTheEvent(model);
    }
}
