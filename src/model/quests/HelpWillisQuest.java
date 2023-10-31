package model.quests;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.quests.scenes.CollaborativeSkillCheckSubScene;
import model.quests.scenes.SoloSkillCheckSubScene;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.QuestState;
import model.states.RecruitState;
import view.BorderFrame;
import view.MyColors;

import java.util.List;

public class HelpWillisQuest extends MainQuest {
    public static final String QUEST_NAME = "Help Willis";
    private static final String INTRO_TEXT = "Willis needs help in the library. You must find and recruit suitable candidates. " +
            "But not everybody is cut out to be a library assistant.";
    private static final String ENDING_TEXT = "Willis is pays you for finding staff for the library.";
    private static final int RECRUITS_NEEDED = 4;
    private int recruited = 0;

    public HelpWillisQuest() {
        super(QUEST_NAME, "", QuestDifficulty.EASY, 0, 75, 0, INTRO_TEXT, ENDING_TEXT);
    }

    @Override
    public void drawSpecialReward(Model model, int x, int y) {
        y++;
        BorderFrame.drawString(model.getScreenHandler(), "Recruit", x, y++, MyColors.WHITE, MyColors.BLACK);
        BorderFrame.drawString(model.getScreenHandler(), "Willis", x, y++, MyColors.WHITE, MyColors.BLACK);
    }

    @Override
    public boolean drawTownOrCastleInBackground() {
        return true;
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(new QuestScene("Find and interview candidates", List.of(
                new CollaborativeSkillCheckSubScene(1, 1, Skill.SeekInfo, 11, "We need to find more candidates."),
                new SoloSkillCheckSubScene(1, 5, Skill.Persuade, 11, "Seems like we need to talk her into it."),
                new PayGoldSubScene(2, 2, 3, "Let's just pay her up front for the job."),
                new ConditionSubScene(4, 5) {
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
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestDecisionPoint qdp = new QuestDecisionPoint(1, 2, List.of(new QuestEdge(scenes.get(0).get(1)),
                new QuestEdge(scenes.get(0).get(2))), "Ah, a good candidate. But how to convince her to take the job?");
        StoryJunction recruitedJunc = new StoryJunction(2, 5, new QuestEdge(scenes.get(0).get(3))) {
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
        SimpleJunction extraFail = new SimpleJunction(0, 5, new QuestEdge(getFailEndingNode(), QuestEdge.VERTICAL));
        return List.of(new QuestStartPointWithoutDecision(new QuestEdge(scenes.get(0).get(0)),
                "First we need some find some candidates for work in the library."),
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
        return MyColors.BLACK;
    }

    @Override
    public MainQuest copy() {
        return new HelpWillisQuest();
    }

    @Override
    public GameState endOfQuest(Model model, QuestState state, boolean questWasSuccess) {
        if (questWasSuccess) {
            new WillisEndingEvent(model).doEvent(model);
        }
        return super.endOfQuest(model, state, questWasSuccess);
    }

    private class WillisEndingEvent extends DailyEventState {
        public WillisEndingEvent(Model model) {
            super(model);
        }

        @Override
        protected void doEvent(Model model) {
            println("You return to the library. Willis is ecstatic.");
            showExplicitPortrait(model, HelpWillisQuest.this.getPortrait(), "Willis");
            portraitSay("I've never had this much help. Just think what this place will become now, " +
                    "a real fulcrum of the community!");
            leaderSay("That's great Willis. Glad we could help. Now we really should be leaving.");
            portraitSay("Wait a minute...");
            println("Willis seems to be thinking about something as she watches the new librarians bustling " +
                    "about the library, moving books and happily discussing authors and editions.");
            leaderSay("What's the matter Willis?");
            portraitSay("It's just. I've worked in this library for 20 years now. I've always felt like " +
                    "it was my duty to keep it in shape. But now, it seems its future is secure. Maybe there's no need for me here?");
            leaderSay("Thinking about a career change?");
            portraitSay("Part of me wants to explore arcanism more. I won't be able to develop my knowledge and skills here, " +
                    "I'll have to venture out in the world.");
            leaderSay("Wanna come with us?");
            println("Press enter to continue.");
            waitForReturn();
            GameCharacter willis = model.getMainStory().getWillisCharacter();
            willis.setLevel((int)Math.ceil(GameState.calculateAverageLevel(model)));
            RecruitState recruit = new RecruitState(model, List.of(willis));
            recruit.run(model);
            removePortraitSubView(model);
            setCurrentTerrainSubview(model);
            if (model.getParty().getPartyMembers().contains(willis)) {
                leaderSay("Good to have you with us Willis.");
                partyMemberSay(willis, "Hope I can be of help.");
            } else {
                leaderSay("On the other hand. Maybe we should go our separate ways.");
                portraitSay("Perhaps it's for the best.");
            }
        }
    }
}
