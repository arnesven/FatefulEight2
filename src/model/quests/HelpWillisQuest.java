package model.quests;

import model.Model;
import model.classes.Skill;
import model.quests.scenes.CollaborativeSkillCheckSubScene;
import model.quests.scenes.SoloSkillCheckSubScene;
import model.states.GameState;
import model.states.QuestState;
import sound.BackgroundMusic;
import view.MyColors;
import view.widget.QuestBackground;

import java.util.List;

public class HelpWillisQuest extends MainQuest {
    public static final String QUEST_NAME = "Help Willis";
    private static final String INTRO_TEXT = "Willis needs help in the library. You must find and recruit suitable candidates. " +
            "But not everybody is cut out to be a library assistant.";
    private static final String ENDING_TEXT = "Willis is pays you for finding staff for the library.";
    private static final int RECRUITS_NEEDED = 4;
    private static final List<QuestBackground> BACKGROUND_SPRITES = MurderMysteryQuest.makeBackground();
    private int recruited = 0;

    public HelpWillisQuest() {
        super(QUEST_NAME, "", QuestDifficulty.EASY,
                new Reward(75), 0, INTRO_TEXT, ENDING_TEXT);
    }

    @Override
    protected List<String> getSpecialRewards() {
        return List.of("Recruit", "Willis");
    }

    @Override
    public boolean drawTownOrCastleInBackground() {
        return false;
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(new QuestScene("Find and interview candidates", List.of(
                new CollaborativeSkillCheckSubScene(1, 2, Skill.SeekInfo, 11, "We need to find more candidates."),
                new SoloSkillCheckSubScene(1, 6, Skill.Persuade, 11, "Seems like we need to talk her into it."),
                new PayGoldSubScene(2, 4, 3, "Let's just pay her up front for the job."),
                new ConditionSubScene(4, 6) {
                    @Override
                    public String getDescription() {
                        return recruited + "/" + RECRUITS_NEEDED + " hired.";
                    }

                    @Override
                    public QuestEdge run(Model model, QuestState state) {
                        if (recruited == RECRUITS_NEEDED) {
                            state.leaderSay("I think we have enough people to work in the library now.");
                            return getSuccessEdge();
                        }
                        state.leaderSay("We still have to hire some more people to help Willis.");
                        return getFailEdge();
                    }
                })));
    }

    @Override
    public QuestJunction getStartNode() {
        return super.getStartNode();
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestDecisionPoint qdp = new QuestDecisionPoint(1, 4, List.of(new QuestEdge(scenes.get(0).get(1)),
                new QuestEdge(scenes.get(0).get(2))), "Ah, a good candidate. But how to convince her to take the job?");
        StoryJunction recruitedJunc = new StoryJunction(2, 6, new QuestEdge(scenes.get(0).get(3))) {
            @Override
            protected void doAction(Model model, QuestState state) {
                switch (recruited) {
                    case 0:
                        state.leaderSay("Welcome aboard. The library's that way. You're boss's name is Willis Johanssen. " +
                                "Hope you like dusty books, because we have lots of them.");
                        state.printQuote("New Librarian", "Uhm, okay...");
                        break;
                    case 1:
                        state.leaderSay("You're hired. Working conditions are fair, pay is decent. " +
                                "Benefits are... anyway, library is that way.");
                        state.printQuote("New Librarian", "Okay, I'm on my way.");
                        break;
                    case 2:
                        state.leaderSay("The job is yours! Head on over to the library. No reading on the job!");
                        state.printQuote("New Librarian", "I understand.");
                        break;
                    default:
                        state.leaderSay("Nice to be working with you. Willis will be so glad to meet you. " +
                                "Do you like to sort books?");
                        state.printQuote("New Librarian", "Sure...");
                }
                recruited++;
            }
        };
        SimpleJunction extraFail = new SimpleJunction(0, 6, new QuestEdge(getFailEndingNode(), QuestEdge.VERTICAL));
        QuestJunction start = new QuestStartPointWithoutDecision(new QuestEdge(scenes.get(0).get(0)),
                "First we need some find some candidates for work in the library.");
        start.setRow(1);
        return List.of(start,
                qdp,
                recruitedJunc, extraFail);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectFail(junctions.get(3));
        scenes.get(0).get(0).connectSuccess(junctions.get(1));

        scenes.get(0).get(1).connectFail(getFailEndingNode(), QuestEdge.VERTICAL);
        scenes.get(0).get(1).connectSuccess(junctions.get(2));

        scenes.get(0).get(2).connectSuccess(junctions.get(2));

        scenes.get(0).get(3).connectSuccess(getSuccessEndingNode());
        scenes.get(0).get(3).connectFail(scenes.get(0).get(0), QuestEdge.VERTICAL);
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.GRAY;
    }

    @Override
    public MainQuest copy() {
        return new HelpWillisQuest();
    }

    @Override
    public GameState endOfQuest(Model model, QuestState state, boolean questWasSuccess) {
        if (questWasSuccess) {
            new WillisEndingEvent(model, getPortrait()).doEvent(model);
        }
        return super.endOfQuest(model, state, questWasSuccess);
    }

    @Override
    public BackgroundMusic getMusic() {
        return BackgroundMusic.lightQuestSong;
    }

    @Override
    public List<QuestBackground> getBackgroundSprites() {
        return BACKGROUND_SPRITES;
    }
}
