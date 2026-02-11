package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.map.UrbanLocation;
import model.states.DailyEventState;
import util.MyLists;
import util.MyRandom;

import java.util.List;

public class HomeTownEvent extends DailyEventState {
    private static final String ALREADY_DONE = "HomeTownEventAlreadyDoneFor";
    private final GameCharacter homeGuy;

    public HomeTownEvent(Model model, GameCharacter homeGuy) {
        super(model);
        this.homeGuy = homeGuy;
    }

    @Override
    public boolean exclusiveToOriginalWorld() {
        return true;
    }

    private static boolean isInHomeTown(Model model, GameCharacter gc) {
        return model.isInOriginalWorld() && gc.hasHomeTown() && gc.getHomeTown(model) == model.getCurrentHex().getLocation();
    }

    private static int getDoneCounter(Model model, GameCharacter gc) {
        if (model.getSettings().getMiscCounters().containsKey(ALREADY_DONE + gc.getName())) {
            return model.getSettings().getMiscCounters().get(ALREADY_DONE + gc.getName());
        }
        return 0;
    }

    private static boolean alreadyDoneEvent(Model model, GameCharacter gc) {
        return getDoneCounter(model, gc) > 0;
    }

    private static void increaseDoneCounter(Model model, GameCharacter gc) {
        int currentDone = getDoneCounter(model, gc);
        model.getSettings().getMiscCounters().put(ALREADY_DONE + gc.getName(), Math.min(6, currentDone + 3));
    }

    public static DailyEventState eventDependentOnHomeTown(Model model) {
        if (model.getCurrentHex().getLocation() instanceof UrbanLocation) {
            List<GameCharacter> homeTownGuys = MyLists.filter(model.getParty().getPartyMembers(),
                    gc -> isInHomeTown(model, gc) &&
                          model.getParty().getLeader() != gc &&
                            MyRandom.rollD6() >= getDoneCounter(model, gc));
            if (!homeTownGuys.isEmpty()) {
                return new HomeTownEvent(model, MyRandom.sample(homeTownGuys));
            }
        }
        return null;
    }

    @Override
    protected void doEvent(Model model) {
        UrbanLocation loc = homeGuy.getHomeTown(model);
        if (alreadyDoneEvent(model, homeGuy)) {
            leaderSay("Hey " + homeGuy.getFirstName() + ", know anything else to do here?");
        } else {
            partyMemberSay(homeGuy, MyRandom.sample(List.of(
                    "Hey, I know this " + loc.getLocationType() + "! This is where I grew up.",
                    "I'm familiar with this place. I used to live here.",
                    "I know this place like the back of my hand. Lived here for years!",
                    "Looks like things haven't changed much around here. I used to live here you know.")));
            leaderSay("Maybe you could show us around. What's worth seeing or doing?");
        }
        increaseDoneCounter(model, homeGuy);
        new GuideEvent(model, 0).chooseFromGuidableEvents(model, line -> partyMemberSay(homeGuy, line));
    }
}
