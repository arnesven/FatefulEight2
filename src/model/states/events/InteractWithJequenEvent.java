package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Classes;
import model.enemies.Enemy;
import model.mainstory.GainSupportOfJungleTribeTask;
import model.races.Race;
import model.states.GameState;
import util.MyPair;
import util.MyRandom;

import java.util.List;
import java.util.Map;

public class InteractWithJequenEvent extends GeneralInteractionEvent {
    private final GainSupportOfJungleTribeTask task;

    public InteractWithJequenEvent(Model model, GainSupportOfJungleTribeTask task) {
        super(model, "Talk to", 5, false);
        this.task = task;
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        setCurrentTerrainSubview(model);
        println("You step into Prince Jequen's hut.");
        showExplicitPortrait(model, task.getJequenPortrait(), "Prince Jequen");
        portraitSay("You have return. Have you found the Jade Crown?");
        leaderSay("No. I wanted to ask you some questions.");
        return true;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        print("Do you want to ask Jequen to tell the story of his father and grandfather again? (Y/N) ");
        if (yesNoInput()) {
            leaderSay("Tell me about your father and grandfather again.");
            portraitSay("Well, to understand the relationship between my father and grandfather, you must understand " +
                    "the state the kingdom was in.");
            leaderSay("You mentioned it was in decline. That it had been for some time?");
            for (MyPair<Boolean, String> pair : MeetWithJequenEvent.makeJequensStory()) {
                if (pair.first) {
                    portraitSay(pair.second);
                } else {
                    leaderSay(pair.second);
                }
            }
        }
        return true;
    }

    @Override
    protected String getVictimSelfTalk() {
        return "I'm Prince Jequen. But I usually just go by 'Jequen'. My title doesn't matter without the Jade Crown.";
    }

    @Override
    protected String getRegionReply() {
        return "This is the Southern Kingdom, or what remains of it at least.";
    }

    @Override
    protected String getOutsideOfKingdomNews() {
        return MyRandom.flipCoin() ? "Orcs, lizardmen and frogmen have been very aggressive lately. " +
                "Our warriors are completely preoccupied with fending them off."
                : "Count Aldec's forces have been spotted in the lands to the north. They don't usually come " +
                "this far south. I wonder what is going on.";
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        return new GameCharacter("Prince Jequen", "", Race.SOUTHERN_HUMAN, Classes.None,
                task.getJequenPortrait(), Classes.NO_OTHER_CLASSES);
    }

    @Override
    protected List<Enemy> getVictimCompanions(Model model) {
        return List.of();
    }

    @Override
    protected ProvokedStrategy getProvokedStrategy() {
        return ProvokedStrategy.FIGHT_TO_DEATH; // Not be used
    }

    @Override
    protected Map<String, MyPair<String, String>> makeSpecificTopics(Model model) {
        return Map.of("King Jaq", new MyPair<>("Who was King Jaq, and what happened to him?",
                "King Jaq was my grandfather. He hid the Jade Crown from my father. He died of old age a short time after he resigned."),
                "Prince Jaquar", new MyPair<>("Who was Prince Jaquar, and what happened to him?",
                "Prince Jaquar was my father. My grandfather snubbed him of his birthright. Jaquar ventured to one of the " +
                        "pyramids in search of the Jade Crown. He never came back."),
                "Jade Crown", new MyPair<>("So you don't know where the Jade Crown is?",
                        "Most people seem to believe it's still hidden in one of the ancient pyramids. " +
                                "But I've heard conflicting stories about which one."));
    }

