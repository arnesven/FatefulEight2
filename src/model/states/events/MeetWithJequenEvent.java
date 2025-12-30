package model.states.events;

import model.Model;
import model.mainstory.GainSupportOfJungleTribeTask;
import model.map.CastleLocation;
import model.states.DailyEventState;
import util.MyStrings;

public class MeetWithJequenEvent extends DailyEventState {
    public MeetWithJequenEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        setCurrentTerrainSubview(model);
        GainSupportOfJungleTribeTask jungleTribeTask = GainSupportOfJungleTribeTask.getTask(model);
        showExplicitPortrait(model, jungleTribeTask.getJequenPortrait(), "Jequen");
        portraitSay("Hello there. You must be the travellers I've heard rumors about.");
        leaderSay("Yes, who are you?");
        portraitSay("I'm prince Jequen. I live here.");
        leaderSay("Are you the ruler of this kingdom? We need to talk to the person in charge.");
        portraitSay("That may be hard. There really isn't anybody in charge. The southern kingdom has been leaderless for many years.");
        leaderSay("That's unfortunate, we were kind of hoping to talk to a king, or a chieftain or something.");
        portraitSay("The last king of these lands was king Jaq, my grandfather. He's long dead however.");
        leaderSay("But he obviously has a descendant. Why are you not king?");
        portraitSay("It's a bit of a long story. I'll gladly tell it to you, but first please explain who you are.");
        String start = "My name is " + model.getParty().getLeader().getName();
        if (model.getParty().size() > 1) {
            start += ", and this is my company of adventurers";
        }
        leaderSay(start + ".");
        leaderSay("As to why " + imOrWere() + " here...");
        leaderSay("As you have surely noticed, the world beyond your borders have been more tumultuous lately.");
        CastleLocation ardh = model.getWorld().getCastleByName(model.getMainStory().getCastleName());
        String kingdom = CastleLocation.placeNameToKingdom(ardh.getPlaceName());
        portraitSay("Indeed. We have had a surge in orcish activity, and the forces of the " +
                kingdom +
                " have begin infringing on our territory in the west. Are you an envoy of " +
                ardh.getLordName() + "?");
        leaderSay("No, or yes... It's complicated.");
        portraitSay("I don't understand. Are you or are you not?");
        leaderSay(MyStrings.capitalize(iWasOrWeWere()) + " investigating a matter for the " + ardh.getLordTitle() + ". The quest " +
                "finally brought " + meOrUs() + " to the ancient stronghold to the west of these lands. But when " + iOrWe() + " returned " +
                iWasOrWeWere() + " wrongfully imprisoned!");
        leaderSay(iOrWeCap() + " narrowly escaped the " + kingdom +
                " alive. It seems the " + ardh.getLordTitle() + " has been possessed or controlled by an evil force known as the Quad. " +
                "Because of this, the kingdom has descended into disorder.");
        portraitSay("I see. And now you seek refuge here?");
        leaderSay("For a time perhaps. " + iOrWeCap() + " aim to rally the support of the kingdoms surrounding the " +
                kingdom + ". We intend to return to the " + kingdom + " to overthrow " +
                ardh.getLordName() + " and root out the evil presence.");
        portraitSay("Ah, now I understand. And you were hoping to petition the King of the southern kingdom to support you in this endeavour.");
        leaderSay("Yes, exactly. If you could distract the armed forces of " + kingdom +
                " and press " + ardh.getLordName() + " from the south, it would " +
                "greatly improve our chances of taking the castle.");
        println("Jequen smiles.");
        portraitSay("I understand, and if I were the king here I would gladly help you. Sadly I am not. I may be a prince, but in name only. " +
                "To the people of this land, I am just a commoner.");
        leaderSay("Please explain. Was king Jaq overthrown or something?");
        waitForReturn();
    }
}
