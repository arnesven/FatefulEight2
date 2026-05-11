package model.quests;

import model.Model;
import model.achievements.Achievement;
import model.characters.appearance.CharacterAppearance;
import model.characters.appearance.FacialExpression;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.classes.SkillChecks;
import model.quests.scenes.*;
import sound.BackgroundMusic;
import view.combat.TownCombatTheme;
import model.enemies.MurdererEnemy;
import model.races.Race;
import view.MyColors;
import view.combat.CombatTheme;
import view.subviews.PortraitSubView;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MurderMysteryQuest extends Quest implements CountingQuest {
    private static final CharacterAppearance PORTRAIT =  PortraitSubView.makeRandomPortrait(Classes.CONSTABLE, Race.ALL);
    private static final String INTRO = "The local authorities need help solving " +
            "this one. The victim was found in an alley, gutted and with mysterious marks " +
            "on her arm. Clues lead to the exclusive Corner Club, but can you get in?";
    private static final String ENDING = "The sheriff thanks you for your service and rewards you well.";
    private static final List<QuestBackground> BACKGROUND_SPRITES = makeBackground();

    private int questSuccesses = 0;

    public MurderMysteryQuest() {
        super("Murder Mystery", "Sheriff", QuestDifficulty.HARD,
                new Reward(250),0, INTRO, ENDING);
    }

    @Override
    public Achievement.Data getAchievementData() {
        return makeAchievement(this,
                "You helped a " + getProvider() + " to solve a particularly difficult homicide case.");
    }

    @Override
    protected void resetQuest() {
        super.resetQuest();
        questSuccesses = 0;
    }

    @Override
    public void addToCount(int x) {
        questSuccesses += x;
    }

    @Override
    public int getCount() {
        return questSuccesses;
    }

    @Override
    public CharacterAppearance getPortrait() {
        return PORTRAIT;
    }

    @Override
    public BackgroundMusic getMusic() {
        return BackgroundMusic.mysticSong;
    }

    @Override
    public QuestIntroEventState getIntroEvent(Model model) {
        return new IntroEvent(model);
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(
                new QuestScene("Crime Scene", List.of(
                        new CountingSubScene(this, new SoloSkillCheckSubScene(5, 2, Skill.Perception, 11,
                        "Can anybody see any clues?")),
                        new CountingSubScene(this, new CollaborativeSkillCheckSubScene(5, 1, Skill.Search, 11,
                        "Maybe we can find some clues if we snoop around a bit.")))),
                new QuestScene("Interview Residents", List.of(
                        new CountingSubScene(this, new CollectiveSkillCheckSubScene(1, 3, Skill.SeekInfo, 5,
                                "Okay, spread out and let's start knocking on some doors. Somebody must have seen something.")),
                        new CountingSubScene(this, new CollaborativeSkillCheckSubScene(3, 4, Skill.SeekInfo, 10,
                                "Let's interview some witnesses together.")))),
                new QuestScene("Corner Club", List.of(
                        new SoloSkillCheckSubScene(2, 6, Skill.Persuade, 13, "Can somebody sweet talk the doorman?"),
                        new PayGoldSubScene(1, 6, 10, "Looks like we're going to have to grease some palms to get into the Corner Club."),
                        new CountingSubScene(this, new CollaborativeSkillCheckSubScene(2, 7, Skill.SeekInfo, 10, "Okay, we're in. Now let's ask around.")))),
                new QuestScene("Confront Killer", List.of(
                        new CountCheckSubScene(this, 3, 7, 2, 3) {
                            protected String getFailText() {
                                return "Unfortunately you do not have enough clues to find the killer. (" + questSuccesses + "/3 *).";
                            }

                            protected String getSuccessText() {
                                return "You have gathered enough clues to track down the killer. (" + questSuccesses + "/3 *).";
                            }
                        },
                        new KillerCombatSubScene(5, 7)))
                );
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestStartPoint qss = new QuestStartPoint(List.of(
                new QuestEdge(scenes.get(0).get(0), QuestEdge.VERTICAL),
                new QuestEdge(scenes.get(0).get(1))),
                "This is the crime scene. You can still see the blood stains on the street.");
        qss.setColumn(7);
        qss.setRow(1);
        QuestDecisionPoint qd1 = new QuestDecisionPoint(3, 3, List.of(
                new QuestEdge(scenes.get(1).get(0)),
                new QuestEdge(scenes.get(1).get(1), QuestEdge.VERTICAL)),
                "We're going to have to interview some locals to find out more.");
        QuestDecisionPoint qd2 = new QuestDecisionPoint(1, 5, List.of(
                new QuestEdge(scenes.get(2).get(0)),
                new QuestEdge(scenes.get(2).get(1))),
                "Clues point to the Corner Club. But how are we going to get in?");
        return List.of(qss, qd1, qd2);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectSuccess(junctions.get(1));
        scenes.get(0).get(1).connectSuccess(junctions.get(1));

        scenes.get(1).get(0).connectSuccess(junctions.get(2));
        scenes.get(1).get(1).connectSuccess(junctions.get(2));

        scenes.get(2).get(0).connectFail(scenes.get(3).get(0));
        scenes.get(2).get(0).connectSuccess(scenes.get(2).get(2));
        scenes.get(2).get(1).connectFail(scenes.get(3).get(0));
        scenes.get(2).get(1).connectSuccess(scenes.get(2).get(2), QuestEdge.VERTICAL);

        scenes.get(2).get(2).connectSuccess(scenes.get(3).get(0));

        scenes.get(3).get(0).connectFail(getFailEndingNode(), QuestEdge.VERTICAL);
        scenes.get(3).get(0).connectSuccess(scenes.get(3).get(1));

        scenes.get(3).get(1).connectSuccess(getSuccessEndingNode());
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.GRAY;
    }

    @Override
    public List<QuestBackground> getBackgroundSprites() {
        return BACKGROUND_SPRITES;
    }

    public static List<QuestBackground> makeBackground() {
        List<QuestBackground> backgrounds = new ArrayList<>();
        for (int i = 0; i < TownCombatTheme.topRow.length; ++i) {
            backgrounds.add(new QuestBackground(new Point(i, 0), TownCombatTheme.topRow[i]));
            backgrounds.add(new QuestBackground(new Point(i, 1), TownCombatTheme.bottomRow[i]));
        }
        return backgrounds;
    }

    @Override
    public CombatTheme getCombatTheme() {
        return new TownCombatTheme();
    }

    private static class KillerCombatSubScene extends CombatSubScene {
        public KillerCombatSubScene(int col, int row) {
            super(col, row, List.of(new MurdererEnemy('A')));
        }

        @Override
        protected String getCombatDetails() {
            return "Murderer";
        }
    }

    private class IntroEvent extends QuestIntroEventState {
        public IntroEvent(Model model) {
            super(model);
        }

        @Override
        protected void runQuestIntro(Model model) {
            println("You're casually walking down the street when you see a small crowd of people. " +
                    "They are murmuring worriedly, and some are even crying");
            showExplicitPortrait(model, getPortrait(), getProvider());
            portraitSay("Okay folks, time to move a long.");
            println("As you come closer you spot a dead body lying in the alley up ahead.");
            println("The sheriff and " + hisOrHer(getPortraitGender())+
                    " deputy manage to usher most of the people away from the area.");
            portraitSay("That means you too outsider. There's been another accident...", FacialExpression.disappointed);
            SkillCheckResult result = model.getParty().getLeader().testSkill(model, Skill.Perception,
                    SkillCheckResult.NO_DIFFICULTY,model.getParty().getLeader().getRankForSkill(Skill.Blades));
            println("You take one look at the victim's wounds and identify them as " +
                    "stabs from a ritual dagger (Perception " + result.asString() + ").");
            leaderSay("Accident? Those are clearly stab wounds. You've got a murderer on the loose in your town Sheriff.", FacialExpression.questioning);
            portraitSay("Keep your voice down! Don't you think I know that?", FacialExpression.disappointed);
            leaderSay("So it's a cover up then? The constabulary never ceases to amaze me.");
            portraitSay("It's the official story until we have a suspect in custody. No need to cause a panic.");
            leaderSay("People always figure out the truth sheriff.", FacialExpression.wicked);
            portraitSay("Yeah, maybe the clever ones will. Say, you seem to have your wits about you. And I'm impressed you " +
                    "picked up on the stabbings. Why don't you take a shot at cracking this case, huh?");
            leaderSay("Seems kind of dangerous.", FacialExpression.disappointed);
            portraitSay("Come on. It'll be something to tell the grand kids about.", FacialExpression.relief);
            leaderSay("Maybe. If I'm lucky.");
            portraitSay("Think it over. We can't let that killer roam free.");
        }
    }
}
