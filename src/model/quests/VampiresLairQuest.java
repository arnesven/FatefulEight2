package model.quests;

import model.Model;
import model.classes.Skill;
import model.enemies.Enemy;
import model.enemies.ThrallEnemy;
import model.enemies.VampirePuppeteer;
import model.quests.scenes.CollectiveSkillCheckSubScene;
import model.quests.scenes.CombatSubScene;
import model.states.GameState;
import model.states.QuestState;
import view.MyColors;

import java.util.ArrayList;
import java.util.List;

public class VampiresLairQuest extends MainQuest {
    public static final String QUEST_NAME = "Vampire's Lair";
    private static final String TEXT = "As you travel through the mountains you encounter Caid outside a door to an old crypt. " +
            "He asks you to accompany him inside to find the sister of his lord.";
    private static final String END_TEXT = "You've solved the mystery of the lord's missing sister. Caid compensates you.";

    public VampiresLairQuest() {
        super(QUEST_NAME, "", QuestDifficulty.HARD, 1, 120, 0, TEXT, END_TEXT);
    }

    @Override
    public MainQuest copy() {
        return new VampiresLairQuest();
    }

    @Override
    public GameState endOfQuest(Model model, QuestState state, boolean questWasSuccess) {
        model.getMainStory().setCaidQuestDone(true);
        return Quest.endOfQuestProcedure(model, state, questWasSuccess);
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(new QuestScene("Thralls", List.of(
                new CollectiveSkillCheckSubScene(1, 1, Skill.Sneak, 5, "Perhaps we can sneak by this lot."),
                new VampirePuppeteerCombatSubScene(1, 2)
        )));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        return List.of(new QuestStartPoint(List.of(new QuestEdge(scenes.get(0).get(0)),
                new QuestEdge(scenes.get(0).get(1))), "TODO"));
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {

    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.BLACK;
    }

    private static class VampirePuppeteerCombatSubScene extends CombatSubScene {
        public VampirePuppeteerCombatSubScene(int col, int row) {
            super(col, row, makeEnemies());

        }

        @Override
        protected String getCombatDetails() {
            return "Lots of thralls";
        }
    }

    private static List<Enemy> makeEnemies() {
        List<Enemy> enemies = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            enemies.add(new ThrallEnemy('B'));
        }
        enemies.add(new VampirePuppeteer('A'));
        for (int i = 0; i < 5; ++i) {
            enemies.add(new ThrallEnemy('B'));
        }
        return enemies;
    }
}