    @Override
    protected void specificTopicHook(Model model, MyPair<String, String> queryAndResponse) {
        if (task.isJaquarTruthKnown() && queryAndResponse.first.contains("Jade Crown")) {
            if (task.isCompleted()) {
                leaderSay("Is that really the Jade Crown?");
                portraitSay("Yes. I'm certain. This is incredible!");
            } else {
                leaderSay("We talked to an old friend of your father. He told us Jaquar returned to " +
                        "this village after recovering the Jade Crown. But that he passed away shortly thereafter.");
                portraitSay("Really? That... hmm...");
                leaderSay("What is it?");
                portraitSay("I have some memory of that from when I was a child, but I thought that was just a dream. " +
                        "At some point I must have just started believing the legend that everybody tells around here.");
                portraitSay("Maybe it was easier to believe that my father died trying to accomplish something, rather than the truth.");
                leaderSay("Do you remember if he had the crown with him?");
                portraitSay("No. I don't remember anything like that. If he actually had the crown but wasn't planning " +
                        "on using it to seize power, I expect he would have kept it a secret, even from me.");
                leaderSay("Did you know of any secret places where he used to go or keep things?");
                portraitSay("No... or wait, maybe... My mother died in child birth, but I remember my father telling me " +
                        "that she always kept her most valuable jewelry in a secret compartment over here in the ceiling...");
                println("Jequen brings out a stool and removes some of the straw in the ceiling.");
                findBoxInCeiling(model);
            }
        } else {
            super.specificTopicHook(model, queryAndResponse);
        }
    }

    private void findBoxInCeiling(Model model) {
        portraitSay("Yes... there is something here... a box - incredible!");
        leaderSay("Open it.");
        portraitSay("A letter... and below it. The crown! Yes this must be it. It resembles very much the way " +
                "it is described by the elders of the village.");
        leaderSay("What does the letter say?");
        println("Jequen opens the letter.");
        portraitSay("It's from my father, to me.");
        portraitSay("'Dear Jequen. If you're reading this, than I must have died before being able to present you " +
                "with the Jade Crown. You were too young to understand what was happening when I left to search for it, so I " +
                "could not explain why I had to leave.'");
        portraitSay("'I wish I could say that I left with something other than my own interests in mind, but that would be a lie. " +
                "I've since realized that I've been very foolish and selfish. After recovering the crown I've come to realize that " +
                "I could never become a good king, but maybe you will be.'");
        portraitSay("'I therefore leave the crown to you. If you do not want it, you can always give it to the elders of our " +
                "village, and they will find our kingdom a new ruler. But if you wish to claim regency of this kingdom I wish you " +
                "the best of luck, and know that I will always love you, whatever you decide.'");
        portraitSay("'Your father, Jaquar.'");
        leaderSay("Now you know the truth about your father.");
        println("Jequen wipes away a tear.");
        portraitSay("Yes. Now I know what happened.");
        leaderSay("So what will you do?");
        portraitSay("I never thought I would ever be in this situation, but now that I'm here, it's clear what I must do. I must " +
                "become the king of the Southern Kingdom.");
        leaderSay("Huzzah!");
        portraitSay("And you shall have the support you wanted. When the come times, I'll meet you at the rendezvous point with " +
                "a force to be reconed with.");
        leaderSay("That's great. Thanks a lot Jequen!");
        task.setCrownGivenToJequen();
    }

    @Override
    protected String getCustomTopicReply(Model model, GameCharacter victim, String topic) {
        if (task.isCompleted()) {
            return super.getCustomTopicReply(model, victim, topic);
        }
        topic = topic.toLowerCase();
        if ((topic.contains("hidden") || topic.contains("secret") || topic.contains("compartment"))
            && topic.contains("ceiling")) {
            portraitSay("What's that? You want me to look for a secret compartment in the ceiling?");
            leaderSay("Yes," + imOrWere() + " certain the Jade Crown is there.");
            portraitSay("That's rather odd. But I guess it doesn't hurt to look.");
            println("Jequen brings out a stool and removes some of the straw in the ceiling.");
            if (task.isCrownInVillage()) {
                findBoxInCeiling(model);
                return "Thank you.";
            }
            portraitSay("No... there's nothing here. But why would it be?");
            leaderSay("It was just a hunch.");
            return "...";
        }
        if (topic.contains("hidden")) {
            return "You think the crown is hidden in this house? Where?";
        }
        if (topic.contains("secret")) {
            return "A secret place in this house? Where?";
        }
        if (topic.contains("ceiling")) {
            return "What about the ceiling?";
        }
        return super.getCustomTopicReply(model, victim, topic);
    }
}
