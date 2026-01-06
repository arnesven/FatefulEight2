package model.states.events;

import model.Model;
import model.journal.JournalEntry;
import model.mainstory.jungletribe.GainSupportOfJungleTribeTask;
import model.map.CastleLocation;
import model.states.DailyEventState;
import util.MyPair;
import util.MyStrings;

import java.util.ArrayList;
import java.util.List;

public class MeetWithJequenEvent extends DailyEventState {
    private final GainSupportOfJungleTribeTask jungleTribeTask;

    public MeetWithJequenEvent(Model model, GainSupportOfJungleTribeTask task) {
        super(model);
        this.jungleTribeTask = task;
    }

    @Override
    protected void doEvent(Model model) {
        setCurrentTerrainSubview(model);
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
        portraitSay("No, nothing like that.");
        for (MyPair<Boolean, String> pair : makeJequensStory()) {
            if (pair.first) {
                portraitSay(pair.second);
            } else {
                leaderSay(pair.second);
            }
        }
        leaderSay("But if the crown was recovered, and returned to you. You could become regent here?");
        portraitSay("Yes, I guess it's possible.");
        leaderSay("Any idea which one of those pyramids the crown could be at?");
        portraitSay("No, sorry. But maybe someone else around here does, you should ask around. " +
                "Especially the older generation may know something about it.");
        leaderSay("Alright. I guess " + iOrWe() + " have no other choice.");
        portraitSay("Thank you for doing this. And... if you find out what happened to my father, I would like to know.");
        leaderSay(iOrWe() + " will be back.");
        JournalEntry.printJournalUpdateMessage(model);
        jungleTribeTask.setJequenMet();
    }

    public static List<MyPair<Boolean, String>> makeJequensStory() {
        List<MyPair<Boolean, String>> list = new ArrayList<>();
        list.add(new MyPair<>(true, "The southern kingdom had been deteriorating for the better part of a century. We used to live in " +
                "majestic cities, with pyramids to the south and east of here. But those days were long gone. Now, the southern tribe was " +
                "scattered across this continent, living in huts, in tiny villages like this one."));
        list.add(new MyPair<>(false, "Why did the kingdom fall into decline?"));
        list.add(new MyPair<>(true, "For various reasons. Trade with the kingdoms of Sunblaze and Ardh had diminished, mostly due to the presence of pirates on the sea north of here."));
        list.add(new MyPair<>(true, "But there were other reasons; disease, famine and infighting between different factions."));
        list.add(new MyPair<>(true, "My grandfather mostly focused on keeping the local villages protected from the wild creatures that roam this continent, while " +
                "hoping his son, Jaquar would one day be able to unite the kingdom and restore it to its former glory."));
        list.add(new MyPair<>(false, "Let me guess. Jaquar never became king?"));
        list.add(new MyPair<>(true, "No. My father was not much of a student. He openly disobeyed king Jaq throughout most of his childhood and " +
                "made it publicly known he had no wish to restore the kingdom."));
        list.add(new MyPair<>(false, "He didn't want to become king?"));
        list.add(new MyPair<>(true, "He did. He just didn't believe in my grandfather's vision of a strong southern kingdom. It was no secret that " +
                "prince Jaquar was just waiting for the day when Jaq would hand over the Jade Crown."));
        list.add(new MyPair<>(false, "The Jade Crown?"));
        list.add(new MyPair<>(true, "Oh yes, I forgot to mention that. The Jade Crown is the symbol of regency in our kingdom. " +
                "It is said to be at least a millennia old, and worn by all the kings and queens of the Southern Kingdom. " +
                "No heir to the throne can hope to rule this land without it."));
        list.add(new MyPair<>(false, "I see... so?"));
        list.add(new MyPair<>(true, "What happened to it? Well. When King Jaq had become very old, and could no longer perform his duties everybody expected " +
                "him to resign. And so he did, but not before hiding the Jade Crown in one of the ancient Pyramid cities."));
        list.add(new MyPair<>(true, "Jaquar was furious. I was just a small child, but I remember his anger well. For a while Jaquar tried " +
                "to enforce his regency anyway, but nobody took him seriously without the crown. The old king dies shortly thereafter."));
        list.add(new MyPair<>(false, "Why didn't Jaquar just retrieve the crown?"));
        list.add(new MyPair<>(true, "He wanted to, but he knew it was no easy feat to trek through the jungle and search the Pyramids for the Crown. " +
                "He needed to prepare."));
        list.add(new MyPair<>(true, "He managed to persuade some of his friends to join him on the quest to find the Crown. I was about six at the time. I never saw him again."));
        list.add(new MyPair<>(true, "And that's the end of the story I'm afraid. As I said earlier, I would gladly lend you a hand, " +
                "but without the Crown, I'm just as much a king as my father was - which is to say, not at all."));
        return list;
    }
}
