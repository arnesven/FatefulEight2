package model.quests;

import model.Model;
import model.classes.Skill;
import model.combat.TownCombatTheme;
import model.enemies.*;
import model.journal.StoryPart;
import model.quests.scenes.*;
import model.states.QuestState;
import util.MyStrings;
import view.MyColors;
import view.subviews.CombatTheme;
import view.subviews.GrassCombatTheme;

import java.util.List;

public class SpecialDeliveryQuest extends MainQuest {
    public static final String QUEST_NAME = "Special Delivery";
    private static final String INTRO_TEXT = "You note down the details about the witch's client. " +
            "Wary of the 'third party', you set out to deliver the special potion.";
    private static final String END_TEXT = "Having delivered the potion, you return to the witch. " +
            "In addition to the information you want she agrees to give you a portion of the payment of the potion.";
    private boolean fightInTown;

    public SpecialDeliveryQuest() {
        super("Special Delivery", "", QuestDifficulty.MEDIUM, 0, 35, 50, INTRO_TEXT, END_TEXT);
        this.fightInTown = false;
    }

    @Override
    protected int getStoryTrack() {
        return StoryPart.TRACK_B;
    }

    @Override
    public CombatTheme getCombatTheme() {
        if (fightInTown) {
            return new TownCombatTheme();
        }
        return new GrassCombatTheme();
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(
                new QuestScene("Spot ambushers", List.of(
                    new SoloSkillCheckSubScene(2, 0, Skill.Perception, 9,
                        "One of us needs to keep a look out for this 'third party', whoever they may be."))),
                new QuestScene("Interceptors 1", List.of(
                    new HandOverPackageSubScene(2, 1, 3),
                    new BrotherHoodCombatSubScene(2, 2, List.of(
                            new BrotherhoodCronyEnemy('A'), new BanditEnemy('B'), new BanditArcherEnemy('C'))
                    ))),
                new QuestScene("Take a different route", List.of(
                    new CollaborativeSkillCheckSubScene(3, 2, Skill.Survival, 10,
                             "They'll surely be waiting for us at that camp ahead. Can we find a different route?"))),
                new QuestScene("Interceptors 2", List.of(
                    new HandOverPackageSubScene(2, 3, 5),
                    new BrotherHoodCombatSubScene(2, 4, List.of(
                            new BrotherhoodCronyEnemy('A'), new BanditArcherEnemy('C'), new BanditEnemy('B'),
                           new BanditEnemy('B'), new BanditArcherEnemy('C')
                    )))),
                new QuestScene("Getting into town", List.of(
                        new PayGoldSubScene(6, 5, 10, "The thieves' guild is offering to smuggle us into town."),
                        new CollectiveSkillCheckSubScene(5, 5, Skill.Sneak, 6, "We'll wait until nightfall, and sneak into town."))),
                new QuestScene("Interceptors 3", List.of(
                        new FinalHandOverPackageSubScene(4, 5),
                        new BrotherHoodCombatSubScene(4, 6, List.of(
                                new BrotherhoodCronyEnemy('A'), new BrotherhoodCronyEnemy('A'),
                                new BanditArcherEnemy('B'), new BanditArcherEnemy('B'),  new BanditArcherEnemy('A'),
                                new BrotherHoodBossEnemy('C'),
                                new BanditArcherEnemy('B'), new BanditArcherEnemy('B'),
                                new BrotherhoodCronyEnemy('A'), new BrotherhoodCronyEnemy('A'))))));

    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestDecisionPoint qdp = new QuestDecisionPoint(4, 4, List.of(
                new QuestEdge(scenes.get(5).get(0)), new QuestEdge(scenes.get(4).get(0)), new QuestEdge(scenes.get(4).get(1))),
                "How to get into town without them spotting us?");
        SimpleJunction fail = new SimpleJunction(0, 5, new QuestEdge(getFailEndingNode(), QuestEdge.VERTICAL));
        StoryJunction story = new StoryJunction(7, 8, new QuestEdge(getSuccessEndingNode())) {
            @Override
            protected void doAction(Model model, QuestState state) {
                state.println("A noble and a constable approaches you.");
                state.println("Noble: \"So he's dead then?\"");
                state.println("Constable: \"Looks so. We'll clean this mess up.\"");
                state.print("The noble turns to you.");
                state.println("Noble: \"Thank you. You played your part brilliantly?\"");
                state.leaderSay("Part? What part? Please explain yourself.");
                state.println("Noble: \"Aha, that sly witch. She didn't fill you in on the details. Well perhaps it was for the best.\"");
                state.leaderSay("So you're the client.");
                state.println("Noble: \"Indeed I am. Let me explain. For a long time, this crime lord here, or should I say, former crime lord, has been " +
                        "making trouble for our town and particularly for my business. It's turned into something of a vendetta I'm afraid. His organization " +
                        "is very well connected and backed by many powerful individuals. I shan't name any names, though. But still, I had to get rid of " +
                        "this particular individual, so, with a little help from our witch friend, we came up with a plan, a trap.\"");
                state.leaderSay("And we were the bait!");
                state.println("Noble: \"Yes, sorry. But the opportunity was too good not to seize. My agents reported that the crime lord was obsessed with the " +
                        "idea of a luck potion that would make him immensely rich. So I started planting rumors that I was trying to obtain one myself. I knew " +
                        "he would not be able to resist swiping it from me.\"");
                state.leaderSay("So that wasn't a luck potion he just drank?");
                state.println("Noble: \"Obviously not.\"");
                state.leaderSay("I don't really know how I feel about this. What if I would have downed the potion instead of him?");
                state.println("Noble: \"Well, I had to take that risk. And after all, everything turned out all right in the end.\"");
                state.leaderSay("Hmph. Well I guess I was lucky.");
                state.println("Noble: \"There you go. Now if you'll excuse us, me and the constable are going to see about cleaning up the rest " +
                        "of the thugs in this town. Good bye!\"");
            }
        };
        return List.of(new QuestStartPointWithoutDecision(new QuestEdge(scenes.get(0).get(0)),
                "Let's do this job quickly, okay?"), qdp, fail, story);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectFail(scenes.get(1).get(0));
        scenes.get(0).get(0).connectSuccess(scenes.get(2).get(0));

        scenes.get(1).get(0).connectSuccess(junctions.get(2));
        scenes.get(1).get(0).connectFail(scenes.get(1).get(1));

        scenes.get(1).get(1).connectFail(getFailEndingNode());
        scenes.get(1).get(1).connectSuccess(scenes.get(2).get(0));

        scenes.get(2).get(0).connectSuccess(junctions.get(1));
        scenes.get(2).get(0).connectFail(scenes.get(3).get(0), QuestEdge.VERTICAL);

        scenes.get(3).get(0).connectSuccess(junctions.get(2));
        scenes.get(3).get(0).connectFail(scenes.get(3).get(1));

        scenes.get(3).get(1).connectFail(getFailEndingNode());
        scenes.get(3).get(1).connectSuccess(junctions.get(1));

        scenes.get(4).get(0).connectSuccess(getSuccessEndingNode(), QuestEdge.VERTICAL);
        scenes.get(4).get(1).connectSuccess(getSuccessEndingNode(), QuestEdge.VERTICAL);
        scenes.get(4).get(1).connectFail(scenes.get(5).get(0));

        scenes.get(5).get(0).connectSuccess(junctions.get(2));
        scenes.get(5).get(0).connectFail(scenes.get(5).get(1));

        scenes.get(5).get(1).connectSuccess(getSuccessEndingNode(), QuestEdge.VERTICAL);
        scenes.get(5).get(1).connectFail(getFailEndingNode());
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.GREEN;
    }

