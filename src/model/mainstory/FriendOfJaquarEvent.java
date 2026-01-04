package model.mainstory;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.map.locations.PyramidLocation;
import util.MyPair;
import util.MyRandom;

import java.util.Map;

public class FriendOfJaquarEvent extends JungleTribeGeneralInteractionEvent {

    private final GainSupportOfJungleTribeTask task;
    private final PyramidLocation pyramid;

    public FriendOfJaquarEvent(Model model, GainSupportOfJungleTribeTask task) {
        super(model, MyRandom.randInt(5, 10));
        this.task = task;
        this.pyramid = task.getPyramidClue(model, 10);
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        println("The party encounters one of the people who inhabit this village.");
        showPortrait(model);
        return true;
    }

    private void showPortrait(Model model) {
        showExplicitPortrait(model, task.getFriendPortrait(), getNameOfFriend());
    }

    private String getNameOfFriend() {
        return task.isFriendOfJaquarKnown()
                ? "Friend of Jaquar"
                : "Commoner";
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        if (task.isFriendOfJaquarKnown()) {
            portraitSay("Oh, it's you again. Did you want to ask more questions?");
        } else {
            portraitSay("Can I help you?");
        }
        return true;
    }

    @Override
    protected String getVictimSelfTalk() {
        return "I'm a commoner. I do a little of this and a little of that.";
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        AdvancedAppearance portrait = task.getFriendPortrait();
        return new GameCharacter(getNameOfFriend(), "",
                portrait.getRace(), Classes.None, portrait, Classes.NO_OTHER_CLASSES);
    }

    @Override
    protected Map<String, MyPair<String, String>> makeSpecificTopics(Model model) {
        return Map.of("King Jaq", new MyPair<>("Who was King Jaq, and what happened to him?",
                        "Jaq was prince Jaquar's father, the king. But instead of passing on the Jade Crown to Jaquar, " +
                                "he hid it in one of the pyramids."),
                "Jequen", new MyPair<>("What do you know about Jequen?",
                        "That's Jaquar's son. He lives in the village."),
                "Prince Jaquar", new MyPair<>("Did you know Prince Jaquar?",
                        "PLACEHOLDER - UNUSED"),
                "Jade Crown", new MyPair<>("Do you know about the Jade Crown?",
                        "King Jaq hid it in the " + pyramid.getName() + ", to prevent his " +
                                "Jaquar from becoming king. I always thought Jaq was too hard on his son."));
    }

    @Override
    protected void specificTopicHook(Model model, MyPair<String, String> queryAndResponse) {
        if (queryAndResponse.first.contains("Prince Jaquar")) {
            leaderSay(queryAndResponse.first);
            portraitSay("Yes I knew Jaquar pretty well. We gre up together.");
            task.setFriendOfJaquarKnown(true);
            showPortrait(model);
            leaderSay("Really? What happened to him?");
            portraitSay("Well, I'm sure you've heard the story about the Jade Crown by now?");
            leaderSay("Yes. Jaquar wasn't good enough to become king, so King Jaq hid the crown in one of the pyramids.");
            portraitSay("Hmph, that's not really true. Jaquar was a good man, but he couldn't live up to " +
                    "King Jaq's impossible expectations. It was completely unfair of Jaq to do what he did.");
            leaderSay("I see. That's not quite what " + iveOrWeve() + " heard from others.");
            portraitSay("Most people only remember the gossip. Anyway, Jaquar organized an expedition to the " +
                    pyramid.getName() + " to recover the crown.");
            task.giveClueAbout(pyramid);
            leaderSay("Did you go with him?");
            if (task.isCrownInVillage()) {
                portraitSay("No. I wasn't part of that group, and I'm happy I wasn't. It was not an easy quest. " +
                        "If I recall correctly, they had to fight a dozen Lizardmen, search the vast pyramid, which was riddled with traps and poisonous creatures." +
                        "And finally, the crown was hidden behind a particularly quixotic puzzle.");
                leaderSay("Wait... I'm confused, who told you about this?");
                portraitSay("Prince Jaquar did, when he returned.");
                leaderSay("So he was actually able to recover the Jade Crown?");
                portraitSay("Yes. I guess most people don't know that part of the story.");
                leaderSay("No. Everybody's been saying he perished looking for the crown.");
                portraitSay("That's rubbish. After Jaquar found the crown he realized he wouldn't be able to become king anyway, since " +
                        "everybody knew the story about how the old king hid the crown. Nobody would see him as a legitimate ruler.");
                portraitSay("So prince Jaquar left these lands and went north, to try to sell the Jade Crown.");
                leaderSay("Where did he go exactly?");
                portraitSay("To the Kingdom fo Ardh, to one of the towns near the coast.");
                leaderSay("Hmm... So South Meadhome, or possibly Sheffield.");
                portraitSay("Yes. I think so. But in any case, he wasn't ever able to sell the Crown. Most did not believed " +
                        "it was authentic, and the few offers he got were not enough for him to give up the Crown.");
                leaderSay("Greed can inhibit success.");
                portraitSay("Yes, but I think Jaquar valued the Jade Crown highly. After all, it was the legacy of our kingdom. " +
                        "In the end, he could not bring himself to peddle it away. It was not greed, but pride and honor that stopped him.");
                leaderSay("Interesting. Sounds like the legend doesn't really do him justice.");
                portraitSay("Not at all. For instance, few remember that he then discretely returned here to raise his son. " +
                        "Not even Jequen remembers that, although he was still rather young at the time. " +
                        "Sadly Jaquar contracted a disease a few months later and passed away.");
                leaderSay("But what happened to the Jade Crown then?");
                portraitSay("Jaquar was planning on giving it to Jequen when he was old enough to rule. But honestly, " +
                        "I don't know what happened to it. Perhaps he himself hid it somewhere to keep it safe until the time " +
                        "came when he would give it to Jequen?");
                leaderSay("Yes, that does sound likely. Probably somewhere close to the village, perhaps even in the village itself.");
                portraitSay("Maybe you should talk to Jequen again. Perhaps he knows of some secret places where his father would go?");
                leaderSay(iOrWe() + "'ll do that. Thank you. This information has been very valuable. " + iOrWe() +
                        " are planning to return the crown to Jequen so he may become king of this land.");
                portraitSay("Jequen is a good man, and he'll make a good king. I know this is what Jaquar would have wanted.");
                task.setTruthAboutJaquarKnown(true);
            } else {
                portraitSay("Yes, me and six others. We made it through the jungle but when we got to the ancient city " +
                        "it had been taken over by lizardmen. We thought we had driven them off, but we were ambushed " +
                        "by another group while we were searching the pyramid for the crown.");
                leaderSay("Lizardmen are vicious. I'm impressed you survived.");
                portraitSay("Yes. But sadly, not all of us did. Jaquar was among those who perished. " +
                        "I was the only one able to make it back. The ordeal still troubles me to this day.");
                leaderSay("So the crown must still be in the " + pyramid.getName() + "?");
                portraitSay("As far as I know. Are you going after it?");
                leaderSay("Yes. We need Jequen to become the King.");
                portraitSay("I see. I hope you are able to achieve what we could not. Jequen is a good man, and he'll " +
                        "make a good king. I know this is what Jaquar would have wanted.");
            }
            waitForReturn();
        } else {
            super.specificTopicHook(model, queryAndResponse);
            if (queryAndResponse.first.contains("Jade Crown")) {
                task.giveClueAbout(pyramid);
            }
        }
    }
}
