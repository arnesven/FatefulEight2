package model.quests;

import model.Model;
import model.characters.DeniseBoyd;
import model.characters.appearance.DefaultAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.enemies.BanditEnemy;
import model.enemies.Enemy;
import model.quests.scenes.CollaborativeSkillCheckSubScene;
import model.quests.scenes.CombatSubScene;
import model.quests.scenes.SoloSkillCheckSubScene;
import model.races.Race;
import model.states.QuestState;
import util.MyRandom;
import view.MyColors;
import view.sprites.BanditSprite;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.CombatTheme;
import view.subviews.GrassCombatTheme;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DefendTheVillageQuest extends Quest {

    private static final String TEXT =
            "Some peasants are looking for capable warriors " +
            "to come and defend their village from a raiding " +
                    "Orcish band.";
    private static final String END_TEXT =
            "The peasants thank you profusely and hope that " +
                    "you will return some day soon.";
    private static List<QuestBackground> bgSprites = makeBackgroundSprites();

    public DefendTheVillageQuest() {
        super("Defend the Village", "some desperate peasants", QuestDifficulty.HARD,
                1, 50, 0, TEXT, END_TEXT);
    }

    @Override
    public CombatTheme getCombatTheme() {
        return new GrassCombatTheme();
    }

    @Override
    public List<QuestBackground> getBackgroundSprites() {
        return bgSprites;
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of(
                new QuestScene("Train Villagers",
                List.of(new SoloSkillCheckSubScene(6, 1, Skill.Leadership, 8,
                        "What's needed here is a speech to inspire some courage!"),
                        new SoloSkillCheckSubScene(4, 1, Skill.Polearms, 7,
                                "Every man and woman who can fight, grab a spear!"))),
                new QuestScene("Fortifications",
                List.of(new CollaborativeSkillCheckSubScene(4, 5, Skill.Security, 10,
                                "With some proper locks, we can just shut the bandits out."),
                        new CollaborativeSkillCheckSubScene(4, 3, Skill.Labor, 11,
                                "Gather all the wood you can, and some nails. Follow me. We're building some barricades."))),
                new QuestScene("Raise Morale",
                List.of(new CollaborativeSkillCheckSubScene(1, 5, Skill.Entertain, 9,
                        "Now all we can do is wait. Or perhaps, we could have some fun?"))),
                new QuestScene("Battle", List.of(new VariableBanditCombatSubScene(3, 7))));
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        QuestStartPoint qss = new QuestStartPoint(List.of(
                new QuestEdge(scenes.get(0).get(0)),
                new QuestEdge(scenes.get(0).get(1)),
                new QuestEdge(scenes.get(3).get(0), QuestEdge.VERTICAL)),
                "Those bandits will be coming soon. How to prepare the peasants?");
        QuestDecisionPoint qd1 = new QuestDecisionPoint(5, 2,
                List.of(new QuestEdge(scenes.get(1).get(0), QuestEdge.VERTICAL),
                        new QuestEdge(scenes.get(1).get(1), QuestEdge.VERTICAL)),
                "Can we improve the fortifications?");
        CountingJunction cnt1 = new PeasantCountJunction(5, 1, new QuestEdge(qd1));
        SimpleJunction sj1 = new SimpleJunction(3, 4, new QuestEdge(scenes.get(2).get(0)));
        CountingJunction cnt2 = new PeasantCountJunction(4, 4, new QuestEdge(sj1));
        SimpleJunction sj2 = new SimpleJunction(2, 6, new QuestEdge(scenes.get(3).get(0)));
        CountingJunction cnt3 = new PeasantCountJunction(1, 6, new QuestEdge(sj2));
        DecorativeJunction bandit1 = new SpriteDecorativeJunction(2, 8, new BanditSprite(Race.HALF_ORC.getColor()), "Bandit");
        DecorativeJunction bandit2 = new SpriteDecorativeJunction(3, 8, new BanditSprite(Race.HALF_ORC.getColor()), "Bandit");
        DecorativeJunction bandit3 = new SpriteDecorativeJunction(4, 8, new BanditSprite(Race.HALF_ORC.getColor()), "Bandit");
        return List.of(qss, cnt1, qd1, sj1, cnt2, sj2, cnt3, bandit1, bandit2, bandit3);
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {
        scenes.get(0).get(0).connectFail(junctions.get(2), QuestEdge.VERTICAL);
        scenes.get(0).get(0).connectSuccess(junctions.get(1));
        scenes.get(0).get(1).connectFail(junctions.get(2), QuestEdge.VERTICAL);
        scenes.get(0).get(1).connectSuccess(junctions.get(1));

        scenes.get(1).get(0).connectFail(junctions.get(3));
        scenes.get(1).get(0).connectSuccess(junctions.get(4));
        scenes.get(1).get(1).connectFail(junctions.get(3));
        scenes.get(1).get(1).connectSuccess(junctions.get(4));

        scenes.get(2).get(0).connectFail(junctions.get(5));
        scenes.get(2).get(0).connectSuccess(junctions.get(6));

        scenes.get(3).get(0).connectSuccess(getSuccessEndingNode());
        scenes.get(3).get(0).connectFail(getFailEndingNode(), QuestEdge.VERTICAL);
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.GREEN;
    }

    private class VariableBanditCombatSubScene extends CombatSubScene {
        public VariableBanditCombatSubScene(int col, int row) {
            super(col, row, List.of(new BanditEnemy('A', "Bandit", 5)), true);
        }

        @Override
        protected String getCombatDetails() {
            return "Bandits";
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            int[] numEnemies = new int[]{9, 6, 4, 2};
            List<Enemy> enemies = new ArrayList<>();
            for (int i = 0; i < numEnemies[state.getCounter()]; ++i) {
                enemies.add(new BanditEnemy('A', "Bandit", 5));
            }
            super.setEnemies(enemies);
            QuestEdge toReturn = super.run(model, state);
            getJunctions().remove(getJunctions().size()-1);
            getJunctions().remove(getJunctions().size()-1);
            getJunctions().remove(getJunctions().size()-1);
            return toReturn;
        }
    }

    private static List<QuestBackground> makeBackgroundSprites() {
        List<QuestBackground> result = new ArrayList<>();
        Random rand = new Random(1234);
        for (int row = 1; row < 9; ++row) {
            for (int col = 0; col < 8; ++col) {
                if (row != 8) {
                    result.add(new QuestBackground(new Point(col, row),
                            GrassCombatTheme.grassSprites[rand.nextInt(GrassCombatTheme.grassSprites.length)]));
                }
            }
        }
        final Sprite townSprite = new Sprite32x32("townspriteqmb", "quest.png", 0x51,
                MyColors.BLACK, MyColors.LIGHT_YELLOW, MyColors.GRAY, MyColors.GREEN);
        final Sprite field = new Sprite32x32("field", "quest.png", 0x63,
                MyColors.BLACK, MyColors.YELLOW, MyColors.DARK_GREEN, MyColors.GREEN);
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (j == 1 && i == 1) {
                    result.add(new QuestBackground(new Point(1+i, 1+j), townSprite));
                } else {
                    result.add(new QuestBackground(new Point(1+i, 1+j), field));
                }
            }
        }

        final Sprite halfTown = new Sprite32x32("halftownspriteqmb", "quest.png", 0x52,
                MyColors.BLACK, MyColors.LIGHT_YELLOW, MyColors.GRAY, MyColors.GREEN);
        result.add(new QuestBackground(new Point(0, 0), halfTown, true));
        return result;
    }

    private static LoopingSprite makePeasantSprite(Race race, MyColors shirtColor) {
        LoopingSprite spr = new LoopingSprite("peasantavatar", "enemies.png", 0x57, 32);
        spr.setColor1(MyColors.BLACK);
        spr.setColor2(shirtColor);
        spr.setColor3(race.getColor());
        spr.setColor4(MyColors.BROWN);
        spr.setFrames(4);
        return spr;
    }

    private static class PeasantCountJunction extends CountingJunction {
        private boolean hasPassed = false;
        private Race race = MyRandom.randInt(2)==0 ? Race.NORTHERN_HUMAN : Race.SOUTHERN_HUMAN;
        private MyColors shirtColor = MyRandom.randInt(2)==0?MyColors.YELLOW:MyColors.ORC_GREEN;
        public final LoopingSprite peasantAvatar = makePeasantSprite(race, shirtColor);
        public final Sprite32x32 peasantStanding =
                new Sprite32x32("peasantsitting", "enemies.png", 0x5B,
                MyColors.BLACK, shirtColor, race.getColor(), MyColors.BROWN);

        public PeasantCountJunction(int col, int row, QuestEdge questEdge) {
            super(col, row, questEdge);
        }

        @Override
        public QuestEdge run(Model model, QuestState state) {
            this.hasPassed = true;
            return super.run(model, state);
        }

        @Override
        public String getDescription() {
            if (hasPassed) {
                return "Rallied Peasant";
            }
            return "Peasant";
        }

        @Override
        public void drawYourself(Model model, int xPos, int yPos) {
            super.drawYourself(model, xPos, yPos);
            if (hasPassed) {
                model.getScreenHandler().register(peasantAvatar.getName(), new Point(xPos, yPos), peasantAvatar, 1);
            } else {
                model.getScreenHandler().register(peasantStanding.getName(), new Point(xPos, yPos), peasantStanding, 1);
            }
        }
    }
}
