package model.quests;

import model.characters.GameCharacter;
import model.characters.appearance.RandomAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.enemies.Enemy;
import model.enemies.OrcishBombThrowerEnemy;
import model.enemies.OrcishNinjaEnemy;
import model.enemies.OrcishNinjaStarThrowerEnemy;
import model.items.Equipment;
import model.items.accessories.SkullCap;
import model.items.clothing.LeatherArmor;
import model.items.weapons.Longsword;
import model.quests.scenes.CombatSubScene;
import model.quests.scenes.SoloSkillCheckSubScene;
import model.races.AllRaces;
import model.states.battle.MilitiaUnit;
import sound.BackgroundMusic;
import util.MyRandom;
import view.MyColors;

import java.util.ArrayList;
import java.util.List;

public class NightAtTheTheaterQuest extends RemotePeopleQuest {
    public static final String QUEST_NAME = "Night at the Theater";
    private static final String INTRO_TEXT = "TODO: intro";
    private static final String END_TEXT = "TODO: outro";

    public NightAtTheTheaterQuest() {
        super(QUEST_NAME, "Lord Shingen", QuestDifficulty.VERY_HARD, new Reward(1, 200, 0),
                0, INTRO_TEXT, END_TEXT);
    }

    @Override
    public MainQuest copy() {
        return new NightAtTheTheaterQuest();
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(new QuestScene("Detect Ninjas", List.of(
                new SoloSkillCheckSubScene(2, 2, Skill.Perception, 16,
                        "Was that something there, up on the roof top?"))),
                new QuestScene("Distributed Fight", List.of(
                        new DistributedCombatSubScene(2, 4))),
                new QuestScene("Gathered Fight", List.of(
                        new GatheredFightCombatSubScene(4, 5))));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        return List.of(new QuestStartPointWithoutDecision(
                new QuestEdge(scenes.get(0).get(0)), "Blabla"));
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectSuccess(scenes.get(2).get(0));
        scenes.get(0).get(0).connectFail(scenes.get(1).get(0));

        scenes.get(1).get(0).connectFail(getFailEndingNode(), QuestEdge.VERTICAL);
        scenes.get(1).get(0).connectSuccess(getSuccessEndingNode());

        scenes.get(2).get(0).connectFail(getFailEndingNode(), QuestEdge.VERTICAL);
        scenes.get(2).get(0).connectSuccess(getSuccessEndingNode());
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.BLACK;
    }

    @Override
    public BackgroundMusic getMusic() {
        return BackgroundMusic.darkQuestSong;
    }

    private static class GatheredFightCombatSubScene extends CombatSubScene {
        public GatheredFightCombatSubScene(int col, int row) {
            super(col, row, makeEnemies(), true);
        }

        @Override
        protected List<GameCharacter> getAllies() {
            // TODO: Use Samurai Class
            return List.of(new GameCharacter("Honorable", "Warrior", AllRaces.EASTERN_HUMAN, Classes.CAP, new RandomAppearance(AllRaces.EASTERN_HUMAN),
                    Classes.NO_OTHER_CLASSES,
                    new Equipment(new Longsword(), new LeatherArmor(), new SkullCap())));
        }

        private static List<Enemy> makeEnemies() {
            List<Enemy> enemies = new ArrayList<>();
            for (int i = 0; i < 22; i++) {
                int roll = MyRandom.rollD6();
                if (roll < 3) {
                    enemies.add(new OrcishNinjaEnemy('A'));
                } else if (roll < 5) {
                    enemies.add(new OrcishBombThrowerEnemy('B'));
                } else {
                    enemies.add(new OrcishNinjaStarThrowerEnemy('C'));
                }
            }
            return enemies;
        }

        @Override
        protected String getCombatDetails() {
            return "Mysterious Ninjas";
        }
    }

    private static class DistributedCombatSubScene extends GatheredFightCombatSubScene {
        public DistributedCombatSubScene(int col, int row) {
            super(col, row);
        }
    }
}