    private static class HandOverPackageSubScene extends YesOrNodeNode {
        private final int people;

        public HandOverPackageSubScene(int col, int row, int people) {
            super(col, row,
                    "Do you hand over the package?",
                    "You reluctantly hand over the package. Realizing that you have given up your only lead on the crimson pearl.",
                    "You refuse to hand over the package.");
            this.people = people;
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            state.println("Suddenly a group of " + MyStrings.numberWord(people) + " people step out in front of the party.");
            state.println("Brotherhood Agent: \"That package you're carrying is very valuable. Why don't you just hand it over " +
                    "to us? That way nobody will get hurt today.\"");
            return super.run(model, state);
        }
    }

    private class FinalHandOverPackageSubScene extends HandOverPackageSubScene {
        public FinalHandOverPackageSubScene(int col, int row) {
            super(col, row, 10);
            fightInTown = true;
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            QuestEdge toReturn = super.run(model, state);
            if (toReturn == getSuccessEdge()) {
                state.println("The leader of the gang steps forward and takes the package, unwraps it, and holds up a small vial.");
                state.println("Brotherhood Boss: \"Much obliged friend. I've been trying to get my hands on this rare potion for years. Do you know what it is?\"");
                state.leaderSay("No. Please enlighten me.");
                state.println("Brotherhood Boss: \"It's a luck potion. Damn strong one too. With this, everything will just go my way you see?\"");
                state.leaderSay("Well, lucky you I guess...");
                state.println("Brotherhood Boss: \"Yes, hehehe. Well bottoms up friend!\"");
                state.println("The Brotherhood boss uncorks the potion and drinks it all in one gulp. He belches loudly and smirks at you. Then suddenly his smirk turns " +
                        "into an ugly grimace.");
                state.println("Brotherhood Boss: \"You! You swapped the potions didn't you. I uhh... I'll...\"");
                state.println("The man suddenly drops to his knees, clutching his belly.");
                state.println("Brotherhood Boss: \"Aaaarghgh. Glllgll.\"");
                state.println("He falls down, froth coming from his mouth.");
                state.println("Brotherhood Agent: \"Boss! Boss! Hey, you awake?\"");
                state.leaderSay("I think he's done for.");
                state.println("Brotherhood Agent: \"Well, I'm out of here. Don't wanna be here when the constable shows up asking questions about this dead body.\"");
                state.println("The gang of thugs promptly disbands. Puzzled you stay for a while, looking around.");
                model.getLog().waitForAnimationToFinish();
                return new QuestEdge(getJunctions().get(3));
            }
            return toReturn;
        }
    }

    private static class BrotherHoodCombatSubScene extends CombatSubScene {
        private String countWord;
        public BrotherHoodCombatSubScene(int col, int row, List<Enemy> enemies) {
            super(col, row, enemies, true);
            this.countWord = MyStrings.numberWord(enemies.size());
            countWord = countWord.substring(0, 1).toUpperCase() + countWord.substring(1);
        }

        @Override
        protected String getCombatDetails() {
            return countWord + " Brotherhood Thugs";
        }
    }
}
