package model.quests;

import model.Model;
import model.QuestDeck;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.combat.TownCombatTheme;
import model.enemies.BanditEnemy;
import model.enemies.MurdererEnemy;
import model.quests.scenes.CollaborativeSkillCheckSubScene;
import model.quests.scenes.CollectiveSkillCheckSubScene;
import model.quests.scenes.CombatSubScene;
import model.quests.scenes.SoloSkillCheckSubScene;
import model.races.Race;
import model.states.QuestState;
import view.MyColors;
import view.sprites.Sprite32x32;
import view.subviews.CombatTheme;
import view.subviews.PortraitSubView;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MurderMysteryQuest extends Quest {
    private static final CharacterAppearance PORTRAIT =  PortraitSubView.makeRandomPortrait(Classes.CONSTABLE, Race.ALL);;
    private static final String INTRO = "The local authorities need help solving " +
            "this one. The victim was found in an alley, gutted and with mysterious marks " +
            "on her arm. Clues lead to the exclusive Corner Club, but can you get in?";
    private static final String ENDING = "The sheriff thanks you for your service and rewards you well.";
    private static final List<QuestBackground> BACKGROUND_SPRITES = makeBackground();

    private int questSuccesses = 0;

    public MurderMysteryQuest() {
        super("Murder Mystery", "Sheriff", QuestDifficulty.HARD, 1, 50, 0, INTRO, ENDING);
    }

    @Override
    public CharacterAppearance getPortrait() {
        return PORTRAIT;
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(
                new QuestScene("Crime Scene", List.of(
                        new CountingSubScene(new SoloSkillCheckSubScene(6, 2, Skill.Perception, 11,
                        "Can anybody see any clues?")),
                        new CountingSubScene(new CollaborativeSkillCheckSubScene(5, 1, Skill.Search, 11,
                        "Maybe we can find some clues if we snoop around a bit.")))),
                new QuestScene("Interview Residents", List.of(
                        new CountingSubScene(new CollectiveSkillCheckSubScene(1, 3, Skill.SeekInfo, 5,
                                "Okay, spread out and let's start knocking on some doors. Somebody must have seen something.")),
                        new CountingSubScene(new CollaborativeSkillCheckSubScene(3, 4, Skill.SeekInfo, 10,
                                "Let's interview some witnesses together.")))),
                new QuestScene("Corner Club", List.of(
                        new SoloSkillCheckSubScene(2, 6, Skill.Persuade, 13, "Can somebody sweet talk the doorman?"),
                        new PayGoldSubScene(1, 6, 10, "Looks like we're going to have to grease some palms to get into the Corner Club."),
                        new CountingSubScene(new CollaborativeSkillCheckSubScene(2, 7, Skill.SeekInfo, 10, "Okay, we're in. Now let's ask around.")))),
                new QuestScene("Confront Killer", List.of(
                        new CountCheckSubScene(3, 7),
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

    private static List<QuestBackground> makeBackground() {
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

    private class CountingSubScene extends QuestSubScene {
        private final QuestSubScene inner;

        public CountingSubScene(QuestSubScene inner) {
            super(inner.getColumn(), inner.getRow());
            this.inner = inner;
        }

        @Override
        public void drawYourself(Model model, int xPos, int yPos) {
            inner.drawYourself(model, xPos, yPos);
        }

        @Override
        public String getDescription() {
            return "*" + inner.getDescription();
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            QuestEdge innerResult = inner.run(model, state);
            if (innerResult == inner.getSuccessEdge()) {
                questSuccesses = questSuccesses + 1;
            }
            return getSuccessEdge();
        }

        @Override
        public void connectSuccess(QuestNode questNode, boolean align) {
            super.connectSuccess(questNode, align);
            inner.connectSuccess(questNode, align);
        }

        @Override
        protected MyColors getSuccessEdgeColor() {
            return MyColors.WHITE;
        }

        @Override
        public String getDetailedDescription() {
            return inner.getDetailedDescription();
        }
    }

    private class CountCheckSubScene extends ConditionSubScene {
        public CountCheckSubScene(int col, int row) {
            super(col, row);
        }

        @Override
        public String getDescription() {
            return "Two out of three * successful?";
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            if (questSuccesses >= 2) {
                state.println("You have gathered enough clues to track down the killer. (" + questSuccesses + "/3 *).");
                return getSuccessEdge();
            }
            state.println("Unfortunately you do not have enough clues to find the killer. (" + questSuccesses + "/3 *).");
            return getFailEdge();
        }
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
}
