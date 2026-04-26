package model.quests;

import model.Model;
import model.characters.PersonalityTrait;
import model.characters.appearance.FacialExpression;
import model.characters.special.WillisCharacter;
import model.classes.Classes;
import model.classes.Skill;
import model.quests.scenes.CollaborativeSkillCheckSubScene;
import model.quests.scenes.SoloSkillCheckSubScene;
import model.quests.scenes.StoryJunctionWithEvent;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.QuestState;
import sound.BackgroundMusic;
import view.MyColors;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.List;

public class HelpWillisQuest extends MainQuest {
    public static final String QUEST_NAME = "Help Willis";
    private static final String INTRO_TEXT = "Willis needs help in the library. You must find and recruit suitable candidates. " +
            "But not everybody is cut out to be a library assistant.";
    private static final String ENDING_TEXT = "Willis is pays you for finding staff for the library.";
    private static final int RECRUITS_NEEDED = 4;
    private static final List<QuestBackground> BACKGROUND_SPRITES = MurderMysteryQuest.makeBackground();
    private int recruited = 0;
    private List<QuestBackground> decorations;

    public HelpWillisQuest() {
        super(QUEST_NAME, "", QuestDifficulty.EASY,
                new Reward(75), 0, INTRO_TEXT, ENDING_TEXT);
        decorations = List.of(new QuestBackground(new Point(7, 8),
                new WillisCharacter().getAvatarSprite(), false));
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
                    public String getDescription(Model model) {
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
        StoryJunction recruitedJunc = new StoryJunctionWithEvent(2, 6, new QuestEdge(scenes.get(0).get(3))) {
            @Override
            public DailyEventState makeEvent(Model model, QuestState state) {
                return new RecruitNewLibrarianEvent(model);
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

    @Override
    public List<QuestBackground> getDecorations() {
        return decorations;
    }

    private class RecruitNewLibrarianEvent extends DailyEventState {
        public RecruitNewLibrarianEvent(Model model) {
            super(model);
        }

        @Override
        protected void doEvent(Model model) {
            showRandomPortrait(model, Classes.None, "New Librarian");
            switch (recruited) {
                case 0:
                    leaderSay("Welcome aboard. The library's that way. You're boss's name is Willis Johanssen. " +
                            "I hope you like dusty books, because we have lots of them.");
                    portraitSay("Uhm, okay...");
                    randomSayIfPersonality(PersonalityTrait.intellectual, List.of(model.getParty().getLeader()),
                            heOrSheCap(getPortraitGender()) + " doesn't seem all that excited about books. " +
                                    "I guess we can't all be bookworms.", FacialExpression.relief);
                    break;
                case 1:
                    leaderSay("You're hired. Working conditions are fair, pay is decent. " +
                            "Benefits are... anyway, library is that way.");
                    portraitSay("Okay, I'm on my way.");
                    randomSayIfPersonality(PersonalityTrait.jovial, List.of(),
                            "Don't mind the piles of broken killer robots!", FacialExpression.relief);
                    break;
                case 2:
                    leaderSay("The job is yours! Head on over to the library. No reading on the job!");
                    portraitSay("I understand.");
                    randomSayIfPersonality(PersonalityTrait.forgiving, List.of(model.getParty().getLeader()),
                             "I think a little reading is okay.", FacialExpression.relief);
                    break;
                default:
                    leaderSay("Nice to be working with you. Willis will be so glad to meet you. " +
                            "Do you like to sort books?");
                    portraitSay("I've heard of them.");
                    randomSayIfPersonality(PersonalityTrait.critical, List.of(model.getParty().getLeader()),
                            "Is this " + (getPortraitGender() ? "girl" : "guy") + " really qualified?", FacialExpression.questioning);
                    leaderSay("You'll do fine.");
            }
            model.getLog().waitForAnimationToFinish();
            removePortraitSubView(model);
            recruited++;
        }
    }
}
